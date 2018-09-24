package org.firstinspires.ftc.teamcode.TestingOpModes;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.Servo;

/**
 * Created by ethachu19 on 10/21/2016.
 */
@Disabled
@TeleOp(name = "CRServoTest", group = "Test Components")
public class CRServoTest extends OpMode {

    CRServo servo;
    double pos;
    double power;

    @Override
    public void init() {
        servo = hardwareMap.crservo.get("crservo1");
        pos = 0;
        power = 0;
        servo.setPower(0);
    }

    @Override
    public void loop() {
        if (gamepad1.a) pos -= 0.02;
        else if (gamepad1.b) pos += 0.02;

        pos = Math.max(Math.min(pos, 1), 0);

        servo.getController().setServoPosition(servo.getPortNumber(),pos);
//        servo.setPower(power);
    }
}
