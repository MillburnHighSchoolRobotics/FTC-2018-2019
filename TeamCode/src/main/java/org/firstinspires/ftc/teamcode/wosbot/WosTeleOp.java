package org.firstinspires.ftc.teamcode.wosbot;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.Servo;

import virtualRobot.utils.MathUtils;

import static com.qualcomm.robotcore.hardware.DcMotorSimple.Direction.REVERSE;

@TeleOp(name = "WOS TELEOP", group = "COMPETITION")
public class WosTeleOp extends OpMode {
    public DcMotorEx lf;
    public DcMotorEx lb;
    public DcMotorEx rf;
    public DcMotorEx rb;
    public DcMotorEx lift;
    public DcMotorEx reaperExtension;
    public DcMotorEx hang;
    private CRServo reaperSpin;
    private Servo dropper;
    private Servo reaperFold;
    private double gearCoefficient = 1;
    private double rotateCoefficient = 1;
    double[] foldPositions = {1,0.8,0.45}; 

    @Override
    public void init() {
        lf = (DcMotorEx)hardwareMap.dcMotor.get("lf");
        lb = (DcMotorEx)hardwareMap.dcMotor.get("lb");
        rf = (DcMotorEx)hardwareMap.dcMotor.get("rf");
        rb = (DcMotorEx)hardwareMap.dcMotor.get("rb");
        lift = (DcMotorEx) hardwareMap.dcMotor.get("lift");
        reaperSpin = hardwareMap.crservo.get("reaper");
        reaperExtension = (DcMotorEx) hardwareMap.dcMotor.get("reaperExtension");
        dropper = hardwareMap.servo.get("dropper");
        reaperFold = hardwareMap.servo.get("reaperFold");
        hang = (DcMotorEx) hardwareMap.dcMotor.get("hanging");
        lf.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        lb.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rf.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rb.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        lift.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        reaperExtension.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        lf.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        lb.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rf.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rb.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        lift.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        reaperExtension.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rf.setDirection(REVERSE);
        rb.setDirection(REVERSE);
        lift.setDirection(REVERSE);
        reaperSpin.setPower(0);
//        reaperFoldLeft.setPosition(foldPositions[foldCount]);
    }

    @Override
    public void loop() {

        if (gamepad1.x) {
            lf.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            lb.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            rf.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            rb.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            lf.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            lb.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            rf.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            rb.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        }

//        if(gamepad2.dpad_up){
//            hanger.setPower(1);
//        }
//        else if (gamepad2.dpad_down){
//            hanger.setPower(-1);
//        }
//        else{
//            hager.setPower(0);
//        }

        if (gamepad1.left_bumper) {
            reaperSpin.setPower(0.6);
        } else if (gamepad1.right_bumper) {
            reaperSpin.setPower(-0.6);
        } else {
            reaperSpin.setPower(0);
        }

        if (gamepad1.dpad_left) {
            reaperExtension.setPower(-0.8);
        } else if (gamepad1.dpad_right) {
            reaperExtension.setPower(0.8);
        } else {
            reaperExtension.setPower(0);
        }

        if (gamepad1.dpad_up) {
            lift.setPower(1 * gearCoefficient);
        } else if (gamepad1.dpad_down) {
            lift.setPower(-1 * gearCoefficient);
        } else {
            lift.setPower(0);
        }

        if(gamepad1.a){
            dropper.setPosition(0); //TODO:Tune
        }
        else{
            dropper.setPosition(1); //TODO:Tune
        }

        double transX = gamepad1.left_stick_x;
        double transY = -gamepad1.left_stick_y;
        double rotX = gamepad1.right_stick_x;
        double rotY = gamepad1.right_stick_y;
        double translateMag = Math.sqrt(transX*transX + transY*transY);
        double translateTheta = Math.atan2(transY, transX);
        translateTheta = Math.toDegrees(translateTheta);
        if (translateTheta < 0) translateTheta += 360;
        double scale = 0;
        double RF = 0, RB = 0, LF = 0, LB = 0;
        if (!MathUtils.equals(rotX, 0, 0.05)) {
            LF = rotX * rotateCoefficient;
            LB = rotX * rotateCoefficient;
            RF = -rotX * rotateCoefficient;
            RB = -rotX * rotateCoefficient;
        } else if (!MathUtils.equals(translateMag, 0, 0.05)) {
            double translatePower = translateMag;
            if (translateTheta >= 0 && translateTheta <= 90) { //quadrant 1
                scale = MathUtils.sinDegrees(translateTheta - 45) / MathUtils.cosDegrees(translateTheta - 45);
                LF = translatePower * WosBot.POWER_MATRIX[0][0];
                LB = translatePower * WosBot.POWER_MATRIX[0][1] * scale;
                RF = translatePower * WosBot.POWER_MATRIX[0][2] * scale;
                RB = translatePower * WosBot.POWER_MATRIX[0][3];
            } else if (translateTheta > 90 && translateTheta <= 180) { //quadrant 2
                translatePower *= -1;
                scale = MathUtils.sinDegrees(translateTheta - 135) / MathUtils.cosDegrees(translateTheta - 135);
                LF = (translatePower * WosBot.POWER_MATRIX[2][0] * scale);
                LB = (translatePower * WosBot.POWER_MATRIX[2][1]);
                RF = (translatePower * WosBot.POWER_MATRIX[2][2]);// * scale);
                RB = (translatePower * WosBot.POWER_MATRIX[2][3] * scale);
            } else if (translateTheta > 180 && translateTheta <= 270) { //quadrant 3
                scale = MathUtils.sinDegrees(translateTheta - 225) / MathUtils.cosDegrees(translateTheta - 225);
                LF = (translatePower * WosBot.POWER_MATRIX[4][0]);
                LB = (translatePower * WosBot.POWER_MATRIX[4][1] * scale);
                RF = (translatePower * WosBot.POWER_MATRIX[4][2] * scale);
                RB = (translatePower * WosBot.POWER_MATRIX[4][3]);
            } else if (translateTheta > 270 && translateTheta < 360) { //quadrant 4
                translatePower *= -1;
                scale = MathUtils.sinDegrees(translateTheta - 315) / MathUtils.cosDegrees(translateTheta - 315);
                LF = (translatePower * WosBot.POWER_MATRIX[6][0] * scale);
                LB = (translatePower * WosBot.POWER_MATRIX[6][1]);
                RF = (translatePower * WosBot.POWER_MATRIX[6][2]);
                RB = (translatePower * WosBot.POWER_MATRIX[6][3] * scale);
            }
        }
        lf.setPower(LF * gearCoefficient);
        lb.setPower(LB * gearCoefficient);
        rf.setPower(RF * gearCoefficient);
        rb.setPower(RB * gearCoefficient);
        telemetry.addData("THETA", translateTheta);
        telemetry.addData("SCALE", scale);
        telemetry.addData("LIFT", lift.getPower() + " " + lift.getCurrentPosition());
        telemetry.addData("LF", lf.getPower() + " " + lf.getCurrentPosition());
        telemetry.addData("LB", lb.getPower() + " " +  lb.getCurrentPosition());
        telemetry.addData("RF", rf.getPower() + " " + rf.getCurrentPosition());
        telemetry.addData("RB", rb.getPower() + " " + rb.getCurrentPosition());
    }
}
