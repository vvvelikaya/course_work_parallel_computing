package ua.kpi.parallels.services;

import ua.kpi.parallels.data.InvertedIndex;


public interface IndexService {

    InvertedIndex buildIndex() throws InterruptedException;

}
