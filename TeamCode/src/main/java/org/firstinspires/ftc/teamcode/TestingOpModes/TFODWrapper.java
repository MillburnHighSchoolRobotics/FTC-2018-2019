package org.firstinspires.ftc.teamcode.TestingOpModes;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp(name = "AHHHHHHHHH TFOD", group = "TESTING")
public class TFODWrapper extends LinearOpMode {
    @Override
    public void runOpMode() throws InterruptedException {
        TFODTest tfod = new TFODTest(hardwareMap);
        tfod.initStuff();
        telemetry.addData("pos", tfod.getGoldPos());
        telemetry.update();
        tfod.clean();
        waitForStart();

    }
}
