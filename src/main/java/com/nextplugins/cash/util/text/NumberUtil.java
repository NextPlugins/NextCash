package com.nextplugins.cash.util.text;

import com.nextplugins.cash.configuration.GeneralConfiguration;

import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.List;

public final class NumberUtil {

    private static final DecimalFormat numberFormat = new DecimalFormat("#,###.#");
    private static final List<String> chars = Arrays.asList("", "K", "M", "B", "T", "Q", "QQ", "S", "SS", "O", "N", "D",
            "UN", "DD", "TR", "QT", "QN", "SD", "SPD", "OD", "ND", "VG", "UVG", "DVG", "TVG", "QTV");
    private static final String FORMAT_TYPE = GeneralConfiguration.get(GeneralConfiguration::formatType);

    public static String letterFormat(double startNumber) {
        int index = 0;
        double number = startNumber;
        while (number / 1000.0D >= 1.0D) {
            number /= 1000.0D;
            index++;
        }

        String character = index < chars.size() ? chars.get(index) : "";
        return numberFormat.format(number) + character;
    }

    public static String decimalFormat(double number) {
        return numberFormat.format(number);
    }

    public static String format(double number) {
        if (FORMAT_TYPE.equalsIgnoreCase("decimal")) {
            return decimalFormat(number);
        } else if (FORMAT_TYPE.equalsIgnoreCase("letter")) {
            return letterFormat(number);
        } else {
            throw new IllegalArgumentException("Opção de formatação inválida: " + FORMAT_TYPE);
        }
    }

}
