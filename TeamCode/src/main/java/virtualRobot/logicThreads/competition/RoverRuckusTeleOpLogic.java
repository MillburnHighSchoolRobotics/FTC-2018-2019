package virtualRobot.logicThreads.competition;

import virtualRobot.JoystickController;
import virtualRobot.LogicThread;
import virtualRobot.utils.MathUtils;

public class RoverRuckusTeleOpLogic extends LogicThread {
    @Override
    protected void realRun() throws InterruptedException {
        double L = 0, R = 0;
        double scale = 0.7;
        JoystickController controller1;
        JoystickController controller2;
        controller1 = robot.getJoystickController1();
        controller2 = robot.getJoystickController2();


        while (true) {
            if (!MathUtils.equals(controller1.getValue(JoystickController.Y_1), 0, 0.005)) {
                double power = controller1.getValue(JoystickController.Y_1);
                L = power;
                R = power;
            } else if (!MathUtils.equals(controller1.getValue(JoystickController.X_2), 0, 0.005)) {
                double power = controller1.getValue(JoystickController.X_2);
                L = power;
                R = -power;
            }
            robot.getLBMotor().setPower(scale * L);
            robot.getLFMotor().setPower(scale * L);
            robot.getRBMotor().setPower(scale * R);
            robot.getRFMotor().setPower(scale * R);
            if (Thread.currentThread().isInterrupted())
                throw new InterruptedException();
            Thread.sleep(10);
        }
    }
}
