package org.firstinspires.ftc.teamcode.TestingOpModes;


import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.OpticalDistanceSensor;

/**
 * Created by Mehmet on 11/14/2017.
 */
@Disabled
public class ODSTest extends OpMode {

    OpticalDistanceSensor ODS;

    @Override
    public void init() {
        ODS = hardwareMap.opticalDistanceSensor.get("ODS");
    }

    @Override
    public void loop() {
        telemetry.addData("ODS: ", ODS.getLightDetected());
        telemetry.addData("ODS raw: ", ODS.getRawLightDetected());
        telemetry.addData("ODS raw max: ", ODS.getRawLightDetectedMax());
    }
}
