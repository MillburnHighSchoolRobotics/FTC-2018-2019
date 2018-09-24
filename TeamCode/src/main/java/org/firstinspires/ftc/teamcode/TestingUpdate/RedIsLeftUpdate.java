package org.firstinspires.ftc.teamcode.TestingUpdate;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;

import org.firstinspires.ftc.teamcode.ReflectionUpdateThread;

import virtualRobot.logicThreads.testing.RedIsLeftLogic;

/**
 * Created by david on 11/15/17.
 */
@Disabled
@Autonomous( name="RedIsLeftTester", group="Testing" )
public class RedIsLeftUpdate extends ReflectionUpdateThread {
    @Override
    public void setLogicThread() {
        logicThread = RedIsLeftLogic.class;
    }
}
