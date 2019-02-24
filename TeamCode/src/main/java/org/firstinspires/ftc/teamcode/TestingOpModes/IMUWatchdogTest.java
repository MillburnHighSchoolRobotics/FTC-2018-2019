package org.firstinspires.ftc.teamcode.TestingOpModes;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.watchdog.IMUWatchdog;
import org.firstinspires.ftc.teamcode.watchdog.WatchdogManager;

@TeleOp(name = "IMU Watchdog Test", group = "testing")
//@Disabled
public class IMUWatchdogTest extends OpMode {
    @Override
    public void init() {
        WatchdogManager.getInstance().setHardwareMap(hardwareMap);
        WatchdogManager.getInstance().provision("IMUWatch", IMUWatchdog.class, "imu 1");
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
