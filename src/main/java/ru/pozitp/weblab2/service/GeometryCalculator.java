package ru.pozitp.weblab2.service;

import ru.pozitp.weblab2.model.PointRequest;

import java.math.BigDecimal;
import java.math.MathContext;

public final class GeometryCalculator {
    private static final BigDecimal TWO = BigDecimal.valueOf(2);
    private static final MathContext MC = MathContext.DECIMAL64;

    private static BigDecimal square(BigDecimal value) {
        return value.multiply(value, MC);
    }

    public boolean isHit(PointRequest request) {
        BigDecimal x = request.x();
        BigDecimal y = request.y();
        BigDecimal r = request.r();

        BigDecimal zero = BigDecimal.ZERO;
        if (r.compareTo(zero) <= 0) {
            return false;
        }

        BigDecimal halfR = r.divide(TWO, MC);

        if (x.compareTo(zero) >= 0 && y.compareTo(zero) >= 0) {
            return x.compareTo(halfR) <= 0 && y.compareTo(r) <= 0;
        }

        if (x.compareTo(zero) < 0 && y.compareTo(zero) > 0) {
            return false;
        }

        if (x.compareTo(zero) <= 0 && y.compareTo(zero) <= 0) {
            BigDecimal check = y.add(x, MC).add(r, MC);
            return check.compareTo(zero) >= 0;
        }

        if (x.compareTo(zero) >= 0 && y.compareTo(zero) <= 0) {
            BigDecimal distanceSquared = square(x).add(square(y), MC);
            BigDecimal radiusSquared = square(halfR);
            return distanceSquared.compareTo(radiusSquared) <= 0;
        }

        return false;
    }
}