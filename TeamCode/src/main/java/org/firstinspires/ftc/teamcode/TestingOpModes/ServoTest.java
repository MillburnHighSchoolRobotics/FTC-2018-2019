package org.firstinspires.ftc.teamcode.TestingOpModes;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.Servo;

/**
 * Created by Ethan Mak on 2/19/2018.
 */

@Autonomous(name = "Testing: Servo Test", group = "Testing")
public class ServoTest extends OpMode {
    Servo left;
    Servo right;
    @Override
    public void init() {
        left = hardwareMap.servo.get("rollerLiftLeft");
        right = hardwareMap.servo.get("rollerLiftRight");
        left.setPosition(0);
        right.setPosition(1);
    }

    @Override
    public void loop() {
        left.setPosition(1);
        right.setPosition(0);
    }
}
