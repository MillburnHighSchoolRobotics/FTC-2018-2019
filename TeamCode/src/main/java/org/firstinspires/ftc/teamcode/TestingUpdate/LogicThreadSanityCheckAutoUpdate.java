package org.firstinspires.ftc.teamcode.TestingUpdate;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;

import org.firstinspires.ftc.teamcode.ReflectionUpdateThread;

import virtualRobot.logicThreads.testing.LogicThreadSanityCheckAutoLogic;

/**
 * Created by david on 10/6/17.
 */
@Disabled
@Autonomous(name = "Logic Thread Sanity Check Auto Test Thing", group = "Testing")
public class LogicThreadSanityCheckAutoUpdate extends ReflectionUpdateThread {
    @Override
    public void setLogicThread() {
        logicThread = LogicThreadSanityCheckAutoLogic.class;
    }
}
