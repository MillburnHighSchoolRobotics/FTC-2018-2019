package org.firstinspires.ftc.teamcode.TestingUpdate;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;

import org.firstinspires.ftc.teamcode.ReflectionUpdateThread;

import virtualRobot.logicThreads.testing.ColorSensorTestLogic;

/**
 * Created by david on 11/3/17.
 */
@Disabled
@Autonomous( name="Color Sensor Test", group="Testing" )
public class ColorSensorTestUpdate extends ReflectionUpdateThread {
    @Override
    public void setLogicThread() {
        logicThread = ColorSensorTestLogic.class;
    }
}
