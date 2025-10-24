package ru.pozitp.weblab2.model;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public final class ResultsBean implements java.io.Serializable {
    private static final int MAX_SIZE = 200;
    private final LinkedList<PointResult> results = new LinkedList<>();

    public ResultsBean() {
    }

    public synchronized void addResult(PointResult result) {
        results.addFirst(result);
        if (results.size() > MAX_SIZE) {
            results.removeLast();
        }
    }

    public synchronized List<PointResult> getResultsSnapshot() {
        return new ArrayList<>(results);
    }

    @Override
    public String toString() {
        return "ResultsBean{" + "results_count=" + results.size() + '}';
    }
}