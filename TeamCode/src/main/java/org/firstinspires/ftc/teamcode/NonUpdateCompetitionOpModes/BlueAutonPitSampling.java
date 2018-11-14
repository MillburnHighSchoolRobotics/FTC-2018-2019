package org.firstinspires.ftc.teamcode.NonUpdateCompetitionOpModes;

import android.util.Log;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName:
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.PIDCoefficients;
import com.qualcomm.robotcore.hardware.PIDFCoefficients;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.Movement;
import org.firstinspires.ftc.teamcode.TestingOpModes.TFODTest;

import virtualRobot.PIDController;
import virtualRobot.SallyJoeBot;
import virtualRobot.commands.Command;
import virtualRobot.utils.MathUtils;

import static org.firstinspires.ftc.teamcode.Movement.distToEncoder;
import static org.firstinspires.ftc.teamcode.Movement.rotateToEncoder;

@Autonomous(name = "Blue Auton Pit Sampling", group = "competition")
@Disabled
public class BlueAutonPitSampling extends LinearOpMode {
    DcMotor lf;
    DcMotor lb;
    DcMotor rf;
    DcMotor rb;
    Servo marker;

    static int kp = 0;
    static int ki = 0;
    static int kd = 0;
    static int threshold = 0;

    SallyJoeBot robot = Command.ROBOT;

    DcMotor liftR;
    DcMotor liftL;
    Movement pidLF;
    Movement pidLB;
    Movement pidRF;
    Movement pidRB;
    int meme = 0;
    WebcamName webcamName;

    @Override
    public void runOpMode() throws InterruptedException {

        //Here's how we find the webcam
        webcamName = hardwareMap.get(WebcamName.class, "Whatever the name of the webcam is")
        lf = hardwareMap.dcMotor.get("leftFront");
        lb = hardwareMap.dcMotor.get("leftBack");
        rf = hardwareMap.dcMotor.get("rightFront");
        rb = hardwareMap.dcMotor.get("rightBack");
        liftL = hardwareMap.dcMotor.get("liftL");
        liftR = hardwareMap.dcMotor.get("liftR");
        marker = hardwareMap.servo.get("marker");
        marker.setPosition(0);

        waitForStart();
        lf.setDirection(DcMotorSimple.Direction.REVERSE);
        lb.setDirection(DcMotorSimple.Direction.REVERSE);
        rf.setDirection(DcMotorSimple.Direction.FORWARD);
        rb.setDirection(DcMotorSimple.Direction.FORWARD);
        initializeMotor(new DcMotor[]{lf, lb, rf, rb});

        liftL.setDirection(DcMotorSimple.Direction.REVERSE);
        int initL = liftL.getCurrentPosition();
        int initR = liftR.getCurrentPosition();

//        moveToPosition(new DcMotor[] {liftL, liftR}, new double[] {1, 1}, new int[] {4796+1067-initL, 4796+1067-initR});
//        Thread.sleep(100);
//       mv.translate(0.5, 104);
//        Thread.sleep(100);
//
//        moveToPosition(new DcMotor[] {liftL, liftR}, new double[] {1, 1}, new int[] {8418+1067-initL, 8418+1067-initR});
//        Thread.sleep(100);

        //sampling
        //   int meme = 0;
        Movement mv = new Movement(lf, lb, rf, rb);
        TFODTest tfod = new TFODTest(hardwareMap);
        liftL.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        liftR.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);




        mv.moveToPosition(new DcMotor[] {liftL, liftR}, new double[] {-0.8, 0.8}, new int[] {10700+100, 10700+100});
//        Thread.sleep(100);

//
//       mv.translate(0.5, 104);
//        Thread.sleep(100);
//
//        moveToPosition(new DcMotor[] {liftL, liftR}, new double[] {1, 1}, new int[] {8418+1067-initL, 8418+1067-initR});
//        Thread.sleep(100);

        //sampling

        pidLF = new Movement(kp,ki,kd,threshold);
        pidLB = new Movement(kp,ki,kd,threshold);
        pidRF = new Movement(kp,ki,kd,threshold);
        pidRB = new Movement(kp,ki,kd,threshold);


//        PIDFCoefficients pidOriginalLF = ((DcMotorEx) lf).getPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER);
//        PIDFCoefficients pidOriginalLB = ((DcMotorEx) lf).getPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER);
//        PIDFCoefficients pidOriginalRF = ((DcMotorEx) lf).getPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER);
//        PIDFCoefficients pidOriginalRB = ((DcMotorEx) lf).getPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER);

        //pidLF.setTarget(HardwareMap.DeviceMapping.);
        //.clamp(Double.toString(robot.getImu().getHeading()), 0, 45)
        pidLB.setTarget(mv.distToEncoder(15));
        pidRF.setTarget(mv.distToEncoder(15));
        pidRB.setTarget(mv.distToEncoder(15));

        double lfPower = pidLF.getPIDOutput(lf.getTargetPosition());
        double lbPower = pidLB.getPIDOutput(lb.getTargetPosition());
        double rfPower = pidRF.getPIDOutput(rf.getTargetPosition());
        double rbPower = pidRB.getPIDOutput(rb.getTargetPosition());

        lf.setPower(lfPower);
        lb.setPower(lbPower);
        rf.setPower(rfPower);
        rb.setPower(rbPower);

        mv.translateDistance(0.7,15);



//        mv.moveToPosition(new DcMotor[] {liftL, liftR}, new double[] {-0.8, 0.8}, new int[] {600, 600});
        liftL.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        liftR.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        liftL.setPower(0.8);
        liftR.setPower(-0.8);
        liftL.setTargetPosition(600);
        liftR.setTargetPosition(600);


//        Thread.sleep(100);

        int num = 1;//TODO:Fix //which mineral is the gold mineral one
        switch (num) {
            case 0:
               mv.rotateDegrees(0.5,60);
               mv.translateDistance(0.7,24);
               mv.translateDistance(0.7,-24);
               mv.rotateDegrees(0.5, -60);
                break;
            case 2:
               mv.rotateDegrees(0.5,-60);
               mv.translateDistance(0.7,24);
               mv.translateDistance(0.7,-24);
               mv.rotateDegrees(0.5, 60);
                break;
            default:
            case 1:
                mv.translateDistance(0.7,16);
                mv.translateDistance(0.7,-16);
                break;
        }
//        Thread.sleep(100);
//       mv.rotateDegrees(0.5,90);//TODO:Add global variable for speed

        mv.rotateDegrees(0.7, 90);
//        Thread.sleep(100);
//        Thread.sleep(100);
        mv.translateDistance(0.7, 41);//TODO:See above immortal TODO
//        Thread.sleep(100);
        mv.rotateDegrees(0.5,45);
//        Thread.sleep(100);
        mv.translateDistance(0.7, 39);
//        Thread.sleep(100);
//        mv.rotateDegrees(0.7, -60);
//        Thread.sleep(100);
        marker.setPosition(1);
        ElapsedTime time = new ElapsedTime();
        while (time.seconds() < 0.5f) {
            Thread.sleep(10);
        }
//        Thread.sleep(100);
//        mv.rotateDegrees(0.7, 60);
//        Thread.sleep(100);
//       mv.rotate(-0.5,-1);
        mv.translateDistance(0.9,-80);
    }
    public void initializeMotor(DcMotor[] motors) {
        for (DcMotor motor : motors) {
            motor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            motor.setPower(0);
//            motor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
//            motor.setTargetPosition(0);
        }
    }
}
