package org.firstinspires.ftc.teamcode.NonUpdateCompetitionOpModes;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.watchdog.WatchdogManager;

@Autonomous(name="Reset WatchDogManager",group="utility")
public class ResetWatchdogManager extends LinearOpMode {
    @Override
    public void runOpMode() throws InterruptedException {
        waitForStart();
        WatchdogManager.getInstance().clean();
    }
}