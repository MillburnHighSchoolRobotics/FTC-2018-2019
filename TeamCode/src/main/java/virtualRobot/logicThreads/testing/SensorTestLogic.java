package virtualRobot.logicThreads.testing;

import virtualRobot.logicThreads.AutonomousLogicThread;

/**
 * Created by Ethan Mak on 2/16/2018.
 */

public class SensorTestLogic extends AutonomousLogicThread {
    @Override
    protected void realRun() throws InterruptedException {
        while (!Thread.currentThread().isInterrupted()) {
            robot.addToTelemetry("IMU: ", robot.getImu().getHeading() + " " + robot.getImu().getRawHeading() + " " + robot.getImu().getPitch() + " " + robot.getImu().getRawRoll());
            robot.addToTelemetry("ColorSensor: ", robot.getColorSensor().getRed() + " " + robot.getColorSensor().getGreen() + " " + robot.getColorSensor().getBlue());

            Thread.sleep(10);
        }
    }
}
