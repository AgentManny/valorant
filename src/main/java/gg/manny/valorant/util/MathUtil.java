package gg.manny.valorant.util;

import com.google.common.base.Strings;
import org.bukkit.ChatColor;

import java.text.DecimalFormat;

public class MathUtil {

    public static String getProgressBar(float progress, float max, int bars, char symbol) {
        float percent = progress / max;
        int progressBars = (int) (bars * percent);

        return Strings.repeat("" + ChatColor.DARK_RED + symbol, progressBars)
                + Strings.repeat("" + ChatColor.GRAY + symbol, bars - progressBars);
    }

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
