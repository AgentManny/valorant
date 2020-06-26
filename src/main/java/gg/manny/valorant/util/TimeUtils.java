package gg.manny.valorant.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class TimeUtils {

    private static final ThreadLocal<StringBuilder> mmssBuilder = ThreadLocal.withInitial(StringBuilder::new);
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm");

    // Static utility class -- cannot be created.
    private TimeUtils() {
    }

    /**
     * Delegate to TimeUtils#formatIntoMMSS for backwards compat
     */
    public static String formatIntoHHMMSS(int secs) {
        return TimeUtils.formatIntoMMSS(secs);
    }

    public static String formatLongIntoHHMMSS(long secs) {
        int unconvertedSeconds = (int)secs;
        return TimeUtils.formatIntoMMSS(unconvertedSeconds);
    }

    /**
     * Formats the time into a format of HH:MM:SS. Example: 3600 (1 hour) displays as '01:00:00'
     *
     * @param secs The input time, in seconds.
     * @return The HH:MM:SS formatted time.
     */
    public static String formatIntoMMSS(int secs) {
        int seconds = secs % 60;
        long minutesCount = (secs -= seconds) / 60;
        long minutes = minutesCount % 60L;
        long hours = (minutesCount -= minutes) / 60L;
        StringBuilder result = mmssBuilder.get();
        result.setLength(0);
        if (hours > 0L) {
            if (hours < 10L) {
                result.append("0");
            }
            result.append(hours);
            result.append(":");
        }
        if (minutes < 10L) {
            result.append("0");
        }
        result.append(minutes);
        result.append(":");
        if (seconds < 10) {
            result.append("0");
        }
        result.append(seconds);
        return result.toString();
    }

    public static String formatLongIntoMMSS(long secs) {
        int unconvertedSeconds = (int)secs;
        return TimeUtils.formatIntoMMSS(unconvertedSeconds);
    }

    /**
     * Formats time into a detailed format. Example: 600 seconds (10 minutes) displays as '10 minutes'
     *
     * @param secs The input time, in seconds.
     * @return The formatted time.
     */
    public static String formatIntoDetailedString(int secs) {
        if (secs == 0) {
            return "0 seconds";
        }
        int remainder = secs % 86400;

        int days = secs / 86400;
        int hours = remainder / 3600;
        int minutes = (remainder / 60) - (hours * 60);
        int seconds = (remainder % 3600) - (minutes * 60);

        String fDays = (days > 0 ? " " + days + " day" + (days > 1 ? "s" : "") : "");
        String fHours = (hours > 0 ? " " + hours + " hour" + (hours > 1 ? "s" : "") : "");
        String fMinutes = (minutes > 0 ? " " + minutes + " minute" + (minutes > 1 ? "s" : "") : "");
        String fSeconds = (seconds > 0 ? " " + seconds + " second" + (seconds > 1 ? "s" : "") : "");

        return ((fDays + fHours + fMinutes + fSeconds).trim());
    }

    public static String formatIntoSimplifiedString(int secs) {
        if (secs == 0) {
            return "0s";
        }

        int remainder = secs % 86400;

        int days = secs / 86400;
        int hours = remainder / 3600;
        int minutes = (remainder / 60) - (hours * 60);
        int seconds = (remainder % 3600) - (minutes * 60);

        String fDays = (days > 0 ? " " + days + "d" : "");
        String fHours = (hours > 0 ? " " + hours + "h" : "");
        String fMinutes = (minutes > 0 ? " " + minutes + "m" : "");
        String fSeconds = (seconds > 0 ? " " + seconds + "s" : "");

        return ((fDays + fHours + fMinutes + fSeconds).trim());
    }


    public static String formatLongIntoDetailedString(long secs) {
        int unconvertedSeconds = (int)secs;
        return TimeUtils.formatIntoDetailedString(unconvertedSeconds);
    }

    /**
     * Formats time into a format of MM/dd/yyyy HH:mm.
     *
     * @param date The Date instance to format.
     * @return The formatted time.
     */
    public static String formatIntoCalendarString(Date date) {
        return dateFormat.format(date);
    }

    /**
     * Parses a string, such as '1h4m25s' into a number of seconds.
     *
     * @param time The string to attempt to parse.
     * @return The number of seconds 'in' the given string.
     */
    public static int parseTime(String time) {
        if (time.equals("0") || time.equals("")) {
            return (0);
        }

        String[] lifeMatch = new String[]{"w", "d", "h", "m", "s"};
        int[] lifeInterval = new int[]{604800, 86400, 3600, 60, 1};
        int seconds = 0;

        for (int i = 0; i < lifeMatch.length; i++) {
            Matcher matcher = Pattern.compile("([0-9]*)" + lifeMatch[i]).matcher(time);

            while (matcher.find()) {
                seconds += Integer.parseInt(matcher.group(1)) * lifeInterval[i];
            }
        }

        return (seconds);
    }

    public static long parseTimeToLong(String time) {
        int unconvertedSeconds = TimeUtils.parseTime(time);
        long seconds = unconvertedSeconds;
        return seconds;
    }

    public static int getSecondsBetween(Date a, Date b) {
        return (int)TimeUtils.getSecondsBetweenLong(a, b);
    }

    /**
     * Gets the seconds between date A and date B. This will never return a negative number.
     *
     * @param a Date A
     * @param b Date B
     * @return The number of seconds between date A and date B.
     */
    public static long getSecondsBetweenLong(Date a, Date b) {
        long diff = a.getTime() - b.getTime();
        long absDiff = Math.abs(diff);
        return absDiff / 1000L;
    }
}

