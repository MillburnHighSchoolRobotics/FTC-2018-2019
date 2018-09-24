package virtualRobot.hardware;

import android.util.Log;

import virtualRobot.utils.MathUtils;

/**
 * Created by david on 2/16/18.
 */

public class ContinuousRotationSensor extends Sensor {
    //offset is the amount of revolutions
    double lastAngle;
    boolean firstTime;

    public ContinuousRotationSensor() {
        super();
        lastAngle = hardValue;
        firstTime = true;
    }

    @Override
    public synchronized void setRawValue(double hardValue) {
        if (firstTime) {
            firstTime = false;
            lastAngle = hardValue;
            super.setRawValue(hardValue);
            return;
        }
        double delta = hardValue - lastAngle;
        if (MathUtils.equals(delta, 0, 0.0001))
            delta = 0;
        if (delta < -180) {
            this.hardValue += ((180-lastAngle) + (hardValue + 180));
        } else if (delta > 180) {
            this.hardValue -= ((lastAngle + 180) + (180 - hardValue));
        } else
            this.hardValue += delta;

        lastAngle = hardValue;
    }
}
