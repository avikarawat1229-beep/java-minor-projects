# MultiThreadedFileProcessor

Reads several CSV sales files in parallel using a fixed thread pool, merges the results into a thread-safe summary and writes a report to `report.txt`.

## Structure

```
src/
  app/App.java                    entry point
  config/ProcessingOptions.java   configuration (Builder pattern)
  model/SaleEntry.java            one CSV row
  worker/FileTask.java            Callable that parses one file
  worker/FileResult.java          result returned by each task
  summary/SalesSummary.java       thread-safe aggregation
  summary/ReportWriter.java       report formatting and file output
data/                             input CSV files
```

CSV format (first line is a header):

```
item,category,units,unit_price
```

## Run

Requires JDK 8 or newer.

Linux / macOS: `./run.sh`

Windows: `run.bat`

Manual:

```
javac -d out src/app/App.java src/config/ProcessingOptions.java src/model/SaleEntry.java src/worker/FileTask.java src/worker/FileResult.java src/summary/SalesSummary.java src/summary/ReportWriter.java
java -cp out app.App
```

Optional arguments: `java -cp out app.App <dataDirectory> <reportFile>`
