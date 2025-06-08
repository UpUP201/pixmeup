package com.corp.pixmeup.check.util;

import com.corp.pixmeup.check.dto.CheckSummary;

import java.time.LocalDateTime;

public class CheckSummaryBuilder {
    private final LocalDateTime dateTime;
    private boolean hasSight = false;
    private boolean hasPresbyopia = false;
    private boolean hasAmsler = false;
    private boolean hasMChart = false;

    public CheckSummaryBuilder(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public CheckSummaryBuilder hasSight(boolean v) { this.hasSight = v; return this; }
    public CheckSummaryBuilder hasPresbyopia(boolean v) { this.hasPresbyopia = v; return this; }
    public CheckSummaryBuilder hasAmsler(boolean v) { this.hasAmsler = v; return this; }
    public CheckSummaryBuilder hasMChart(boolean v) { this.hasMChart = v; return this; }

    public CheckSummary build() {
        return new CheckSummary(dateTime, hasSight, hasPresbyopia, hasAmsler, hasMChart);
    }
}
