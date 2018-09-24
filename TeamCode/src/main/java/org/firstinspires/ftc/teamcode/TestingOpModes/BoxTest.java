package org.firstinspires.ftc.teamcode.TestingOpModes;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.Servo;

/**
 * Created by Ethan Mak on 11/30/2017.
 */
@Disabled
public class BoxTest extends OpMode {
    Servo boxLeft, boxRight, intakeLeft, intakeRight, clawLeft, clawRight;
    @Override
    public void init() {
        boxLeft = hardwareMap.servo.get("boxLeft");
        boxRight = hardwareMap.servo.get("boxRight");
        intakeLeft = hardwareMap.servo.get("intakeLeft");
        intakeRight = hardwareMap.servo.get("intakeRight");
        clawLeft = hardwareMap.servo.get("clawLeft");
        clawRight = hardwareMap.servo.get("clawRight");

        clawLeft.setPosition(230/254);
        clawRight.setPosition(70/254);
    }

    @Override
    public void loop() {
        if(gamepad1.a) {
            boxLeft.setPosition(0);
            boxRight.setPosition(1);
        } else if(gamepad1.b) {
            boxLeft.setPosition(1);
            boxRight.setPosition(0);
        } else {
            boxLeft.setPosition(0.5);
            boxRight.setPosition(0.5);
        }

        if(gamepad1.x) {
            intakeLeft.setPosition(0);
            intakeRight.setPosition(1);
        } else if(gamepad1.y) {
            intakeLeft.setPosition(1);
            intakeRight.setPosition(0);
        } else {
            intakeLeft.setPosition(0.5);
            intakeRight.setPosition(0.5);
        }
    }
}
