package virtualRobot.logicThreads.testing;

import virtualRobot.JoystickController;
import virtualRobot.LogicThread;

/**
 * Created by ethan on 10/5/17.
 */

public class TestLiftTeleOpLogic extends LogicThread {
    @Override
    protected void addPresets() {
        shouldStartISR = false;
    }

    @Override
    protected void realRun() throws InterruptedException {
        boolean isInterrupted = false;
        JoystickController controller1 = robot.getJoystickController1();
        JoystickController controller2 = robot.getJoystickController2();

        while (!isInterrupted) {
            controller1.logicalRefresh();
            controller2.logicalRefresh();

            if (controller1.isDown(JoystickController.BUTTON_A)) {

            }

            if (Thread.currentThread().isInterrupted())
                throw new InterruptedException();
            Thread.sleep(10);
        }
    }
}
