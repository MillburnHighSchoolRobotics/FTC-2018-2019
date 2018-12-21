package org.firstinspires.ftc.teamcode.CompetitionUpdate;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;

import org.firstinspires.ftc.teamcode.ReflectionUpdateThread;

import virtualRobot.SallyJoeBot;
import virtualRobot.logicThreads.competition.RedFarBasicAutoLogic;
import virtualRobot.utils.GlobalUtils;

/**
 * Created by ethan on 9/22/17.
 */

@Autonomous(name = "Autonomous: Red Far Basic", group = "Competition")
@Disabled
public class RedFarBasicAutoUpdate extends ReflectionUpdateThread {
    @Override
    public void setLogicThread() {
        logicThread = RedFarBasicAutoLogic.class;
        GlobalUtils.currentTeam = SallyJoeBot.Team.RED;
    }
}
