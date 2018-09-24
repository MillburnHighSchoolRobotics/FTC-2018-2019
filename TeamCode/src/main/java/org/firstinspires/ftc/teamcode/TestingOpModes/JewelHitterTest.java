package org.firstinspires.ftc.teamcode.TestingOpModes;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;

/**
 * Created by david on 1/26/18.
 */
@Disabled
@TeleOp(name = "Jewel Hitter Test", group = "Testing")
public class JewelHitterTest extends OpMode {
    Servo hitter;
    @Override
    public void init() {
        hitter = hardwareMap.servo.get("jewelHitter");
    }

    @Override
    public void loop() {
        if (gamepad1.dpad_up) {
            hitter.setPosition(1);
        } else if (gamepad1.dpad_down) {
            hitter.setPosition(0);
        } else if (gamepad1.dpad_left || gamepad1.dpad_right) {
            hitter.setPosition(0.5);
        }
    }
}
