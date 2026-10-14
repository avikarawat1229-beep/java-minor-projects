package app;

import config.ProcessingOptions;
import summary.ReportWriter;
import summary.SalesSummary;
import worker.FileResult;
import worker.FileTask;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

public class App {

    public static void main(String[] args) {
        ProcessingOptions options = ProcessingOptions.builder()
                .workers(3)
                .dataDirectory(args.length > 0 ? args[0] : "data")
                .reportPath(args.length > 1 ? args[1] : "report.txt")
                .headerPresent(true)
                .separator(",")
                .build();

        System.out.println("Multi-Threaded File Processor");
        System.out.println(options);

        List<Path> files;
        try {
            files = discoverCsvFiles(Paths.get(options.getDataDirectory()));
        } catch (IOException ex) {
            System.err.println("Cannot read directory '" + options.getDataDirectory() + "': " + ex.getMessage());
            return;
        }

        if (files.isEmpty()) {
            System.err.println("No .csv files found in '" + options.getDataDirectory() + "'");
            return;
        }

        System.out.println("Found " + files.size() + " file(s)");

        SalesSummary summary = new SalesSummary();
        ExecutorService pool = Executors.newFixedThreadPool(options.getWorkers());
        List<Future<FileResult>> pending = new ArrayList<>();
        long begin = System.currentTimeMillis();

        try {
            for (Path file : files) {
                pending.add(pool.submit(new FileTask(file, options)));
            }

            for (Future<FileResult> future : pending) {
                try {
                    summary.absorb(future.get());
                } catch (ExecutionException ex) {
                    System.err.println("A file task failed: " + ex.getCause());
                } catch (InterruptedException ex) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        } finally {
            pool.shutdown();
            try {
                if (!pool.awaitTermination(30, TimeUnit.SECONDS)) {
                    pool.shutdownNow();
                }
            } catch (InterruptedException ex) {
                pool.shutdownNow();
                Thread.currentThread().interrupt();
            }
        }

        System.out.println("All files processed in " + (System.currentTimeMillis() - begin) + " ms");
        System.out.println();

        ReportWriter writer = new ReportWriter();
        String report = writer.render(summary);
        System.out.println(report);

        try {
            writer.write(report, options.getReportPath());
            System.out.println("Report written to " + options.getReportPath());
        } catch (IOException ex) {
            System.err.println("Could not write report: " + ex.getMessage());
        }
    }

    private static List<Path> discoverCsvFiles(Path directory) throws IOException {
        List<Path> found = new ArrayList<>();
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(directory, "*.csv")) {
            for (Path p : stream) {
                if (Files.isRegularFile(p)) {
                    found.add(p);
                }
            }
        }
        Collections.sort(found);
        return found;
    }
}
