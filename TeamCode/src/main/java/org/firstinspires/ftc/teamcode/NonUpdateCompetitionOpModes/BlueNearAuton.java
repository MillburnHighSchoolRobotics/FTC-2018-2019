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
@Autonomous(name="Blue Near Jewel Park", group="DumbComp")
public class BlueNearAuton extends LinearOpMode {
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
        int travel = 500;
        double power = .5;
        jewelArm.setPosition(0.5);
        telemetry.addData("ididit2","done");
        Thread.sleep(1000);
        int startPosition;
        if (!Double.isNaN(jewelDistance.getDistance(DistanceUnit.CM))) {
            int red = jewel.red();
            int blue = jewel.blue();
            telemetry.addData("Red ", red);
            telemetry.addData("Blue ", blue);
            telemetry.addData("Updated ", true);
//            if(red + blue >= )

                double rat = red/blue;
                telemetry.addData("Rat ", rat);
                if (rat <= 0.5) {
                    startPosition = leftFront.getCurrentPosition();
                    leftFront.setPower(power);
                    leftBack.setPower(power);
                    rightFront.setPower(power);
                    rightBack.setPower(power);
                    telemetry.addData("ididit","rat is lower than 0.5");
                    while (leftFront.getCurrentPosition() - startPosition < travel){}
                    dist = travel;
                } else if (rat >= 1.5) {
                    telemetry.addData("ididit","rat is greater than 1.5");
                    startPosition = leftFront.getCurrentPosition();
                    leftFront.setPower(-power);
                    leftBack.setPower(-power);
                    rightFront.setPower(-power);
                    rightBack.setPower(-power);
                    while (leftFront.getCurrentPosition() - startPosition > -travel){}
                    dist = -travel;
                }

        }
        else
            telemetry.addData("Distance ", "failed");
        telemetry.addData("ididit2","done");
        jewelArm.setPosition(0);
        telemetry.update();
        Thread.sleep(1000);
        startPosition = leftFront.getCurrentPosition();
        leftFront.setPower(-power);
        leftBack.setPower(-power);
        rightFront.setPower(-power);
        rightBack.setPower(-power);
        while (leftFront.getCurrentPosition() - startPosition > -1440*1 + dist && opModeIsActive()){}
        leftFront.setPower(0);
        leftBack.setPower(0);
        rightFront.setPower(0);
        rightBack.setPower(0);
        while (opModeIsActive()){}
    }
}
