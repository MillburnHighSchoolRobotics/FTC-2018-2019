package org.firstinspires.ftc.teamcode.TestingOpModes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.IMUWatchdog;
import org.firstinspires.ftc.teamcode.WatchdogManager;

@TeleOp(name = "IMU Watchdog Test", group = "testing")
public class IMUWatchdogTest extends OpMode {
    @Override
    public void init() {
        WatchdogManager.getInstance().provision("IMUWatch", IMUWatchdog.class, hardwareMap.get("imu"));
    }

    @Override
    public void loop() {
        telemetry.addData("Rotation", WatchdogManager.getInstance().getValue("rotation"));
    }

    @Override
    public void stop() {
        WatchdogManager.getInstance().clean();
    }
}
