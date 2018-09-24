package org.firstinspires.ftc.teamcode.TestingOpModes;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

/**
 * Created by david on 1/16/18.
 */
@Disabled
@Autonomous(name = "Rotate Test", group = "Testing")
public class RotateTest extends OpMode {
    DcMotor LF, RF, LB, RB;
    int lf, rf, lb, rb;
    @Override
    public void init() {
        LF = hardwareMap.dcMotor.get("leftFront");
        RF = hardwareMap.dcMotor.get("rightFront");
        LB = hardwareMap.dcMotor.get("leftBack");
        RB = hardwareMap.dcMotor.get("rightBack");
        RF.setDirection(DcMotorSimple.Direction.REVERSE);
        RB.setDirection(DcMotorSimple.Direction.REVERSE);
        lf = LF.getCurrentPosition();
        rf = RF.getCurrentPosition();
        lb = LB.getCurrentPosition();
        rb = RB.getCurrentPosition();
    }

    @Override
    public void loop() {
        if (gamepad1.a) {
            LF.setPower(1);
            RF.setPower(-1);
            RB.setPower(-1);
            LB.setPower(1);
        } else {
            LF.setPower(0);
            RF.setPower(0);
            RB.setPower(0);
            LB.setPower(0);
        }
        telemetry.addData("LF", LF.getCurrentPosition() - lf);
        telemetry.addData("RF", RF.getCurrentPosition() - rf);
        telemetry.addData("RB", RB.getCurrentPosition() - rb);
        telemetry.addData("LB", LB.getCurrentPosition() - lb);
    }
}
