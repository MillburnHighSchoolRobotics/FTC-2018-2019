package org.firstinspires.ftc.teamcode.TestingUpdate;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;

import org.firstinspires.ftc.teamcode.ReflectionUpdateThread;

import virtualRobot.logicThreads.testing.TestBackendLogic;

/**
 * Created by Ethan Mak on 8/28/2017.
 */
@Autonomous(name="Testing: Test Backend", group="Testing")
@Disabled
public class TestBackendUpdate extends ReflectionUpdateThread {
    @Override
    public void setLogicThread() {
        logicThread = TestBackendLogic.class;
    }
}
