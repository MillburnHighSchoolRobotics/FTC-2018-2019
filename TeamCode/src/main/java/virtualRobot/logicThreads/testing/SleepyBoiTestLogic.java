package virtualRobot.logicThreads.testing;

import virtualRobot.LogicThread;
import virtualRobot.commands.PlayMusic;

/**
 * Created by david on 12/2/17
 * Tests thread sleeps on newbot
 */

@Deprecated
// *sad dab*
public class SleepyBoiTestLogic extends LogicThread {
    protected void realRun() throws InterruptedException {
        int meme = 0;
        int pepe = 0;
        int pepe2 = 1;
        while (true) {
            robot.addToTelemetry("Count", meme++);
            pepe = 1 - pepe;
            pepe2 = 1 - pepe2;
//            robot.getClawLeft().setPosition(pepe);
//            robot.getClawRight().setPosition(1 - pepe2);
            Thread.sleep(700);
            pepe = 1 - pepe;
            pepe2 = 1 - pepe2;
//            robot.getClawLeft().setPosition(pepe);
//            robot.getClawRight().setPosition(1 - pepe2);
            runCommand(new PlayMusic("thewheelsonthebus.mp3", false));
        }
    }
}
