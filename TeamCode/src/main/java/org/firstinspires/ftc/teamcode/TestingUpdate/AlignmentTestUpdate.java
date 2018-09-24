package org.firstinspires.ftc.teamcode.TestingUpdate;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.ReflectionUpdateThread;

import virtualRobot.logicThreads.testing.AlignmentTestLogic;

/**
 * Created by david on 2/12/18.
 */

@Autonomous( name = "Alignment Test", group = "Testing" )
public class AlignmentTestUpdate extends ReflectionUpdateThread {
    @Override
    public void setLogicThread() {
        logicThread = AlignmentTestLogic.class;
    }
}
