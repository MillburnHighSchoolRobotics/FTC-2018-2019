package org.firstinspires.ftc.teamcode.TestingOpModes;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.AnalogInput;
import com.qualcomm.robotcore.hardware.UltrasonicSensor;

/**
 * Created by Yanjun on 12/9/2015.
 */
@Disabled
public class SonarTest extends OpMode {

    UltrasonicSensor sonar;
    //ColorSensor colorSensor;


    @Override
    public void init() {
        sonar = hardwareMap.ultrasonicSensor.get("sonar1");
        //ultrasonicSensor = hardwareMap.ultrasonicSensor.get("ultrasonic");
        //colorSensor = hardwareMap.colorSensor.get("color");
    }

    @Override
    public void loop() {
        telemetry.addData("Sonar: ", sonar.getUltrasonicLevel());
        /*telemetry.addData("Sonar: ", ultrasonicSensor.getUltrasonicLevel());
        telemetry.addData("Color Red: ", colorSensor.red());
        telemetry.addData("Color Green: ", colorSensor.green());
        telemetry.addData("Color Blue: ", colorSensor.blue());
        telemetry.addData("Color Alpha: ", colorSensor.alpha());*/

    }
}
