package org.firstinspires.ftc.teamcode.TestingUpdate;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;

import org.firstinspires.ftc.teamcode.ReflectionUpdateThread;

import virtualRobot.logicThreads.testing.TranslateAutoPIDLogic;

/**
 * Created by Ethan Mak on 8/30/2017.
 */
@Disabled
public class TranslateAutoPIDUpdate extends ReflectionUpdateThread {
    @Override
    public void setLogicThread() {
        logicThread = TranslateAutoPIDLogic.class;
    }
}
