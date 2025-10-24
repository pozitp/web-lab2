package ru.pozitp.weblab2.service;

import ru.pozitp.weblab2.model.PointRequest;

import java.math.BigDecimal;
import java.util.*;

public final class InputValidator {
    private static final Set<String> ALLOWED_X_VALUES = Set.of("-4", "-3", "-2", "-1", "0", "1", "2", "3", "4");
    private static final Set<String> ALLOWED_R_VALUES = Set.of("1", "2", "3", "4", "5");
    private static final BigDecimal Y_MIN = new BigDecimal("-5");
    private static final BigDecimal Y_MAX = new BigDecimal("3");

    private static BigDecimal validateX(String value, List<String> errors) {
        if (value == null || value.isBlank()) {
            errors.add("X value is required.");
            return null;
        }
        if (!ALLOWED_X_VALUES.contains(value)) {
            errors.add("X must be one of the values from -4 to 4.");
            return null;
        }
        return new BigDecimal(value);
    }

    private static BigDecimal validateY(String value, List<String> errors) {
        if (value == null || value.isBlank()) {
            errors.add("Y value is required.");
            return null;
        }
        BigDecimal number;
        try {
            number = new BigDecimal(value.replace(',', '.'));
        } catch (NumberFormatException ex) {
            errors.add("Y must be a valid number.");
            return null;
        }
        if (number.compareTo(Y_MIN) < 0 || number.compareTo(Y_MAX) > 0) {
            errors.add(String.format(Locale.ROOT, "Y must be within the range [%s, %s].", Y_MIN, Y_MAX));
        }
        return number;
    }

    private static BigDecimal validateR(String value, List<String> errors) {
        if (value == null || value.isBlank()) {
            errors.add("Radius R must be selected.");
            return null;
        }
        if (!ALLOWED_R_VALUES.contains(value)) {
            errors.add("R must be one of: 1, 2, 3, 4, 5.");
            return null;
        }
        return new BigDecimal(value);
    }

    private static String normalize(String value) {
        return value == null ? "" : value.trim();
    }

    public ValidationResult validate(String rawX, String rawY, String rawR) {
        List<String> errors = new ArrayList<>();
        Map<String, String> normalized = new LinkedHashMap<>();

        String xValue = normalize(rawX);
        String yValue = normalize(rawY);
        String rValue = normalize(rawR);

        normalized.put("x", xValue);
        normalized.put("y", yValue);
        normalized.put("r", rValue);

        BigDecimal x = validateX(xValue, errors);
        BigDecimal y = validateY(yValue, errors);
        BigDecimal r = validateR(rValue, errors);

        if (!errors.isEmpty()) {
            return ValidationResult.withErrors(errors, normalized);
        }
        return ValidationResult.ok(new PointRequest(x, y, r), normalized);
    }

    public static final class ValidationResult {
        private final PointRequest payload;
        private final List<String> errors;
        private final Map<String, String> normalizedValues;

        private ValidationResult(PointRequest payload, List<String> errors, Map<String, String> normalizedValues) {
            this.payload = payload;
            this.errors = errors;
            this.normalizedValues = normalizedValues;
        }

        public static ValidationResult ok(PointRequest payload, Map<String, String> normalizedValues) {
            Objects.requireNonNull(payload, "payload");
            return new ValidationResult(payload, List.of(), Map.copyOf(normalizedValues));
        }

        public static ValidationResult withErrors(List<String> errors, Map<String, String> normalizedValues) {
            return new ValidationResult(null, List.copyOf(errors), Map.copyOf(normalizedValues));
        }

        public boolean hasErrors() {
            return !errors.isEmpty();
        }

        public List<String> getErrors() {
            return errors;
        }

        public Map<String, String> getNormalizedValues() {
            return normalizedValues;
        }

        public PointRequest getPayload() {
            return payload;
        }
    }
}