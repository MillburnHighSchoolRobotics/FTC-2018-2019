package org.firstinspires.ftc.teamcode.TestingOpModes;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by mehme_000 on 1/12/2017.
 */
@Disabled
public class TestIndividualMotor extends OpMode {
        private DcMotor motor1;
        private double initMotorEncoder;
        private double motorEncoder = 0;
        private ArrayList<Map.Entry<String, DcMotor>> motors, motorsR;

        @Override
        public void init() {
            motors = new ArrayList<>(hardwareMap.dcMotor.entrySet());
            motorsR = motors;
            motor1 = motors.get(0).getValue();
            initMotorEncoder = motor1.getCurrentPosition();
        }

        @Override
        public void loop(){
            int i = 0;


            if(gamepad1.right_bumper)
            {
                i+=1;

                if(i == motors.size()){
                    i = 0;
                }

                motor1 = motors.get(i).getValue();
                initMotorEncoder = motor1.getCurrentPosition();

                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            if(gamepad1.left_bumper)
            {
                i-=1;

                if(i == -1){
                    i = motors.size()-1;
                }

                motor1 = motors.get(i).getValue();
                initMotorEncoder = motor1.getCurrentPosition();

                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            telemetry.addData("Current Motor:", motor1);

            if (gamepad1.a && gamepad1.b) {
                //Something here
            }

            else {
                if (gamepad1.a) {
                    motor1.setPower(1);
                }
                else if(gamepad1.b) {
                    motor1.setPower(-1);
                }
                else {
                    motor1.setPower(0);
                }
            }
            motorEncoder = motor1.getCurrentPosition() - initMotorEncoder;

            telemetry.addData("motor Encoder", motorEncoder);
            telemetry.update();
        }
    }

