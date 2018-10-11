package org.firstinspires.ftc.teamcode.NonUpdateCompetitionOpModes;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

public class BlueAuton extends LinearOpMode {
    DcMotor lf = hardwareMap.dcMotor.get("lf");
    DcMotor lb = hardwareMap.dcMotor.get("lb");
    DcMotor rf = hardwareMap.dcMotor.get("rf");
    DcMotor rb = hardwareMap.dcMotor.get("rb");

    @Override
    public void runOpMode() throws InterruptedException {
        lf.setDirection(DcMotorSimple.Direction.REVERSE);
        lf.setDirection(DcMotorSimple.Direction.REVERSE);
        rf.setDirection(DcMotorSimple.Direction.FORWARD);
        rb.setDirection(DcMotorSimple.Direction.FORWARD);
        for (int x = 0; x < 4; x++) {
            initializeMotor(new DcMotor[]{lf, lb, rf, rb});
        }

        //sampling
        translate(0.7,1);
        int num = 1; //which mineral is the gold mineral one
        switch (num) {
            case 1:
                rotate(-0.5,-1);
                translate(0.7,1);
                break;
            case 2:
                translate(0.7,1);
                break;
            case 3:
                rotate(0.5,1);
                translate(0.7,1);

        }


        //claiming
        int position = 1;
        switch (position) {
            case 1:
                rotate(-0.5,-1);
                translate(0.7,1);
                break;
            case 2:
                translate(0.9,1);
                break;
        }
        //place marker
        rotate(-0.5,-1);
        translate(0.7,1);

        //park
        rotate(0.5,1);
        translate(0.9,1);
    }
    public void initializeMotor(DcMotor[] motors) {
        for (DcMotor motor : motors) {
            motor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            motor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            motor.setTargetPosition(0);
        }
    }

    public void moveToPosition(DcMotor[] motors, double[] power, int[] position) {
        for (int x = 0; x < motors.length; x++) {
            motors[x].setPower(power[x]);
            motors[x].setTargetPosition(position[x]);
        }
        boolean busy = true;
        while (motors[0].isBusy() || motors[1].isBusy() || motors[2].isBusy() || motors[3].isBusy()) {
            try {
                Thread.sleep(10);
            } catch (InterruptedException ex) {
            }
        }
    }

    public void translate(double power, int positionChange) {
        moveToPosition(new DcMotor[] {lf,lb,rf,rb}, new double[] {power,power,power,power}, new int[] {(lf.getCurrentPosition()+positionChange),(lb.getCurrentPosition()+positionChange),(rf.getCurrentPosition()+positionChange),(rb.getCurrentPosition()+positionChange)});
    }

    public void rotate(double power, int positionChange) {
        moveToPosition(new DcMotor[] {lf,lb,rf,rb}, new double[] {power,power,-power,-power},  new int[] {(lf.getCurrentPosition()+positionChange),(lb.getCurrentPosition()+positionChange),(rf.getCurrentPosition()-positionChange),(rb.getCurrentPosition()-positionChange)});
    }
}