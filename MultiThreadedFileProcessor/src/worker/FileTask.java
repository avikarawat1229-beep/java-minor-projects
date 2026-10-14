package worker;

import config.ProcessingOptions;
import model.SaleEntry;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.regex.Pattern;

public class FileTask implements Callable<FileResult> {

    private static final int EXPECTED_COLUMNS = 4;

    private final Path file;
    private final ProcessingOptions options;

    public FileTask(Path file, ProcessingOptions options) {
        this.file = file;
        this.options = options;
    }

    @Override
    public FileResult call() throws IOException {
        long started = System.currentTimeMillis();
        String thread = Thread.currentThread().getName();
        System.out.println("[" + thread + "] started   " + file.getFileName());

        List<SaleEntry> entries = new ArrayList<>();
        int rejected = 0;
        String splitter = Pattern.quote(options.getSeparator());

        try (BufferedReader reader = Files.newBufferedReader(file, StandardCharsets.UTF_8)) {
            String line;
            boolean headerPending = options.hasHeader();
            while ((line = reader.readLine()) != null) {
                if (headerPending) {
                    headerPending = false;
                    continue;
                }
                line = line.trim();
                if (line.isEmpty()) {
                    continue;
                }
                SaleEntry entry = parse(line, splitter);
                if (entry == null) {
                    rejected++;
                    System.err.println("[" + thread + "] rejected line in " + file.getFileName() + ": " + line);
                } else {
                    entries.add(entry);
                }
            }
        }

        long elapsed = System.currentTimeMillis() - started;
        System.out.println("[" + thread + "] finished  " + file.getFileName()
                + " (" + entries.size() + " rows, " + elapsed + " ms)");
        return new FileResult(file.getFileName().toString(), thread, entries, rejected, elapsed);
    }

    private SaleEntry parse(String line, String splitter) {
        String[] cols = line.split(splitter, -1);
        if (cols.length != EXPECTED_COLUMNS) {
            return null;
        }
        try {
            String item = cols[0].trim();
            String category = cols[1].trim();
            int units = Integer.parseInt(cols[2].trim());
            double price = Double.parseDouble(cols[3].trim());
            if (item.isEmpty() || category.isEmpty() || units < 0 || price < 0) {
                return null;
            }
            return new SaleEntry(item, category, units, price);
        } catch (NumberFormatException ex) {
            return null;
        }
    }
}
