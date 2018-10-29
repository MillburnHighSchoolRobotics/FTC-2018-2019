package org.firstinspires.ftc.teamcode.NonUpdateCompetitionOpModes;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;
import org.firstinspires.ftc.teamcode.Movement;

@Autonomous(name = "Blue Auton Pit", group = "competition")
public class BlueAuton2 extends LinearOpMode {
    DcMotor lf;
    DcMotor lb;
    DcMotor rf;
    DcMotor rb;
    Servo marker;



    DcMotor liftR;
    DcMotor liftL;

    int meme = 0;

    @Override
    public void runOpMode() throws InterruptedException {

        lf = hardwareMap.dcMotor.get("leftFront");
        lb = hardwareMap.dcMotor.get("leftBack");
        rf = hardwareMap.dcMotor.get("rightFront");
        rb = hardwareMap.dcMotor.get("rightBack");
        liftL = hardwareMap.dcMotor.get("liftL");
        liftR = hardwareMap.dcMotor.get("liftR");
        marker = hardwareMap.servo.get("marker");
        int initL = liftL.getCurrentPosition();
        int initR = liftR.getCurrentPosition();
        waitForStart();
        lf.setDirection(DcMotorSimple.Direction.FORWARD);
        lb.setDirection(DcMotorSimple.Direction.FORWARD);
        rf.setDirection(DcMotorSimple.Direction.REVERSE);
        rb.setDirection(DcMotorSimple.Direction.REVERSE);
//        for (int x = 0; x < 4; x++) {
            initializeMotor(new DcMotor[]{lf, lb, rf, rb});
//        }

//        liftL.setDirection(DcMotorSimple.Direction.REVERSE);

//        moveToPosition(new DcMotor[] {liftL, liftR}, new double[] {1, 1}, new int[] {4796+1067-initL, 4796+1067-initR});
//        Thread.sleep(100);
//        translate(0.5, 104);
//        Thread.sleep(100);
//
//        moveToPosition(new DcMotor[] {liftL, liftR}, new double[] {1, 1}, new int[] {8418+1067-initL, 8418+1067-initR});
//        Thread.sleep(100);

        //sampling
        Movement mv = new Movement(lf, lb, rf, rb);

        mv.translateDistance(0.7,10);
        Thread.sleep(100);

        mv.rotateDegrees(0.7, 90);
        Thread.sleep(100);
//        int num = 0;//TODO:Fix //which mineral is the gold mineral one
//        switch (num) {
//            case 1:
//                rotate(-0.5,-1);
//                translate(0.7,1);
//                rotate(0.5, 1);
//                break;
//            case 2:
//                translate(0.7,1);
//                break;
//            case 3:
//                rotate(0.5,1);
//                translate(0.7,1);
//                rotate(-0.5, -1);
//                break;
//            default:
//                break;
//        }
//        Thread.sleep(100);
//        rotate(-0.5, rotateToEncoder(Math.toRadians(-90)));//TODO:Add global variable for speed
//        Thread.sleep(100);
//        translate(0.7, distToEncoder(48));//TODO:See above immortal TODO
//        Thread.sleep(100);
//        rotate(0.5, rotateToEncoder(-45));
//        Thread.sleep(100);
//        translate(0.7, distToEncoder(39));
//        Thread.sleep(100);
//        marker.setPosition(0);
////        rotate(-0.5,-1);
//        translate(-0.7,distToEncoder(-110));
    }
    public void initializeMotor(DcMotor[] motors) {
        for (DcMotor motor : motors) {
            motor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            motor.setPower(0);
//            motor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
//            motor.setTargetPosition(0);
        }
    }



//    public void translate(double power, int positionChange) {
//        telemetry.addData("meme", meme);
//        meme++;
//        lf.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
//        lb.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
//        rf.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
//        rb.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
//        moveToPosition(new DcMotor[] {lf,lb,rf,rb}, new double[] {power,power,power,power}, new int[] {(positionChange),(positionChange),(positionChange),(positionChange)});
//    }
//
//    public void rotate(double power, int positionChange) {
//        telemetry.addData("meme", meme);
//        meme++;
//        lf.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
//        lb.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
//        rf.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
//        rb.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
//        moveToPosition(new DcMotor[] {lf,lb,rf,rb}, new double[] {power,power,-power,-power},  new int[] {(positionChange),(positionChange),(-positionChange),(-positionChange)});
//    }
//}



}