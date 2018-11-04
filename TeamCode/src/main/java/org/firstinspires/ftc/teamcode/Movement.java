package org.firstinspires.ftc.teamcode;

import android.util.Log;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

import virtualRobot.utils.MathUtils;

import static virtualRobot.utils.MathUtils.sgn;

public class Movement {
    DcMotor lf;
    DcMotor lb;
    DcMotor rf;
    DcMotor rb;
    final static float botWidth = 16.5f; //inches
    final static float botRadius = botWidth/2; //in
    final static float wheelWidth = 3; //in
    final static float wheelRadius = wheelWidth/2; //in
    final static int ticksPerRev = 730; //ticks per rev
    public Movement(DcMotor lf, DcMotor lb, DcMotor rf, DcMotor rb) {
        this.lf = lf;
        this.lb = lb;
        this.rf = rf;
        this.rb = rb;
    }
    public void translate(double power, int positionChange) throws InterruptedException {
//        lf.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
//        lb.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
//        rf.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
//        rb.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
//        lf.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
//        lb.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
//        rf.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
//        rb.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
//        lf.setPower(power);
//        lb.setPower(power);
//        rf.setPower(power);
//        rb.setPower(power);
//        while (Math.abs(lf.getCurrentPosition()) < Math.abs(positionChange) && Math.abs(lb.getCurrentPosition()) < Math.abs(positionChange) && Math.abs(rf.getCurrentPosition()) < Math.abs(positionChange) && Math.abs(rb.getCurrentPosition()) < Math.abs(positionChange)) {
//            telemetry.addData("meme", lf.getCurrentPosition() + " " + lb.getCurrentPosition() + " " + rf.getCurrentPosition() + " " + rb.getCurrentPosition());
//            telemetry.update();
//            Thread.sleep(10);
//        }

        lf.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        lb.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rf.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rb.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        moveToPosition(new DcMotor[] {lf,lb,rf,rb}, new double[] {power,power,-power,-power}, new int[] {(positionChange),(positionChange),(positionChange),(positionChange)});
    }

    public void translateDistance(double power, double dist) throws InterruptedException {
        translate(power, distToEncoder(dist));
    }

    public void rotate(double power, int positionChange) throws InterruptedException {

//        lf.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
//        lb.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
//        rf.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
//        rb.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
//        lf.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
//        lb.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
//        rf.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
//        rb.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
//        lf.setPower(power);
//        lb.setPower(power);
//        rf.setPower(-power);
//        rb.setPower(-power);
//        while (Math.abs(lf.getCurrentPosition()) < Math.abs(positionChange) && Math.abs(lb.getCurrentPosition()) < Math.abs(positionChange) && Math.abs(rf.getCurrentPosition()) < Math.abs(positionChange) && Math.abs(rb.getCurrentPosition()) < Math.abs(positionChange)) {
//            telemetry.addData("meme", lf.getCurrentPosition() + " " + lb.getCurrentPosition() + " " + rf.getCurrentPosition() + " " + rb.getCurrentPosition());
//            telemetry.update();
//            Thread.sleep(10);
//        }

        lf.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        lb.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rf.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rb.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        moveToPosition(new DcMotor[] {lf,lb,rf,rb}, new double[] {power,power,-power,-power},  new int[] {(positionChange),(positionChange),(-positionChange),(-positionChange)});
    }

    public void rotateDegrees(double power, double degrees) throws InterruptedException {
        rotate(power, rotateToEncoder(MathUtils.sgn(degrees) * Math.toRadians(Math.abs(degrees))));
    }
    public void moveToPosition(DcMotor[] motors, double[] power, int[] position) throws InterruptedException {
        for (int x = 0; x < motors.length; x++) {
            motors[x].setPower(power[x]);
            motors[x].setTargetPosition(position[x]);
            motors[x].setMode(DcMotor.RunMode.RUN_TO_POSITION);
        }
        while (true) {
            if (Thread.currentThread().isInterrupted()) {
                throw new InterruptedException();
            }
            boolean flag = false;
            for (int i = 0; i < motors.length; i++) {
                flag = flag || (motors[i].isBusy() && !MathUtils.equals(motors[i].getCurrentPosition(), position[i], 50));
            }
            for (int i = 0; i < motors.length; i++) {
                Log.d("bigmeme", motors[i].isBusy() + " " + i);
            }
            if (!flag) {
                break;
            }
            try {
                Thread.sleep(10);
            } catch (InterruptedException ex) {}
        }
        boolean flag = false;
        for (DcMotor motor : motors) {
            flag = flag || motor.isBusy();
        }
        if (flag) {
            ElapsedTime et = new ElapsedTime();
            while (et.milliseconds() < 100) {
                boolean flag2 = false;
                for (DcMotor motor : motors) {
                    flag2 = flag2 || motor.isBusy();
                }
                if (!flag2) {
                    break;
                }
                Thread.sleep(10);
            }
        }
        for (DcMotor motor : motors) {
            motor.setPower(0);
            motor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        }
    }
    public static int distToEncoder(double dist) { //inches
        return (int)(dist/(2*Math.PI*wheelRadius) * ticksPerRev);
    }

    public static int rotateToEncoder(double rad) {
        double dist = botRadius*rad;
        return distToEncoder(dist);
    }
}
