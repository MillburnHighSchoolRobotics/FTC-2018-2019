package org.firstinspires.ftc.teamcode.TestingOpModes;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.DigitalChannel;

import org.firstinspires.ftc.teamcode.JeffBot;

@TeleOp(name = "MagLev", group = "Testing")
@Disabled
public class MagLev extends LinearOpMode {
    DcMotor lf;
    DcMotor lb;
    DcMotor rf;
    DcMotor rb;
    DcMotor liftR;
    DcMotor liftL;
    DigitalChannel magneticLimitSwitch;
    @Override
    public void runOpMode() throws InterruptedException {
        liftL = hardwareMap.dcMotor.get("liftL");
        liftR = hardwareMap.dcMotor.get("liftR");
        magneticLimitSwitch = hardwareMap.get(DigitalChannel.class, "Switchy");
        waitForStart();
        liftR.setDirection(DcMotorSimple.Direction.REVERSE);
        JeffBot mv = new JeffBot(lf, lb, rf, rb);
        liftL.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        liftR.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        mv.moveUntilPressed(new DcMotor[]{liftL, liftR}, magneticLimitSwitch, mv.POS_POWER_CONST);
    }
}
