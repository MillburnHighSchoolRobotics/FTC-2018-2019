package org.firstinspires.ftc.teamcode.CompetitionUpdate;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.ReflectionUpdateThread;

import virtualRobot.SallyJoeBot;
import virtualRobot.logicThreads.competition.BlueNearBasicAutoLogic;
import virtualRobot.utils.GlobalUtils;

/**
 * Created by ethan on 9/22/17.
 */

@Autonomous(name = "Autonomous: Blue Near Basic", group = "Competition")
public class BlueNearBasicAutoUpdate extends ReflectionUpdateThread {
    @Override
    public void setLogicThread() {
        logicThread = BlueNearBasicAutoLogic.class;
        GlobalUtils.currentTeam = SallyJoeBot.Team.BLUE;
    }
}
