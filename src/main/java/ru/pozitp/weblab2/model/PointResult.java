package ru.pozitp.weblab2.model;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.Objects;

public record PointResult(BigDecimal x, BigDecimal y, BigDecimal r, boolean hit, OffsetDateTime processedAt,
                          BigDecimal processingTimeMs) {
    public PointResult(BigDecimal x, BigDecimal y, BigDecimal r, boolean hit, OffsetDateTime processedAt, BigDecimal processingTimeMs) {
        this.x = Objects.requireNonNull(x, "x");
        this.y = Objects.requireNonNull(y, "y");
        this.r = Objects.requireNonNull(r, "r");
        this.hit = hit;
        this.processedAt = Objects.requireNonNull(processedAt, "processedAt");
        this.processingTimeMs = Objects.requireNonNull(processingTimeMs, "processingTimeMs");
    }
}