package org.firstinspires.ftc.teamcode.TestingOpModes;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.Servo;

/**
 * Created by david on 10/26/17.
 */
@Disabled
@Autonomous( name="SoumyaClass", group="memes" )
public class SoumyaClass extends OpMode {
    Servo clawLeft, clawRight, gll, glr;
    double pos;
    long lastTime;
    public void init() {
        pos = 0;
        clawLeft = hardwareMap.servo.get("clawLeft");
        clawRight = hardwareMap.servo.get("clawRight");
        gll = hardwareMap.servo.get("glyphLiftLeft");
        glr = hardwareMap.servo.get("glyphLiftRight");
        lastTime = System.currentTimeMillis();
    }
    public void loop() {
        if (System.currentTimeMillis() - lastTime > 1000) {
            pos = 1 - pos;
            clawLeft.setPosition(pos);
            clawRight.setPosition(1 - pos);
            gll.setPosition(pos);
            glr.setPosition(1 - pos);
            telemetry.addData("cl", clawLeft.getPosition());
            telemetry.addData("cr", clawRight.getPosition());
            telemetry.addData("gll", gll.getPosition());
            telemetry.addData("glr", glr.getPosition());
            lastTime = System.currentTimeMillis();
        }
    }
}
