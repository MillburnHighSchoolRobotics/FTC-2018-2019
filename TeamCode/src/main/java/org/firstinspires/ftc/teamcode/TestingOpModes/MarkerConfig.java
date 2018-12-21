package org.firstinspires.ftc.teamcode.TestingOpModes;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;

@TeleOp(name="markerConfig", group="testing")
@Disabled
public class MarkerConfig extends LinearOpMode {
    Servo marker;
    @Override
    public void runOpMode(){
        marker = hardwareMap.get(Servo.class, "marker");
        waitForStart();
        while(true) {
            marker.setPosition(0);
            telemetry.addData("Pos", "0");//Out
            telemetry.update();
            try {
                Thread.sleep(2000);
            } catch (Exception e) {
            }
            marker.setPosition(1);
            telemetry.addData("Pos", "1");//In
            telemetry.update();
            try {
                Thread.sleep(2000);
            } catch (Exception e) {
            }
        }
    }
}
