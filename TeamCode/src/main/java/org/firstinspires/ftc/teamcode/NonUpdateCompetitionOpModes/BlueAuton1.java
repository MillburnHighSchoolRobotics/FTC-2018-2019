package org.firstinspires.ftc.teamcode.NonUpdateCompetitionOpModes;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;
import org.firstinspires.ftc.teamcode.Movement;

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

    final float botWidth = 16.5f; //inches
    final float botRadius = botWidth/2; //in
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
        marker = hardwareMap.servo.get("marker");
        marker.setPosition(0);
        waitForStart();
        lf.setDirection(DcMotorSimple.Direction.REVERSE);
        lb.setDirection(DcMotorSimple.Direction.REVERSE);
        rf.setDirection(DcMotorSimple.Direction.FORWARD);
        rb.setDirection(DcMotorSimple.Direction.FORWARD);
        initializeMotor(new DcMotor[]{lf, lb, rf, rb});
//        for (int x = 0; x < 4; x++) {

//        }
        liftL.setDirection(DcMotorSimple.Direction.REVERSE);

        int initL = liftL.getCurrentPosition();
        int initR = liftR.getCurrentPosition();
//        moveToPosition(new DcMotor[] {liftL, liftR}, new double[] {1, 1}, new int[] {4796+1067-initL, 4796+1067-initR});
//        Thread.sleep(100);
//
//       mv.translate(0.5, 104);
//        Thread.sleep(100);
//
//        moveToPosition(new DcMotor[] {liftL, liftR}, new double[] {1, 1}, new int[] {8418+1067-initL, 8418+1067-initR});
//        Thread.sleep(100);

        //sampling
        Movement mv = new Movement(lf, lb, rf, rb);
        mv.translateDistance(0.7,50);
        Thread.sleep(100);
//        telemetry.addData("Here", "I said here");
        telemetry.update();

        int num = 0;//TODO:Fix //which mineral is the gold mineral one
        switch (num) {
            case 1:
                mv.rotateDegrees(0.5,60);
                mv.translateDistance(0.7,24);
                mv.rotateDegrees(0.5, -60);
                break;
            case 2:
                mv.translateDistance(0.7,35);
                break;
            case 3:
                mv.rotateDegrees(0.5,-60);
                mv.translateDistance(0.7,24);
                mv.rotateDegrees(0.5, 60);
                break;
            default:
                break;
        }
//        Thread.sleep(100);


        //place marker
        marker.setPosition(1);
        Thread.sleep(100);
        mv.rotateDegrees(0.5, -135);
        marker.setPosition(0);
        mv.translateDistance(0.7,75);
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
