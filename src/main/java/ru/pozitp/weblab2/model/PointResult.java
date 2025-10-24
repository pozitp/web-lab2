package ru.pozitp.weblab2.model;

import ru.pozitp.weblab2.util.NumberFormatter;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Objects;

public record PointResult(BigDecimal x, BigDecimal y, BigDecimal r, boolean hit, OffsetDateTime processedAt,
                          BigDecimal processingTimeMs) {
    private static final DateTimeFormatter DISPLAY_FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss", Locale.ROOT);

    public PointResult(BigDecimal x, BigDecimal y, BigDecimal r, boolean hit, OffsetDateTime processedAt, BigDecimal processingTimeMs) {
        this.x = Objects.requireNonNull(x, "x");
        this.y = Objects.requireNonNull(y, "y");
        this.r = Objects.requireNonNull(r, "r");
        this.hit = hit;
        this.processedAt = Objects.requireNonNull(processedAt, "processedAt");
        this.processingTimeMs = Objects.requireNonNull(processingTimeMs, "processingTimeMs");
    }

    public BigDecimal getX() {
        return x;
    }

    public BigDecimal getY() {
        return y;
    }

    public BigDecimal getR() {
        return r;
    }

    public boolean isHit() {
        return hit;
    }

    public OffsetDateTime getProcessedAt() {
        return processedAt;
    }

    public BigDecimal getProcessingTimeMs() {
        return processingTimeMs;
    }

    public String getXFormatted() {
        return NumberFormatter.format(x);
    }

    public String getYFormatted() {
        return NumberFormatter.format(y);
    }

    public String getRFormatted() {
        return NumberFormatter.format(r);
    }

    public String getProcessingTimeFormatted() {
        return NumberFormatter.format(processingTimeMs);
    }
}