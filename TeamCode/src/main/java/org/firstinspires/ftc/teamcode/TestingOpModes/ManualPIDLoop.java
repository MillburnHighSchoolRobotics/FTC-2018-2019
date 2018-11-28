package org.firstinspires.ftc.teamcode.TestingOpModes;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.watchdog.IMUWatchdog;
import org.firstinspires.ftc.teamcode.watchdog.PIDValueWatchdog;
import org.firstinspires.ftc.teamcode.watchdog.WatchdogManager;

import virtualRobot.PIDController;

import static com.qualcomm.robotcore.hardware.DcMotorSimple.Direction.REVERSE;

@TeleOp(name = "Manual PID Tuning", group = "testing")
public class ManualPIDLoop extends LinearOpMode {
    @Override
    public void runOpMode() throws InterruptedException {
        WatchdogManager wdm = WatchdogManager.getInstance();
        wdm.setHardwareMap(hardwareMap);
//        wdm.provision("IMUWatch", IMUWatchdog.class, "imu");
        wdm.provision("PIDValues", PIDValueWatchdog.class);

        DcMotor lf = hardwareMap.dcMotor.get("leftFront");
        DcMotor lb = hardwareMap.dcMotor.get("leftBack");
        DcMotor rf = hardwareMap.dcMotor.get("rightFront");
        DcMotor rb = hardwareMap.dcMotor.get("rightBack");

        lf.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        lb.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rf.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rb.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rf.setDirection(REVERSE);
        rb.setDirection(REVERSE);

        waitForStart();
        PIDController pid = new PIDController();
        pid.setThreshold(5);
        while (!Thread.currentThread().isInterrupted() && !gamepad1.a) {
            Double rotation = wdm.getValue("rotation", Double.class);
            rotation = rotation == null ? 0 : rotation;
            Double p = wdm.getValue("p", Double.class);
            p = p == null ? 0 : rotation;
            Double i = wdm.getValue("i", Double.class);
            i = i == null ? 0 : rotation;
            Double d = wdm.getValue("d", Double.class);
            d = d == null ? 0 : rotation;
            Double target = wdm.getValue("target", Double.class);
            target = target == null ? 0 : rotation;
            pid.setKP(p);
            pid.setKI(i);
            pid.setKD(d);
            pid.setTarget(target);
            double power = pid.getPIDOutput(rotation);
            telemetry.addData("PID Output", power);

            lf.setPower(power * -1);
            lb.setPower(power * -1);
            rf.setPower(power * 1);
            rb.setPower(power * 1);
            telemetry.update();

            Thread.sleep(5);
        }
        wdm.clean();
    }
}
