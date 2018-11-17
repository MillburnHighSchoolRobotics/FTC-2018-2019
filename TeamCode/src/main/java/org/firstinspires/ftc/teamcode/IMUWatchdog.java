package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class IMUWatchdog extends Watchdog {
    private static final String TAG = "IMUWatchdog";
    private String imuTag;
    private final BNO055IMU imu;
    private int rotation;
    private int turns;
    public IMUWatchdog(Thread parentThread, HardwareMap hardwareMap, String imuTag) {
        super(parentThread, hardwareMap);
        this.imuTag = imuTag;
        BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();
        parameters.angleUnit = BNO055IMU.AngleUnit.DEGREES;
        parameters.accelUnit = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC;
        parameters.calibrationDataFile = "BNO055IMUCalibration.json"; // see the calibration sample opmode
        parameters.loggingEnabled = false;
        parameters.loggingTag = "IMU";
        parameters.accelerationIntegrationAlgorithm = null;//new JustLoggingAccelerationIntegrator();

        synchronized (this.hardwareMap) {
            this.imu = hardwareMap.get(BNO055IMU.class, this.imuTag);
        }
        synchronized (imu) {
            imu.initialize(parameters);
        }
        rotation = -1;
        turns = 0;
    }
    @Override
    protected void loop() {
        int newRot = (int)imu.getAngularOrientation().firstAngle;
        if (Math.abs(newRot - rotation) > 180) {
            if (newRot > 180) {
                turns--;
            } else {
                turns++;
            }
        }
        rotation = newRot;
        WatchdogManager.getInstance().setValue("rotation", turns * 360 + rotation);
    }
}
