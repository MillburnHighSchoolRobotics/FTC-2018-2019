package org.firstinspires.ftc.teamcode.TestingOpModes;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import org.firstinspires.ftc.teamcode.JeffBot;
import org.firstinspires.ftc.teamcode.watchdog.IMUWatchdog;
import org.firstinspires.ftc.teamcode.watchdog.WatchdogManager;

@TeleOp(name = "Circle 3 Times")
public class Circle3Times extends LinearOpMode {
    @Override
    public void runOpMode() throws InterruptedException {
        DcMotor lf = hardwareMap.dcMotor.get("leftFront");
        DcMotor lb = hardwareMap.dcMotor.get("leftBack");
        DcMotor rf = hardwareMap.dcMotor.get("rightFront");
        DcMotor rb = hardwareMap.dcMotor.get("rightBack");
        WatchdogManager wdm = WatchdogManager.getInstance();
        wdm.setHardwareMap(hardwareMap);
        wdm.setCurrentAuton(this);
        wdm.provision("IMUWatch", IMUWatchdog.class, "imu 1");
        lf.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        lb.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rf.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rb.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        lf.setDirection(DcMotorSimple.Direction.REVERSE);
        lb.setDirection(DcMotorSimple.Direction.REVERSE);
        JeffBot jeff = new JeffBot(lf, lb, rf, rb);
        jeff.circleAround(JeffBot.BOT_WIDTH/2 + 6, 8, -720);
    }
}
