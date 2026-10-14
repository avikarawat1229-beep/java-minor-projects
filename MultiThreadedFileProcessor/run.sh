#!/bin/sh
mkdir -p out
javac -d out src/app/App.java src/config/ProcessingOptions.java src/model/SaleEntry.java src/worker/FileTask.java src/worker/FileResult.java src/summary/SalesSummary.java src/summary/ReportWriter.java || exit 1
java -cp out app.App
