package org.firstinspires.ftc.teamcode.NonUpdateCompetitionOpModes;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

/**
 * Created by Mehmet on 10/27/2017.
 */
@Disabled
@Autonomous(name="Blue Far Jewel Park", group="DumbComp")
public class BlueFarAuton extends LinearOpMode {
    private DcMotor leftFront,rightFront,leftBack,rightBack;
    private Servo glyphLiftLeft, glyphLiftRight, clawLeft, clawRight, jewelArm;
    private ColorSensor jewel;
    private DistanceSensor jewelDistance;
    private float hsvValues[] = {0,0,0};
    private final float values[] = hsvValues;

    @Override
    public void runOpMode() throws InterruptedException {
        glyphLiftLeft = hardwareMap.servo.get("glyphLiftLeft");
        glyphLiftRight = hardwareMap.servo.get("glyphLiftRight");
        clawLeft = hardwareMap.servo.get("clawLeft");
        clawRight = hardwareMap.servo.get("clawRight");
        jewelArm = hardwareMap.servo.get("jewelArm");
        leftFront = hardwareMap.dcMotor.get("leftFront");
        rightFront = hardwareMap.dcMotor.get("rightFront");
        leftBack = hardwareMap.dcMotor.get("leftBack");
        rightBack = hardwareMap.dcMotor.get("rightBack");
        jewel = hardwareMap.get(ColorSensor.class, "colorSensor");
        jewelDistance = hardwareMap.get(DistanceSensor.class, "colorSensor");

        rightFront.setDirection(DcMotorSimple.Direction.REVERSE);
        rightBack.setDirection(DcMotorSimple.Direction.REVERSE);

        jewelArm.setPosition(0);

        waitForStart();
        int dist  = 0;
        jewelArm.setPosition(0.5);
        Thread.sleep(1000);
        if (!Double.isNaN(jewelDistance.getDistance(DistanceUnit.CM))) {
            int red = jewel.red();
            int blue = jewel.blue();
            telemetry.addData("Red ", red);
            telemetry.addData("Blue ", blue);
            telemetry.addData("Updated ", true);
            if(red + blue >= 250) {
                double rat = (double)red/blue;
                if (rat <= 0.2) {
                    leftFront.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                    leftFront.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
                    leftFront.setPower(1);
                    leftBack.setPower(1);
                    rightFront.setPower(1);
                    rightBack.setPower(1);
                    while (leftFront.getCurrentPosition() < 200){}
                    dist = 200;
                } else if (rat >= 2.5) {
                    leftFront.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                    leftFront.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
                    leftFront.setPower(-1);
                    leftBack.setPower(-1);
                    rightFront.setPower(-1);
                    rightBack.setPower(-1);
                    while (leftFront.getCurrentPosition() > -200){}
                    dist = -200;
                }
            }
        }
        else
            telemetry.addData("Distance ", "failed");
        jewelArm.setPosition(0);
        telemetry.update();
        Thread.sleep(1000);
        leftFront.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        leftFront.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        leftFront.setPower(-1);
        leftBack.setPower(-1);
        rightFront.setPower(-1);
        rightBack.setPower(-1);
        while (leftFront.getCurrentPosition() > -1440*1 + dist){}
        leftFront.setPower(0);
        leftBack.setPower(0);
        rightFront.setPower(0);
        rightBack.setPower(0);
        while (opModeIsActive()){}
    }
}
