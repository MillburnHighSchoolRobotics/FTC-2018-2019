package virtualRobot.commands;

import android.graphics.Bitmap;
import android.graphics.Color;
import virtualRobot.utils.BetterLog;

import java.util.concurrent.atomic.AtomicBoolean;

import virtualRobot.Condition;
import virtualRobot.SallyJoeBot;
import virtualRobot.exceptions.CameraException;
import virtualRobot.VuforiaLocalizerImplSubclass;
import virtualRobot.utils.Vector2i;

/**
 * Created by ethachu19 on 2/17/17.
 *
 * Do not use, use EthanClass instead
 */

@Deprecated
public class fastRedIsLeft extends Command {
    public enum Mode {
        MIDSPLIT, TWORECTANGLES
    }

    AtomicBoolean redIsLeft;
    private final static boolean flipUpsideDown = true;
    public final static Mode currentMode = Mode.MIDSPLIT;
    public volatile static double startXPercent = 0;
    public volatile static double endXPercent = 1;
    public volatile static double startYPercent = 0.135;
    public volatile static double endYPercent = 1;

    public volatile static double start1XPercent = 0;
    public volatile static double end1XPercent = .2;

    public volatile static double start1YPercent = 0;
    public volatile static double end1YPercent = 1;

    public volatile static double start2XPercent = 0.4;
    public volatile static double end2XPercent = 1;

    public volatile static double start2YPercent = 0;
    public volatile static double end2YPercent = 1;

    SallyJoeBot robot = Command.ROBOT;
    VuforiaLocalizerImplSubclass vuforia;
    Condition condition = new Condition() {
        @Override
        public boolean isConditionMet() {
            return false;
        }
    };
    private int whiteTape = 13;
    public final static double BLUETHRESHOLD = 0.7; //.65
    public final static double REDTHRESHOLD = 1.43;
    public final static double LINETHRESHOLD = 0.62;
    public final static double FIRST_LINE_TARGET = .475;
    public final static double SECOND_LINE_TARGET = .475;
    public double timeLimit = -1;
    public fastRedIsLeft(AtomicBoolean redIsLeft, VuforiaLocalizerImplSubclass vuforia) {
        this.redIsLeft = redIsLeft;
        this.vuforia = vuforia;
    }

    @Override
    public boolean changeRobotState() throws InterruptedException {
        robot.getLFMotor().clearEncoder();
        robot.getRFMotor().clearEncoder();
        robot.getLBMotor().clearEncoder();
        robot.getRBMotor().clearEncoder();
        int width = vuforia.rgb.getWidth(), height = vuforia.rgb.getHeight();
        Vector2i start1;
        Vector2i end1;
        Vector2i start2;
        Vector2i end2;

        Bitmap bm = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
        int coF = 12;
        BetterLog.d("width/height", width + " " + height);
        start2 = new Vector2i((int) (startXPercent * width), (int) (startYPercent * height));
        end1 = new Vector2i((int) (endXPercent * width), (int) (endYPercent * height));
        end2 = new Vector2i((start2.x + end1.x) / 2, end1.y);
        start1 = new Vector2i(end2.x, start2.y);
        double power, curr = 0;
        int covered;
        double currLeft = 0;
        double currRight = 0;
        double red;
        double blue;
        boolean isInterrupted = false;
        double adjustedPower = 0;
        bm.copyPixelsFromBuffer(vuforia.rgb.getPixels());
        long start = System.currentTimeMillis();
        Vector2i currentPos,currentPos2;
        curr = 0;
        bm.copyPixelsFromBuffer(vuforia.rgb.getPixels());
        currentPos = new Vector2i(start1);
        currentPos2 = new Vector2i(start1.x, end1.y-1);
        Vector2i slope1, slope2;
        if (vuforia.rgb.getHeight() > vuforia.rgb.getWidth()) {
            slope1 = new Vector2i(coF, closestToFrac(((double) (end1.y - start1.y)) / (end1.x - start1.x), coF));
            slope2 = new Vector2i(coF, closestToFrac(((double) (end2.y - start2.y)) / (end2.x - start2.x), coF));
        } else {
            slope1 = new Vector2i(coF, closestToFrac(((double) (end1.y - start1.y)) / (end1.x - start1.x), coF));
            slope2 = new Vector2i(coF, closestToFrac(((double) (end2.y - start2.y)) / (end2.x - start2.x), coF));
        }
        double leftCovered;
        double rightCovered;
        for (leftCovered = 0; (currentPos.x < end1.x && currentPos.y < end1.y) || (currentPos2.x < end1.x && currentPos2.y > start1.y); ) {
            if (currentPos.x < end1.x && currentPos.y < end1.y) {
                red = Color.red(bm.getPixel(currentPos.x, currentPos.y));
                blue = Color.blue(bm.getPixel(currentPos.x, currentPos.y));
                if (blue != 0) {
                    BetterLog.d("Debug", red + " " + blue + " " + currentPos.toString());

                    currLeft += red / blue;
                    leftCovered++;
                }
            }
            if (currentPos2.x < end1.x && currentPos2.y > start1.y) {
                red = Color.red(bm.getPixel(currentPos2.x, currentPos2.y));
                blue = Color.blue(bm.getPixel(currentPos2.x, currentPos2.y));
                if (blue != 0) {
                    BetterLog.d("Debug", red + " " + blue + " " + currentPos2.toString());

                    currLeft += red / blue;
                    leftCovered++;
                }
            }
            currentPos.x += slope1.x;
            currentPos.y += slope1.y;
            currentPos2.x += slope1.x;
            currentPos2.y -= slope1.y;
            BetterLog.d("CURRPOS","LEFT: " + currentPos.toString() + " " + currentPos2.toString() + " " + currLeft);
        }
        if (leftCovered == 0)
            throw new CameraException("Failed to get appropriate data from left side"); //TODO: Implement
        currLeft /= leftCovered;
        robot.addToTelemetry("currLEFT: ", currLeft);
        currentPos = new Vector2i(start2);
        currentPos2 = new Vector2i(start2.x, end2.y-1);
        for (rightCovered = 0; (currentPos.x < end2.x && currentPos.y < end2.y) || (currentPos2.x < end2.x && currentPos2.y > start2.y); ) {
            if (currentPos.x < end1.x && currentPos.y < end2.y) {
                red = Color.red(bm.getPixel(currentPos.x, currentPos.y));
                blue = Color.blue(bm.getPixel(currentPos.x, currentPos.y));
                if (blue != 0) {
                    BetterLog.d("Debug", red + " " + blue + " " + currentPos.toString());

                    currRight += red / blue;
                    rightCovered++;
                }
            }
            if (currentPos2.x < end2.x && currentPos2.y > start2.y) {
                red = Color.red(bm.getPixel(currentPos2.x, currentPos2.y));
                blue = Color.blue(bm.getPixel(currentPos2.x, currentPos2.y));
                if (blue != 0) {
                    BetterLog.d("Debug", red + " " + blue + " " + currentPos2.toString());

                    currRight += red / blue;
                    rightCovered++;
                }
            }
            currentPos.x += slope2.x;
            currentPos.y += slope2.y;
            currentPos2.x += slope2.x;
            currentPos2.y -= slope2.y;
            BetterLog.d("CURRPOS","RIGHT: " + currentPos.toString() + " " + currentPos2.toString());
        }
        if (rightCovered == 0)
            throw new CameraException("Failed to get appropriate data from right side"); //TODO: Implement
        currRight /= rightCovered;
        robot.addToTelemetry("currRIGHT: ", currRight);
        //  BetterLog.d("currRIGHT", String.valueOf(currRight));
        robot.addToProgress("currLEFT, currRIGHT: " + currLeft + " " + currRight);
        if (currLeft > currRight) {
            redIsLeft.set(true);
        } else {
            redIsLeft.set(false);
        }
        return isInterrupted;

    }
    private int closestToFrac(double num, double frac) {
        int res = -1;
        double leastDist = Double.MAX_VALUE;
        for (int i = 0; true; i++) {
            if (Math.abs(i/frac - num) < leastDist) {
                res = i;
                leastDist = Math.abs(i/frac - num);
            } else {
                break;
            }
        }
        return res;
    }

    public void stall() {
        while(!Thread.currentThread().isInterrupted()){}
    }
}
