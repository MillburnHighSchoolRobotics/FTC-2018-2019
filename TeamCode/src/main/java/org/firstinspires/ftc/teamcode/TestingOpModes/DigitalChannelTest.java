package org.firstinspires.ftc.teamcode.TestingOpModes;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.DigitalChannelController;

/**
 * Created by ethachu19 on 11/1/2016.
 */
@Disabled
@Autonomous(name = "Sensor: Test Digital Channel", group = "Sensor")
public class DigitalChannelTest extends OpMode {
    DigitalChannel channel;
    @Override
    public void init() {
        channel = hardwareMap.digitalChannel.get("sonarLeft");
        channel.setMode(DigitalChannelController.Mode.OUTPUT);
    }

    @Override
    public void loop() {
        channel.setState(true);
    }
}
