package virtualRobot.logicThreads.testing;

import virtualRobot.JoystickController;
import virtualRobot.LogicThread;

/**
 * Created by david on 9/29/17.
 */

public class JoystickTelemetryLogic extends LogicThread {
    @Override
    protected void realRun() throws InterruptedException {
        JoystickController controller1 = robot.getJoystickController1();
        while (true) {
            controller1.logicalRefresh();
            robot.addToTelemetry("X: ", controller1.getValue(JoystickController.X_1));
            robot.addToTelemetry("Y: ", controller1.getValue(JoystickController.Y_1));
            robot.addToTelemetry("THETA: ", controller1.getValue(JoystickController.THETA_1));
            double thetaDeg = Math.toDegrees(controller1.getValue(JoystickController.THETA_1));
            robot.addToTelemetry("THETADEG: ", thetaDeg);
            if (thetaDeg < 0) thetaDeg += 360;
            robot.addToTelemetry("ADJTHETADEG: ", thetaDeg);
            robot.addToTelemetry("R: ", controller1.getValue(JoystickController.R_1));
            if (Thread.currentThread().isInterrupted())
                throw new InterruptedException();
            Thread.sleep(10);
        }
    }
}
