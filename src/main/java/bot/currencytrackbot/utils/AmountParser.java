package bot.currencytrackbot.utils;

import java.math.BigDecimal;

public final class AmountParser {

    private static final String AMOUNT_PATTERN = "^-?\\d+(\\.\\d{1,2})?$";

    public static BigDecimal parseAmount(String raw) {
        if (raw == null) {
            throw new IllegalArgumentException("Сумма не указана");
        }

        String normalized = raw.trim()
                .replace(" ", "")
                .replace(",", ".");

        if (normalized.isEmpty()) {
            throw new IllegalArgumentException("Сумма пустая");
        }

        if (!normalized.matches(AMOUNT_PATTERN)) {
            throw new IllegalArgumentException("Некорректный формат суммы. Пример: 100 или 100.50");
        }

        BigDecimal amount = new BigDecimal(normalized);

        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Сумма должна быть больше нуля");
        }

        return amount;
    }

    public static boolean isValid(String raw) {
        if (raw == null) return false;

        String normalized = raw.trim()
                .replace(" ", "")
                .replace(",", ".");

        return normalized.matches(AMOUNT_PATTERN);
    }
}
