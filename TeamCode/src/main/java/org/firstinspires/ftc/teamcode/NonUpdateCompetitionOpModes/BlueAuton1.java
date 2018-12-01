package org.firstinspires.ftc.teamcode.NonUpdateCompetitionOpModes;

import android.util.Log;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.teamcode.Movement;
import org.firstinspires.ftc.teamcode.SahilClass;
import org.firstinspires.ftc.teamcode.TestingOpModes.TFODTest;
import org.firstinspires.ftc.teamcode.watchdog.IMUWatchdog;
import org.firstinspires.ftc.teamcode.watchdog.WatchdogManager;
import org.opencv.android.OpenCVLoader;

import virtualRobot.VuforiaLocalizerImplSubclass;
import virtualRobot.utils.MathUtils;

import static org.firstinspires.ftc.teamcode.Movement.distToEncoder;
import static org.firstinspires.ftc.teamcode.Movement.rotateToEncoder;
import static virtualRobot.utils.MathUtils.sgn;

@Autonomous(name = "Blue Auton Depot", group = "competition")
public class BlueAuton1 extends LinearOpMode {
    DcMotor lf;
    DcMotor lb;
    DcMotor rf;
    DcMotor rb;
    DcMotor liftR;
    DcMotor liftL;
    Servo marker;
    Servo stopper;
    DigitalChannel magneticLimitSwitch;

    final float botWidth = 16.5f; //inches
    final float botRadiuxs = botWidth/2; //in
    final float wheelWidth = 3; //in
    final float wheelRadius = wheelWidth/2; //in
    final int ticksPerRev = 680; //ticks per rev
//    final float targetRadius = 10+c; //inches
//    final double targetSpeed =4; //inches/sec
//    final double encoderSpeed = (targetSpeed/(2*Math.PI*wheelRadius)) * ticksPerRev; //encoders per sec

    static {
        if(OpenCVLoader.initDebug()) {
            Log.d("opencv","yay it works");
        } else {
            Log.d("opencv","nope it doesnt work");
        }
    }

    @Override
    public void runOpMode() throws InterruptedException {
        lf = hardwareMap.dcMotor.get("leftFront");
        lb = hardwareMap.dcMotor.get("leftBack");
        rf = hardwareMap.dcMotor.get("rightFront");
        rb = hardwareMap.dcMotor.get("rightBack");
        liftL = hardwareMap.dcMotor.get("liftL");
        liftR = hardwareMap.dcMotor.get("liftR");
        magneticLimitSwitch = hardwareMap.get(DigitalChannel.class, "Switchy");
        marker = hardwareMap.servo.get("marker");
        stopper = hardwareMap.servo.get("stopper");
        WatchdogManager wdm = WatchdogManager.getInstance();
        wdm.setHardwareMap(hardwareMap);
        wdm.provision("IMUWatch", IMUWatchdog.class, "imu");
        marker.setPosition(0);
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
        Movement mv = new Movement(lf, lb, rf, rb);
        TFODTest tfod = new TFODTest(hardwareMap);
        liftL.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        liftR.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        liftL.setPower(mv.NEG_POWER_CONST);
        liftR.setPower(mv.NEG_POWER_CONST);
        ElapsedTime time = new ElapsedTime();
        while(time.milliseconds() < 100) {
            Thread.sleep(5);
        }
        stopper.setPosition(1);
        while(time.milliseconds()<250){
            Thread.sleep(5);
        }
        mv.moveUntilPressed(new DcMotor[]{liftL, liftR}, magneticLimitSwitch, mv.POS_POWER_CONST);//Move until limit switch pressed
//        liftL.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
//        liftR.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        liftL.setPower(0.6);
        liftR.setPower(0.6);
        ElapsedTime extraLiftTimer = new ElapsedTime();
        while (extraLiftTimer.milliseconds() < 250) {
            Thread.sleep(5);
        }
        liftL.setPower(0);
        liftR.setPower(0);
        //mv.moveToPosition(new DcMotor[] {liftL, liftR}, new double[] {-0.8, 0.8}, new int[] {10700+100, 10700+100});
//        Thread.sleep(100);
//
//       mv.translate(0.5, 104);
//        Thread.sleep(100);
//
//        moveToPosition(new DcMotor[] {liftL, liftR}, new double[] {1, 1}, new int[] {8418+1067-initL, 8418+1067-initR});
//        Thread.sleep(100);

        //sampling
        mv.translateDistance(0.7,-15);
//        mv.rotateTo(0);

        VuforiaLocalizer.Parameters params = new VuforiaLocalizer.Parameters(hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName()));
        params.cameraDirection = VuforiaLocalizer.CameraDirection.BACK;

        params.vuforiaLicenseKey = "AdVGalv/////AAAAGYhiDIdk+UI+ivt0Y7WGvUJnm5cKX/lWesW2pH7gnK3eOLTKThLekYSO1q65ttw7X1FvNhxxhdQl3McS+mzYjO+HkaFNJlHxltsI5+b4giqNQKWhyKjzbYbNw8aWarI5YCYUFnyiPPjH39/CbBzzFk3G2RWIzNB7cy4AYhjwYRKRiL3k33YvXv0ZHRzJRkMpnytgvdv5jEQyWa20DIkriC+ZBaj8dph8/akyYfyD1/U19vowknmzxef3ncefgOZoI9yrK82T4GBWazgWvZkIz7bPy/ApGiwnkVzp44gVGsCJCUFERiPVwfFa0SBLeCrQMrQaMDy3kOIVcWTotFn4m1ridgE5ZP/lvRzEC4/vcuV0";

        VuforiaLocalizerImplSubclass vuforiaInstance = new VuforiaLocalizerImplSubclass(params);
        waitForStart();

        SahilClass sahilClass = new SahilClass(vuforiaInstance, 1000); //this only loops once after 1000 millis but keep this constraint just in case
//        while (!Thread.currentThread().isInterrupted()) {
        int num = sahilClass.getPosition();
        telemetry.addData("Position", num + "");
        telemetry.update();
//        tfod.clean();
//        String name[] = new String[] {"LEFT", "CENTER", "RIGHT"};
//        telemetry.addData("Mineral", num > -1 && num < 4 ? name[num] : "UNKNOWN");
//        telemetry.update();

        //TODO: SWITCH TO ROTATETO
        switch (num) {
            case 0:
                mv.rotateTo(45);
                mv.translateDistance(0.7,-24);
                mv.rotateTo(-45);
                mv.translateDistance(0.7, -24);
                mv.rotateTo(0);
                break;
            case 2:
                mv.rotateTo(-45);
                mv.translateDistance(0.7,-24);
                mv.rotateTo(45);
                mv.translateDistance(0.7, -24);
                mv.rotateTo(0);
                break;
            default:
            case 1:
                mv.translateDistance(0.7,-34);
                break;
        }
        mv.translateDistance(0.7, -17);

//        mv.moveToPosition(new DcMotor[] {liftL, liftR}, new double[] {-0.8, 0.8}, new int[] {600, 600});
        liftL.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        liftR.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        liftL.setPower(0.8);
        liftR.setPower(-0.8);
        liftL.setTargetPosition(600);
        liftR.setTargetPosition(600);
//        Thread.sleep(100);
//        telemetry.addData("Here", "I said here");
//        telemetry.update();

//        sampleThisShit(mv, tfod);
        Thread.sleep(100);


        //place marker
        mv.rotateTo(-60);
        marker.setPosition(1);
        Thread.sleep(1000);
        mv.rotateTo(-135);
        marker.setPosition(0);
        mv.translateDistance(0.7,-80);

        wdm.clean();
    }
    public void initializeMotor(DcMotor[] motors) {
        for (DcMotor motor : motors) {
            motor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            motor.setPower(0);
//            motor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
//            motor.setTargetPosition(0);
        }
    }
    public void sampleThisShit(Movement mv, TFODTest tfod) {

//        tfod.initStuff();

    }
}
