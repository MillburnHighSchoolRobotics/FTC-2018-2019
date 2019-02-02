package org.firstinspires.ftc.teamcode;

import android.app.Activity;
import android.util.Log;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.internal.opmode.OpModeManagerImpl;
import org.firstinspires.ftc.robotcore.internal.system.AppUtil;
import org.firstinspires.ftc.teamcode.watchdog.WatchdogManager;

import virtualRobot.PIDController;
import virtualRobot.utils.MathUtils;

public class JeffBot {
    public final double POS_POWER_CONST = 0.7;
    public final double NEG_POWER_CONST = -0.7;
    final double kP = 0.0125; //0.0225
    final double kI = 0; //0.0035
    final double kD = 0.005; //0.0175
    DcMotor lf;
    DcMotor lb;
    DcMotor rf;
    DcMotor rb;
    final static float botWidth = 16.5f; //inches
    final static float botRadius = botWidth/2; //in
    final static float wheelWidth = 3; //in
    final static float wheelRadius = wheelWidth/2; //in
    final static int ticksPerRev = 730; //ticks per rev
    public final static String vuforiaKey = "AcSW/tj/////AAABmUB3byzZQksfqhymb0Tr3M92yvtrzF4HgDl0t7Z07OZ2xscXR1yyeX4GxftrShvm9T926ZCW0VglQKXuQE5+JkrABVijohk5DCkcE9CcxHy3mTs2Ui76Nz+9CQTgOsr6/AMLV+Te6uyXTs3rZwGdnHGRo0Q1yboJCQ51Ap2rgJc//ehVdkp/QetIMnfhRffac0edAHFt0i2F5++S/OH/4kdxFd5ha0lswd4nTnqU2MiJrz+OH4WQPQ8JC94dQZ6F3m/iX5mk4TCq/9xg3cTJvFccEUawf7PIsapABxKMJB6hcPikwa0XtyGB+vEb7fQAXZ80tRal2mcwKSHrDM4ZvYisD73X+sTIAqQnXgxYiL14";
    public JeffBot(DcMotor lf, DcMotor lb, DcMotor rf, DcMotor rb) {
        this.lf = lf;
        this.lb = lb;
        this.rf = rf;
        this.rb = rb;
    }
    public JeffBot() {}
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
        moveToPosition(new DcMotor[] {lf,lb,rf,rb}, new double[] {-power,-power,power,power}, new int[] {(positionChange),(positionChange),(positionChange),(positionChange)});
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
        moveToPosition(new DcMotor[] {lf,lb,rf,rb}, new double[] {-power,-power,power,power},  new int[] {(positionChange),(positionChange),(-positionChange),(-positionChange)});
    }

    public void rotateTo(double target) throws InterruptedException {
        ElapsedTime time = new ElapsedTime();
        while (!shouldStop() && !Thread.currentThread().isInterrupted() && WatchdogManager.getInstance().getValue("rotation") == null) {
            Thread.sleep(5);
        }
        PIDController pidController = new PIDController(kP, kI, kD, 1, target);
        double lastTime = -1;
        while (!shouldStop()) {
            if (Thread.currentThread().isInterrupted()) {
                throw new InterruptedException();
            }
            double output = pidController.getPIDOutput(WatchdogManager.getInstance().getValue("rotation", Double.class));
            if (Math.abs(WatchdogManager.getInstance().getValue("rotation", Double.class) - target) < 2) {
                if (lastTime < 0) lastTime = time.milliseconds();
                else if (time.milliseconds() - lastTime > 100) {
                    stop();
                    break;
                }
            } else {
                lastTime = -1;
            }
            lf.setPower(-1 * output);
            lb.setPower(-1 * output);
            rf.setPower(1 * output);
            rb.setPower(1 * output);
            Thread.sleep(5);
        }
    }



    public void circleAround(double r, double speed, double stoppingAngle) throws InterruptedException {
        double botWidth = 17.75; //inches
        double c = botWidth/2; //in
        double wheelWidth = 3; //in
        double wheelRadius = wheelWidth/2; //in
        double targetRadius = r; //inches
        double targetSpeed =speed; //inches/sec
        double encoderSpeed = (targetSpeed/(2*Math.PI*wheelRadius)) * ticksPerRev; //encoders per sec

        double a = (targetRadius+c)/(targetRadius-c);
        double v1 = (2*a*encoderSpeed)/(a+1);
        double v2 = (2*encoderSpeed)/(a+1);
        int sgn = MathUtils.sgn(stoppingAngle - WatchdogManager.getInstance().getValue("rotation", Double.class));
        ((DcMotorEx)lf).setVelocity(v1);
        ((DcMotorEx)lb).setVelocity(v1);
        ((DcMotorEx)rf).setVelocity(v2);
        ((DcMotorEx)rb).setVelocity(v2);
        while (!shouldStop() && !Thread.currentThread().isInterrupted() && sgn * (stoppingAngle - WatchdogManager.getInstance().getValue("rotation", Double.class)) > 0) {
            Thread.sleep(10);
        }
    }

    public void rotateDegrees(double power, double degrees) throws InterruptedException {
        rotate(power, rotateToEncoder(MathUtils.sgn(degrees) * Math.toRadians(Math.abs(degrees))));
    }
    public void moveUntilPressed(DcMotor[] motors, DigitalChannel limitSwitch, double power) throws InterruptedException {
        for (int x = 0; x < motors.length; x++) {
            motors[x].setPower(power);
            motors[x].setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        }
        while (!shouldStop()) {
            if (Thread.currentThread().isInterrupted()) {
                throw new InterruptedException();
            }
            if (!limitSwitch.getState()) {//Yes, it is inverted.
                break;
            }
            try {
                Thread.sleep(10);
            } catch (InterruptedException ex) {
            }
        }
        for (DcMotor motor : motors) {
            motor.setPower(0);
            motor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        }
    }
    public void moveToPosition(DcMotor[] motors, double[] power, int[] position) throws InterruptedException {
        for (int x = 0; x < motors.length; x++) {
            motors[x].setPower(power[x]);
            motors[x].setTargetPosition(position[x]);
            motors[x].setMode(DcMotor.RunMode.RUN_TO_POSITION);
        }
        while (!shouldStop()) {
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
            while (!shouldStop() && et.milliseconds() < 100) {
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

    public void stop() {
        lf.setPower(0);
        lb.setPower(0);
        rf.setPower(0);
        rb.setPower(0);
    }

    public static int rotateToEncoder(double rad) {
        double dist = botRadius*rad;
        return distToEncoder(dist);
    }
    public static boolean shouldStop() {
        Activity currActivity = AppUtil.getInstance().getActivity();
        OpModeManagerImpl manager = OpModeManagerImpl.getOpModeManagerOfActivity(currActivity);
        OpMode currentOpMode = manager.getActiveOpMode();
        return currentOpMode instanceof LinearOpMode &&
                ((LinearOpMode) currentOpMode).isStarted() &&
                ((LinearOpMode) currentOpMode).isStopRequested();
    }
}
