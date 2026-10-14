package summary;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

public class ReportWriter {

    private static final String LINE = repeat('=', 56);
    private static final String THIN = repeat('-', 56);

    public String render(SalesSummary s) {
        StringBuilder sb = new StringBuilder();
        sb.append(LINE).append('\n');
        sb.append("  SALES REPORT\n");
        sb.append(LINE).append('\n');
        sb.append(String.format("  Files processed        : %d%n", s.getFilesProcessed()));
        sb.append(String.format("  Valid rows             : %d%n", s.getRowsAccepted()));
        sb.append(String.format("  Rejected rows          : %d%n", s.getRowsRejected()));
        sb.append(String.format("  Units sold             : %,d%n", s.getTotalUnits()));
        sb.append(String.format("  Total revenue          : %,.2f%n", s.getTotalRevenue()));
        sb.append(String.format("  Average per row        : %,.2f%n", s.getAverageRevenuePerRow()));
        sb.append(String.format("  Best selling item      : %s (%,d units)%n",
                s.bestSellingItem(), s.bestSellingItemUnits()));
        sb.append(String.format("  Top revenue category   : %s%n", s.topRevenueCategory()));

        sb.append('\n').append(THIN).append('\n');
        sb.append("  ROWS PER FILE\n");
        sb.append(THIN).append('\n');
        for (Map.Entry<String, Integer> e : s.rowsByFile().entrySet()) {
            sb.append(String.format("  %-28s %6d%n", e.getKey(), e.getValue()));
        }

        sb.append('\n').append(THIN).append('\n');
        sb.append("  REVENUE BY CATEGORY\n");
        sb.append(THIN).append('\n');
        for (Map.Entry<String, Double> e : s.revenueByCategory().entrySet()) {
            sb.append(String.format("  %-24s %,15.2f%n", e.getKey(), e.getValue()));
        }

        sb.append('\n').append(THIN).append('\n');
        sb.append("  UNITS BY CATEGORY\n");
        sb.append(THIN).append('\n');
        for (Map.Entry<String, Long> e : s.unitsByCategory().entrySet()) {
            sb.append(String.format("  %-24s %,15d%n", e.getKey(), e.getValue()));
        }
        sb.append(LINE).append('\n');
        return sb.toString();
    }

    public void write(String report, String destination) throws IOException {
        Path path = Paths.get(destination);
        Path parent = path.toAbsolutePath().getParent();
        if (parent != null) {
            Files.createDirectories(parent);
        }
        Files.write(path, report.getBytes(StandardCharsets.UTF_8));
    }

    private static String repeat(char c, int n) {
        StringBuilder sb = new StringBuilder(n);
        for (int i = 0; i < n; i++) {
            sb.append(c);
        }
        return sb.toString();
    }
}
