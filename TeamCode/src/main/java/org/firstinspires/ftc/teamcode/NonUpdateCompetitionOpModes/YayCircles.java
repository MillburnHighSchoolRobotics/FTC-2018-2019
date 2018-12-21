package org.firstinspires.ftc.teamcode.NonUpdateCompetitionOpModes;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import virtualRobot.utils.MathUtils;

@Autonomous(name = "YayCircles", group = "testing")
@Disabled
public class YayCircles extends OpMode {
    private DcMotorEx lf;
    private DcMotorEx lb;
    private DcMotorEx rf;
    private DcMotorEx rb;
    //    private DcMotor reaper;
//    private Servo reaperLift;
//    private DcMotor reaperFold;
//    private DcMotor lift;
//    private Servo deposit;
    float threshold = 0.1f;
    double power = 0.9;

    final float botWidth = 16.5f; //inches
    final float c = botWidth/2; //in
    final float wheelWidth = 3; //in
    final float wheelRadius = wheelWidth/2; //in
    final int ticksPerRev = 680; //ticks per rev
    final float targetRadius = 10+c; //inches
    final double targetSpeed =4; //inches/sec
    final double encoderSpeed = (targetSpeed/(2*Math.PI*wheelRadius)) * ticksPerRev; //encoders per sec
    @Override
    public void init() {
        lf = (DcMotorEx)hardwareMap.dcMotor.get("leftFront");
        lb = (DcMotorEx)hardwareMap.dcMotor.get("leftBack");
        rf = (DcMotorEx)hardwareMap.dcMotor.get("rightFront");
        rb = (DcMotorEx)hardwareMap.dcMotor.get("rightBack");

        lf.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        lb.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rf.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rb.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        lf.setDirection(DcMotorSimple.Direction.FORWARD);
        lb.setDirection(DcMotorSimple.Direction.FORWARD);

        rf.setDirection(DcMotorSimple.Direction.REVERSE);
        rb.setDirection(DcMotorSimple.Direction.REVERSE);

        lf.setPower(0);
        lb.setPower(0);
        rf.setPower(0);
        rb.setPower(0);


    }

    @Override
    public void loop() {
        double a = (targetRadius+c)/(targetRadius-c);
        double v1 = (2*a*encoderSpeed)/(a+1);
        double v2 = (2*encoderSpeed)/(a+1);
        lf.setVelocity(v1);
        lb.setVelocity(v1);
        rf.setVelocity(v2);
        rb.setVelocity(v2);
    }
}
