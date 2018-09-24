package virtualRobot.hardware;

/**
 * Created by 17osullivand on 11/4/16.
 */

public class UltrasonicSensor extends Sensor {
    public double getFilteredValue() {
        double levels[] = new double[10];
        for(int i = 0; i < levels.length; i++) {
            levels[i] = getValue();
        }
        for (int i = 1; i < levels.length; i++) {
            double temp = levels[i];
            int j;
            for (j = i - 1; j >= 0 && temp < levels[j]; j--) {
                levels[j+1] = levels[j];
            }
            levels[j+1] = temp;
        }
        return levels[levels.length/2];
    }

}
