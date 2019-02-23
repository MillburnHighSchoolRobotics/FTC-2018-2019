package org.firstinspires.ftc.teamcode.wosbot;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

@Autonomous(name = "translate test")
public class WosBotTranslateTest extends LinearOpMode {
    @Override
    public void runOpMode() throws InterruptedException {
        DcMotor lf = hardwareMap.dcMotor.get("lf");
        DcMotor lb = hardwareMap.dcMotor.get("lb");
        DcMotor rf = hardwareMap.dcMotor.get("rf");
        DcMotor rb = hardwareMap.dcMotor.get("rb");
        rf.setDirection(DcMotorSimple.Direction.REVERSE);
        rb.setDirection(DcMotorSimple.Direction.REVERSE);
        lf.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        lb.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rf.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rb.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        WosBot mv = new WosBot(lf, lb, rf, rb);
        waitForStart();
        mv.translateDistance(0.7, 12);
        Thread.sleep(1000);

    }
}
