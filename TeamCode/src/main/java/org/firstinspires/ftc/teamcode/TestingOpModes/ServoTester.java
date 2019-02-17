package org.firstinspires.ftc.teamcode.TestingOpModes;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;


@TeleOp(name="Servo Tester")
public class ServoTester extends OpMode {
    Servo marker;
    double val = 0;
    ElapsedTime time;

    @Override
    public void init() {
        marker = hardwareMap.servo.get("marker");
        time = new ElapsedTime();
    }

    @Override
    public void loop() {
        marker.setPosition(val);
        if (time.milliseconds() > 333) {
            if (gamepad1.dpad_down) {
                val -= 0.1;
            } else if (gamepad1.dpad_up) {
                val += 0.1;
            }
            time.reset();

        }
        marker.setPosition(val);
        telemetry.addData("Val", val);
    }
}
