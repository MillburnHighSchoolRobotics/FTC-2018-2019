package org.firstinspires.ftc.teamcode.NonUpdateCompetitionOpModes;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.Movement;
import org.firstinspires.ftc.teamcode.TestingOpModes.TFODTest;
import org.firstinspires.ftc.teamcode.watchdog.IMUWatchdog;
import org.firstinspires.ftc.teamcode.watchdog.WatchdogManager;

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
        liftL.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        liftR.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        liftL.setPower(-0.5);
        liftR.setPower(-0.5);
        ElapsedTime time = new ElapsedTime();
        while(time.milliseconds() < 500) {
            Thread.sleep(5);
        }
        stopper.setPosition(1);
        while(time.milliseconds()<2000){
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

        int num = 1;//tfod.getGoldPos();//TODO:Fix //which mineral is the gold mineral one
//        tfod.clean();
//        String name[] = new String[] {"LEFT", "CENTER", "RIGHT"};
//        telemetry.addData("Mineral", num > -1 && num < 4 ? name[num] : "UNKNOWN");
//        telemetry.update();

        //TODO: SWITCH TO ROTATETO
        switch (num) {
            case 0:
                mv.rotateDegrees(0.5,60);
                mv.translateDistance(0.7,-24);
                mv.rotateDegrees(0.5, -60);
                break;
            case 2:
                mv.rotateDegrees(0.5,-60);
                mv.translateDistance(0.7,-24);
                mv.rotateDegrees(0.5, 60);
                break;
            default:
            case 1:
                mv.translateDistance(0.7,-35);
                break;
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
