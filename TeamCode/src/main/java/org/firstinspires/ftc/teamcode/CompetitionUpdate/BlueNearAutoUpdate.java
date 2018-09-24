package org.firstinspires.ftc.teamcode.CompetitionUpdate;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.ReflectionUpdateThread;

import virtualRobot.SallyJoeBot;
import virtualRobot.logicThreads.competition.BlueNearAutoLogic;
import virtualRobot.utils.GlobalUtils;

/**
 * Created by Ethan Mak on 8/29/2017.
 */

@Autonomous(name = "Autonomous: Blue Near Full", group = "Competition")
public class BlueNearAutoUpdate extends ReflectionUpdateThread {
    @Override
    public void setLogicThread() {
        logicThread = BlueNearAutoLogic.class;
        GlobalUtils.currentTeam = SallyJoeBot.Team.BLUE;
    }
}
