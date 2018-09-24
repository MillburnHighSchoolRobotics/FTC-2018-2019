package org.firstinspires.ftc.teamcode.TestingUpdate;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.ReflectionUpdateThread;

import virtualRobot.logicThreads.competition.CollectDepositLogic;

/**
 * Created by warre on 2/23/2018.
 */

@Autonomous( name="COllection depot", group="Testing" )
public class CollectionDepositUpdate extends ReflectionUpdateThread {
    @Override
    public void setLogicThread() {
        logicThread = CollectDepositLogic.class;
    }
}
