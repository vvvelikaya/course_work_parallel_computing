package ua.kpi.parallels.threads;

import ua.kpi.parallels.InvertedIndex;
import ua.kpi.parallels.ParserUtil;
import ua.kpi.parallels.services.ParallelService;

import java.io.File;
import java.io.IOException;
import java.util.Set;
import java.util.concurrent.BlockingQueue;

public class Indexer implements Runnable {

    private final BlockingQueue<File> queue;
    private static volatile boolean stop = false;
    private InvertedIndex invertedIndex;

    public Indexer(BlockingQueue<File> queue, InvertedIndex invertedIndex) {
        this.queue = queue;
        this.invertedIndex = invertedIndex;
    }

    @Override
    public void run() {
        try {
            while (!stop) {
                File file = queue.take();
                if (file == ParallelService.POISON) {
                    stop = true;
                    break;
                }
                indexFile(file);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private void indexFile(File file) {
        try {
            Set<String> terms = ParserUtil.parseFile(file.getAbsolutePath());
            for (String term : terms) {
                invertedIndex.add(term, file);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
