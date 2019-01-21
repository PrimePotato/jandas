package com.lchclearnet.report;

import java.io.File;

public interface ReportBuilder {
    ReportBuilder build();

    <T> T getReport();

    void writeCsv(File csvFile);
}
