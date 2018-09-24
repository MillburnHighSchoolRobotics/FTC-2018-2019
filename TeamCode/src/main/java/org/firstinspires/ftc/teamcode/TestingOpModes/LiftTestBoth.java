package org.firstinspires.ftc.teamcode.TestingOpModes;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

import virtualRobot.PIDController;

/**
 * Created by shant on 2/14/2016.
 */
@Disabled
@TeleOp(name = "Test: Whole Lift", group = "Tests")
public class LiftTestBoth extends OpMode {
    DcMotor liftLeft;
    DcMotor liftRight;

    Servo clawLeft;
    Servo clawRight;
    int initLiftLeftEncoder;
    int initLiftRightEncoder;

    @Override
    public void init() {
        liftLeft = hardwareMap.dcMotor.get("liftLeft");
        liftRight = hardwareMap.dcMotor.get("liftRight");
        clawLeft = hardwareMap.servo.get("clawLeft");
        clawRight = hardwareMap.servo.get("clawRight");
        liftLeft.setDirection(DcMotor.Direction.REVERSE);
        initLiftLeftEncoder = liftLeft.getCurrentPosition();
        initLiftRightEncoder = liftRight.getCurrentPosition();
        clawLeft.setPosition(0);
        clawRight.setPosition(0);
    }

    @Override
    public void loop() {
        //PID CONTROLLER TO KEEP LIFT ARMS AT THE SAME EXTENSION
        //TODO TUNE THIS PID CONTROLLER
        double liftLeftPower;
        double liftRightPower;
        double tp = 0.4;
        PIDController liftController = new PIDController(0, 0, 0, 0);
        liftController.setTarget(0);
        double liftPIDOut = liftController.getPIDOutput((liftLeft.getCurrentPosition() - initLiftLeftEncoder) - (liftRight.getCurrentPosition() - initLiftRightEncoder));
        //liftPIDOut = 0;
        if (gamepad1.a && !(gamepad1.a && gamepad1.b)) {
            liftLeftPower = tp + liftPIDOut;
            liftRightPower = tp - liftPIDOut;

        }
        else if (gamepad1.b && !(gamepad1.a && gamepad1.b)) {
            liftLeftPower = -tp + liftPIDOut;
            liftRightPower = -tp - liftPIDOut;
        }
        else {
            liftLeftPower = 0;
            liftRightPower = 0;
        }
        if (gamepad1.dpad_up) {
            clawLeft.setPosition(clawLeft.getPosition()+.01);

        }
        if (gamepad1.dpad_down) {
            clawLeft.setPosition(clawLeft.getPosition()-.01);

        } if (gamepad1.dpad_left) {
            clawLeft.setPosition(clawRight.getPosition()+.01);

        }
        if (gamepad1.dpad_right) {
            clawLeft.setPosition(clawRight.getPosition()-.01);

        }
        
        liftLeftPower = Math.max(Math.min(liftLeftPower, 1), -1);
        liftRightPower = Math.max(Math.min(liftRightPower, 1), -1);


        liftLeft.setPower(liftLeftPower);
        liftRight.setPower(liftRightPower);

        telemetry.addData("pid value", liftPIDOut);
        telemetry.addData("left encoder", liftLeft.getCurrentPosition() - initLiftLeftEncoder);
        telemetry.addData("right encoder", liftRight.getCurrentPosition() - initLiftRightEncoder);
        telemetry.addData("left power", liftLeftPower);
    }
}
