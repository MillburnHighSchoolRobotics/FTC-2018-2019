package virtualRobot.logicThreads.testing;

import virtualRobot.LogicThread;

/**
 * Created by david on 11/3/17.
 */

public class ColorSensorTestLogic extends LogicThread {
    @Override
    protected void realRun() throws InterruptedException {
        while (true) {
            robot.addToTelemetry("RGB: ", "(" + robot.getColorSensor().getRed() + ", " + robot.getColorSensor().getGreen() + ", " + robot.getColorSensor().getBlue() + ")");
            if (Thread.currentThread().isInterrupted())
                throw new InterruptedException();
            Thread.sleep(10);
        }
    }
}
