package ua.kpi.parallels;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class InvertedIndex {

    private Map<String, List<String>> index = new ConcurrentHashMap<>();

    public Map<String, List<String>> getIndex() {
        return index;
    }

    public void setIndex(ConcurrentHashMap<String, List<String>> index) {
        this.index = index;
    }

    public void add(String term, String doc) {
        if(!index.containsKey(term)) {
            ArrayList<String> docsList = new ArrayList<>();
            docsList.add(doc);
            index.put(term, docsList);
        } else {
            List<String> docsList = index.get(term);
            docsList.add(doc);
        }
    }
}
