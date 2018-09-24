package org.firstinspires.ftc.teamcode.TestingOpModes;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.Servo;

/**
 * Created by david on 2/9/18.
 */
@Disabled
@TeleOp(name = "Jewel Arm Full Motion", group = "Testing")
public class JewelArmFullMotion extends OpMode {
    private Servo vertical;
    private Servo horizontal;
    private ColorSensor colorSensor;
    @Override
    public void init() {
        vertical = hardwareMap.servo.get("jewelArm");
        horizontal = hardwareMap.servo.get("jewelHitter");
        colorSensor = hardwareMap.colorSensor.get("jewelColorSensor");
    }

    @Override
    public void loop() {
        double vert = (gamepad1.right_stick_y + 1) / 2;
        double horiz = (gamepad1.right_stick_x + 1) / 2;
        vertical.setPosition(vert);
        horizontal.setPosition(horiz);
        telemetry.addData("Vert", vert);
        telemetry.addData("Horiz", horiz);
        if (gamepad1.right_stick_button) {
            int red = colorSensor.red();
            int green = colorSensor.green();
            int blue = colorSensor.blue();
            telemetry.addData("Red", red);
            telemetry.addData("Blue", blue);
            telemetry.addData("Green", green);
        }
    }
}
