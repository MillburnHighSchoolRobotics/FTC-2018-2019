package org.firstinspires.ftc.teamcode.NonUpdateCompetitionOpModes;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
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

import virtualRobot.VuforiaLocalizerImplSubclass;

import static com.qualcomm.robotcore.hardware.DcMotorSimple.Direction.REVERSE;

@Autonomous(name = "Blue Auton Pit Double Mineral", group = "competition")
public class BlueAutonPitDoubleMineral extends LinearOpMode {
    //TODO: Synchronize hardwaremap
    DcMotor lf;
    DcMotor lb;
    DcMotor rf;
    DcMotor rb;
    DcMotorEx reaperLeft;
    DcMotorEx reaperRight;
    Servo marker;
    Servo stopper;
    Servo dropper;
    Servo reaperFoldLeft;
    Servo reaperFoldRight;
    DigitalChannel magneticLimitSwitch;


    DcMotor liftR;
    DcMotor liftL;

    int meme = 0;

    @Override
    public void runOpMode() throws InterruptedException {

        lf = hardwareMap.dcMotor.get("leftFront");
        lb = hardwareMap.dcMotor.get("leftBack");
        rf = hardwareMap.dcMotor.get("rightFront");
        rb = hardwareMap.dcMotor.get("rightBack");
        reaperLeft = (DcMotorEx)hardwareMap.dcMotor.get("horizLeft");
        reaperRight = (DcMotorEx)hardwareMap.dcMotor.get("horizRight");
        liftL = hardwareMap.dcMotor.get("liftLeft");
        liftR = hardwareMap.dcMotor.get("liftRight");
        magneticLimitSwitch = hardwareMap.get(DigitalChannel.class, "Switchy");
        marker = hardwareMap.servo.get("marker");
        stopper = hardwareMap.servo.get("stopper");
        dropper = hardwareMap.servo.get("dropper");
        reaperFoldLeft = hardwareMap.servo.get("reaperFoldLeft");
        reaperFoldRight = hardwareMap.servo.get("reaperFoldRight");
        dropper.setPosition(1);
        reaperFoldRight.setDirection(Servo.Direction.REVERSE);
        reaperRight.setDirection(REVERSE);
        reaperLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        reaperRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        reaperLeft.setPower(0);
        reaperRight.setPower(0);
        reaperFoldRight.setPosition(0.3);
        reaperFoldLeft.setPosition(0.3);
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
        Thread.sleep(1000);
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

        reaperFoldLeft.setPosition(0.95);
        reaperFoldRight.setPosition(0.95);
        Thread.sleep(500);
        dropper.setPosition(0.2);

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
        Thread.sleep(500);
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
        reaperFoldLeft.setPosition(0.3);
        reaperFoldRight.setPosition(0.3);
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

//        mv.moveToPosition(new DcMotor[] {liftL, liftR}, new double[] {-0.8, 0.8}, new int[] {600, 600});
        liftL.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        liftR.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        liftL.setPower(0.8);
        liftR.setPower(-0.8);
        liftL.setTargetPosition(600);
        liftR.setTargetPosition(600);


//        Thread.sleep(100);



//        VuforiaLocalizer.Parameters params = new VuforiaLocalizer.Parameters(R.id.cameraMonitorViewId);
//        params.cameraDirection = VuforiaLocalizer.CameraDirection.BACK;
//        params.vuforiaLicenseKey = "AdVGalv/////AAAAGYhiDIdk+UI+ivt0Y7WGvUJnm5cKX/lWesW2pH7gnK3eOLTKThLekYSO1q65ttw7X1FvNhxxhdQl3McS+mzYjO+HkaFNJlHxltsI5+b4giqNQKWhyKjzbYbNw8aWarI5YCYUFnyiPPjH39/CbBzzFk3G2RWIzNB7cy4AYhjwYRKRiL3k33YvXv0ZHRzJRkMpnytgvdv5jEQyWa20DIkriC+ZBaj8dph8/akyYfyD1/U19vowknmzxef3ncefgOZoI9yrK82T4GBWazgWvZkIz7bPy/ApGiwnkVzp44gVGsCJCUFERiPVwfFa0SBLeCrQMrQaMDy3kOIVcWTotFn4m1ridgE5ZP/lvRzEC4/vcuV0";
//        VuforiaLocalizerImplSubclass vuforiaInstance = new VuforiaLocalizerImplSubclass(params);
//
//

        int reaperInitL = reaperLeft.getCurrentPosition();
        int reaperInitR = reaperRight.getCurrentPosition();
        switch (pos) {
            case 0:
                mv.rotateTo(45);
                reaperFoldLeft.setPosition(0.95);
                reaperFoldRight.setPosition(0.95);;
                mv.moveToPosition(new DcMotor[] {reaperLeft, reaperRight}, new double[] {0.75, 0.75}, new int[] {3000-reaperInitL, 3000-reaperInitR});
                Thread.sleep(250);
                reaperFoldLeft.setPosition(0.3);
                reaperFoldRight.setPosition(0.3);
                mv.moveToPosition(new DcMotor[] {reaperLeft, reaperRight}, new double[] {-0.75, -0.75}, new int[] {3000-reaperInitL, 3000-reaperInitR});
                mv.rotateTo(0);
                break;
            case 2:
                mv.rotateTo(-45);
                reaperFoldLeft.setPosition(0.95);
                reaperFoldRight.setPosition(0.95);;
                mv.moveToPosition(new DcMotor[] {reaperLeft, reaperRight}, new double[] {0.75, 0.75}, new int[] {3000-reaperInitL, 3000-reaperInitR});
                Thread.sleep(250);
                reaperFoldLeft.setPosition(0.3);
                reaperFoldRight.setPosition(0.3);
                mv.moveToPosition(new DcMotor[] {reaperLeft, reaperRight}, new double[] {-0.75, -0.75}, new int[] {3000-reaperInitL, 3000-reaperInitR});
                mv.rotateTo(0);
                break;
            default:
            case 1:
                reaperFoldLeft.setPosition(0.95);
                reaperFoldRight.setPosition(0.95);;
                mv.moveToPosition(new DcMotor[] {reaperLeft, reaperRight}, new double[] {0.75, 0.75}, new int[] {3000-reaperInitL, 3000-reaperInitR});
                Thread.sleep(250);
                reaperFoldLeft.setPosition(0.3);
                reaperFoldRight.setPosition(0.3);
                mv.moveToPosition(new DcMotor[] {reaperLeft, reaperRight}, new double[] {-0.75, -0.75}, new int[] {3000-reaperInitL, 3000-reaperInitR});
                break;
        }

        Thread.sleep(100);
//       mv.rotateDegrees(0.5,90);//TODO:Add global variable for speed

        mv.translateDistance(0.7,-3);
        mv.rotateTo(90);
//        Thread.sleep(100);
//        Thread.sleep(100);
        mv.translateDistance(1, -36*Math.sqrt(2));//TODO:See above immortal TODO
//        Thread.sleep(100);
        mv.rotateTo(135);
//        Thread.sleep(100);
        mv.translateDistance(1, -36);
        mv.rotateTo(90);
//        Thread.sleep(100);
//        mv.rotateDegrees(0.7, -60);
//        Thread.sleep(100);
        marker.setPosition(1);
        Thread.sleep(1000);
        marker.setPosition(0.5);
        mv.rotateTo(145);
//        Thread.sleep(100);
//        mv.rotateDegrees(0.7, 60);
//        Thread.sleep(100);
//       mv.rotate(-0.5,-1);
        mv.translateDistance(1,85);
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
