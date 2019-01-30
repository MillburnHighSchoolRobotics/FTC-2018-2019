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

@Autonomous(name = "Blue Auton Pit Double Mineral", group = "competition")
public class BlueAutonPitDoubleMineral extends LinearOpMode {
    static {
        OpenCVLoader.initDebug();
    }
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
        reaperRight = (DcMotorEx)hardwareMap.dcMotor.get("horizRight");
        dropper.setPosition(0.7);
        reaperFoldRight.setDirection(Servo.Direction.REVERSE);
        reaperFoldRight.setPosition(0.45);
        reaperFoldLeft.setPosition(0.45);
        WatchdogManager wdm = WatchdogManager.getInstance();
        wdm.setHardwareMap(hardwareMap);
        wdm.setCurrentAuton(this);
        wdm.provision("IMUWatch", IMUWatchdog.class, "imu 1");
        marker.setPosition(0.7);
        VuforiaLocalizer.Parameters params = new VuforiaLocalizer.Parameters(hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName()));
        params.cameraDirection = VuforiaLocalizer.CameraDirection.BACK;

        params.vuforiaLicenseKey = JeffBot.vuforiaKey;
        telemetry.addData("Vuforia Status: ", "Loading...");
        telemetry.update();
        VuforiaLocalizerImplSubclass vuforiaInstance = new VuforiaLocalizerImplSubclass(params);
        telemetry.addData("Vuforia Status: ", "Loaded!");
        telemetry.update();
        waitForStart();
        rf.setDirection(DcMotorSimple.Direction.REVERSE);
        rb.setDirection(DcMotorSimple.Direction.REVERSE);
        lf.setDirection(DcMotorSimple.Direction.FORWARD);
        lb.setDirection(DcMotorSimple.Direction.FORWARD);
        initializeMotor(new DcMotor[]{lf, lb, rf, rb});
//        for (int x = 0; x < 4; x++) {
//        }

        liftR.setDirection(DcMotorSimple.Direction.REVERSE);
        int initL = liftL.getCurrentPosition();
        int initR = liftR.getCurrentPosition();

//        moveToPosition(new DcMotor[] {liftL, liftR}, new double[] {1, 1}, new int[] {4796+1067-initL, 4796+1067-initR});
//        Thread.sleep(100);
//       mv.translate(0.5, 104);
//        Thread.sleep(100);
//
//        moveToPosition(new DcMotor[] {liftL, liftR}, new double[] {1, 1}, new int[] {8418+1067-initL, 8418+1067-initR});
//        Thread.sleep(100);

        //sampling
        //   int meme = 0;
        JeffBot mv = new JeffBot(lf, lb, rf, rb);

        reaperFoldLeft.setPosition(1);
        reaperFoldRight.setPosition(1);
        Thread.sleep(250);
//        dropper.setPosition(0);

//        TFODTest tfod = new TFODTest(hardwareMap);
        liftL.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        liftR.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        liftL.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        liftR.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        liftL.setPower(-0.5);
        liftR.setPower(-0.5);
        ElapsedTime time = new ElapsedTime();
        Thread.sleep(250);
        stopper.setPosition(1);
        Thread.sleep(250);
        mv.moveUntilPressed(new DcMotor[]{liftL, liftR}, magneticLimitSwitch, 1);//Move until limit switch pressed
//        liftL.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
//        liftR.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
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
        //mv.moveToPosition(new DcMotor[] {liftL, liftR}, new double[] {-0.8, 0.8}, new int[] {10700+100, 10700+100});
//        Thread.sleep(100);

//
//       mv.translate(0.5, 104);
//        Thread.sleep(100);
//
//        moveToPosition(new DcMotor[] {liftL, liftR}, new double[] {1, 1}, new int[] {8418+1067-initL, 8418+1067-initR});
//        Thread.sleep(100);

        //sampling
        mv.translateDistance(1,-12);


        liftL.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        liftR.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        liftL.setPower(0.8);
        liftR.setPower(-0.8);
        liftL.setTargetPosition(600);
        liftR.setTargetPosition(600);
//        mv.moveToPosition(new DcMotor[] {liftL, liftR}, new double[] {-0.8, 0.8}, new int[] {600, 600});
//        liftL.setMode(DcMotor.RunMode.RUN_TO_POSITION);
//        liftR.setMode(DcMotor.RunMode.RUN_TO_POSITION);
//        liftL.setPower(0.8);
//        liftR.setPower(-0.8);
//        liftL.setTargetPosition(600);
//        liftR.setTargetPosition(600);


//        Thread.sleep(100);



//        VuforiaLocalizer.Parameters params = new VuforiaLocalizer.Parameters(R.id.cameraMonitorViewId);
//        params.cameraDirection = VuforiaLocalizer.CameraDirection.BACK;
//        params.vuforiaLicenseKey = "AdVGalv/////AAAAGYhiDIdk+UI+ivt0Y7WGvUJnm5cKX/lWesW2pH7gnK3eOLTKThLekYSO1q65ttw7X1FvNhxxhdQl3McS+mzYjO+HkaFNJlHxltsI5+b4giqNQKWhyKjzbYbNw8aWarI5YCYUFnyiPPjH39/CbBzzFk3G2RWIzNB7cy4AYhjwYRKRiL3k33YvXv0ZHRzJRkMpnytgvdv5jEQyWa20DIkriC+ZBaj8dph8/akyYfyD1/U19vowknmzxef3ncefgOZoI9yrK82T4GBWazgWvZkIz7bPy/ApGiwnkVzp44gVGsCJCUFERiPVwfFa0SBLeCrQMrQaMDy3kOIVcWTotFn4m1ridgE5ZP/lvRzEC4/vcuV0";
//        VuforiaLocalizerImplSubclass vuforiaInstance = new VuforiaLocalizerImplSubclass(params);
//
//
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
                reaperFoldRight.setPosition(0.95);
                reaperFoldLeft.setPosition(0.95);
                reaperLeft.setTargetPosition(2250);
                while (reaperLeft.isBusy()) {
                    Thread.sleep(5);
                }
                reaperSpin.setPower(0);
                reaperFoldRight.setPosition(0.78);
                reaperFoldLeft.setPosition(0.78);
                reaperLeft.setTargetPosition(0);
                reaperLeft.setPower(0.6);
                reaperFoldLeft.setPosition(0.45);
                reaperFoldRight.setPosition(0.45);
//                mv.translateDistance(1,-24);
//                mv.translateDistance(1,24);
                mv.rotateTo(0);
                break;
            case 2:
                reaperFoldLeft.setPosition(0.825);
                reaperFoldRight.setPosition(0.825);
                reaperSpin.setPower(0.6);
                reaperLeft.setTargetPosition(0);
                reaperLeft.setPower(1);
                mv.rotateTo(-35);
                reaperFoldRight.setPosition(0.95);
                reaperFoldLeft.setPosition(0.95);
                reaperLeft.setTargetPosition(2250);
                while (reaperLeft.isBusy()) {
                    Thread.sleep(5);
                }
                reaperSpin.setPower(0);
                reaperFoldRight.setPosition(0.78);
                reaperFoldLeft.setPosition(0.78);
                reaperLeft.setTargetPosition(0);
                reaperLeft.setPower(0.6);
                reaperFoldLeft.setPosition(0.45);
                reaperFoldRight.setPosition(0.45);
//                mv.translateDistance(1,-24);
//                mv.translateDistance(1,24);
                mv.rotateTo(0);
                break;
            default:
            case 1:
                reaperFoldRight.setPosition(0.95);
                reaperFoldLeft.setPosition(0.95);
                reaperSpin.setPower(0.6);
                reaperLeft.setTargetPosition(1450);
                reaperLeft.setPower(1);
                while (reaperLeft.isBusy()) {
                    Thread.sleep(5);
                }
                reaperFoldLeft.setPosition(0.78);
                reaperFoldRight.setPosition(0.78);
                reaperSpin.setPower(0);
                reaperLeft.setTargetPosition(0);
                reaperLeft.setPower(0.6);
                reaperFoldLeft.setPosition(0.45);
                reaperFoldRight.setPosition(0.45);
//                mv.translateDistance(1,-16);
//                mv.translateDistance(1,16);
                break;
        }
//        Thread.sleep(100);
//       mv.rotateDegrees(0.5,90);//TODO:Add global variable for speed

        mv.translateDistance(1,9);
        mv.rotateTo(45);

//        Thread.sleep(100);
//        Thread.sleep(100);
        mv.translateDistance(1, -40);//-36*Math.sqrt(2));//TODO:See above immortal TODO
//        Thread.sleep(100);
        mv.rotateTo(120);
//        Thread.sleep(100);
        mv.translateDistance(1, -62);
        mv.rotateTo(90);
//        Thread.sleep(100);
//        Thread.sleep(100);
        marker.setPosition(0);
        Thread.sleep(500);
        marker.setPosition(0.7);
        switch (pos) {
            case 0:
                mv.rotateTo(-120);
                Thread.sleep(250);
                reaperFoldLeft.setPosition(1);
                reaperFoldRight.setPosition(1);
                reaperSpin.setPower(0.6);
                reaperLeft.setTargetPosition(3200);
                reaperLeft.setPower(1);
                while (reaperLeft.isBusy()) {
                    Thread.sleep(5);
                }
                break;
            case 2:
                mv.rotateTo(-60);
                Thread.sleep(250);
                reaperFoldLeft.setPosition(1);
                reaperFoldRight.setPosition(1);
                reaperSpin.setPower(0.6);
                reaperLeft.setTargetPosition(3200);
                reaperLeft.setPower(1);
                while (reaperLeft.isBusy()) {
                    Thread.sleep(5);
                }
                break;
            default:
            case 1:
                mv.rotateTo(-90);
                Thread.sleep(250);
                reaperFoldLeft.setPosition(1);
                reaperFoldRight.setPosition(1);
                reaperSpin.setPower(0.6);
                reaperLeft.setTargetPosition(2800);
                reaperLeft.setPower(1);
                while (reaperLeft.isBusy()) {
                    Thread.sleep(5);
                }
                break;
        }
        reaperLeft.setTargetPosition(0);
        reaperLeft.setPower(1);
        while (reaperLeft.isBusy()) {
            Thread.sleep(5);
        }
        reaperFoldLeft.setPosition(0.45);
        reaperFoldRight.setPosition(0.45);
//        mv.rotateTo(-140);
//        reaperLeft.setTargetPosition(3200);
//        mv.translateDistance(1,50);
    }
    public void initializeMotor(DcMotor[] motors) {
        for (DcMotor motor : motors) {
            motor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            motor.setPower(0);
        }
    }
}
