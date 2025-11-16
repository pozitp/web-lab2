package ru.pozitp.weblab2.model;

import jakarta.ejb.Singleton;
import jakarta.ejb.Startup;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@Singleton
@Startup
public class ResultsBean {
    private static final int MAX_SIZE = 200;
    private final List<PointResult> results = new LinkedList<>();

    public synchronized void addResult(PointResult result) {
        results.add(0, result);
        if (results.size() > MAX_SIZE) {
            results.remove(results.size() - 1);
        }
    }

    public synchronized List<PointResult> getResultsSnapshot() {
        return new ArrayList<>(results);
    }

    @Override
    public String toString() {
        return "ResultsBean{results_count=" + results.size() + '}';
    }
}