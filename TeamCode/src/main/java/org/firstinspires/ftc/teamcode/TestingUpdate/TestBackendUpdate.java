package org.firstinspires.ftc.teamcode.TestingUpdate;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;

import org.firstinspires.ftc.teamcode.ReflectionUpdateThread;

import virtualRobot.logicThreads.testing.TestBackendLogic;

/**
 * Created by Ethan Mak on 8/28/2017.
 */
@Disabled
@Autonomous(name="Testing: Test Backend", group="Testing")
public class TestBackendUpdate extends ReflectionUpdateThread {
    @Override
    public void setLogicThread() {
        logicThread = TestBackendLogic.class;
    }
}
