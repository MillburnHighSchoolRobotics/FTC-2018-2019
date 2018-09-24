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
    protected void realRun() throws InterruptedException {
        StackingLogic.GlyphColor[][] cryptobox = new StackingLogic.GlyphColor[3][4];

        for (StackingLogic.GlyphColor[] x : cryptobox) {
            Arrays.fill(x, StackingLogic.GlyphColor.UNKNOWN);
        }

        StackingLogic stackingLogic = new StackingLogic();
        runCommand(new SpawnNewThread(stackingLogic));

        runCommand(new Translate(50, Translate.Direction.FORWARD, 0));

        runCommand(new EthanClass());

        runCommand(new Rotate(70));

        runCommand(new GetVuMarkSide());

        runCommand(new Rotate(20));

        if(redIsLeft.get()) {
            robot.getJewelServo().setPosition(1);
            runCommand(new Rotate(-90));
        }
        else{
            robot.getJewelServo().setPosition(0);
            //runCommand(new Translate(100, Translate.Direction.RIGHT, 0, .5));
            runCommand(new Rotate(90));
        }

//        robot.getRollerLeft().setPower(1);
//        robot.getRollerRight().setSpeed(1);

        runCommand(new Translate(2000, Translate.Direction.BACKWARD,0));

        runCommand(new Rotate(30));

        runCommand(new Translate(2000, Translate.Direction.FORWARD, 0));

        runCommand(new Rotate(30));

        while(stackingLogic.getTopGlyph() == StackingLogic.GlyphColor.STACKING) {
            Thread.sleep(1);
        }

        stackingLogic.pause();
        int index = 1;
        switch (currentVuMark) {
            case LEFT:
                index = 0;
                break;
            case CENTER:
                index = 1;
                break;
            case RIGHT:
                index = 2;
                break;
        }
        int vuMarkIndex = index;
        if (!getPattern(stackingLogic.getTopGlyph(), stackingLogic.getGlyph(0),index)) {
            pattern = snake;
            index = 2;
            isFlipped = stackingLogic.getTopGlyph() == BROWN;
            usedVuMark = false;
        } else {
            usedVuMark = true;
        }

        if (index != 1)
            runCommand(new Translate(75, index == 2 ? Translate.Direction.RIGHT : Translate.Direction.LEFT, 0));

//        depositGlyph();
        cryptobox[index][0] = stackingLogic.getGlyph(0);
        cryptobox[index][1] = stackingLogic.getGlyph(1);
        stackingLogic.resumeThread();

        Pair<Integer, Integer> indexData;
        for (int i = 0; getMonitorData(TimeMonitor.class); i++) {
            runCommand(new Rotate(-30));
//            robot.getRollerLeft().setPower(1);
//            robot.getRollerRight().setSpeed(1);
            runCommand(new Translate(2000 + i*300, Translate.Direction.BACKWARD,0));
            runCommand(new Translate(2000 + i*300, Translate.Direction.FORWARD, 0));

            runCommand(new Rotate(30));

            while(stackingLogic.getTopGlyph() == StackingLogic.GlyphColor.STACKING) {
                Thread.sleep(1);
            }

            stackingLogic.pause();

            if (usedVuMark) {
                indexData = getIndex(stackingLogic.getTopGlyph(), stackingLogic.getGlyph(0));
            } else {
                if (stackingLogic.getGlyph(0) == pattern[vuMarkIndex][0].getRealColor(isFlipped) && stackingLogic.getGlyph(1) == pattern[vuMarkIndex][1].getRealColor(isFlipped)) {
                    indexData = new Pair<>(vuMarkIndex, 0);
                } else {
                    indexData = getIndex(stackingLogic.getTopGlyph(), stackingLogic.getGlyph(0));
                }
            }

//            runCommand(new MoveMotor(robot.getGlyphLift(), 1, indexData.second * 500, Translate.RunMode.WITH_PID, true));

            if (indexData.first != 1)
                runCommand(new Translate(75, indexData.first == 2 ? Translate.Direction.RIGHT : Translate.Direction.LEFT, 0));

//            depositGlyph();

            //runCommand(new MoveMotor(robot.getGlyphLift(), 1, indexData.second * -500, Translate.RunMode.WITH_PID, true));

            if (indexData.first != 1)
                runCommand(new Translate(75, indexData.first == 2 ? Translate.Direction.LEFT : Translate.Direction.RIGHT, 0));

            if (Thread.currentThread().isInterrupted())
                break;
        }
    }

    private boolean getPattern(StackingLogic.GlyphColor top, StackingLogic.GlyphColor bottom, int index) {
        if (top == snake[index][1] && bottom == snake[index][1]) {
            pattern = snake;
            return true;
        }
        if (top == bird[index][1] && bottom == bird[index][1]) {
            pattern = bird;
            return true;
        }
        if (top == frog[index][1] && bottom == frog[index][1]) {
            pattern = frog;
            return true;
        }

        if (top == snake[index][1].getRealColor(true) && bottom == snake[index][1].getRealColor(true)) {
            pattern = snake;
            isFlipped = true;
            return true;
        }
        if (top == bird[index][1].getRealColor(true) && bottom == bird[index][1].getRealColor(true)) {
            pattern = bird;
            isFlipped = true;
            return true;
        }
        if (top == frog[index][1].getRealColor(true) && bottom == frog[index][1].getRealColor(true)) {
            pattern = frog;
            isFlipped = true;
            return true;
        }
        return false;
    }

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
