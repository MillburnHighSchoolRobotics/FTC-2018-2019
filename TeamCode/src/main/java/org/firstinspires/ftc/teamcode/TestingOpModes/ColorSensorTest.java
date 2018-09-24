package org.firstinspires.ftc.teamcode.TestingOpModes;

import com.qualcomm.hardware.adafruit.AdafruitI2cColorSensor;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.ColorSensor;

/**
 * Created by Ethan Mak on 1/22/2018.
 */

@Autonomous(name="Sensor: Color Sensor Test", group="Sensor")
public class ColorSensorTest extends OpMode {
    ColorSensor sensorRGB;

    public void init(){
        sensorRGB = hardwareMap.colorSensor.get("colorSensor");
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
