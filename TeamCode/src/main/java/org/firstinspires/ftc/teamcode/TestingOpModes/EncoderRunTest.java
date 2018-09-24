package org.firstinspires.ftc.teamcode.TestingOpModes;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

/**
 * Created by 17osullivand on 2/4/17.
 */
//@Disabled
@Autonomous(name = "Sensor: Motor Run Using Encoders", group = "Sensor")
public class EncoderRunTest extends OpMode {
    DcMotor motor;
    double lastSpeed;
    double lastEncoder;
    double lastTime;

    @Override
    public void init() {
        motor = hardwareMap.dcMotor.get("leftBack");
        motor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }

    public void init_loop() {
        lastEncoder = motor.getCurrentPosition();
        lastTime = System.currentTimeMillis();
    }

    @Override
    public void start() {
        motor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motor.setTargetPosition(motor.getCurrentPosition() + 2000);
        motor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        motor.setPower(1);
    }

    @Override
    public void loop() {
        telemetry.addData("B: ", motor.isBusy());
        telemetry.addData("E: ", motor.getCurrentPosition());
    }
}
