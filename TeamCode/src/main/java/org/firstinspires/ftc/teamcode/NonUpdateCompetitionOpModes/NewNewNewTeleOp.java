package org.firstinspires.ftc.teamcode.NonUpdateCompetitionOpModes;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.internal.android.dx.rop.code.RegisterSpecSet;
import org.firstinspires.ftc.teamcode.JeffBot;
import org.opencv.core.Mat;

import virtualRobot.utils.MathUtils;

import static com.qualcomm.robotcore.hardware.DcMotor.RunMode.RUN_TO_POSITION;
import static com.qualcomm.robotcore.hardware.DcMotor.RunMode.RUN_USING_ENCODER;
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
    private DigitalChannel bottomSwitch;
//    private JeffBot mv;


    double power = 0.9;
    double gearing = 1;

    private ElapsedTime canToggleStopper;
    private ElapsedTime canToggleDropper;
    private ElapsedTime canToggleFolder;
    private ElapsedTime canToggleMarker;


    int foldCount = 2;
    double[] foldPositions = {1,0.8,0.45};

    boolean dropperIsUp = false;
    double[] dropperPositions = {0.7, 0};
    boolean targetIsTop = false;
    private ElapsedTime canToggleTarget;

    private double rightModifier = 1;
    private double leftModifier = 1;

    private boolean descending = false;

    @Override
    public void init() {
        canToggleStopper = new ElapsedTime();
        canToggleDropper = new ElapsedTime();
        canToggleFolder = new ElapsedTime();
        canToggleMarker = new ElapsedTime();
        canToggleTarget = new ElapsedTime();
        this.msStuckDetectStop = 3000;
        lf = (DcMotorEx)hardwareMap.dcMotor.get("leftFront");
        lb = (DcMotorEx)hardwareMap.dcMotor.get("leftBack");
        rf = (DcMotorEx)hardwareMap.dcMotor.get("rightFront");
        rb = (DcMotorEx)hardwareMap.dcMotor.get("rightBack");
        liftLeft = (DcMotorEx)hardwareMap.dcMotor.get("liftLeft");
        liftRight = (DcMotorEx)hardwareMap.dcMotor.get("liftRight");
        reaperLeft = (DcMotorEx)hardwareMap.dcMotor.get("horizLeft");

        reaperLeft.setDirection(DcMotorSimple.Direction.REVERSE);
        reaperRight = (DcMotorEx)hardwareMap.dcMotor.get("horizRight");
        reaperSpin = hardwareMap.crservo.get("reaper");
        stopper = hardwareMap.servo.get("stopper");
        dropper = hardwareMap.servo.get("dropper");
        marker = hardwareMap.servo.get("marker");
        reaperFoldLeft = hardwareMap.servo.get("reaperFoldLeft");
        reaperFoldRight = hardwareMap.servo.get("reaperFoldRight");
        bottomSwitch = hardwareMap.get(DigitalChannel.class, "bottom");



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
        dropper.setPosition(dropperPositions[dropperIsUp ? 1 : 0]);
        marker.setPosition(0.5);
        reaperFoldLeft.setPosition(foldPositions[foldCount]);
        reaperFoldRight.setPosition(foldPositions[foldCount]);
    }

    @Override
    public void loop() {

        if (MathUtils.equals(gamepad1.right_trigger, 1, 0.05)) {
            rightModifier = 0.25;
        } else {
            rightModifier = 1;
        }
        if (MathUtils.equals(gamepad1.left_trigger, 1, 0.05)) {
            leftModifier = 0.25;
        } else {
            leftModifier = 1;
        }

        // robot movement
        if (!MathUtils.equals(gamepad1.right_stick_x, 0)) {
            lf.setPower(-1 * power * gearing * gamepad1.right_stick_x);
            lb.setPower(-1 * power * gearing * gamepad1.right_stick_x);
            rf.setPower(power * gearing * gamepad1.right_stick_x);
            rb.setPower(power * gearing * gamepad1.right_stick_x);
        } else if (!MathUtils.equals(gamepad1.left_stick_y, 0)) {
            lf.setPower(power * leftModifier * gearing * gamepad1.left_stick_y);
            lb.setPower(power * leftModifier * gearing * gamepad1.left_stick_y);
            rf.setPower(power * rightModifier * gearing * gamepad1.left_stick_y);
            rb.setPower(power * rightModifier * gearing * gamepad1.left_stick_y);
        } else if (!MathUtils.equals(gamepad1.right_trigger, 0, 0.05)) {
            lf.setPower(power * gearing * gamepad1.right_trigger * 0.3);
            lb.setPower(power * gearing * gamepad1.right_trigger * 0.3);
            rf.setPower(-1 * power * gearing * gamepad1.right_trigger * 0.3);
            rb.setPower(-1 * power * gearing * gamepad1.right_trigger * 0.3);
        } else if (!MathUtils.equals(gamepad1.left_trigger, 0, 0.05)) {
            lf.setPower(-1 * power * gearing * gamepad1.left_trigger * 0.3);
            lb.setPower(-1 * power * gearing * gamepad1.left_trigger * 0.3);
            rf.setPower(power * gearing * gamepad1.left_trigger * 0.3);
            rb.setPower(power * gearing * gamepad1.left_trigger * 0.3);
        } else {
            lf.setPower(0);
            lb.setPower(0);
            rf.setPower(0);
            rb.setPower(0);
        }

        // lift movement
        if(gamepad1.dpad_up || gamepad2.dpad_up) {
            descending = false;
            if (liftLeft.getMode().equals(RUN_TO_POSITION)) {
                liftLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            }
            if (liftRight.getMode().equals(RUN_TO_POSITION)) {
                liftRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            }
            liftLeft.setPower(1 * gearing);
            liftRight.setPower(1 * gearing);
        } else if (gamepad1.dpad_down || gamepad2.dpad_down) {
            descending = false;
            if (liftLeft.getMode().equals(RUN_TO_POSITION)) {
                liftLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            }
            if (liftRight.getMode().equals(RUN_TO_POSITION)) {
                liftRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            }
            liftLeft.setPower(-1 * gearing);
            liftRight.setPower(-1 * gearing);
        } else {
            if (!descending && (!liftLeft.getMode().equals(RUN_TO_POSITION) || !liftRight.getMode().equals(RUN_TO_POSITION))) {
                liftLeft.setPower(0);
                liftRight.setPower(0);
            }
        }

        if (gamepad2.x) {
            gearing = 1;
        } else if (gamepad2.y) {
            gearing = 0.8;
        } else if (gamepad2.b) {
            gearing = 0.6;
        } else if (gamepad2.a) {
            gearing = 0.4;
        }

        //auto lift movement
        if (gamepad1.x && canToggleTarget.milliseconds() > 250) {
            targetIsTop = !targetIsTop;
            canToggleTarget.reset();
            if (targetIsTop) {
                if (foldCount == 2) foldCount = 1;
                reaperFoldLeft.setPosition(foldPositions[foldCount%3]);
                reaperFoldRight.setPosition(foldPositions[foldCount%3]);
//                if (foldCount != foldPositions.length - 1) {
//                    dropperPositions[0] = 0.7;
//                    dropper.setPosition(dropperPositions[dropperIsUp ? 1 : 0]);
//                }
                liftLeft.setMode(RUN_TO_POSITION);
                liftRight.setMode(RUN_TO_POSITION);
                liftLeft.setTargetPosition(3400);
                liftRight.setTargetPosition(3400);
                liftLeft.setPower(1);
                liftRight.setPower(1);
                descending = false;
            } else {
                if (foldCount == 2) foldCount = 1;
                reaperFoldLeft.setPosition(foldPositions[foldCount%3]);
                reaperFoldRight.setPosition(foldPositions[foldCount%3]);
//                if (foldCount != foldPositions.length - 1) {
//                dropperPositions[0] = 0.7;
//                dropper.setPosition(dropperPositions[dropperIsUp ? 1 : 0]);
//                }
                liftLeft.setMode(RUN_USING_ENCODER);
                liftRight.setMode(RUN_USING_ENCODER);
//                liftLeft.setTargetPosition(0);
//                liftRight.setTargetPosition(0);
                descending = true;
                liftLeft.setPower(-1);
                liftRight.setPower(-1);
            }
        }

        // reaper extension
        if (gamepad1.dpad_left) {
            reaperLeft.setPower(0.8 * gearing);
            reaperRight.setPower(0.8 * gearing);
        } else if (gamepad1.dpad_right) {
            reaperLeft.setPower(-0.8 * gearing);
            reaperRight.setPower(-0.8 * gearing);
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
//            if (foldCount == foldPositions.length - 1) {
//                dropperPositions[0] = 0.8;
//                dropper.setPosition(dropperPositions[dropperIsUp ? 1 : 0]);
//            }
            canToggleFolder.reset();
        }
        if (gamepad1.a && canToggleFolder.milliseconds() > 250) {
            foldCount--;
            if (foldCount < 0) foldCount = 0;
            reaperFoldLeft.setPosition(foldPositions[foldCount%3]);
            reaperFoldRight.setPosition(foldPositions[foldCount%3]);
//            if (foldCount != foldPositions.length - 1) {
//                dropperPositions[0] = 0.7;
//                dropper.setPosition(dropperPositions[dropperIsUp ? 1 : 0]);
//            }
            canToggleFolder.reset();
        }



        // dump truck
        if (gamepad1.b  && canToggleDropper.milliseconds() > 1000) {
            dropperIsUp = !dropperIsUp;
            dropper.setPosition(dropperPositions[dropperIsUp ? 1 : 0]);
            canToggleDropper.reset();
        }



        // reset pls
        if (gamepad2.x) {
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

            reaperLeft.setDirection(DcMotorSimple.Direction.REVERSE);
        }


        // toggle locking lift
        if (MathUtils.equals(gamepad2.left_trigger, 1, 0.05) && canToggleStopper.milliseconds() > 500) {
            stopper.setPosition(1.1 - stopper.getPosition());
            canToggleStopper.reset();
        }


        // marker bc why not
        if (MathUtils.equals(gamepad2.right_trigger, 1, 0.05) && canToggleMarker.milliseconds() > 500) {
            marker.setPosition(0.5 - marker.getPosition());
            canToggleMarker.reset();
        }

        if (!bottomSwitch.getState() && !(liftLeft.getMode().equals(RUN_TO_POSITION) || liftRight.getMode().equals(RUN_TO_POSITION))) {
            liftLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            liftRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            liftLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            liftRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            if (liftLeft.getPower() < 0 || liftRight.getPower() < 0) {
                liftLeft.setPower(0);
                liftRight.setPower(0);
                descending = false;
            }
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
        stopper.setPosition(0.1);
        try {
            Thread.sleep(750);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
