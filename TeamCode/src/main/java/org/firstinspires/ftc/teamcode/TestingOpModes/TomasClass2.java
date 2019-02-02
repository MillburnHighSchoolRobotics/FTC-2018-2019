package org.firstinspires.ftc.teamcode.TestingOpModes;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

import virtualRobot.utils.MathUtils;

@TeleOp(name = "Tomas Class 2: Electric Boogaloo")
//@Disabled
public class TomasClass2 extends OpMode {
    private DcMotor motor1;
    private DcMotor motor2;
    @Override
    public void init() {
        motor1 = hardwareMap.dcMotor.get("liftLeft");
        motor2 = hardwareMap.dcMotor.get("liftRight");
        motor1.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        motor2.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }

    @Override
    public void loop() {
        int directionLeft = 1;
        int directionRight = 1;
        if (gamepad1.left_bumper) {
            directionLeft = -1;
        }
        if (gamepad1.right_bumper) {
            directionRight = -1;
        }
        if (!MathUtils.equals(gamepad1.left_trigger, 0, 0.05)) {
            motor1.setPower(gamepad1.left_trigger * 0.5 * directionLeft);
        } else {
            motor1.setPower(0);
        }
        if (!MathUtils.equals(gamepad1.right_trigger, 0, 0.05)) {
            motor2.setPower(gamepad1.right_trigger * 0.5 * directionRight);
        } else {
            motor2.setPower(0);
        }
    }
}
