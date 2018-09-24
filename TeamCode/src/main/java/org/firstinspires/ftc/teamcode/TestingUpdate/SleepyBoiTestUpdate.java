package org.firstinspires.ftc.teamcode.TestingUpdate;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;

import org.firstinspires.ftc.teamcode.ReflectionUpdateThread;

import virtualRobot.logicThreads.testing.SleepyBoiTestLogic;

/**
 * Created by david on 12/2/17.
 */
@Disabled
@Autonomous( name="Sleepy Boi Test", group = "Testing" )
public class SleepyBoiTestUpdate extends ReflectionUpdateThread {
    @Override
    public void setLogicThread() {
        logicThread = SleepyBoiTestLogic.class;
    }
}
