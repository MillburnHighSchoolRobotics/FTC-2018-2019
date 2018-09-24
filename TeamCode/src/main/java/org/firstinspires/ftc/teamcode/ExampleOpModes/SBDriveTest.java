package org.firstinspires.ftc.teamcode.ExampleOpModes;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

/**
 * _____ ______   ___  ___  ________
 * |\   _ \  _   \|\  \|\  \|\   ____\             .-""-.
 * \ \  \\\__\ \  \ \  \\\  \ \  \___|_           /[] _ _\
 * \ \  \\|__| \  \ \   __  \ \_____  \         _|_o_LII|_
 * \ \  \    \ \  \ \  \ \  \|____|\  \       / | ==== | \
 * \ \__\    \ \__\ \__\ \__\____\_\  \      |_| ==== |_|
 * \|__|     \|__|\|__|\|__|\_________\      ||" ||  ||
 * \|_________|      ||LI  o ||
 * ||'----'||
 * /__|    |__\
 * <p/>
 * Created by shant on 11/24/2015.
 */
@Disabled
public class SBDriveTest extends OpMode {
    DcMotor rightFront, rightBack, leftFront, leftBack;
    Servo backShieldRight, backShieldLeft, frontShield;

    double servoDelta = 0.05;
    double currentPosBack = .5;
    double currentPosFront = .5;

    @Override
    public void init() {
        rightFront = hardwareMap.dcMotor.get("rightFront");
        rightBack = hardwareMap.dcMotor.get("rightBack");
        leftFront = hardwareMap.dcMotor.get("leftFront");
        leftBack = hardwareMap.dcMotor.get("leftBack");
        rightBack.setDirection(DcMotor.Direction.REVERSE);
        rightFront.setDirection(DcMotor.Direction.REVERSE);

        backShieldRight = hardwareMap.servo.get("backShieldRight");
        backShieldLeft = hardwareMap.servo.get("backShieldLeft");
        backShieldRight.setDirection(Servo.Direction.REVERSE);
        frontShield = hardwareMap.servo.get("frontShield");

        backShieldRight.setPosition(currentPosBack);
        backShieldLeft.setPosition(currentPosBack);
        frontShield.setPosition(currentPosFront);

    }

    @Override
    public void loop() {
        double leftPower = -gamepad1.left_stick_y;
        double rightPower = -gamepad1.right_stick_y;
        rightFront.setPower(rightPower);
        rightBack.setPower(rightPower);
        leftFront.setPower(leftPower);
        leftBack.setPower(leftPower);
        telemetry.addData("leftPower", Double.toString(leftPower));

        if (gamepad1.a) {
            currentPosBack += servoDelta;
            if (currentPosBack > 1) currentPosBack = 1;
            if (currentPosBack < 0) currentPosBack = 0;
            backShieldRight.setPosition(currentPosBack);
            backShieldLeft.setPosition(currentPosBack);
        }

        if (gamepad1.b) {

            currentPosBack -= servoDelta;
            if (currentPosBack > 1) currentPosBack = 1;
            if (currentPosBack < 0) currentPosBack = 0;
            backShieldLeft.setPosition(currentPosBack);
            backShieldRight.setPosition(currentPosBack);
        }

        if (gamepad1.x) {
            currentPosFront += servoDelta;
            if (currentPosFront > 1) currentPosFront = 1;
            if (currentPosFront < 0) currentPosFront = 0;

            frontShield.setPosition(currentPosFront);
        }

        if (gamepad1.y) {
            currentPosFront -= servoDelta;
            if (currentPosFront > 1) currentPosFront = 1;
            if (currentPosFront < 0) currentPosFront = 0;
            frontShield.setPosition(currentPosFront);
        }
    }
}
