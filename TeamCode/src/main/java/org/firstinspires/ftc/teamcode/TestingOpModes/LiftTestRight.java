package org.firstinspires.ftc.teamcode.TestingOpModes;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

/**
 * Created by shant on 2/13/2016.
 */
@Disabled
@TeleOp(name = "Test: Right Lift", group = "Tests")
public class LiftTestRight extends OpMode {
    DcMotor motor1;
    double initMotorEncoder;
    double motorEncoder = 0;
    @Override
    public void init() {
        motor1 = hardwareMap.dcMotor.get("liftRight");
        initMotorEncoder = motor1.getCurrentPosition();
    }

    @Override
    public void loop() {
        if (gamepad1.a && gamepad1.b) {

        }
        else {
            if (gamepad1.a) {
                motor1.setPower(0.6);
            }
            else if (gamepad1.b) {
                motor1.setPower(-0.6);
            }
            else {
                motor1.setPower(0);
            }
        }
        motorEncoder = motor1.getCurrentPosition() - initMotorEncoder;

        telemetry.addData("motor Encoder", motorEncoder);

    }
}
