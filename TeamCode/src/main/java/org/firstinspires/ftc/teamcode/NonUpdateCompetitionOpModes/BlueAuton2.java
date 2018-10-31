package org.firstinspires.ftc.teamcode.NonUpdateCompetitionOpModes;

import android.util.Log;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.Movement;

import static org.firstinspires.ftc.teamcode.Movement.distToEncoder;
import static org.firstinspires.ftc.teamcode.Movement.rotateToEncoder;

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
        lf.setDirection(DcMotorSimple.Direction.REVERSE);
        lb.setDirection(DcMotorSimple.Direction.REVERSE);
        rf.setDirection(DcMotorSimple.Direction.FORWARD);
        rb.setDirection(DcMotorSimple.Direction.FORWARD);
//        for (int x = 0; x < 4; x++) {
            initializeMotor(new DcMotor[]{lf, lb, rf, rb});
//        }

//        liftL.setDirection(DcMotorSimple.Direction.REVERSE);

//        moveToPosition(new DcMotor[] {liftL, liftR}, new double[] {1, 1}, new int[] {4796+1067-initL, 4796+1067-initR});
//        Thread.sleep(100);
//       mv.translate(0.5, 104);
//        Thread.sleep(100);
//
//        moveToPosition(new DcMotor[] {liftL, liftR}, new double[] {1, 1}, new int[] {8418+1067-initL, 8418+1067-initR});
//        Thread.sleep(100);

        //sampling
        int meme = 0;
        Movement mv = new Movement(lf, lb, rf, rb);
        Log.d("fuckkkk", "" + meme++); //0

        mv.translateDistance(0.7,15);
        Log.d("fuckkkk", "" + meme++); //1
        Thread.sleep(100);

        mv.rotateDegrees(0.7, 90);
        Log.d("fuckkkk", "" + meme++); //2
        Thread.sleep(100);
        int num = 0;//TODO:Fix //which mineral is the gold mineral one
        switch (num) {
            case 1:
               mv.rotate(-0.5,-1);
               mv.translate(0.7,1);
               mv.rotate(0.5, 1);
                break;
            case 2:
               mv.translate(0.7,1);
                break;
            case 3:
               mv.rotate(0.5,1);
               mv.translate(0.7,1);
               mv.rotate(-0.5, -1);
                break;
            default:
                break;
        }
        Thread.sleep(100);
//       mv.rotateDegrees(0.5,90);//TODO:Add global variable for speed
        Log.d("fuckkkk", "" + meme++); //3
        Thread.sleep(100);
       mv.translateDistance(0.7, 44);//TODO:See above immortal TODO
        Log.d("fuckkkk", "" + meme++); //4
        Thread.sleep(100);
       mv.rotateDegrees(0.5,60);
        Log.d("fuckkkk", "" + meme++); //5
        Thread.sleep(100);
       mv.translateDistance(0.7, 39);
        Log.d("fuckkkk", "" + meme++); //6
//        Thread.sleep(100);
//        mv.rotateDegrees(0.7, -60);
//        Thread.sleep(100);
        marker.setPosition(1);
        ElapsedTime time = new ElapsedTime();
        while (time.seconds() < 1.0f) {
            Thread.sleep(10);
        }
        Thread.sleep(100);
//        mv.rotateDegrees(0.7, 60);
//        Thread.sleep(100);
//       mv.rotate(-0.5,-1);
       mv.translateDistance(0.7,-80);
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
