package org.firstinspires.ftc.teamcode.CompetitionUpdate;

import org.firstinspires.ftc.teamcode.ReflectionUpdateThread;

import virtualRobot.logicThreads.competition.RoverRuckusTeleOpLogic;

public class RoverRuckusTeleOpUpdate extends ReflectionUpdateThread {
    @Override
    public void setLogicThread() {
        this.logicThread = RoverRuckusTeleOpLogic.class;
    }
}
