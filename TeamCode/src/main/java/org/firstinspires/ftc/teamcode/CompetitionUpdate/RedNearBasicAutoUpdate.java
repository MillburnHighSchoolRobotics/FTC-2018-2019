package org.firstinspires.ftc.teamcode.CompetitionUpdate;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.ReflectionUpdateThread;

import virtualRobot.SallyJoeBot;
import virtualRobot.logicThreads.competition.RedNearBasicAutoLogic;
import virtualRobot.utils.GlobalUtils;

/**
 * Created by ethan on 9/22/17.
 */

@Autonomous(name = "Autonomous: Red Near Basic", group = "Competition")
public class RedNearBasicAutoUpdate extends ReflectionUpdateThread {
    @Override
    public void setLogicThread() {
        logicThread = RedNearBasicAutoLogic.class;
        GlobalUtils.currentTeam = SallyJoeBot.Team.RED;
    }
}
