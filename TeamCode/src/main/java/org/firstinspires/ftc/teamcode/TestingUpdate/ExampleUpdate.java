package org.firstinspires.ftc.teamcode.TestingUpdate;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;

import org.firstinspires.ftc.teamcode.ReflectionUpdateThread;

import virtualRobot.logicThreads.ExampleLogic;

/**
 * Created by Ethan Mak on 8/28/2017.
 *
 * An example of what subclasses of UpdateThread should look like
 */
//@Disabled
@Autonomous(name="Example: Test", group="Example")
public class ExampleUpdate extends ReflectionUpdateThread {
    @Override
    public void setLogicThread() {
        logicThread = ExampleLogic.class;
    }
}
