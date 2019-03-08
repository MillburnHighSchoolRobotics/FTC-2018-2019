package org.firstinspires.ftc.teamcode.TestingOpModes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.util.ElapsedTime;

import static com.qualcomm.robotcore.hardware.DcMotorSimple.Direction.REVERSE;

@TeleOp(name = "Test Woz Reaper", group = "Tests")
public class WosbotReaper extends OpMode {
    private ElapsedTime canChangeReaperSpeed;
    DcMotorEx reaperExtension;
    int speed;
    @Override
    public void init() {
        reaperExtension = (DcMotorEx) hardwareMap.dcMotor.get("reaperExtension");
        reaperExtension.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        canChangeReaperSpeed = new ElapsedTime();
        speed = 60;
    }

    @Override
    public void loop() {
        if (gamepad1.dpad_down) {
            reaperExtension.setPower(-speed/100.0);
        } else if (gamepad1.dpad_up) {
            reaperExtension.setPower(speed/100.0);
        } else {
            reaperExtension.setPower(0);
        }
        if (canChangeReaperSpeed.milliseconds() > 250) {
            if (gamepad1.dpad_right) {
                if (speed <= 90) {
                    speed += 10;
                }
            } else if (gamepad1.dpad_left) {
                if (speed >= 10) {
                    speed -= 10;
                }
            }
            canChangeReaperSpeed.reset();
        }
        telemetry.addData("Reaper Extension", reaperExtension.getCurrentPosition() + "");
        telemetry.addData("Speed", speed + "%");
        telemetry.update();
    }
}
