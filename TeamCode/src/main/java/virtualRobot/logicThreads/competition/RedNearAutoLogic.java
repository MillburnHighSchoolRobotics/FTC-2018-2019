package virtualRobot.logicThreads.competition;

import android.util.Pair;

import java.util.Arrays;

import virtualRobot.commands.EthanClass;
import virtualRobot.commands.GetVuMarkSide;
import virtualRobot.commands.MoveMotor;
import virtualRobot.commands.Rotate;
import virtualRobot.commands.SpawnNewThread;
import virtualRobot.commands.Translate;
import virtualRobot.logicThreads.AutonomousLogicThread;
import virtualRobot.logicThreads.competition.utilities.StackingLogic;
import virtualRobot.monitorThreads.TimeMonitor;

import static virtualRobot.logicThreads.competition.utilities.StackingLogic.GlyphColor.BROWN;
import static virtualRobot.logicThreads.competition.utilities.StackingLogic.GlyphColor.GREY;
import static virtualRobot.logicThreads.competition.utilities.StackingLogic.GlyphColor.UNKNOWN;

/**
 * Created by Ethan Mak on 8/29/2017.
 */
public class RedNearAutoLogic extends AutonomousLogicThread {
    final StackingLogic.GlyphColor[][] snake = {{GREY, GREY, BROWN, BROWN},
                                                {GREY, BROWN, BROWN, GREY},
                                                {BROWN, BROWN, GREY, GREY}};
    final StackingLogic.GlyphColor[][] frog = {{BROWN, GREY, GREY, BROWN},
                                                {GREY, BROWN, BROWN, GREY},
                                                {BROWN, GREY, GREY, BROWN}};
    final StackingLogic.GlyphColor[][] bird = {{BROWN, GREY, BROWN, GREY},
                                                {GREY, BROWN, GREY, BROWN},
                                                {BROWN, GREY, BROWN, GREY}};

    boolean isFlipped = false;
    boolean usedVuMark = false;

    private StackingLogic.GlyphColor[][] pattern;



    @Override
    protected void addPresets() {
        delegateMonitor(new TimeMonitor(25000));
    }

    @Override
    protected void realRun() throws InterruptedException {}

    private Pair<Integer, Integer> getIndex(StackingLogic.GlyphColor top, StackingLogic.GlyphColor bottom) {
        for (int i = 0; i < 3; i++){
            for (int j = 0; j < 3; j++) {
                if (pattern[i][j] == UNKNOWN) {
                    if (pattern[i][j] == bottom && pattern[i][j + 1] == top)
                        return new Pair<>(i, j);
                    else
                        break;
                }
            }
        }
        return null;
    }
}
