package org.firstinspires.ftc.teamcode.CompetitionUpdate;

import android.provider.Settings;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.ReflectionUpdateThread;

import virtualRobot.SallyJoeBot;
import virtualRobot.logicThreads.competition.BlueFarAutoLogic;
import virtualRobot.utils.GlobalUtils;

/**
 * Created by ethan on 9/22/17.
 */

@Autonomous(name = "Autonomous: Blue Far Full", group = "Competition")
public class BlueFarAutoUpdate extends ReflectionUpdateThread {
    @Override
    public void setLogicThread() {
        logicThread = BlueFarAutoLogic.class;
        GlobalUtils.currentTeam = SallyJoeBot.Team.BLUE;
    }
}
