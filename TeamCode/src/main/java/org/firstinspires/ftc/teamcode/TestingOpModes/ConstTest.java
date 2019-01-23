package org.firstinspires.ftc.teamcode.TestingOpModes;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.Servo;

@TeleOp(name="Const",group="Testing")
public class ConstTest extends LinearOpMode {

    private Servo dropper;

    @Override
    public void runOpMode() throws InterruptedException {
        dropper = hardwareMap.servo.get("dropper");
        dropper.setPosition(0.3);//The Right Values
        waitForStart();
        dropper.setPosition(1);//The Right Values
        Thread.sleep(10000);
    }
}
