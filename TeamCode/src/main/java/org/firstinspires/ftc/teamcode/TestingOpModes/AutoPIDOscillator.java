package org.firstinspires.ftc.teamcode.TestingOpModes;

import android.util.Log;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.IMUWatchdog;
import org.firstinspires.ftc.teamcode.Movement;
import org.firstinspires.ftc.teamcode.WatchdogManager;

import java.util.ArrayList;

@Autonomous(name = "AutoPID", group="testing")
@Disabled
public class AutoPIDOscillator extends LinearOpMode {
    private final double power = 1;
    private final int iterations = 10;
    private final double setpoint = 90;
    private final String TAG = "AutoPID";

    private final DcMotor lf = hardwareMap.dcMotor.get("leftFront");
    private final DcMotor lb = hardwareMap.dcMotor.get("leftBack");
    private final DcMotor rf = hardwareMap.dcMotor.get("rightFront");
    private final DcMotor rb = hardwareMap.dcMotor.get("rightBack");
    @Override
    public void runOpMode() throws InterruptedException {
        ArrayList<Double> periods = new ArrayList<>();
        ArrayList<Double> extremes = new ArrayList<>();
        WatchdogManager wdm = WatchdogManager.getInstance();
        BNO055IMU imu = (BNO055IMU)hardwareMap.get("imu");
        wdm.provision("IMUWatch", IMUWatchdog.class, imu);
        Movement mv = new Movement(lf, lb, rf, rb);
        waitForStart();
        ElapsedTime time = new ElapsedTime();
        lf.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        lb.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rf.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rb.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        lf.setPower(power * -1);
        lb.setPower(power * -1);
        rf.setPower(power * 1);
        rb.setPower(power * 1);
        while(!Thread.currentThread().isInterrupted() && (double)wdm.getValue("rotation") < setpoint) {
            Thread.sleep(1);
        }
        boolean direction = true; //true means positive, false means negative
        for (int i = 0; i < iterations; i++) {
            lf.setPower(power * 1);
            lb.setPower(power * 1);
            rf.setPower(power * -1);
            rb.setPower(power * -1);
            while (!Thread.currentThread().isInterrupted()) {
                if (direction) {
                    synchronized(imu) {
                        double angularVelocity = imu.getAngularVelocity().zRotationRate;
                        if (angularVelocity <= 0) {
                            direction = false;
                            extremes.add((double)wdm.getValue("rotation"));
                        }
                    }
                } else {
                    if ((double)wdm.getValue("rotation") <= setpoint) {
                        break;
                    }
                }
                Thread.sleep(1);
            }
            lf.setPower(power * -1);
            lb.setPower(power * -1);
            rf.setPower(power * 1);
            rb.setPower(power * 1);
            while (!Thread.currentThread().isInterrupted()) {
                if (!direction) {
                    synchronized(imu) {
                        double angularVelocity = imu.getAngularVelocity().zRotationRate;
                        if (angularVelocity >= 0) {
                            direction = true;
                            if (i != 0) periods.add(time.seconds());
                            extremes.add((double)wdm.getValue("rotation"));
                            time.reset();
                        }
                    }
                } else {
                    if ((double)wdm.getValue("rotation") >= setpoint) {
                        break;
                    }
                }
                Thread.sleep(1);
            }
        }
        lf.setPower(0);
        lb.setPower(0);
        rf.setPower(0);
        rb.setPower(0);

        Log.d(TAG, "Periods: " + periods);
        Log.d(TAG, "Extremes: " + extremes);

        double avg = 0;
        for (double period : periods) {
            avg += period;
        }
        avg /= periods.size();
        Log.d(TAG, "Tu: " + avg);

        double max = 0;
        int n = 0;
        for (int i = 0; i < extremes.size(); i += 2) {
            max += extremes.get(i);
            n++;
        }
        max /= n;
        Log.d(TAG, "Max: " + max);

        double min = 0;
        n = 0;
        for (int i = 1; i < extremes.size(); i += 2) {
            min += extremes.get(i);
            n++;
        }
        min /= n;
        Log.d(TAG, "Min: " + min);

        double Ku = (4*(max - min))/(2 * Math.abs(power) * Math.PI);
        Log.d(TAG, "Ku: " + Ku);

        // Clean the Watchdogs
        WatchdogManager.getInstance().clean();
    }
}
