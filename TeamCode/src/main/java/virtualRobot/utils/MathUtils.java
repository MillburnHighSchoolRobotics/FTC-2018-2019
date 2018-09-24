package virtualRobot.utils;

/**
 * Created by 17osullivand on 9/26/16.
 * Various mathematical tools.
 */
public class MathUtils {
    private static final double epsilon = 0.01;
    private static final double epsilonSquared = 0.001;

    public static double sinDegrees(double  d) {
        return Math.sin(Math.toRadians(d));
    }
    public static double cosDegrees(double  d) {
        return Math.cos(Math.toRadians(d));
    }
    public static double tanDegrees(double d) { return Math.tan(Math.toRadians(d)); }
    public static double clamp(double number, double lowerBound, double upperBound) {
    	return Math.max(lowerBound, Math.min(upperBound, number));
    }
    public static double truncate(double d, int place) {
        return (double) ((int)(d * place)) / place;
    }
    public static boolean equals(double d, double e) {
        return equals(d,e,epsilon);
    }
    public static boolean equals(double d, double e, double tolerance) {
        if (Double.isNaN(d) || Double.isNaN(e))
            return false;
        return Math.abs(d-e) < tolerance;
    }

    public static double wrap(double d, double min, double max) {
        if (!equals(d, min) &&d < min) {
            d = max - (min - d);
        } else if (!equals(d, max) && d > max) {
            d = min + (d - max);
        }
        return d;
    }

    public static double standardDeviation(int[] values) {
        int len = values.length;
        double mean = 0;
        for (int i = 0; i < len; i++) {
            mean += values[i];
        }
        mean /= len;
        double stddev = 0;
        for (int i = 0; i < len; i++) {
            double diff = values[i] - mean;
            stddev += diff * diff;
        }
        stddev /= len;
        return Math.sqrt(stddev);
    }
}
