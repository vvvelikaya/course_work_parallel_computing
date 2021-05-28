package ua.kpi.parallels;

import ua.kpi.parallels.data.InvertedIndex;
import ua.kpi.parallels.utils.ParserUtil;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.Duration;
import java.time.Instant;
import java.util.*;

public class IndexServer {

    private InvertedIndex invertedIndex;
    private final List<File> files = new ArrayList<>();
    private FileFilter fileFilter;

    public final int PORT = 7777;
    public final static File root = new File("D:\\Nika\\Документы\\data_for_indexing\\aclImdb");

    public IndexServer() {
        run(); //запускаємо сервер при створені об'єкту
    }

    public void run() {
        try {
            ServerSocket serverSocket = new ServerSocket(PORT);//створення сокеру сервера
            System.out.println("Index server is running...");
            buildIndex();
            while (true) {
                try {
                    Socket clientSocket = serverSocket.accept();//очікує під'єднання сокету клієнта
                    new Thread(() -> {
                        try (PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
                             BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));) {
                            System.out.println("Client connected");
                            out.println("Enter comma-separated words to find (or exit):");
                            String inputLine;
                            while (!(inputLine = in.readLine()).equals("exit")) {
                                System.out.println("Client`s input: " + inputLine); //дублюється повіомлення клієнта
                                Set<File> intersection = findIntersection(inputLine);
                                out.println("'" + inputLine + "' found in " + intersection.size() + " files");
                                for (File file : intersection) {
                                    out.println(file);
                                }
                                out.println("end");
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }).start();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void buildIndex() {
        System.out.println("Building index...");
        invertedIndex = new InvertedIndex(new HashMap<>());
        fileFilter = f -> {
            if (!f.isDirectory()) {
                return f.getName().endsWith("txt");
            }
            return true;
        };
        Instant start = Instant.now();
        try {
            crawl(root);
            for (File file : files) {
                indexFile(file);
            }
        } catch (InterruptedException e) {
            System.out.println("Some thread is interrupted");
        } catch (IOException c) {
            System.out.println("I/O mistake");
        }
        Instant finish = Instant.now();
        System.out.println("Index was built in: " + Duration.between(start, finish).toMillis() + " ms");
    }

    private void crawl(File root) throws InterruptedException {
        File[] entries = root.listFiles(fileFilter);
        if (entries != null) {
            for (File entry : entries) {
                if (entry.isDirectory()) {
                    crawl(entry);
                } else {
                    files.add(entry);
                }
            }
        }
    }

    private void indexFile(File file) throws IOException {
        Set<String> terms = ParserUtil.parseFile(file.getAbsolutePath());
        for (String term : terms) {
            invertedIndex.add(term, file);
        }
    }

    private Set<File> findIntersection(String terms) {
        String[] words = terms.split(", ");
        Set<File> intersection = new HashSet<>(invertedIndex.find(words[0]));
        for (int i = 1; i < words.length; i++) {
            intersection.retainAll(new HashSet<>(invertedIndex.find(words[i])));
        }
        return intersection;
    }

    public static void main(String[] args) {
        new IndexServer(); // створюємо об'єкт класу
    }
}
