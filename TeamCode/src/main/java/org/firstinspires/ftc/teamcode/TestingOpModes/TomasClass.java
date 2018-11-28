package org.firstinspires.ftc.teamcode.TestingOpModes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;


@TeleOp(name = "TomasClass", group = "testing")
public class TomasClass extends OpMode {
    private DcMotor horizR;

    @Override
    public void init() {
        horizR = hardwareMap.dcMotor.get("horizR");
    }

    @Override
    public void loop() {
        horizR.setPower(gamepad1.left_stick_y * 0.5);
    }
}
