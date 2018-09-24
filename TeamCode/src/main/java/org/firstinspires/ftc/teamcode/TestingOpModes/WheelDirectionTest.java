package org.firstinspires.ftc.teamcode.TestingOpModes;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

/**
 * Created by david on 9/29/17.
 */
@Disabled
@Autonomous(name = "Testing: Wheel Direction", group = "Testing")
public class WheelDirectionTest extends OpMode {
    private DcMotor LF, RF, LB, RB;
    public void init() {
        LF = hardwareMap.dcMotor.get("LF");
        LB = hardwareMap.dcMotor.get("LB");
        RF = hardwareMap.dcMotor.get("RF");
        RB = hardwareMap.dcMotor.get("RB");
    }
    public void loop() {
        //This is crappy but it works
        DcMotor curr = null;
        if (gamepad1.dpad_up) curr = LF;
        if (gamepad1.dpad_right) curr = RF;
        if (gamepad1.dpad_down) curr = RB;
        if (gamepad1.dpad_left) curr = LB;
        int power = 0;
        if (gamepad1.a) power++;
        if (gamepad1.b) power--;
        if (curr != null) curr.setPower(power);
        else {
            LF.setPower(0);
            RF.setPower(0);
            RB.setPower(0);
            LB.setPower(0);
        }
        try {
            Thread.sleep(500);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
