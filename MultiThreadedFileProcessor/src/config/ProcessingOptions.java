package config;

public final class ProcessingOptions {

    private final int workers;
    private final String dataDirectory;
    private final String reportPath;
    private final boolean headerPresent;
    private final String separator;

    private ProcessingOptions(Builder b) {
        this.workers = b.workers;
        this.dataDirectory = b.dataDirectory;
        this.reportPath = b.reportPath;
        this.headerPresent = b.headerPresent;
        this.separator = b.separator;
    }

    public int getWorkers() {
        return workers;
    }

    public String getDataDirectory() {
        return dataDirectory;
    }

    public String getReportPath() {
        return reportPath;
    }

    public boolean hasHeader() {
        return headerPresent;
    }

    public String getSeparator() {
        return separator;
    }

    @Override
    public String toString() {
        return "ProcessingOptions[workers=" + workers
                + ", dataDirectory=" + dataDirectory
                + ", reportPath=" + reportPath
                + ", header=" + headerPresent
                + ", separator='" + separator + "']";
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {

        private int workers = 2;
        private String dataDirectory = "data";
        private String reportPath = "report.txt";
        private boolean headerPresent = true;
        private String separator = ",";

        private Builder() {
        }

        public Builder workers(int workers) {
            if (workers <= 0) {
                throw new IllegalArgumentException("workers must be positive");
            }
            this.workers = workers;
            return this;
        }

        public Builder dataDirectory(String dataDirectory) {
            if (dataDirectory == null || dataDirectory.trim().isEmpty()) {
                throw new IllegalArgumentException("dataDirectory must not be empty");
            }
            this.dataDirectory = dataDirectory;
            return this;
        }

        public Builder reportPath(String reportPath) {
            if (reportPath == null || reportPath.trim().isEmpty()) {
                throw new IllegalArgumentException("reportPath must not be empty");
            }
            this.reportPath = reportPath;
            return this;
        }

        public Builder headerPresent(boolean headerPresent) {
            this.headerPresent = headerPresent;
            return this;
        }

        public Builder separator(String separator) {
            if (separator == null || separator.isEmpty()) {
                throw new IllegalArgumentException("separator must not be empty");
            }
            this.separator = separator;
            return this;
        }

        public ProcessingOptions build() {
            return new ProcessingOptions(this);
        }
    }
}
