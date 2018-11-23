package org.firstinspires.ftc.teamcode;

import android.util.Log;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class IMUWatchdog extends Watchdog {
    private static final String TAG = "IMUWatchdog";
    private final BNO055IMU imu;
    private float rotation;
    private String imuTag;
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
        synchronized (this.imu) {
            this.imu.initialize(parameters);
        }
        turns = 0;
    }
    @Override
    protected void loop() {
        float newRot;
        synchronized (imu) {
            newRot = imu.getAngularOrientation().firstAngle;
        }
        if (Math.abs(newRot - rotation) > 180) {
            if (newRot > 0) {
                turns--;
            } else {
                turns++;
            }
        }
        rotation = newRot;
        Log.d(TAG, "Rotation: " + turns + " turns, " + rotation + " degrees");
        WatchdogManager.getInstance().setValue("rotation", turns * 360 + rotation);
    }
}
