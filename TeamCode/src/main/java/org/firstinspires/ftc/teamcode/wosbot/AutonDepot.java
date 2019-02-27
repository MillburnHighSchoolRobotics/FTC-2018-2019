package org.firstinspires.ftc.teamcode.wosbot;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import org.opencv.android.OpenCVLoader;

@Autonomous(name = "WosBot Auton Depot", group = "competition")
public class AutonDepot extends LinearOpMode {
    static {
        OpenCVLoader.initDebug();
    }
    DcMotor lf;
    DcMotor lb;
    DcMotor rf;
    DcMotor rb;

    @Override
    public void runOpMode() throws InterruptedException {

        lf = hardwareMap.dcMotor.get("leftFront");
        lb = hardwareMap.dcMotor.get("leftBack");
        rf = hardwareMap.dcMotor.get("rightFront");
        rb = hardwareMap.dcMotor.get("rightBack");

        waitForStart();

        rf.setDirection(DcMotorSimple.Direction.REVERSE);
        rb.setDirection(DcMotorSimple.Direction.REVERSE);
        lf.setDirection(DcMotorSimple.Direction.FORWARD);
        lb.setDirection(DcMotorSimple.Direction.FORWARD);
        initializeMotor(new DcMotor[]{lf, lb, rf, rb});

        WosBotMovement mv = new WosBotMovement(lf, lb, rf, rb);
        Thread.sleep(500);

        mv.translate(0, 12, 1);
    }
    public void initializeMotor(DcMotor[] motors) {
        for (DcMotor motor : motors) {
            motor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            motor.setPower(0);
        }
    }
}
