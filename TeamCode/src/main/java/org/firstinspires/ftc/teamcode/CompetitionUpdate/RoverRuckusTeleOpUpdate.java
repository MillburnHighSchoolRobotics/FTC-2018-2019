package org.firstinspires.ftc.teamcode.CompetitionUpdate;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.ReflectionUpdateThread;

import virtualRobot.logicThreads.competition.RoverRuckusTeleOpLogic;

@TeleOp(name = "Rover Ruckus NewNewNewTeleOp", group = "competition")
public class RoverRuckusTeleOpUpdate extends ReflectionUpdateThread {
    @Override
    public void setLogicThread() {
        this.logicThread = RoverRuckusTeleOpLogic.class;
    }
}
