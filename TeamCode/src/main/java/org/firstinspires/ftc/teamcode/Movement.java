package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;

public class Movement {
    DcMotor lf;
    DcMotor lb;
    DcMotor rf;
    DcMotor rb;
    final static float botWidth = 16.5f; //inches
    final static float botRadius = botWidth/2; //in
    final static float wheelWidth = 3; //in
    final static float wheelRadius = wheelWidth/2; //in
    final static int ticksPerRev = 680; //ticks per rev
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
        moveToPosition(new DcMotor[] {lf,lb,rf,rb}, new double[] {power,power,-power,-power},  new int[] {(-positionChange),(-positionChange),(positionChange),(positionChange)});
    }

    public void rotateDegrees(double power, double degrees) throws InterruptedException {
        rotate(power, rotateToEncoder(Math.toRadians(degrees)));
    }
    public void moveToPosition(DcMotor[] motors, double[] power, int[] position) {
        for (int x = 0; x < motors.length; x++) {
            motors[x].setPower(power[x]);
            motors[x].setTargetPosition(position[x]);
            motors[x].setMode(DcMotor.RunMode.RUN_TO_POSITION);
        }
        while (true) {
            boolean flag = false;
            for (DcMotor motor : motors) {
                flag = flag || motor.isBusy();
            }
            if (!flag) {
                break;
            }
            try {
                Thread.sleep(10);
            } catch (InterruptedException ex) {}
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
