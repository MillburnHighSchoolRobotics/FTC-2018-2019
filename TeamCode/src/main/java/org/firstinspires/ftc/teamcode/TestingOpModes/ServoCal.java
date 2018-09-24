package org.firstinspires.ftc.teamcode.TestingOpModes;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;

/**
 * Created by Yanjun on 2/6/2016.
 */
@Disabled
@TeleOp(name = "ServoTest", group = "Test Components")
public class ServoCal extends OpMode {

    Servo servo;
    double pos;

    @Override
    public void init() {
        servo = hardwareMap.servo.get("testServo");
        pos = 0;
    }

    @Override
    public void loop() {
        if (gamepad1.a) pos = 0;
        if (gamepad1.b) pos = 0.6;

        pos = Math.max(Math.min(pos, 1), 0);

        telemetry.addData("Pos: ", pos);

        servo.setPosition(pos);
    }
}
