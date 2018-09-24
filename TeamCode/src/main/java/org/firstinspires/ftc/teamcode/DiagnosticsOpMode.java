package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

import static java.lang.Math.*;

/**
 * Created by Yanjun on 11/24/2015.
 *
 * connect misc servo controller and reaper motor controller to
 * core device interface module in order to use this opmode
 *
 * joysticks control motor speed: all the way up for full speed forward,
 * all the way down for full speed backward (left for motor 1, right for motor 2)
 *
 * abxy, dpad, trigger + bumpers for servos (see implementation below for specific mappings)
 *
 * load "diagnostics.xml" onto robot controller before use.
 *
 * Juniors coders, see Shantanu if you are reading this and don't know what i'm talking about
 *
 * also remember: college isn't for
 * everyone.
 */
public class DiagnosticsOpMode extends OpMode {

    private DcMotor motor1, motor2;
    private Servo servo1, servo2, servo3, servo4, servo5, servo6;

    private static final double SERVO_DELTA = 0.01;

    private double servo1Val, servo2Val, servo3Val, servo4Val, servo5Val, servo6Val;

    @Override
    public void init() {
        motor1 = hardwareMap.dcMotor.get("motor1");
        motor2 = hardwareMap.dcMotor.get("motor2");

        servo1 = hardwareMap.servo.get("servo1");
        servo2 = hardwareMap.servo.get("servo2");
        servo3 = hardwareMap.servo.get("servo3");
        servo4 = hardwareMap.servo.get("servo4");
        servo5 = hardwareMap.servo.get("servo5");
        servo6 = hardwareMap.servo.get("servo6");

        servo1Val = servo2Val = servo3Val = servo4Val = servo5Val = servo6Val = 0;
    }

    @Override
    public void loop() {
        motor1.setPower(-gamepad1.left_stick_y);
        motor2.setPower(-gamepad1.right_stick_y);

        if (gamepad1.y) {
            servo1Val += SERVO_DELTA;
        }

        if (gamepad1.x) {
            servo1Val -= SERVO_DELTA;
        }

        if (gamepad1.b) {
            servo2Val += SERVO_DELTA;
        }

        if (gamepad1.a) {
            servo2Val -= SERVO_DELTA;
        }

        if (gamepad1.dpad_up) {
            servo3Val += SERVO_DELTA;
        }

        if (gamepad1.dpad_down) {
            servo3Val -= SERVO_DELTA;
        }

        if (gamepad1.dpad_right) {
            servo4Val += SERVO_DELTA;
        }

        if (gamepad1.dpad_left) {
            servo4Val -= SERVO_DELTA;
        }

        if (gamepad1.left_bumper) {
            servo5Val += SERVO_DELTA;
        }

        if (gamepad1.left_trigger > 0.7) {
            servo5Val -= SERVO_DELTA;
        }

        if (gamepad1.right_bumper) {
            servo6Val += SERVO_DELTA;
        }

        if (gamepad1.right_trigger > 0.7) {
            servo6Val -= SERVO_DELTA;
        }

        servo1Val = (min(max(servo1Val, 0), 1));
        servo2Val = (min(max(servo2Val, 0), 1));
        servo3Val = (min(max(servo3Val, 0), 1));
        servo4Val = (min(max(servo4Val, 0), 1));
        servo5Val = (min(max(servo5Val, 0), 1));
        servo6Val = (min(max(servo6Val, 0), 1));

        servo1.setPosition(servo1Val);
        servo2.setPosition(servo2Val);
        servo3.setPosition(servo3Val);
        servo4.setPosition(servo4Val);
        servo5.setPosition(servo5Val);
        servo6.setPosition(servo6Val);
    }
}
