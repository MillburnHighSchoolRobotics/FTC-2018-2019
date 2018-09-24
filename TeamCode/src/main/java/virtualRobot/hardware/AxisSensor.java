package virtualRobot.hardware;

import virtualRobot.utils.Vector3f;

/**
 * Created by ethachu19 on 12/5/2016.
 */

public class AxisSensor {
    Vector3f values = new Vector3f();
    Vector3f offset = new Vector3f();

    public synchronized void clearValue() {
        synchronized (this) {
            offset = new Vector3f(values);
        }
    }

    //return the current softValue of the sensor
    public synchronized double getValueX() {
        double retVal = 0;
        synchronized (this) {
            retVal = values.subtract(offset).x;
        }
        return retVal;
    }

    public synchronized double getValueY() {
        double retVal = 0;
        synchronized (this) {
            retVal = values.subtract(offset).y;
        }
        return retVal;
    }

    public synchronized double getValueZ() {
        double retVal = 0;
        synchronized (this) {
            retVal = values.subtract(offset).z;
        }
        return retVal;
    }

    //allows the UpdateThread to set the HardValue
    public synchronized void setRawValue(Vector3f hardValue) {
        synchronized (this) {
            this.values = hardValue;
        }
    }

    public synchronized double getRawValueX() {
        return getValueVector().x;
    }

    public synchronized double getRawValueY() {
        return getValueVector().y;
    }

    public synchronized double getRawValueZ() {
        return getValueVector().z;
    }

    public synchronized Vector3f getValueVector() {
        return new Vector3f(values).subtract(offset);
    }
}
