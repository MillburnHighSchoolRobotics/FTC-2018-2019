package org.firstinspires.ftc.teamcode.NonUpdateCompetitionOpModes;

import android.util.Log;

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
import org.firstinspires.ftc.teamcode.SahilClassButPartTwo;
import org.firstinspires.ftc.teamcode.TestingOpModes.TFODTest;
import org.firstinspires.ftc.teamcode.watchdog.IMUWatchdog;
import org.firstinspires.ftc.teamcode.watchdog.WatchdogManager;
import org.opencv.android.OpenCVLoader;

import virtualRobot.VuforiaLocalizerImplSubclass;

import static com.qualcomm.robotcore.hardware.DcMotorSimple.Direction.REVERSE;

@Autonomous(name = "Blue Auton Depot Double Mineral", group = "competition")
@Disabled
public class BlueAutonDepotDoubleMineral extends LinearOpMode {
    DcMotor lf;
    DcMotor lb;
    DcMotor rf;
    DcMotor rb;
    DcMotor liftR;
    DcMotor liftL;
    Servo marker;
    Servo stopper;
    DigitalChannel magneticLimitSwitch;

    DcMotorEx reaperLeft;
    DcMotorEx reaperRight;

//    final float BOT_WIDTH = 16.5f; //inches
//    final float botRadiuxs = BOT_WIDTH/2; //in
//    final float wheelWidth = 3; //in
//    final float wheelRadius = wheelWidth/2; //in
//    final int ticksPerRev = 680; //ticks per rev
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

    private Servo dropper;
    private Servo reaperFoldLeft;
    private Servo reaperFoldRight;

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
//        reaperRight.setDirection(REVERSE);
        reaperLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        reaperLeft.setDirection(DcMotorSimple.Direction.REVERSE);
        reaperRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        reaperLeft.setPower(0);
        reaperRight.setPower(0);
        dropper.setPosition(1);
        reaperFoldRight.setDirection(Servo.Direction.REVERSE);
        reaperFoldLeft.setPosition(0);
        reaperFoldRight.setPosition(0);
        WatchdogManager wdm = WatchdogManager.getInstance();
        wdm.setCurrentAuton(this);
        wdm.setHardwareMap(hardwareMap);
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
        JeffBot mv = new JeffBot(lf, lb, rf, rb);

        reaperFoldLeft.setPosition(0.95);
        reaperFoldRight.setPosition(0.95);
        Thread.sleep(500);
        dropper.setPosition(0.2);
//        TFODTest tfod = new TFODTest(hardwareMap);
        liftL.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        liftR.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        liftL.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        liftR.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        liftL.setPower(-0.5);
        liftR.setPower(-0.5);
        ElapsedTime time = new ElapsedTime();
        Thread.sleep(500);
        stopper.setPosition(1);
        Thread.sleep(1000);
        liftL.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        liftR.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        mv.moveUntilPressed(new DcMotor[]{liftL, liftR}, magneticLimitSwitch, mv.POS_POWER_CONST);//Move until limit switch pressed
//        liftL.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
//        liftR.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        liftL.setPower(0.8);
        liftR.setPower(0.8);
        ElapsedTime extraLiftTimer = new ElapsedTime();
        while (extraLiftTimer.milliseconds() < 750) {
            Thread.sleep(5);
        }
        liftL.setPower(0);
        liftR.setPower(0);
        Thread.sleep(750);
        SahilClassButPartTwo sahilClass2 = new SahilClassButPartTwo(vuforiaInstance);
        int num = sahilClass2.getPositionOld();
        telemetry.addData("Position", num);
        telemetry.update();
        Thread.sleep(750);
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
//        mv.rotateTo(0);


//        while (!Thread.currentThread().isInterrupted()) {
        telemetry.addData("Position", num + "");
        telemetry.update();
        mv.translateDistance(0.7,-12);
//        tfod.clean();
//        String name[] = new String[] {"LEFT", "CENTER", "RIGHT"};
//        telemetry.addData("Mineral", num > -1 && num < 4 ? name[num] : "UNKNOWN");
//        telemetry.update();
        num = 2;
        int reaperInitL = reaperLeft.getCurrentPosition();
        int reaperInitR = reaperRight.getCurrentPosition();
        //TODO: SWITCH TO ROTATETO
        switch (num) {
            case 2:
                mv.rotateTo(-90);
                mv.circleAround(12*Math.sqrt(2), -15, 90);
                mv.rotateTo(0);
                mv.translateDistance(0.7, -12);
//                mv.rotateTo(0);
                break;
            case 0:
                mv.rotateTo(45);
                mv.translateDistance(0.7,-30);
//                mv.rotateTo(-90);
//                mv.rotateTo(45);
                mv.rotateTo(-52);
                mv.translateDistance(0.7, -24);
                break;
            default:
            case 1:
                mv.translateDistance(0.7,-32);

                mv.translateDistance(0.7, -17);
                break;
        }
        if (num != 0) {


            mv.translateDistance(1,-7);
            mv.rotateTo(48);
            mv.translateDistance(1, 12);
        }
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
        marker.setPosition(1);
        Thread.sleep(1000);
//            mv.rotateTo(135);
        marker.setPosition(0.5);
        mv.translateDistance(0.7, 66);
        mv.rotateTo(138);
//        lf.setPower(0.5);
//        rf.setPower(0.5);
//        lb.setPower(0.5);
//        rb.setPower(0.5)

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
    public void sampleThisShit(JeffBot mv, TFODTest tfod) {

//        tfod.initStuff();

    }
}
