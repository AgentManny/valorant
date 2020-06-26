package gg.manny.valorant.util;

import java.text.DecimalFormat;

public class MathUtil {

    /**
     * Round a number to two decimal points
     * @param d - The number to round
     * @param decimalPlaces - The amount of decimal places to round to
     * @return The rounded number
     */
    public static final double round(double d, int decimalPlaces){
        String format = "#.";
        for(int x = 1; x <= decimalPlaces; x++){
            format = format + "#";
        }
        DecimalFormat form = new DecimalFormat(format);
        return Double.parseDouble(form.format(d));
    }

    /**
     * Get the percent of two integers
     * @param n - The first integer
     * @param v - The second integer
     * @return The percent out of 100
     */
    public static final double getPercent(double n, double v){
        return round(((n * 100) / v), 1);
    }

    public static double randomDouble(double min, double max) {
        return Math.random() < 0.5 ? ((1 - Math.random()) * (max - min) + min) : (Math.random() * (max - min) + min);
    }

}
