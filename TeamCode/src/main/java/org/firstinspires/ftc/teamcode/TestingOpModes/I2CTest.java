package org.firstinspires.ftc.teamcode.TestingOpModes;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.I2cAddr;
import com.qualcomm.robotcore.hardware.I2cController;
import com.qualcomm.robotcore.hardware.I2cDevice;
import com.qualcomm.robotcore.hardware.I2cDeviceSynch;
import com.qualcomm.robotcore.hardware.I2cDeviceSynchImpl;
import com.qualcomm.robotcore.hardware.I2cDeviceSynchSimple;

/**
 * Created by ethachu19 on 11/3/2016.
 */
@Disabled
@Autonomous(name = "Sensor: I2CTest", group = "Sensor")
public class I2CTest extends OpMode {
    I2cDeviceSynchSimple device;
    @Override
    public void init() {
        device = new I2cDeviceSynchImpl(hardwareMap.i2cDevice.get("sonarLeft"),I2cAddr.create7bit(0x70), true);
        device.setLogging(true);
        device.setLoggingTag("I2CLogging");
//        device.
        telemetry.addData("Software: ", device.isArmed()+" "+device.read8(0x00));
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    byte[] data;
    @Override
    public void loop() {
        device.write(0x00,new byte[]{0x50});
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        telemetry.addData("Error: ", "Done Initializing " + device.isArmed());
        data = device.read(0x02,2);
//        device.
        if (data.length == 0) {
            telemetry.addData("Error: ", "Buffer empty");
        } else {
            telemetry.addData("Response: ", (data[0] << data[1]));
            telemetry.addData("Error: ", "No Error");
        }
        try {
            Thread.sleep(10);
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
    }
}
