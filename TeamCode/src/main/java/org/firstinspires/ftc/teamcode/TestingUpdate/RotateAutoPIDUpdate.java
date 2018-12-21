package org.firstinspires.ftc.teamcode.TestingUpdate;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;

import org.firstinspires.ftc.teamcode.ReflectionUpdateThread;

import virtualRobot.logicThreads.testing.RotateAutoPIDLogic;

/**
 * Created by Ethan Mak on 8/30/2017.
 */

@Autonomous(name = "Testing: Rotate AutoPID", group = "Testing")
@Disabled
public class RotateAutoPIDUpdate extends ReflectionUpdateThread {
    @Override
    public void setLogicThread() {
        logicThread = RotateAutoPIDLogic.class;
    }
}
