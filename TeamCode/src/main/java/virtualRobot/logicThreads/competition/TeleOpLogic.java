package virtualRobot.logicThreads.competition;

import virtualRobot.JoystickController;
import virtualRobot.LogicThread;
import virtualRobot.commands.Command;
import virtualRobot.commands.Translate;

/**
 * Created by Ethan Mak on 8/29/2017.
 */

public class TeleOpLogic extends LogicThread {
    @Override
    protected void addPresets() {
        shouldStartISR = false;
    }

    @Override
    protected void realRun() throws InterruptedException {
        JoystickController controller1;
        JoystickController controller2;
        controller1 = robot.getJoystickController1();
        controller2 = robot.getJoystickController2();
        Translate.Direction direction = null;
        Translate.Direction lastDirection = null;
        Translate headingMovement = null;
        while (true) {
            controller1.logicalRefresh();
            controller2.logicalRefresh();
            double movementTheta = Math.toDegrees(controller1.getValue(JoystickController.THETA_1)); //movement angle
            if (movementTheta < 0) movementTheta += 360;
            double movementMag = Math.toDegrees(controller1.getValue(JoystickController.R_1)); //movement magnitude
            //calc direction
            if (movementMag < 0.1) {
                direction = null;
            } else if (movementTheta >= 67.5 && movementTheta < 112.5) {
                direction = Translate.Direction.FORWARD;
            } else if (movementTheta >= 112.5 && movementTheta < 157.5) {
                direction = Translate.Direction.FORWARD_RIGHT;
            } else if (movementTheta >= 157.5 && movementTheta < 202.5) {
                direction = Translate.Direction.RIGHT;
            } else if (movementTheta >= 202.5 && movementTheta < 247.5) {
                direction = Translate.Direction.BACKWARD_RIGHT;
            } else if (movementTheta >= 247.5 && movementTheta < 292.5) {
                direction = Translate.Direction.BACKWARD;
            } else if (movementTheta >= 292.5 && movementTheta < 337.5) {
                direction = Translate.Direction.BACKWARD_LEFT;
            } else if (movementTheta >= 337.5 || movementTheta < 22.5) {
                direction = Translate.Direction.LEFT;
            } else if (movementTheta >= 22.5 && movementTheta < 67.5) {
                direction = Translate.Direction.FORWARD_LEFT;
            }
            if (direction != null && headingMovement == null) {
                headingMovement = new Translate(Translate.RunMode.HEADING_ONLY, direction, 0, 1000);
                runCommand((headingMovement));
            } else if (direction == null && headingMovement != null) {
                headingMovement.stopCommand();
                headingMovement = null;
            } else if (direction != null && lastDirection != null && direction != lastDirection) {
                headingMovement.setDirection(direction);
            }
            lastDirection = direction;
            //TODO: Use THETA_2 to rotate
            if (Thread.currentThread().isInterrupted())
                throw new InterruptedException();
            Thread.sleep(10);
        }
    }
}
