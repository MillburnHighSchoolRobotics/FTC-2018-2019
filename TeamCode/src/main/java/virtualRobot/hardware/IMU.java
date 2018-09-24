package virtualRobot.hardware;

import virtualRobot.utils.Vector3f;

/**
 * Created by ethan on 9/21/17.
 */

public class IMU {
    private AxisSensor linearAccel;
    private AxisSensor totalAccel;

    private AxisSensor angularVelocity;

    private Sensor pitch, roll;
    private ContinuousRotationSensor yaw;

    public volatile long linearAcquisition, totalAcquisition, angleAcquisition;

    public IMU() {
        linearAccel = new AxisSensor();
        totalAccel = new AxisSensor();
        linearAcquisition = 0;
        totalAcquisition = 0;

        angularVelocity = new AxisSensor();

        yaw = new ContinuousRotationSensor();
        pitch = new Sensor();
        roll = new Sensor();
        angleAcquisition = 0;
    }

    public void setLinearAccel(Vector3f v) {
        linearAccel.setRawValue(v);
    }

    public void setTotalAccel(Vector3f v) {
        totalAccel.setRawValue(v);
    }

    public void clearLinearAccel() {
        linearAccel.clearValue();
    }

    public void clearTotalAccel() {
        totalAccel.clearValue();
    }

    public Vector3f getLinearAccel() {
        return linearAccel.getValueVector();
    }

    public Vector3f getTotalAccel() {
        return totalAccel.getValueVector();
    }

    public void setAngularVelocity(Vector3f v) { angularVelocity.setRawValue(v); }

    public void clearAngularVelocity() { angularVelocity.clearValue(); }

    public Vector3f getAngularVelocity() { return angularVelocity.getValueVector(); }

    public void setYaw(double x) {
        yaw.setRawValue(x);
    }

    public void setPitch(double x) {
        pitch.setRawValue(x);
    }

    public void setRoll(double x) {
        roll.setRawValue(x);
    }

    public double getHeading() {
        return yaw.getValue();
    }

    public double getPitch() {
        return pitch.getValue();
    }

    public double getRoll() {
        return roll.getValue();
    }

    public double getRawHeading() {
        return yaw.getRawValue();
    }

    public double getRawPitch() {
        return pitch.getRawValue();
    }

    public double getRawRoll() {
        return roll.getRawValue();
    }

    public void clearHeading() {
        yaw.clearValue();
    }

    public void clearPitch() {
        pitch.clearValue();
    }

    public void clearRoll() {
        roll.clearValue();
    }

    public String toString() { return "[" + getHeading() + "," + getPitch() + "," + getRoll() + "]"; }
}
