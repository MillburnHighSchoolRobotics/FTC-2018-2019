package org.firstinspires.ftc.teamcode.TestingOpModes;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;


@TeleOp(name = "TomasClass", group = "testing")
@Disabled
public class TomasClass extends OpMode {
    private DcMotor horizR;
    private DcMotor horizL;

    @Override
    public void init() {
        horizR = hardwareMap.dcMotor.get("horizR");
        horizL = hardwareMap.dcMotor.get("horizL");
//        horizL.setDirection(DcMotorSimple.Direction.REVERSE);
        horizR.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        horizL.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }

    @Override
    public void loop() {
        horizR.setPower(gamepad1.left_stick_y * 0.5);
        horizL.setPower(gamepad1.left_stick_y * 0.5);
        telemetry.addData("Left", horizL.getCurrentPosition());
        telemetry.addData("Right", horizR.getCurrentPosition());
    }
}
