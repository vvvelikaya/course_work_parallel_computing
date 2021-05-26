package ua.kpi.parallels;

import java.io.File;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class InvertedIndex {

    private Map<String, Set<File>> index = new ConcurrentHashMap<>();

    public Map<String, Set<File>> getIndex() {
        return index;
    }

    public void setIndex(Map<String, Set<File>> index) {
        this.index = index;
    }

    public void add(String term, File doc) {
        if (!index.containsKey(term)) {
            Set<File> docsList = new HashSet<>();
            docsList.add(doc);
            index.put(term, docsList);
        } else {
            Set<File> docsList = index.get(term);
            docsList.add(doc);
        }
    }

    public Set<File> find(String term) {
        Set<File> docs = index.get(term);
        if (docs == null) {
            return new HashSet<>();
        }
        return index.get(term);
    }
}
