package virtualRobot.logicThreads.competition.utilities;

import java.util.Arrays;

import javax.microedition.khronos.opengles.GL;

import virtualRobot.LogicThread;
import virtualRobot.commands.MoveMotor;
import virtualRobot.commands.Translate;

/**
 * Created by ethan on 9/23/17.
 */

public class StackingLogic extends LogicThread {
    private GlyphColor[] glyphsHeld = new GlyphColor[2];
    private int currentIndex = 0;

    @Override
    protected void realRun() throws InterruptedException {
        boolean isInterrupted = false;
        resetGlyphsHeld();
        while (!isInterrupted) {
            if (false && !isPaused.get()) { //See the block hit end of conveyor
                //Check for color
                currentIndex++;
                GlyphColor color = GlyphColor.UNKNOWN;
                glyphsHeld[currentIndex] = GlyphColor.STACKING;
//                robot.getRollerLeft().setPower(0);
//                robot.getRollerRight().setSpeed(0);
//                robot.moveClaw(false);
//                runCommand(new MoveMotor(robot.getLift(), 1, 2000, Translate.RunMode.WITH_PID, false));
//                robot.getRollerLeft().setPower(1);
//                robot.getRollerRight().setSpeed(1);
//                robot.moveClaw(true);
//                runCommand(new MoveMotor(robot.getLift(), 1, 0, Translate.RunMode.WITH_PID, false));
                glyphsHeld[currentIndex] = color;
//                robot.moveClaw(false);
            }
            if (Thread.currentThread().isInterrupted())
                break;
            Thread.sleep(5);
        }
    }

    public synchronized int getNumGlyphsHeld() {
        return glyphsHeld.length;
    }

    public synchronized GlyphColor getGlyph(int index) {
        return glyphsHeld[index];
    }

    public synchronized GlyphColor getTopGlyph() {
        return getGlyph(currentIndex);
    }

    public synchronized void resetGlyphsHeld() {
        Arrays.fill(glyphsHeld, GlyphColor.UNKNOWN);
        currentIndex = 0;
    }

    public synchronized void pause() {
        isPaused.set(true);
    }

    public synchronized void resumeThread() {
        isPaused.set(false);
    }

    public enum GlyphColor {
        UNKNOWN(10), BROWN(-1), GREY(1), STACKING(20);

        public GlyphColor getRealColor(boolean isFlipped) {
            int multiplier = isFlipped ? -1 : 1;
            for (GlyphColor color : GlyphColor.values()) {
                if (color.val == this.val * multiplier)
                    return color;
            }
            return this;
        }

        int val;
        GlyphColor(int val) {
            this.val = val;
        }
    }
}
