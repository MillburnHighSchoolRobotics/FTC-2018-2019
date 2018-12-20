package org.firstinspires.ftc.teamcode.NonUpdateCompetitionOpModes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;
import org.firstinspires.ftc.teamcode.JeffBot;
import org.opencv.core.Mat;

import virtualRobot.utils.MathUtils;
import static com.qualcomm.robotcore.hardware.DcMotorSimple.Direction.REVERSE;

/*
Controls:
Joystick left - forward/backward
Joystick right - left/right
Dpad up and down - lift
Dpad left and right - reaper extends forward/backward
right/left bumper - hold to spin reaper forward/backward
a - position of fold ++
y - position of fold --
x - reset every damn thing
trigger left - folding reaper forward/backward
trigger right - dumping mineral servo
 */

@TeleOp(name = "REAL TeleOp", group = "competition")
public class NewNewNewTeleOp extends OpMode {
    private DcMotorEx lf;
    private DcMotorEx lb;
    private DcMotorEx rf;
    private DcMotorEx rb;
    private DcMotorEx reaperLeft;
    private DcMotorEx reaperRight;
    private DcMotorEx liftLeft;
    private DcMotorEx liftRight;
    private CRServo reaperSpin;
    private Servo stopper;
    private Servo dropper;
    private Servo marker;
    private Servo reaperFoldLeft;
    private Servo reaperFoldRight;
//    private JeffBot mv;


    double power = 0.9;
    double gearing = 1;

    private ElapsedTime canToggleStopper;
    private ElapsedTime canToggleDropper;
    private ElapsedTime canToggleFolder;
    private ElapsedTime canToggleMarker;


    int foldCount = 2;
    double[] foldPositions = {0.9,0.7,0.3};

    @Override
    public void init() {
        canToggleStopper = new ElapsedTime();
        canToggleDropper = new ElapsedTime();
        canToggleFolder = new ElapsedTime();
        canToggleMarker = new ElapsedTime();
        this.msStuckDetectStop = 3000;
        lf = (DcMotorEx)hardwareMap.dcMotor.get("leftFront");
        lb = (DcMotorEx)hardwareMap.dcMotor.get("leftBack");
        rf = (DcMotorEx)hardwareMap.dcMotor.get("rightFront");
        rb = (DcMotorEx)hardwareMap.dcMotor.get("rightBack");
        liftLeft = (DcMotorEx)hardwareMap.dcMotor.get("liftLeft");
        liftRight = (DcMotorEx)hardwareMap.dcMotor.get("liftRight");
        reaperLeft = (DcMotorEx)hardwareMap.dcMotor.get("horizLeft");
        reaperRight = (DcMotorEx)hardwareMap.dcMotor.get("horizRight");
        reaperSpin = hardwareMap.crservo.get("reaper");
        stopper = hardwareMap.servo.get("stopper");
        dropper = hardwareMap.servo.get("dropper");
        marker = hardwareMap.servo.get("marker");
        reaperFoldLeft = hardwareMap.servo.get("reaperFoldLeft");
        reaperFoldRight = hardwareMap.servo.get("reaperFoldRight");


        lf.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        lb.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rf.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rb.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        liftLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        liftRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        lf.setDirection(DcMotorSimple.Direction.FORWARD);
        lb.setDirection(DcMotorSimple.Direction.FORWARD);
        lf.setDirection(REVERSE);
        lb.setDirection(REVERSE);
        liftRight.setDirection(REVERSE);
        reaperFoldRight.setDirection(Servo.Direction.REVERSE);
        reaperRight.setDirection(REVERSE);

        lf.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        lb.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rf.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rb.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        liftLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        liftRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        reaperLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        reaperRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        lf.setPower(0);
        lb.setPower(0);
        rf.setPower(0);
        rb.setPower(0);
        liftLeft.setPower(0);
        liftRight.setPower(0);
        reaperLeft.setPower(0);
        reaperRight.setPower(0);
        reaperSpin.setPower(0);
        stopper.setPosition(1);
        dropper.setPosition(0.95);
        marker.setPosition(0.5);
        reaperFoldLeft.setPosition(foldPositions[foldCount]);
        reaperFoldRight.setPosition(foldPositions[foldCount]);
    }

    @Override
    public void loop() {

        // robot movement
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

        // lift movement
        if(gamepad1.dpad_up) {
            liftLeft.setPower(1 * gearing);
            liftRight.setPower(1 * gearing);
        } else if (gamepad1.dpad_down) {
            liftLeft.setPower(-1 * gearing);
            liftRight.setPower(-1 * gearing);
        } else {
            liftLeft.setPower(0);
            liftRight.setPower(0);
        }

        // reaper extension
        if (gamepad1.dpad_left) {
            reaperLeft.setPower(-0.5 * gearing);
            reaperRight.setPower(-0.5 * gearing);
        } else if (gamepad1.dpad_right) {
            reaperLeft.setPower(0.5 * gearing);
            reaperRight.setPower(0.5 * gearing);
        } else {
            reaperLeft.setPower(0);
            reaperRight.setPower(0);
        }


        // you spin me right round
        if (gamepad1.left_bumper) {
            reaperSpin.setPower(0.6);
        } else if (gamepad1.right_bumper) {
            reaperSpin.setPower(-0.6);
        } else {
            reaperSpin.setPower(0);
        }


        if (gamepad1.y && canToggleFolder.milliseconds() > 250) {
            foldCount++;
            if (foldCount >= foldPositions.length) foldCount = foldPositions.length - 1;
            reaperFoldLeft.setPosition(foldPositions[foldCount%3]);
            reaperFoldRight.setPosition(foldPositions[foldCount%3]);
            canToggleFolder.reset();
        }
        if (gamepad1.a && canToggleFolder.milliseconds() > 250) {
            foldCount--;
            if (foldCount < 0) foldCount = 0;
            reaperFoldLeft.setPosition(foldPositions[foldCount%3]);
            reaperFoldRight.setPosition(foldPositions[foldCount%3]);
            canToggleFolder.reset();
        }



        // dump truck
        if (gamepad1.b  && canToggleDropper.milliseconds() > 500) {
            dropper.setPosition(1.15 - dropper.getPosition());
            canToggleDropper.reset();
        }



        // reset pls
        if (gamepad1.x) {
            lf.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            lb.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            rf.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            rb.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            liftLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            liftRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            reaperLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            reaperRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            lf.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            lb.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            rf.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            rb.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            liftLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            liftRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            reaperLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            reaperRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        }


        // toggle locking lift
        if (MathUtils.equals(gamepad1.left_trigger, 1, 0.05) && canToggleStopper.milliseconds() > 500) {
            stopper.setPosition(1 - stopper.getPosition());
            canToggleStopper.reset();
        }


        // marker bc why not
        if (MathUtils.equals(gamepad1.right_trigger, 1, 0.05) && canToggleMarker.milliseconds() > 500) {
            marker.setPosition(1.5 - marker.getPosition());
            canToggleMarker.reset();
        }



        telemetry.addData("lf", lf.getCurrentPosition() + "");
        telemetry.addData("lb", lb.getCurrentPosition() + "");
        telemetry.addData("rf", rf.getCurrentPosition() + "");
        telemetry.addData("rb", rb.getCurrentPosition() + "");
        telemetry.addData("lift l", liftLeft.getCurrentPosition() + "");
        telemetry.addData("lift r", liftRight.getCurrentPosition() + "");
        telemetry.addData("reaper extension l", reaperLeft.getCurrentPosition() + "");
        telemetry.addData("reaper extension r", reaperRight.getCurrentPosition() + "");
        telemetry.addData("reaper power", reaperSpin.getPower() + "");
        telemetry.addData("stopper", stopper.getPosition() + "");
        telemetry.addData("dropper", dropper.getPosition() + "");
        telemetry.addData("reaper fold l", reaperFoldLeft.getPosition() + "");
        telemetry.addData("reaper fold r", reaperFoldRight.getPosition() + "");
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
