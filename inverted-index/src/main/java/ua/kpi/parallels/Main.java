package ua.kpi.parallels;

import java.io.File;
import java.io.FileFilter;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class Main {

    static final File POISON = new File("");
    static boolean stop = false;
    final static int BOUND = 1000;
    final static int N_CONSUMERS = 2;
    static final File root = new File("D:\\Nika\\Документы\\data_for_indexing\\aclImdb");
    static InvertedIndex index;

    public static void main(String[] args) {
        try {
            index = new InvertedIndex();
            startIndexing();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void startIndexing() throws InterruptedException {
        BlockingQueue<File> queue = new ArrayBlockingQueue<>(BOUND);
        FileFilter filter = f -> {
            if (!f.isDirectory()) {
                return f.getName().endsWith("txt");
            }
            return true;
        };

        new Thread(new FileCrawler(queue, filter, root)).start();
        for (int i = 0; i < N_CONSUMERS; i++) {
            new Thread(new Indexer(queue, index)).start();
        }
    }

}
