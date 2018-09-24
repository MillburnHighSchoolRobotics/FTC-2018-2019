package org.firstinspires.ftc.teamcode.CompetitionUpdate;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;

import org.firstinspires.ftc.teamcode.ReflectionUpdateThread;

import virtualRobot.logicThreads.competition.TeleOpCustomLogic;

/**
 * Created by david on 9/29/17.
 */
@Disabled
//@TeleOp(name = "TeleOp: Run TeleOp Rewrite", group = "Competition")
public class TeleOpRewriteUpdate extends ReflectionUpdateThread {
    public void setLogicThread() {
        logicThread = TeleOpCustomLogic.class;
    }
}
