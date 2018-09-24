package org.firstinspires.ftc.teamcode.TestingOpModes;

import com.qualcomm.hardware.adafruit.AdafruitI2cColorSensor;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

/**
 * Created by david on 12/14/17.
 * class made by mehmet for weird adafruit stuff
 */
@Disabled
@Autonomous(name = "Autonomous: adafruitRGBTest", group = "Testing")
public class AdafruitRGBTest extends OpMode{
    AdafruitI2cColorSensor sensorRGB;

    public void init(){
        sensorRGB = new AdafruitI2cColorSensor(hardwareMap.i2cDeviceSynch.get("adafruitRGB"));
        sensorRGB.initialize();
    }
    public void loop() {
        telemetry.addData("argb: ", sensorRGB.argb());
        telemetry.addData("r: ", sensorRGB.red());
        telemetry.addData("g: ", sensorRGB.green());
        telemetry.addData("b: ", sensorRGB.blue());
    }

    public void stop() {
        sensorRGB.close();
    }
}
