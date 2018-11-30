package org.firstinspires.ftc.teamcode.TestingOpModes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;

@TeleOp(name="markerConfig", group="testing")
public class MarkerConfig extends OpMode {
    Servo marker;
    @Override
    public void init(){
        marker = hardwareMap.servo.get("marker");
    }
    @Override
    public void loop(){
        marker.setPosition(0);
        telemetry.addData("Pos","0");
        telemetry.update();
        try {
            Thread.sleep(2000);
        }
        catch(Exception e){
        }
        marker.setPosition(1);
        telemetry.addData("Pos","1");
        telemetry.update();
        try {
            Thread.sleep(2000);
        }
        catch(Exception e){
        }
    }
}
