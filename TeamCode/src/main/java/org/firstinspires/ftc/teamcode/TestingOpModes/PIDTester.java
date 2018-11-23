package org.firstinspires.ftc.teamcode.TestingOpModes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import org.firstinspires.ftc.teamcode.IMUWatchdog;
import org.firstinspires.ftc.teamcode.Movement;
import org.firstinspires.ftc.teamcode.WatchdogManager;

import static com.qualcomm.robotcore.hardware.DcMotorSimple.Direction.REVERSE;

@TeleOp(name = "PIDTester", group = "testing")
public class PIDTester extends OpMode {
    private DcMotor lf;
    private DcMotor rf;
    private DcMotor lb;
    private DcMotor rb;
    private Movement mv;
    @Override
    public void init() {
        lf = hardwareMap.dcMotor.get("leftFront");
        lb = hardwareMap.dcMotor.get("leftBack");
        rf = hardwareMap.dcMotor.get("rightFront");
        rb = hardwareMap.dcMotor.get("rightBack");
        lf.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        lb.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rf.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rb.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rf.setDirection(REVERSE);
        rb.setDirection(REVERSE);
        WatchdogManager.getInstance().setHardwareMap(hardwareMap);
        WatchdogManager.getInstance().provision("IMUWatch", IMUWatchdog.class, "imu");
        mv = new Movement(lf, lb, rf, rb);

    }

    @Override
    public void loop() {
        try {
            mv.rotateTo(90);
            Thread.sleep(1000);
            mv.rotateTo(-90);
            Thread.sleep(1000);

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
