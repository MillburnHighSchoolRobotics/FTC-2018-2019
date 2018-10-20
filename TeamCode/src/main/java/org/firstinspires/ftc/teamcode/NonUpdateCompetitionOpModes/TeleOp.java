package org.firstinspires.ftc.teamcode.NonUpdateCompetitionOpModes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;

import virtualRobot.utils.MathUtils;

public class TeleOp extends OpMode {
    private DcMotor lf;
    private DcMotor lb;
    private DcMotor rf;
    private DcMotor rb;
    private DcMotor reaper;
    private Servo reaperLift;
    private DcMotor reaperFold;
    private DcMotor lift;
    private Servo deposit;
    float threshold = 0.1f;
    double power = 0.7;
    @Override
    public void init() {
        lf.setPower(0);
        lb.setPower(0);
        rf.setPower(0);
        rb.setPower(0);
        reaper = hardwareMap.dcMotor.get("reaper");
        reaperLift = hardwareMap.servo.get("reaperLift");
        reaperFold = hardwareMap.dcMotor.get("reaperFold");
        lift = hardwareMap.dcMotor.get("lift");
        deposit = hardwareMap.servo.get("deposit");
    }

    @Override
    public void loop() {
        //left stick = up and down
        //right stick = rotate
        //both sticks = rotate priority
        //buttons for reaper, reaper lift, reaper fold, lift, deposit

        if (MathUtils.equals(gamepad1.right_stick_x,0)) {
            lf.setPower(-1 * power * gamepad1.left_stick_y);
            lb.setPower(-1 * power * gamepad1.left_stick_y);
            rf.setPower(power * gamepad1.left_stick_y);
            rb.setPower(power * gamepad1.left_stick_y);
        } else {
            if (MathUtils.equals(gamepad1.left_stick_y,0)) {
                lf.setPower(power * gamepad1.left_stick_y);
                lb.setPower(power * gamepad1.left_stick_y);
                rf.setPower(power * gamepad1.left_stick_y);
                rb.setPower(power * gamepad1.left_stick_y);
            } else {
                lf.setPower(0);
                lb.setPower(0);
                rf.setPower(0);
                rb.setPower(0);
            }
        }
        if (gamepad1.dpad_up) {
            reaper.setPower(power);
        } else if (gamepad1.dpad_down) {
            reaper.setPower(-1 * power)
        }
    }
}
