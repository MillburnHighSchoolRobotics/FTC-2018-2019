package org.firstinspires.ftc.teamcode.NonUpdateCompetitionOpModes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import virtualRobot.utils.MathUtils;

import static com.qualcomm.robotcore.hardware.DcMotorSimple.Direction.REVERSE;

@TeleOp(name = "REAL TeleOp", group = "competition")
public class NewNewNewTeleOp extends OpMode {
    private DcMotorEx lf;
    private DcMotorEx lb;
    private DcMotorEx rf;
    private DcMotorEx rb;
//    private DcMotor reaper;
//    private Servo reaperLift;
//    private DcMotor reaperFold;
    private DcMotorEx liftL;
    private DcMotorEx liftR;
//    private Servo deposit;
    float threshold = 0.1f;
    double power = 0.9;
    double gearing = 1;


    @Override
    public void init() {
        lf = (DcMotorEx)hardwareMap.dcMotor.get("leftFront");
        lb = (DcMotorEx)hardwareMap.dcMotor.get("leftBack");
        rf = (DcMotorEx)hardwareMap.dcMotor.get("rightFront");
        rb = (DcMotorEx)hardwareMap.dcMotor.get("rightBack");
        liftL = (DcMotorEx)hardwareMap.dcMotor.get("liftL");
        liftR = (DcMotorEx)hardwareMap.dcMotor.get("liftR");

        lf.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        lb.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rf.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rb.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        lf.setDirection(DcMotorSimple.Direction.FORWARD);
        lb.setDirection(DcMotorSimple.Direction.FORWARD);

        lf.setDirection(REVERSE);
        lb.setDirection(REVERSE);
        liftL.setDirection(REVERSE);

        liftL.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        liftR.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        lf.setPower(0);
        lb.setPower(0);
        rf.setPower(0);
        rb.setPower(0);
        liftL.setPower(0);
        liftR.setPower(0);
//        reaper = hardwareMap.dcMotor.get("reaper");
//        reaperLift = hardwareMap.servo.get("reaperLift");
//        reaperFold = hardwareMap.dcMotor.get("reaperFold");
//        lift = hardwareMap.dcMotor.get("lift");
//        deposit = hardwareMap.servo.get("deposit");
    }

    @Override
    public void loop() {
        //left stick = up and down
        //right stick = rotate
        //both sticks = rotate priority
        //buttons for reaper, reaper lift, reaper fold, lift, deposit

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

        if (gamepad1.dpad_left) {
            gearing = 1;
        } else if (gamepad1.dpad_right) {
            gearing = 0.5;
        }

        telemetry.addData("LF", lf.getCurrentPosition() + "");
        telemetry.addData("LB", lb.getCurrentPosition() + "");
        telemetry.addData("RF", rf.getCurrentPosition() + "");
        telemetry.addData("RB", rb.getCurrentPosition() + "");
        telemetry.addData("LIFTL", liftL.getCurrentPosition() + "");
        telemetry.addData("LIFTR", liftR.getCurrentPosition() + "");

/*        if (gamepad1.dpad_up) {
            reaper.setPower(power);
        } else if (gamepad1.dpad_down) {
            reaper.setPower(-1 * power)
        }*/
    }
}
