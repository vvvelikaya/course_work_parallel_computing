package ua.kpi.parallels.services;

import ua.kpi.parallels.InvertedIndex;


public interface IndexService {

    void buildIndex(InvertedIndex index) throws InterruptedException;

}
