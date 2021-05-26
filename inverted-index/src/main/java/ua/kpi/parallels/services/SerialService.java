package ua.kpi.parallels.services;

import ua.kpi.parallels.InvertedIndex;
import ua.kpi.parallels.ParserUtil;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class SerialService implements IndexService {

    private List<File> files = new ArrayList<>();
    FileFilter fileFilter;

    static final File root = new File("D:\\Nika\\Документы\\data_for_indexing\\aclImdb");

    @Override
    public void buildIndex(InvertedIndex index) {
        fileFilter = f -> {
            if (!f.isDirectory()) {
                return f.getName().endsWith("txt");
            }
            return true;
        };
        try {
            crawl(root);
            for (File file : files) {
                indexFile(file, index);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (IOException c) {
            c.printStackTrace();
        }
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

    private void indexFile(File file, InvertedIndex invertedIndex) throws IOException {
        Set<String> terms = ParserUtil.parseFile(file.getAbsolutePath());
        for (String term : terms) {
            invertedIndex.add(term, file);
        }
    }

}
