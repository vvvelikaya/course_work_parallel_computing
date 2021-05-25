package ua.kpi.parallels;

import java.io.File;
import java.io.IOException;
import java.util.Set;
import java.util.concurrent.BlockingQueue;

public class Indexer implements Runnable{

    private final BlockingQueue<File> queue;
    private InvertedIndex invertedIndex;

    public Indexer(BlockingQueue<File> queue, InvertedIndex invertedIndex) {
        this.queue = queue;
        this.invertedIndex = invertedIndex;
    }

    @Override
    public void run() {
        try {
            while (true) {
                indexFile(queue.take());
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private void indexFile(File file) {
        try {
            Set<String> terms = ParserUtil.parseFile(file.getAbsolutePath());
            for (String term : terms) {
                System.out.println(term);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
