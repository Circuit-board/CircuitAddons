package cool.circuit.circuitAddons.utils;

import java.util.List;

public class NumberFormatter {
    private static final List<String> ILLIONS = List.of(
            "thousand", "million", "billion", "trillion"
    );

    private static int getBase(int number) {
        int base = 0;
        while (number >= 1000 && base < ILLIONS.size()) {
            number /= 1000;
            base++;
        }
        return base - 1; // Adjust for zero-based index
    }

    public static String formatNumber(int number) {
        if (number < 1000) {
            return String.valueOf(number);
        }

        int base = getBase(number);
        double value = number / Math.pow(1000, base + 1); // Get decimal part

        return String.format("%.1f %s", value, ILLIONS.get(base));
    }
}
