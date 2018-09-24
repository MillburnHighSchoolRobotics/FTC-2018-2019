package virtualRobot.hardware;

/**
 * Created by shant on 12/10/2015.
 * Virtual Color Sensor. Can red, blue, green, and alpha of sensor
 * Form of ARGB
 */
public class ColorSensor extends Sensor {

    public double getRed() {
        int color = (int) getValue();
        return (color >> 16) & 0xFF;
    }

    public double getBlue() {
        int color = (int) getValue();
        return color & 0xFF;
    }

    public double getGreen() {
        int color = (int) getValue();
        return (color >> 8) & 0xFF;
    }

    public double getAlpha() {
        int color = (int) getValue();
        return (color >> 24) & 0xFF;
    }
}
