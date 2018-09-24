package virtualRobot.hardware;

import com.qualcomm.robotcore.hardware.*;
import com.qualcomm.robotcore.hardware.ColorSensor;

/**
 * Created by shant on 10/8/2015.
 *
 * A class that reads in values from the sensors, including motors.
 * All sensors and encoders should extend this class
 * Motor Encoders, Color Sensor, etc should use this class
 */
public class Sensor {
    protected volatile double hardValue;
    protected volatile double offset;


    //Soft clears a sensor or encoder value
    public synchronized void clearValue() {
    		offset = hardValue;
    }

    //return the current softValue of the sensor
    public synchronized double getValue() {
        return hardValue - offset;
    }

    public synchronized void setValue(double softValue) {
        hardValue = softValue + offset;
    }

    //allows the UpdateThread to set the HardValue
    public synchronized void setRawValue(double hardValue) {
    	this.hardValue = hardValue;
    }

    public synchronized double getRawValue() {
    	return hardValue;
    }

    public synchronized void incrementRawValue(double delta) {
    		this.hardValue += delta;
    }

    public synchronized void copyFrom(HardwareDevice sensor) {
        if (sensor instanceof com.qualcomm.robotcore.hardware.ColorSensor) {
            ColorSensor cs = (ColorSensor)sensor;
            this.hardValue = cs.argb();
            return;
        }
    }

}
