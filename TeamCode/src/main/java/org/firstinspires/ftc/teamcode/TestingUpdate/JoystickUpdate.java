package org.firstinspires.ftc.teamcode.TestingUpdate;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.ReflectionUpdateThread;

import virtualRobot.logicThreads.testing.JoystickTelemetryLogic;

/**
 * Created by david on 9/29/17.
 */
@Disabled
@TeleOp( name = "Testing: Joystick", group = "Testing")
public class JoystickUpdate extends ReflectionUpdateThread {
    @Override
    public void setLogicThread() {
        logicThread = JoystickTelemetryLogic.class;
    }
}
