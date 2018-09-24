package org.firstinspires.ftc.teamcode.CompetitionUpdate;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.ReflectionUpdateThread;

import virtualRobot.logicThreads.competition.TeleOpCustomLogic;

/**
 * Created by Ethan Mak on 8/29/2017.
 */
@Disabled
@TeleOp(name = "TeleOp: Run TeleOp", group = "Competition")
public class TeleOpUpdate extends ReflectionUpdateThread {
    @Override
    public void setLogicThread() {
        logicThread = TeleOpCustomLogic.class;
    }
}
