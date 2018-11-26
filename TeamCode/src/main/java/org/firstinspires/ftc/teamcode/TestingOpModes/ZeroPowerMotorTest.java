package org.firstinspires.ftc.teamcode.TestingOpModes;


import android.util.Log;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;

@Autonomous(name = "Zero Power Motor Test", group = "Testing")
public class ZeroPowerMotorTest extends OpMode {
    @Override
    public void init() {
        DcMotorEx motor = (DcMotorEx) hardwareMap.dcMotor.get("motor1");
        motor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
    }

    @Override
    public void loop() {
    }
}
