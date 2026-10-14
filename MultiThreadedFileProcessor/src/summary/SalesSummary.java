package summary;

import model.SaleEntry;
import worker.FileResult;

import java.util.Collections;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.DoubleAdder;
import java.util.concurrent.atomic.LongAdder;

public class SalesSummary {

    private final AtomicInteger filesProcessed = new AtomicInteger();
    private final AtomicInteger rowsAccepted = new AtomicInteger();
    private final AtomicInteger rowsRejected = new AtomicInteger();
    private final LongAdder totalUnits = new LongAdder();
    private final DoubleAdder totalRevenue = new DoubleAdder();
    private final Map<String, DoubleAdder> revenueByCategory = new ConcurrentHashMap<>();
    private final Map<String, LongAdder> unitsByCategory = new ConcurrentHashMap<>();
    private final Map<String, LongAdder> unitsByItem = new ConcurrentHashMap<>();
    private final Map<String, Integer> rowsByFile = new ConcurrentHashMap<>();

    public void absorb(FileResult result) {
        filesProcessed.incrementAndGet();
        rowsRejected.addAndGet(result.getRejectedLines());
        rowsAccepted.addAndGet(result.getEntries().size());
        rowsByFile.put(result.getFileName(), result.getEntries().size());

        for (SaleEntry e : result.getEntries()) {
            totalUnits.add(e.getUnits());
            totalRevenue.add(e.revenue());
            revenueByCategory.computeIfAbsent(e.getCategory(), k -> new DoubleAdder()).add(e.revenue());
            unitsByCategory.computeIfAbsent(e.getCategory(), k -> new LongAdder()).add(e.getUnits());
            unitsByItem.computeIfAbsent(e.getItem(), k -> new LongAdder()).add(e.getUnits());
        }
    }

    public int getFilesProcessed() {
        return filesProcessed.get();
    }

    public int getRowsAccepted() {
        return rowsAccepted.get();
    }

    public int getRowsRejected() {
        return rowsRejected.get();
    }

    public long getTotalUnits() {
        return totalUnits.sum();
    }

    public double getTotalRevenue() {
        return totalRevenue.sum();
    }

    public double getAverageRevenuePerRow() {
        int rows = rowsAccepted.get();
        return rows == 0 ? 0.0 : totalRevenue.sum() / rows;
    }

    public Map<String, Double> revenueByCategory() {
        Map<String, Double> out = new TreeMap<>();
        revenueByCategory.forEach((k, v) -> out.put(k, v.sum()));
        return out;
    }

    public Map<String, Long> unitsByCategory() {
        Map<String, Long> out = new TreeMap<>();
        unitsByCategory.forEach((k, v) -> out.put(k, v.sum()));
        return out;
    }

    public Map<String, Integer> rowsByFile() {
        return Collections.unmodifiableMap(new TreeMap<>(rowsByFile));
    }

    public String bestSellingItem() {
        String best = "N/A";
        long max = Long.MIN_VALUE;
        for (Map.Entry<String, LongAdder> e : new TreeMap<>(unitsByItem).entrySet()) {
            long v = e.getValue().sum();
            if (v > max) {
                max = v;
                best = e.getKey();
            }
        }
        return best;
    }

    public long bestSellingItemUnits() {
        LongAdder a = unitsByItem.get(bestSellingItem());
        return a == null ? 0 : a.sum();
    }

    public String topRevenueCategory() {
        String best = "N/A";
        double max = -1;
        for (Map.Entry<String, Double> e : revenueByCategory().entrySet()) {
            if (e.getValue() > max) {
                max = e.getValue();
                best = e.getKey();
            }
        }
        return best;
    }
}
