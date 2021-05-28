package ua.kpi.parallels.services;

import ua.kpi.parallels.data.InvertedIndex;
import ua.kpi.parallels.Main;
import ua.kpi.parallels.utils.ParserUtil;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

public class SerialService implements IndexService {

    private List<File> files = new ArrayList<>();
    FileFilter fileFilter;


    @Override
    public InvertedIndex buildIndex() {
        InvertedIndex index = new InvertedIndex(new HashMap<>());
        fileFilter = f -> {
            if (!f.isDirectory()) {
                return f.getName().endsWith("txt");
            }
            return true;
        };
        try {
            crawl(Main.root);
            for (File file : files) {
                indexFile(file, index);
            }

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (IOException c) {
            c.printStackTrace();
        }
        return index;
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
