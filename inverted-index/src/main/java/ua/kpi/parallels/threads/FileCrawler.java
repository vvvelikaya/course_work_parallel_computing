package ua.kpi.parallels.threads;

import ua.kpi.parallels.services.ParallelService;

import java.io.File;
import java.io.FileFilter;
import java.util.concurrent.BlockingQueue;

public class FileCrawler implements Runnable {

    private final BlockingQueue<File> fileQueue;
    private final FileFilter fileFilter;
    private final File root;

    public FileCrawler(BlockingQueue<File> fileQueue, FileFilter fileFilter, File root) {
        this.fileQueue = fileQueue;
        this.fileFilter = fileFilter;
        this.root = root;
    }

    @Override
    public void run() {
        try {
            crawl(root);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        finally {
            while (true) {
                try {
                    fileQueue.put(ParallelService.POISON);
                    break;
                } catch (InterruptedException e1) { /* retry */ }
            }
        }
    }

    private void crawl(File root) throws InterruptedException {
        File[] entries = root.listFiles(fileFilter);
        if (entries != null) {
            for (File entry : entries) {
                if (entry.isDirectory()) {
                    crawl(entry);
                } else {
                    fileQueue.put(entry);
                }
            }
        }
    }
}
