package org.firstinspires.ftc.teamcode.CompetitionUpdate;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.ReflectionUpdateThread;

import virtualRobot.logicThreads.competition.TeleOpCustomLogic;

/**
 * Created by david on 1/18/18.
 */

@TeleOp(name = "TeleOp: Run TeleOp (RUT)", group = "Competition")
public class TeleOpReflectionUpdate extends ReflectionUpdateThread {
    @Override
    public void setLogicThread() {
        this.logicThread = TeleOpCustomLogic.class;
    }
}
