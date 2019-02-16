package org.firstinspires.ftc.teamcode.NonUpdateCompetitionOpModes;

import android.util.Log;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.teamcode.JeffBot;
import org.firstinspires.ftc.teamcode.SahilClass;
import org.firstinspires.ftc.teamcode.watchdog.IMUWatchdog;
import org.firstinspires.ftc.teamcode.watchdog.WatchdogManager;
import org.opencv.android.OpenCVLoader;

import virtualRobot.VuforiaLocalizerImplSubclass;

@Autonomous(name = "Blue Auton Pit", group = "competition")
public class BlueAutonPitNoMarker extends LinearOpMode {
    static {
        OpenCVLoader.initDebug();
    }
    final static int delay = 0;
    //TODO: Synchronize hardwaremap
    DcMotor lf;
    DcMotor lb;
    DcMotor rf;
    DcMotor rb;
    DcMotor liftR;
    DcMotor liftL;
    Servo marker;
    Servo stopper;
    DigitalChannel magneticLimitSwitch;

    static {
        if(OpenCVLoader.initDebug()) {
            Log.d("opencv","yay it works");
        } else {
            Log.d("opencv","nope it doesnt work");
        }
    }

    private Servo dropper;
    private Servo reaperFoldLeft;
    private Servo reaperFoldRight;
    private DcMotorEx reaperLeft;
    private DcMotorEx reaperRight;
    private CRServo reaperSpin;

    @Override
    public void runOpMode() throws InterruptedException {
        lf = hardwareMap.dcMotor.get("leftFront");
        lb = hardwareMap.dcMotor.get("leftBack");
        rf = hardwareMap.dcMotor.get("rightFront");
        rb = hardwareMap.dcMotor.get("rightBack");
        liftL = hardwareMap.dcMotor.get("liftLeft");
        liftR = hardwareMap.dcMotor.get("liftRight");
        reaperSpin = hardwareMap.crservo.get("reaper");
        magneticLimitSwitch = hardwareMap.get(DigitalChannel.class, "Switchy");
        marker = hardwareMap.servo.get("marker");
        stopper = hardwareMap.servo.get("stopper");
        dropper = hardwareMap.servo.get("dropper");
        reaperFoldLeft = hardwareMap.servo.get("reaperFoldLeft");
        reaperFoldRight = hardwareMap.servo.get("reaperFoldRight");

        reaperLeft = (DcMotorEx)hardwareMap.dcMotor.get("horizLeft");

        reaperLeft.setDirection(DcMotorSimple.Direction.REVERSE);
        reaperRight = (DcMotorEx)hardwareMap.dcMotor.get("horizRight");
        dropper.setPosition(0.7);
        reaperFoldRight.setDirection(Servo.Direction.REVERSE);
        reaperFoldRight.setPosition(0.45);
        reaperFoldLeft.setPosition(0.45);
        WatchdogManager wdm = WatchdogManager.getInstance();
        wdm.setHardwareMap(hardwareMap);
        wdm.setCurrentAuton(this);
        wdm.provision("IMUWatch", IMUWatchdog.class, "imu 1");
        marker.setPosition(0.5);
        VuforiaLocalizer.Parameters params = new VuforiaLocalizer.Parameters(hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName()));
        params.cameraDirection = VuforiaLocalizer.CameraDirection.BACK;

        params.vuforiaLicenseKey = JeffBot.vuforiaKey;
        telemetry.addData("Vuforia Status: ", "Loading...");
        telemetry.update();
        VuforiaLocalizerImplSubclass vuforiaInstance = new VuforiaLocalizerImplSubclass(params);
        telemetry.addData("Vuforia Status: ", "Loaded!");
        telemetry.update();
        waitForStart();
        Thread.sleep(delay);
        rf.setDirection(DcMotorSimple.Direction.REVERSE);
        rb.setDirection(DcMotorSimple.Direction.REVERSE);
        lf.setDirection(DcMotorSimple.Direction.FORWARD);
        lb.setDirection(DcMotorSimple.Direction.FORWARD);
        initializeMotor(new DcMotor[]{lf, lb, rf, rb});


        liftR.setDirection(DcMotorSimple.Direction.REVERSE);
        int initL = liftL.getCurrentPosition();
        int initR = liftR.getCurrentPosition();


        JeffBot mv = new JeffBot(lf, lb, rf, rb);

        reaperFoldLeft.setPosition(1);
        reaperFoldRight.setPosition(1);
        Thread.sleep(500);


        liftL.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        liftR.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        liftL.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        liftR.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        liftL.setPower(-0.6);
        liftR.setPower(-0.6);
        ElapsedTime time = new ElapsedTime();
        Thread.sleep(500);
        stopper.setPosition(1);
        Thread.sleep(500);
        mv.moveUntilPressed(new DcMotor[]{liftL, liftR}, magneticLimitSwitch, 1);//Move until limit switch pressed


        liftL.setPower(0.8);
        liftR.setPower(0.8);
        ElapsedTime extraLiftTimer = new ElapsedTime();
        while (extraLiftTimer.milliseconds() < 350) {
            Thread.sleep(5);
        }
        liftL.setPower(0);
        liftR.setPower(0);
        Thread.sleep(250);
        SahilClass sahilClass = new SahilClass(vuforiaInstance, 1000);
        int pos = sahilClass.getThreeMineralPosition();

        telemetry.addData("Position", pos);
        telemetry.update();

        reaperFoldLeft.setPosition(0.78);
        reaperFoldRight.setPosition(0.78);
        mv.translateDistance(1,-12);


        liftL.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        liftR.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        liftL.setPower(1);
        liftR.setPower(-1);
        liftL.setTargetPosition(0);
        liftR.setTargetPosition(0);

        reaperLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        reaperLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);

//        pos = 0;

        switch (pos) {
            case 0:
                reaperFoldLeft.setPosition(0.825);
                reaperFoldRight.setPosition(0.825);
                reaperSpin.setPower(0.6);
                reaperLeft.setTargetPosition(0);
                reaperLeft.setPower(1);
                mv.rotateTo(35);
                reaperFoldRight.setPosition(1);
                reaperFoldLeft.setPosition(1);
                reaperLeft.setTargetPosition(2250);
                while (reaperLeft.isBusy()) {
                    Thread.sleep(5);
                }
                reaperFoldRight.setPosition(0.825);
                reaperFoldLeft.setPosition(0.825);
                Thread.sleep(500);
                mv.rotateTo(20);
                break;
            case 2:
                reaperFoldLeft.setPosition(0.825);
                reaperFoldRight.setPosition(0.825);
                reaperSpin.setPower(-0.6);
                reaperLeft.setTargetPosition(0);
                reaperLeft.setPower(1);
                mv.rotateTo(-45);
                reaperFoldRight.setPosition(1);
                reaperFoldLeft.setPosition(1);
                reaperLeft.setTargetPosition(2250);
                while (reaperLeft.isBusy()) {
                    Thread.sleep(5);
                }
                reaperFoldRight.setPosition(0.825);
                reaperFoldLeft.setPosition(0.825);
                Thread.sleep(500);
                mv.rotateTo(-30);
                break;
            default:
            case 1:
                reaperFoldRight.setPosition(1);
                reaperFoldLeft.setPosition(1);
                Thread.sleep(250);
                reaperSpin.setPower(0.6);
                reaperLeft.setTargetPosition(1450);
                reaperLeft.setPower(1);
                while (reaperLeft.isBusy()) {
                    Thread.sleep(5);
                }
                reaperFoldLeft.setPosition(0.825);
                reaperFoldRight.setPosition(0.825);
                Thread.sleep(500);
                reaperLeft.setTargetPosition(2250);
                reaperLeft.setPower(1);
                while (reaperLeft.isBusy()) {
                    Thread.sleep(5);
                }
                break;
        }
    }
    public void initializeMotor(DcMotor[] motors) {
        for (DcMotor motor : motors) {
            motor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            motor.setPower(0);
//            motor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
//            motor.setTargetPosition(0);
        }
    }
}
