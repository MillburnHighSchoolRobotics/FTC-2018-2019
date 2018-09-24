package org.firstinspires.ftc.teamcode.TestingUpdate;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;

import org.firstinspires.ftc.teamcode.ReflectionUpdateThread;

import virtualRobot.logicThreads.testing.PIDTesterLogic;

/**
 * Created by Ethan Mak on 8/30/2017.
 */
//@Disabled
    @Autonomous(name="Testing: PID Tester", group="Testing")
public class PIDTesterUpdate extends ReflectionUpdateThread {
    @Override
    public void setLogicThread() {
        logicThread = PIDTesterLogic.class;
    }
}
