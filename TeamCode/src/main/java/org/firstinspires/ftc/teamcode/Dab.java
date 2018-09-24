package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

public class Dab extends OpMode {
    @Override
    public void init() {
        DcMotor meme = hardwareMap.get(DcMotor.class, "meme");
        meme.setPower(1);
    }

    @Override
    public void loop() {

    }
}
