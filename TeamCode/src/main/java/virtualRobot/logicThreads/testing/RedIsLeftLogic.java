package virtualRobot.logicThreads.testing;

import virtualRobot.commands.Command;
import virtualRobot.commands.EthanClass;
import virtualRobot.logicThreads.AutonomousLogicThread;

/**
 * Created by david on 11/15/17.
 */

public class RedIsLeftLogic extends AutonomousLogicThread {
    @Override
    protected void realRun() throws InterruptedException {
        Command jewelDetector = new EthanClass();
        jewelDetector.setParentThread(this);
        while (true) {
            runCommand(jewelDetector);
            robot.addToTelemetry("Red is Left", redIsLeft.get());
        }
    }
}
