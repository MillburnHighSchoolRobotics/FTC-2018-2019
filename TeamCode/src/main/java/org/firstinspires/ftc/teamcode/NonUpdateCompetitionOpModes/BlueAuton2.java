package org.firstinspires.ftc.teamcode.NonUpdateCompetitionOpModes;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;

public class BlueAuton2 extends LinearOpMode {
    DcMotor lf = hardwareMap.dcMotor.get("lf");
    DcMotor lb = hardwareMap.dcMotor.get("lb");
    DcMotor rf = hardwareMap.dcMotor.get("rf");
    DcMotor rb = hardwareMap.dcMotor.get("rb");
    Servo marker = hardwareMap.servo.get("marker");
    final float botWidth = 16.5f; //inches
    final float botRadius = botWidth/2; //in
    final float wheelWidth = 3; //in
    final float wheelRadius = wheelWidth/2; //in
    final int ticksPerRev = 680; //ticks per rev


    public int distToEncoder(double dist) { //inches
        return (int)(dist/(2*Math.PI*wheelRadius) * ticksPerRev);
    }

    public int rotateToEncoder(double rad) {
        double dist = botRadius*rad;
        return distToEncoder(dist);
    }

    @Override
    public void runOpMode() throws InterruptedException {
        lf.setDirection(DcMotorSimple.Direction.FORWARD);
        lb.setDirection(DcMotorSimple.Direction.FORWARD);
        rf.setDirection(DcMotorSimple.Direction.REVERSE);
        rb.setDirection(DcMotorSimple.Direction.REVERSE);
//        for (int x = 0; x < 4; x++) {
            initializeMotor(new DcMotor[]{lf, lb, rf, rb});
//        }

        //sampling
        translate(0.7,distToEncoder(18));
        int num = 0;//TODO:Fix //which mineral is the gold mineral one
        switch (num) {
            case 1:
                rotate(-0.5,-1);
                translate(0.7,1);
                rotate(0.5, 1);
                break;
            case 2:
                translate(0.7,1);
                break;
            case 3:
                rotate(0.5,1);
                translate(0.7,1);
                rotate(-0.5, -1);
                break;
            default:
                break;
        }
        rotate(-0.5, rotateToEncoder(Math.toRadians(-90)));//TODO:Add global variable for speed
        translate(0.7, distToEncoder(48));//TODO:See above immortal TODO
        rotate(0.5, rotateToEncoder(-45));
        translate(0.7, distToEncoder(39));
        marker.setPosition(0);
//        rotate(-0.5,-1);
        translate(-0.7,distToEncoder(-110));
    }
    public void initializeMotor(DcMotor[] motors) {
        for (DcMotor motor : motors) {
            motor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
//            motor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
//            motor.setTargetPosition(0);
        }
    }

    public void moveToPosition(DcMotor[] motors, double[] power, int[] position) {
        for (int x = 0; x < motors.length; x++) {
            motors[x].setPower(power[x]);
            motors[x].setTargetPosition(position[x]);
            motors[x].setMode(DcMotor.RunMode.RUN_TO_POSITION);
        }
        while (motors[0].isBusy() || motors[1].isBusy() || motors[2].isBusy() || motors[3].isBusy()) {
            try {
                Thread.sleep(10);
            } catch (InterruptedException ex) {}
        }
        for (DcMotor motor : motors) {
            motor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        }
    }

    public void translate(double power, int positionChange) {
        moveToPosition(new DcMotor[] {lf,lb,rf,rb}, new double[] {power,power,power,power}, new int[] {(lf.getCurrentPosition()+positionChange),(lb.getCurrentPosition()+positionChange),(rf.getCurrentPosition()+positionChange),(rb.getCurrentPosition()+positionChange)});
    }

    public void rotate(double power, int positionChange) {
        moveToPosition(new DcMotor[] {lf,lb,rf,rb}, new double[] {power,power,-power,-power},  new int[] {(lf.getCurrentPosition()+positionChange),(lb.getCurrentPosition()+positionChange),(rf.getCurrentPosition()-positionChange),(rb.getCurrentPosition()-positionChange)});
    }
}