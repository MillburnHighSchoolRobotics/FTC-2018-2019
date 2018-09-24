package org.firstinspires.ftc.teamcode.TestingOpModes;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

/**
 * Created by Yanjun on 1/16/2016.
 */
@Disabled
@Autonomous(name = "Testing: WheelTest", group = "Testing")
public class WheelTest extends LinearOpMode {
    @Override
    public void runOpMode() throws InterruptedException {
        DcMotor a, b, c, d;
        a = hardwareMap.dcMotor.get("RF");
        b = hardwareMap.dcMotor.get("RB");
        c = hardwareMap.dcMotor.get("LF");
        d = hardwareMap.dcMotor.get("LB");

        a.setDirection(DcMotor.Direction.REVERSE);
        b.setDirection(DcMotor.Direction.REVERSE);

        waitForStart();

        a.setPower(1);
        b.setPower(1);
        c.setPower(1);
        d.setPower(1);

        while (true) {
            waitOneFullHardwareCycle();
        }
    }
}