package org.firstinspires.ftc.teamcode.NonUpdateCompetitionOpModes;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

@Autonomous(name = "dumb dumb dumb")
public class DumbDumbAutonPit extends LinearOpMode {
    DcMotor lf;
    DcMotor lb;
    DcMotor rf;
    DcMotor rb;
    DcMotor liftR;
    DcMotor liftL;
    //Servo marker = hardwareMap.servo.get("marker");

    final float botWidth = 16.5f; //inches
    final float botRadius = botWidth / 2; //in
    final float wheelWidth = 3; //in
    final float wheelRadius = wheelWidth / 2; //in
    final int ticksPerRev = 680; //ticks per rev
//    final float targetRadius = 10+c; //inches
//    final double targetSpeed =4; //inches/sec
//    final double encoderSpeed = (targetSpeed/(2*Math.PI*wheelRadius)) * ticksPerRev; //encoders per sec

    public int distToEncoder(double dist) { //inches
        return (int) (dist / (2 * Math.PI * wheelRadius) * ticksPerRev);
    }

    public int rotateToEncoder(double rad) {
        double dist = botRadius * rad;
        return distToEncoder(dist);
    }

    int meme = 0;

    @Override
    public void runOpMode() throws InterruptedException {
        lf = hardwareMap.dcMotor.get("leftFront");
        lb = hardwareMap.dcMotor.get("leftBack");
        rf = hardwareMap.dcMotor.get("rightFront");
        rb = hardwareMap.dcMotor.get("rightBack");
        liftL = hardwareMap.dcMotor.get("liftL");
        liftR = hardwareMap.dcMotor.get("liftR");
        waitForStart();
        lf.setDirection(DcMotorSimple.Direction.FORWARD);
        lb.setDirection(DcMotorSimple.Direction.FORWARD);
        rf.setDirection(DcMotorSimple.Direction.REVERSE);
        rb.setDirection(DcMotorSimple.Direction.REVERSE);
//        for (int x = 0; x < 4; x++) {
        initializeMotor(new DcMotor[]{lf, lb, rf, rb});
//        }
        liftL.setDirection(DcMotorSimple.Direction.REVERSE);

        int initL = liftL.getCurrentPosition();
        int initR = liftR.getCurrentPosition();
//        moveToPosition(new DcMotor[] {liftL, liftR}, new double[] {1, 1}, new int[] {4796+1067-initL, 4796+1067-initR});
//        Thread.sleep(100);
//
//        translate(0.5, 104);
//        Thread.sleep(100);
//
//        moveToPosition(new DcMotor[] {liftL, liftR}, new double[] {1, 1}, new int[] {8418+1067-initL, 8418+1067-initR});
//        Thread.sleep(100);

        //sampling
        translate(0.7, distToEncoder(30));
    }

    public void initializeMotor(DcMotor[] motors) {
        for (DcMotor motor : motors) {
            motor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
//            motor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
//            motor.setTargetPosition(0);
        }
    }

    public void translate(double power, int positionChange) throws InterruptedException {
        lf.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        lb.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rf.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rb.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        lf.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        lb.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rf.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rb.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        lf.setPower(power);
        lb.setPower(power);
        rf.setPower(power);
        rb.setPower(power);
        while (Math.abs(lf.getCurrentPosition()) < Math.abs(positionChange) && Math.abs(lb.getCurrentPosition()) < Math.abs(positionChange) && Math.abs(rf.getCurrentPosition()) < Math.abs(positionChange) && Math.abs(rb.getCurrentPosition()) < Math.abs(positionChange)) {
            telemetry.addData("meme", lf.getCurrentPosition() + " " + lb.getCurrentPosition() + " " + rf.getCurrentPosition() + " " + rb.getCurrentPosition());
            telemetry.update();
            Thread.sleep(10);
        }

//        telemetry.addData("meme", meme);
//        meme++;
//        lf.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
//        lb.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
//        rf.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
//        rb.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
//        moveToPosition(new DcMotor[] {lf,lb,rf,rb}, new double[] {power,power,power,power}, new int[] {(positionChange),(positionChange),(-positionChange),(-positionChange)});
    }

    public void rotate(double power, int positionChange) throws InterruptedException {

        lf.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        lb.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rf.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rb.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        lf.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        lb.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rf.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rb.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        lf.setPower(power);
        lb.setPower(power);
        rf.setPower(-power);
        rb.setPower(-power);
        while (Math.abs(lf.getCurrentPosition()) < Math.abs(positionChange) && Math.abs(lb.getCurrentPosition()) < Math.abs(positionChange) && Math.abs(rf.getCurrentPosition()) < Math.abs(positionChange) && Math.abs(rb.getCurrentPosition()) < Math.abs(positionChange)) {
            telemetry.addData("meme", lf.getCurrentPosition() + " " + lb.getCurrentPosition() + " " + rf.getCurrentPosition() + " " + rb.getCurrentPosition());
            telemetry.update();
            Thread.sleep(10);
        }

    }
}
