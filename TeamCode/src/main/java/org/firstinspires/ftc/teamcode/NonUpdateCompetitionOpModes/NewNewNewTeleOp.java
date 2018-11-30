package org.firstinspires.ftc.teamcode.NonUpdateCompetitionOpModes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import virtualRobot.utils.MathUtils;

import static com.qualcomm.robotcore.hardware.DcMotorSimple.Direction.REVERSE;

@TeleOp(name = "REAL TeleOp", group = "competition")
public class NewNewNewTeleOp extends OpMode {
    private DcMotorEx lf;
    private DcMotorEx lb;
    private DcMotorEx rf;
    private DcMotorEx rb;
    private DcMotorEx reaperL;
    private DcMotorEx reaperR;
    private DcMotorEx liftL;
    private DcMotorEx liftR;
//    private CRServo reaper;
    private Servo stopper;
    double power = 0.9;
    double gearing = 1;

    private ElapsedTime canToggleStopper;

    @Override
    public void init() {
        canToggleStopper = new ElapsedTime();
        this.msStuckDetectStop = 3000;
        lf = (DcMotorEx)hardwareMap.dcMotor.get("leftFront");
        lb = (DcMotorEx)hardwareMap.dcMotor.get("leftBack");
        rf = (DcMotorEx)hardwareMap.dcMotor.get("rightFront");
        rb = (DcMotorEx)hardwareMap.dcMotor.get("rightBack");
        liftL = (DcMotorEx)hardwareMap.dcMotor.get("liftL");
        liftR = (DcMotorEx)hardwareMap.dcMotor.get("liftR");
        reaperL = (DcMotorEx)hardwareMap.dcMotor.get("horizL");
        reaperR = (DcMotorEx)hardwareMap.dcMotor.get("horizR");
//        reaper = hardwareMap.crservo.get("reaper");
        stopper = hardwareMap.servo.get("stopper");

        lf.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        lb.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rf.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rb.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        liftL.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        liftR.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        lf.setDirection(DcMotorSimple.Direction.FORWARD);
        lb.setDirection(DcMotorSimple.Direction.FORWARD);

        lf.setDirection(REVERSE);
        lb.setDirection(REVERSE);
        liftR.setDirection(REVERSE);

        lf.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        lb.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rf.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rb.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        liftL.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        liftR.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        reaperL.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        reaperR.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        lf.setPower(0);
        lb.setPower(0);
        rf.setPower(0);
        rb.setPower(0);
        liftL.setPower(0);
        liftR.setPower(0);
        reaperL.setPower(0);
        reaperR.setPower(0);
//        reaper.setPower(0);
        stopper.setPosition(1);
    }

    @Override
    public void loop() {

        if (!MathUtils.equals(gamepad1.right_stick_x,0)) {
            lf.setPower(-1 * power * gearing * gamepad1.right_stick_x);
            lb.setPower(-1 * power * gearing * gamepad1.right_stick_x);
            rf.setPower(power * gearing * gamepad1.right_stick_x);
            rb.setPower(power * gearing * gamepad1.right_stick_x);
        } else if (!MathUtils.equals(gamepad1.left_stick_y,0)) {
            lf.setPower(power * gearing * gamepad1.left_stick_y);
            lb.setPower(power * gearing * gamepad1.left_stick_y);
            rf.setPower(power * gearing * gamepad1.left_stick_y);
            rb.setPower(power * gearing * gamepad1.left_stick_y);
        } else {
            lf.setPower(0);
            lb.setPower(0);
            rf.setPower(0);
            rb.setPower(0);
        }

        if(gamepad1.dpad_up) {
            liftL.setPower(1 * gearing);
            liftR.setPower(1 * gearing);
        } else if (gamepad1.dpad_down) {
            liftL.setPower(-1 * gearing);
            liftR.setPower(-1 * gearing);
        } else {
            liftL.setPower(0);
            liftR.setPower(0);
        }

        if (gamepad1.dpad_left) {
            gearing = 1;
        } else if (gamepad1.dpad_right) {
            gearing = 0.5;
        }

        if (gamepad1.a) {
            lf.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            lb.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            rf.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            rb.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            liftL.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            liftR.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            lf.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            lb.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            rf.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            rb.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            liftL.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            liftR.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        }



        if (!MathUtils.equals(gamepad2.right_stick_x,0)) {
//            reaper.setPower(Math.signum(gamepad2.right_stick_x));
        } else {
//            reaper.setPower(0);
        }

        if(gamepad2.dpad_up) {
            reaperL.setPower(1 * gearing);
            reaperR.setPower(1 * gearing);
        } else if (gamepad2.dpad_down) {
            reaperL.setPower(-1 * gearing);
            reaperR.setPower(-1 * gearing);
        } else {
            reaperL.setPower(0);
            reaperR.setPower(0);
        }

        if (gamepad2.a) {
            reaperL.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            reaperR.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            reaperL.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            reaperR.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        }

        if (gamepad1.x && canToggleStopper.milliseconds() > 500) {
            stopper.setPosition(1 - stopper.getPosition());
            canToggleStopper.reset();
        }



        telemetry.addData("LF", lf.getCurrentPosition() + "");
        telemetry.addData("LB", lb.getCurrentPosition() + "");
        telemetry.addData("RF", rf.getCurrentPosition() + "");
        telemetry.addData("RB", rb.getCurrentPosition() + "");
        telemetry.addData("LIFTL", liftL.getCurrentPosition() + "");
        telemetry.addData("LIFTR", liftR.getCurrentPosition() + "");
        telemetry.addData("REAPERL", reaperL.getCurrentPosition() + "");
        telemetry.addData("REAPERR", reaperR.getCurrentPosition() + "");
//        telemetry.addData("REAPER", reaper.getPower() + "");
    }

    @Override
    public void stop() {
        stopper.setPosition(0);
        try {
            Thread.sleep(750);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
