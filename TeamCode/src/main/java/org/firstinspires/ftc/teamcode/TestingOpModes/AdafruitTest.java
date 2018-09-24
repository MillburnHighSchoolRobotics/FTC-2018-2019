package org.firstinspires.ftc.teamcode.TestingOpModes;

import com.qualcomm.hardware.adafruit.AdafruitI2cColorSensor;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

/**
 * Created by Ethan Mak on 12/16/2017.
 */
@Disabled
@Autonomous(name="Sensor: Adafruit Color Sensor", group = "Sensor")
public class AdafruitTest extends OpMode {
    AdafruitI2cColorSensor sensor;
    @Override
    public void init() {
        sensor = (AdafruitI2cColorSensor) hardwareMap.get("colorSensor");
        sensor.initialize();
    }

    @Override
    public void loop() {
        telemetry.addData("RED: ", sensor.red());
        telemetry.addData("GREEN: ", sensor.green());
        telemetry.addData("BLUE: ", sensor.blue());
        telemetry.addData("ALPHA: ", sensor.alpha());
    }

    public void stop(){
        sensor.close();
    }
}
