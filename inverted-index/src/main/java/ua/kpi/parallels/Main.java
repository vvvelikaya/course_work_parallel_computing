package ua.kpi.parallels;

import java.io.File;
import java.io.FileFilter;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class Main {

    final static int BOUND = 100;
    static final File root = new File("D:\\Nika\\Документы\\data_for_indexing\\aclImdb");

    public static void main(String[] args) {
        try {
            startIndexing();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void startIndexing() throws InterruptedException {
        BlockingQueue<File> queue = new LinkedBlockingQueue<>(BOUND);
        FileFilter filter = f -> {
            if (!f.isDirectory()) {
                return f.getName().endsWith("txt");
            }
            return true;
        };

        new Thread(new FileCrawler(queue, filter, root)).start();

    }

}
