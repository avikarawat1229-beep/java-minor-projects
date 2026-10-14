package worker;

import model.SaleEntry;

import java.util.Collections;
import java.util.List;

public final class FileResult {

    private final String fileName;
    private final String threadName;
    private final List<SaleEntry> entries;
    private final int rejectedLines;
    private final long elapsedMillis;

    public FileResult(String fileName, String threadName, List<SaleEntry> entries,
                      int rejectedLines, long elapsedMillis) {
        this.fileName = fileName;
        this.threadName = threadName;
        this.entries = Collections.unmodifiableList(entries);
        this.rejectedLines = rejectedLines;
        this.elapsedMillis = elapsedMillis;
    }

    public String getFileName() {
        return fileName;
    }

    public String getThreadName() {
        return threadName;
    }

    public List<SaleEntry> getEntries() {
        return entries;
    }

    public int getRejectedLines() {
        return rejectedLines;
    }

    public long getElapsedMillis() {
        return elapsedMillis;
    }
}
