package org.firstinspires.ftc.teamcode.wosbot;

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

public class WosBotMovement {
    public static final int[][] POWER_MATRIX = { //for each of the directions
            {1, 1, 1, 1},
            {1, 0, 0, 1},
            {1, -1, -1, 1},
            {0, -1, -1, 0},
            {-1, -1, -1, -1},
            {-1, 0, 0, -1},
            {-1, 1, 1, -1},
            {0, 1, 1, 0}
    };
    public final double POS_POWER_CONST = 0.7;
    public final double NEG_POWER_CONST = -0.7;
    final double kP = 0.0125; //0.0225
    final double kI = 0; //0.0035
    final double kD = 0.005; //0.0175
    DcMotorEx lf;
    DcMotorEx lb;
    DcMotorEx rf;
    DcMotorEx rb;
    public static double x_coordinate;
    public static double y_coordinate;
    public final static float BOT_WIDTH = 15f; //inches
    final static float botRadius = BOT_WIDTH /2; //in
    final static float wheelWidth = 4; //in
    final static float wheelRadius = wheelWidth/2; //in
    final static int ticksPerRev = 730; //ticks per rev
    public final static String vuforiaKey = "AcSW/tj/////AAABmUB3byzZQksfqhymb0Tr3M92yvtrzF4HgDl0t7Z07OZ2xscXR1yyeX4GxftrShvm9T926ZCW0VglQKXuQE5+JkrABVijohk5DCkcE9CcxHy3mTs2Ui76Nz+9CQTgOsr6/AMLV+Te6uyXTs3rZwGdnHGRo0Q1yboJCQ51Ap2rgJc//ehVdkp/QetIMnfhRffac0edAHFt0i2F5++S/OH/4kdxFd5ha0lswd4nTnqU2MiJrz+OH4WQPQ8JC94dQZ6F3m/iX5mk4TCq/9xg3cTJvFccEUawf7PIsapABxKMJB6hcPikwa0XtyGB+vEb7fQAXZ80tRal2mcwKSHrDM4ZvYisD73X+sTIAqQnXgxYiL14";

    public WosBotMovement(DcMotor lf, DcMotor lb, DcMotor rf, DcMotor rb) {
        this.lf = (DcMotorEx)lf;
        this.lb = (DcMotorEx)lb;
        this.rf = (DcMotorEx)rf;
        this.rb = (DcMotorEx)rb;
    }
    public WosBotMovement() {}

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

    public void moveTo(double x_new, double y_new, double power) throws InterruptedException {
        double distance = Math.sqrt(Math.pow((x_coordinate-x_new),2)+Math.pow((y_coordinate-y_new),2));
        double angle = Math.atan2((y_new-y_coordinate), (x_new-x_coordinate));
        translate(angle, distance, power);
    }

    public void translate(double angle, double distance, double power) throws InterruptedException {
        double current = WatchdogManager.getInstance().getValue("rotation", Double.class);
        double theta = angle-current;
        if (theta < 0) theta += 360;
        double scale;
        double RF = 0, RB = 0, LF = 0, LB = 0;
        if (theta >= 0 && theta <= 90) { //quadrant 1
            scale = MathUtils.sinDegrees(theta - 45) / MathUtils.cosDegrees(theta - 45);
            LF = power * POWER_MATRIX[0][0];
            LB = power * POWER_MATRIX[0][1] * scale;
            RF = power * POWER_MATRIX[0][2] * scale;
            RB = power * POWER_MATRIX[0][3];
        } else if (theta > 90 && theta <= 180) { //quadrant 2
            power *= -1;
            scale = MathUtils.sinDegrees(theta - 135) / MathUtils.cosDegrees(theta - 135);
            LF = (power * POWER_MATRIX[2][0] * scale);
            LB = (power * POWER_MATRIX[2][1]);
            RF = (power * POWER_MATRIX[2][2]);
            RB = (power * POWER_MATRIX[2][3] * scale);
        } else if (theta > 180 && theta <= 270) { //quadrant 3
            scale = MathUtils.sinDegrees(theta - 225) / MathUtils.cosDegrees(theta - 225);
            LF = (power * POWER_MATRIX[4][0]);
            LB = (power * POWER_MATRIX[4][1] * scale);
            RF = (power * POWER_MATRIX[4][2] * scale);
            RB = (power * POWER_MATRIX[4][3]);
        } else if (theta > 270 && theta < 360) { //quadrant 4
            power *= -1;
            scale = MathUtils.sinDegrees(theta - 315) / MathUtils.cosDegrees(theta - 315);
            LF = (power * POWER_MATRIX[6][0] * scale);
            LB = (power * POWER_MATRIX[6][1]);
            RF = (power * POWER_MATRIX[6][2]);
            RB = (power * POWER_MATRIX[6][3] * scale);
        }
        int encoder = distToEncoder(distance);
        lf.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        lb.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rf.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rb.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        moveToPosition(new DcMotor[] {lf,lb,rf,rb}, new double[] {-LF,-LB,RF,RB}, new int[] {(encoder),(encoder),(encoder),(encoder)});
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

    public void moveUntilPressed(DcMotor[] motors, DigitalChannel limitSwitch, double power) throws InterruptedException {
        for (int x = 0; x < motors.length; x++) {
            motors[x].setPower(power);
            motors[x].setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        }
        while (!shouldStop()) {
            if (Thread.currentThread().isInterrupted()) {
                throw new InterruptedException();
            }
            if (!limitSwitch.getState()) {
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
    public void stop() {
        lf.setPower(0);
        lb.setPower(0);
        rf.setPower(0);
        rb.setPower(0);
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
