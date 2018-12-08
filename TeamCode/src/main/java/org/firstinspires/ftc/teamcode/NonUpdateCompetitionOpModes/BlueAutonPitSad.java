package org.firstinspires.ftc.teamcode.NonUpdateCompetitionOpModes;

import android.util.Log;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.teamcode.JeffBot;
import org.firstinspires.ftc.teamcode.SahilClass;
import org.firstinspires.ftc.teamcode.watchdog.IMUWatchdog;
import org.firstinspires.ftc.teamcode.watchdog.WatchdogManager;
import org.opencv.android.OpenCVLoader;
import virtualRobot.VuforiaLocalizerImplSubclass;

@Autonomous(name = "Blue Auton Pit Sad", group = "competition")
public class BlueAutonPitSad extends LinearOpMode {
    DcMotor lf;
    DcMotor lb;
    DcMotor rf;
    DcMotor rb;
    Servo marker;
    Servo stopper;
    DigitalChannel magneticLimitSwitch;

    DcMotor liftR;
    DcMotor liftL;


    static {
        if(OpenCVLoader.initDebug()) {
            Log.d("opencv","yay it works");
        } else {
            Log.d("opencv","nope it doesnt work");
        }
    }

    @Override
    public void runOpMode() throws InterruptedException {

        lf = hardwareMap.dcMotor.get("leftFront");
        lb = hardwareMap.dcMotor.get("leftBack");
        rf = hardwareMap.dcMotor.get("rightFront");
        rb = hardwareMap.dcMotor.get("rightBack");
        liftL = hardwareMap.dcMotor.get("liftLeft");
        liftR = hardwareMap.dcMotor.get("liftRight");
        magneticLimitSwitch = hardwareMap.get(DigitalChannel.class, "Switchy");
        marker = hardwareMap.servo.get("marker");
        stopper = hardwareMap.servo.get("stopper");
        WatchdogManager wdm = WatchdogManager.getInstance();
        wdm.setHardwareMap(hardwareMap);
        wdm.setCurrentAuton(this);
        wdm.provision("IMUWatch", IMUWatchdog.class, "imu 1");
        marker.setPosition(0.5);
        VuforiaLocalizer.Parameters params = new VuforiaLocalizer.Parameters(hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName()));
        params.cameraDirection = VuforiaLocalizer.CameraDirection.BACK;

        params.vuforiaLicenseKey = JeffBot.vuforiaKey;

        VuforiaLocalizerImplSubclass vuforiaInstance = new VuforiaLocalizerImplSubclass(params);
        waitForStart();
        rf.setDirection(DcMotorSimple.Direction.REVERSE);
        rb.setDirection(DcMotorSimple.Direction.REVERSE);
        lf.setDirection(DcMotorSimple.Direction.FORWARD);
        lb.setDirection(DcMotorSimple.Direction.FORWARD);
        initializeMotor(new DcMotor[]{lf, lb, rf, rb});

        liftR.setDirection(DcMotorSimple.Direction.REVERSE);
        JeffBot mv = new JeffBot(lf, lb, rf, rb);
        liftL.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        liftR.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        liftL.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        liftR.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        liftL.setPower(-0.5);
        liftR.setPower(-0.5);
        Thread.sleep(500);
        stopper.setPosition(1);
        Thread.sleep(1000);
        liftL.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        liftR.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        mv.moveUntilPressed(new DcMotor[]{liftL, liftR}, magneticLimitSwitch, mv.POS_POWER_CONST);//Move until limit switch pressed
        liftL.setPower(0.6);
        liftR.setPower(0.6);
        ElapsedTime extraLiftTimer = new ElapsedTime();
        while (extraLiftTimer.milliseconds() < 250) {
            Thread.sleep(5);
        }
        liftL.setPower(0);
        liftR.setPower(0);

        SahilClass sahilClass = new SahilClass(vuforiaInstance, 1000);
        int num = sahilClass.getThreeMineralPosition();
        telemetry.addData("Position", num + "");
        telemetry.update();
        mv.translateDistance(0.7,-12);

        switch (num) {
            case 2:
                mv.rotateTo(-50);
                mv.translateDistance(0.7,-37);
                break;
            case 0:
                mv.rotateTo(50);
                mv.translateDistance(0.7,-37);
                break;
            default:
            case 1:
                mv.translateDistance(0.7,-32);
                break;
        }
        mv.rotateTo(0);
        mv.translateDistance(0.7, -6);
        wdm.clean();
    }
    public void initializeMotor(DcMotor[] motors) {
        for (DcMotor motor : motors) {
            motor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            motor.setPower(0);
        }
    }
}
