package org.firstinspires.ftc.teamcode.TestingOpModes;

import android.util.Log;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

/**
 * Created by Mehmet on 10/25/2017.
 */
@Disabled
@TeleOp(name="DriveTrainTest", group="Testing")
public class DriveTrainTest extends OpMode {
    DcMotor leftFront,rightFront,leftBack,rightBack;
    float threshold = 0.1f;

    @Override
    public void init() {
        leftFront = hardwareMap.dcMotor.get("leftFront");
        rightFront = hardwareMap.dcMotor.get("rightFront");
        leftBack = hardwareMap.dcMotor.get("leftBack");
        rightBack = hardwareMap.dcMotor.get("rightBack");
    }

    @Override
    public void loop() {

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
    }

    @Override
    public void stop() {
        Log.d("stop()", "called");
    }
}

