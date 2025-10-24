package ru.pozitp.weblab2.model;

import java.math.BigDecimal;
import java.util.Objects;

public record PointRequest(BigDecimal x, BigDecimal y, BigDecimal r) {
    public PointRequest(BigDecimal x, BigDecimal y, BigDecimal r) {
        this.x = Objects.requireNonNull(x, "x");
        this.y = Objects.requireNonNull(y, "y");
        this.r = Objects.requireNonNull(r, "r");
    }
}