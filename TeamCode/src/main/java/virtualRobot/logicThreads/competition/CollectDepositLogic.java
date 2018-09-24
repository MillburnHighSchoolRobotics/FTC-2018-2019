package virtualRobot.logicThreads.competition;

import org.firstinspires.ftc.robotcore.external.navigation.RelicRecoveryVuMark;

import virtualRobot.JoystickController;
import virtualRobot.commands.ShustinClass;
import virtualRobot.commands.RotateEncoder;
import virtualRobot.commands.Translate;
import virtualRobot.logicThreads.AutonomousLogicThread;
import virtualRobot.utils.GlobalUtils;

/**
 * Created by warre on 2/23/2018.
 */

public class CollectDepositLogic extends AutonomousLogicThread {
    @Override
    protected void realRun() throws InterruptedException {
        robot.getPhoneServo().setPosition(0.45);
        RelicRecoveryVuMark mark = GlobalUtils.forcedVumark;
        Thread.sleep(2000);
        robot.setRollerPower(-1);
        runCommand(new Translate(1400, Translate.Direction.FORWARD,0,0.5f));
        Thread.sleep(3000);
        robot.setRollerPower(0);
        Thread.sleep(500);
        runCommand(new RotateEncoder(0,1));
        Thread.sleep(500);
        runCommand(new Translate(100, Translate.Direction.BACKWARD, 0, 0.5f));
        int lastOffset = -1;
        while (true) {
            runCommand(new ShustinClass(mark));
            robot.addToTelemetry("Offset", offset.get());
            if (Math.abs(offset.get()) < 7) {
                break;
            }
            if (lastOffset >= 0 && Math.abs(offset.get()) - Math.abs(lastOffset) > 75) continue;
            lastOffset = Math.abs(offset.get());
            runCommand(new Translate(offset.get() * 10, Translate.Direction.LEFT, 0, 0.9));
            runCommand(new RotateEncoder(0,0.9));
        }
        runCommand(new Translate(1000, Translate.Direction.BACKWARD, 0, 0.5f));
        robot.getFlipper().setPosition(0.9);
        Thread.sleep(3000);
        robot.moveFlipper(true);
        runCommand(new Translate(500, Translate.Direction.BACKWARD, 0, 0.5f));
        Thread.sleep(1000);
        runCommand(new Translate(500, Translate.Direction.FORWARD, 0, 0.5f));
    }


}
