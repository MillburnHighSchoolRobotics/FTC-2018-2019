package com.kauailabs.navx.ftc;



import com.qualcomm.robotcore.hardware.DeviceInterfaceModule;

import virtualRobot.utils.BetterLog;

/**
 * Created by Yanjun on 11/24/2015.
 * Our extentsion of the methods provides to us by the manufacturers of the imu.
 * This is what should always be used in code.
 */
public class MPU9250 extends AHRS {
    private double prevYaw, prevPitch, prevRoll;
    private double yawOffset, pitchOffset, rollOffset;
    private double xOffset, yOffset, zOffset;


    protected MPU9250(DeviceInterfaceModule dim, int dim_i2c_port,
                   DeviceDataType data_type, int update_rate_hz) {
        super(dim, dim_i2c_port, data_type, update_rate_hz);

        xOffset = yawOffset = zOffset = 0;
        prevYaw = prevPitch = prevRoll = 0;
        yawOffset = pitchOffset = rollOffset = 0;
    }

    protected MPU9250(DeviceInterfaceModule dim, AHRS instance) {
        super(dim, instance.getDimI2cPort(), instance.getDeviceDataType(), NAVX_DEFAULT_UPDATE_RATE_HZ);
        xOffset = yawOffset = zOffset = 0;
        prevYaw = prevPitch = prevRoll = 0;
        yawOffset = pitchOffset = rollOffset = 0;
    }

    public static MPU9250 getInstance(DeviceInterfaceModule dim, int port) {
//        MPU9250 imu = null;
//        try {
//            imu = new MPU9250(dim, instance);
//        }
        if (instance == null) {
//        catch (NullPointerException ex){
            instance = new MPU9250(dim, port, DeviceDataType.kProcessedData, NAVX_DEFAULT_UPDATE_RATE_HZ);
        } else {
            BetterLog.d("IMUTesting", "Instance is " + instance + "MPU? " + (instance instanceof MPU9250));
        }
//        return imu;
        return (MPU9250) instance;
    }

    @Override
    public void zeroYaw() {
        super.zeroYaw();

        prevYaw = 0;
        yawOffset = 0;
    }

    public double getIntegratedYaw() {
        if (Math.abs(getYaw() - prevYaw) > 200) {
            if (getYaw() < prevYaw) {
                yawOffset += 360;
            }

            if (getYaw() > prevYaw) {
                yawOffset -= 360;
            }
        }

        prevYaw = getYaw();
        return getYaw() + yawOffset;
    }
    
    public double getIntegratedPitch() {
        if (Math.abs(getPitch() - prevPitch) > 200) {
            if (getPitch() < prevPitch) {
                pitchOffset += 360;
            }

            if (getPitch() > prevPitch) {
                pitchOffset -= 360;
            }
        }

        prevPitch = getPitch();
        return getPitch() + pitchOffset;
    }

    public void zeroPitch() {
        pitchOffset = -getPitch();
    }

    public double getIntegratedRoll() {
        if (Math.abs(getRoll() - prevRoll) > 200) {
            if (getRoll() < prevRoll) {
                rollOffset += 360;
            }

            if (getRoll() > prevRoll) {
                rollOffset -= 360;
            }
        }

        prevRoll = getRoll();
        return getRoll() + rollOffset;
    }

    public void zeroRoll() { rollOffset = -getRoll(); }

    public double getIntegratedAccelX() { return getRawAccelX() - xOffset; }

    public void zeroAccelX() { xOffset = getRawAccelX(); }

    public double getIntegratedAccelY() { return getRawAccelY() - yOffset; }

    public void zeroAccelY() { yOffset = getRawAccelY(); }

    public double getIntegratedAccelZ() { return getRawAccelZ() - zOffset; }

    public void zeroAccelZ() { zOffset = getRawAccelZ(); }

    public void zeroAccel(){
        zeroAccelX();
        zeroAccelY();
        zeroAccelZ();
    }
}
