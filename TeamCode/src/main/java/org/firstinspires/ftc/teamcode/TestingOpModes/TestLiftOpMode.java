package org.firstinspires.ftc.teamcode.TestingOpModes;

import android.util.Log;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.Servo;
import virtualRobot.utils.MathUtils;

/**
 * Created by ethan on 10/5/17.
 */

@Disabled
@TeleOp(name="MainTeleop", group="LALALA")
public class TestLiftOpMode extends OpMode {
    DcMotor motor1;
    DcMotor motor2;
    DcMotor leftFront,rightFront,leftBack,rightBack;

    Servo servo1;
    Servo servo2;
    float threshold = 0.1f ;
    int motor1offset, motor2offset;

    @Override
    public void init() {
        motor1 = hardwareMap.dcMotor.get("glyphLiftLeft");
        motor2 = hardwareMap.dcMotor.get("glyphLiftRight");
        servo1 = hardwareMap.servo.get("clawLeft");
        servo2 = hardwareMap.servo.get("clawRight");
        leftFront = hardwareMap.dcMotor.get("leftFront");
        rightFront = hardwareMap.dcMotor.get("rightFront");
        leftBack = hardwareMap.dcMotor.get("leftBack");
        rightBack = hardwareMap.dcMotor.get("rightBack");
        motor1.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        motor2.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        motor1offset = 0;
        motor2offset = 0;
    }

    @Override
    public void loop() {
        telemetry.addData("Lift Encoder values (L,R)", (motor1offset - motor1.getCurrentPosition()) + "," + (motor2offset - motor2.getCurrentPosition()));
        if (gamepad2.y)
            motor1.setPower(1);
        else if (gamepad2.x)
            motor1.setPower(-1);
        else
            motor1.setPower(0);

        if (gamepad2.b)
            motor2.setPower(1);
        else if (gamepad2.a)
            motor2.setPower(-1);
        else
            motor2.setPower(0);

        if (gamepad2.right_bumper) {
            servo1.setPosition(0.1);
            servo2.setPosition(0.9);
        }

        if (gamepad2.left_bumper) {
            servo1.setPosition(0.3);
            servo2.setPosition(0.7);
        }

        if (gamepad2.dpad_down) {
            motor1.setPower(-1);
            motor2.setPower(-1);
        } else if (gamepad2.dpad_up) {
            motor1.setPower(1);
            motor2.setPower(1);
        } else {
            motor1.setPower(0);
            motor2.setPower(0);
        }

        if(Math.abs(gamepad1.left_stick_y) > threshold || Math.abs(gamepad1.left_stick_x) > threshold)
        {
            float jx = gamepad1.left_stick_x;
            float jy = gamepad1.left_stick_y;
            rightFront.setPower(jx);
            rightBack.setPower(jx);

            leftBack.setPower(jx);
            leftFront.setPower(jx);
            if(jx<0.05){
                rightFront.setPower(jy);

                rightBack.setPower(jy/1.5);

                leftBack.setPower(-jy);
                leftFront.setPower(-jy);
            }


        }else{
          rightFront.setPower(0);
            leftFront.setPower(0);
            rightBack.setPower(0);
            leftBack.setPower(0);

        }
        if(Math.abs(gamepad1.right_stick_x) > threshold)
        {
            //rotate
            float jx = gamepad1.right_stick_x;
            float jy = gamepad1.right_stick_y;
            rightFront.setPower(-jx);
            leftFront.setPower(-jx);
            rightBack.setPower(jx);
            leftBack.setPower(jx);



        }else{
            rightFront.setPower(0);
            leftFront.setPower(0);
            rightBack.setPower(0);
            leftBack.setPower(0);

        }
        if (gamepad2.dpad_left || gamepad2.dpad_right) {
            motor1offset = motor1.getCurrentPosition();
            motor2offset = motor2.getCurrentPosition();
        }
    }

    @Override
    public void stop() {
        Log.d("stop()", "called");
    }
}
