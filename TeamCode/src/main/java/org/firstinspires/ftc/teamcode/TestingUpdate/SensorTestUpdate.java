package org.firstinspires.ftc.teamcode.TestingUpdate;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.ReflectionUpdateThread;

import virtualRobot.logicThreads.testing.SensorTestLogic;

/**
 * Created by Ethan Mak on 2/16/2018.
 */

@Autonomous(name= "Sensor: Sensor Test ALL Lol", group="Sensor")
public class SensorTestUpdate extends ReflectionUpdateThread {
    @Override
    public void setLogicThread() {
        logicThread = SensorTestLogic.class;
    }
}
