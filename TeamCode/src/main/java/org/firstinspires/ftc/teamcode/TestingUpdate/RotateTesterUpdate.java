package org.firstinspires.ftc.teamcode.TestingUpdate;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;

import org.firstinspires.ftc.teamcode.ReflectionUpdateThread;

import virtualRobot.logicThreads.testing.RotateTesterLogic;

/**
 * Created by david on 1/16/18.
 */
@Autonomous(name="Rotate Tester Loop", group="Testing")
@Disabled
public class RotateTesterUpdate extends ReflectionUpdateThread {
    @Override
    public void setLogicThread() {
        this.logicThread = RotateTesterLogic.class;
    }
}
