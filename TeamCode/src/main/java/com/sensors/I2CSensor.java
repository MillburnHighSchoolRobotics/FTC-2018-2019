package com.sensors;

import com.qualcomm.robotcore.hardware.HardwareDevice;
import com.qualcomm.robotcore.hardware.I2cDeviceSynch;
import com.qualcomm.robotcore.hardware.I2cDeviceSynchDevice;
import com.qualcomm.robotcore.hardware.I2cWaitControl;

/**
 * Created by Ethan Mak on 11/22/2017.
 */

public abstract class I2CSensor extends I2cDeviceSynchDevice<I2cDeviceSynch> {
    protected I2CSensor(I2cDeviceSynch i2cDeviceSynch, boolean deviceClientIsOwned) {
        super(i2cDeviceSynch, deviceClientIsOwned);
    }

    public interface Register {
        int getVal();
    }

    protected interface Command {
        int getVal();
    }

    protected byte read8(byte reg) {
        return this.deviceClient.read8(reg);
    }

    protected byte read8(Register reg) { return read8((byte)reg.getVal()); }

    protected byte[] read(byte reg, int num) {
        return this.deviceClient.read(reg, num);
    }

    protected byte[] read(Register reg, int num) { return read((byte)reg.getVal(), num); }

    protected void write8(int reg, int val) {
        this.deviceClient.write8(reg, val);
    }

    protected void write8(Register reg, int val) {
        write8(reg.getVal(), val);
    }

    protected void write8(Register reg, Command com) {
        write8(reg, com.getVal());
    }

    protected void write(int reg, byte[] data) {
        this.deviceClient.write(reg, data);
    }

    protected void write(Register reg, byte[] data) {
        write(reg.getVal(), data);
    }

    protected void write8(int reg, int val, I2cWaitControl waitControl) {
        this.deviceClient.write8(reg, val, waitControl);
    }

    protected void write(int reg, byte[] data, I2cWaitControl waitControl) {
        this.deviceClient.write(reg,data,waitControl);
    }

    protected void write8(Register reg, int val, I2cWaitControl waitControl) {
        write8(reg.getVal(), val, waitControl);
    }

    protected void write8(Register reg, Command com, I2cWaitControl waitControl) {
        write8(reg.getVal(), com.getVal(), waitControl);
    }

    protected void write(Register reg, byte[] val, I2cWaitControl waitControl) {
        write(reg.getVal(), val,waitControl);
    }

    protected boolean delay(int ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            e.printStackTrace();
            return true;
        }
        return false;
    }
}
