package virtualRobot.logicThreads.testing;

import virtualRobot.LogicThread;
import virtualRobot.commands.Pause;
import virtualRobot.commands.Rotate90;

/**
 * Created by david on 1/16/18.
 */

public class RotateTesterLogic extends LogicThread {
    @Override
    protected void realRun() throws InterruptedException {
        int meme = 1;
        while (true) {
            runCommand(new Pause(2000));
            runCommand(new Rotate90(meme));
            meme *= -1;
        }
    }
}
