package ua.kpi.parallels.services;

import ua.kpi.parallels.data.InvertedIndex;


public interface IndexService {

    void buildIndex(InvertedIndex index) throws InterruptedException;

}
