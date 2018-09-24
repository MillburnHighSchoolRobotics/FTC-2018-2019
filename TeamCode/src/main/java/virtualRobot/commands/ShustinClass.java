package virtualRobot.commands;

import android.graphics.Bitmap;
import android.util.Log;

import org.firstinspires.ftc.robotcore.external.navigation.RelicRecoveryVuMark;
import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

import java.io.IOException;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;

import retrofit2.Call;
import virtualRobot.VuforiaLocalizerImplSubclass;
import virtualRobot.logicThreads.AutonomousLogicThread;
import virtualRobot.utils.BetterLog;
import virtualRobot.utils.GlobalUtils;

/**
 * Created by Ethan Mak on 2/16/2018.
 */

public class ShustinClass extends Command {
    public ShustinClass(RelicRecoveryVuMark vumark) {
        this.vumark = vumark;
        this.offset = new AtomicInteger();
    }

    public ShustinClass(RelicRecoveryVuMark vumark, AtomicInteger offset) {
        this(vumark);
        this.offset = offset;
    }

    RelicRecoveryVuMark vumark;

    private int width, height;
    private boolean inAuton = true;
    AtomicInteger offset;

    private VuforiaLocalizerImplSubclass vuforiaInstance;
    AutonomousLogicThread currentThread;
//    static {
//        if(!OpenCVLoader.initDebug()){
//            BetterLog.d("OpenCV", "OpenCV not loaded");
//        } else {
//            BetterLog.d("OpenCV", "OpenCV loaded");
//        }
//    }

    //These are some hardcoded values, tuned for blue
    final int hue = 86; //107
    final int sat = 120; //143
    final int val = 12; //84
    final int del = 150; //128
    final double tolerance = 0.4;
    //The relic arm stand gets in the way, so I just ignore it lol
    final int widthOfStupidBarThatsInTheWay = 100; //TODO: tune this stupid value

    @Override
    public boolean changeRobotState() throws InterruptedException {
        if (parentThread instanceof AutonomousLogicThread) {
            currentThread = (AutonomousLogicThread) parentThread;
        } else {
//            throw new RuntimeException("Was not called in Autonomous!");
            inAuton = false;
        }
        robot.initCTelemetry();
        vuforiaInstance = GlobalUtils.vuforiaInstance;
        width = vuforiaInstance.rgb.getWidth();
        height = vuforiaInstance.rgb.getHeight();
        int target; // The target column. 0 = LEFT, 1 = CENTER, 2 = RIGHT
        switch (vumark) {
            case LEFT:
                target = 0;
                break;
            case CENTER:
                target = 1;
                break;
            case RIGHT:
                target = 2;
                break;
            default:
                target = 0;
                break;
        }
//        robot.addToTelemetry("Target", target);
        int cutoff = 40; //cutoff constant.  lines that are closer than this value to another vertical line are ignored. you'll see what that means later

        //Start CV
        int runtimes = 0;
//        while (runtimes <2) {
//            robot.stopMotors();
//        robot.addToTelemetry("Cutoff", cutoff);
        Mat img = new Mat();
        Bitmap bm = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
        bm.copyPixelsFromBuffer(vuforiaInstance.rgb.getPixels());
        Utils.bitmapToMat(bm, img); //get the pic
        Imgproc.resize(img, img, new Size(0, 0), 0.3, 0.3, Imgproc.INTER_LINEAR); //resize the image so that hough lines goes faster.  you might need to lower the amount of downscaling for reasons explained later, but for now the image is reduced by 30% on each dimension
        Mat hsv = new Mat();
        Imgproc.GaussianBlur(img, hsv, new Size(5, 5), 0); //blur the image
        Imgproc.cvtColor(hsv, hsv, Imgproc.COLOR_RGB2HSV); //RGB -> HSV
        Mat element = Imgproc.getStructuringElement(Imgproc.CV_SHAPE_RECT, new Size(3, 10)); //This is a "structuring element" that is shaped like a tall rectangle.
        Mat inrange = new Mat();
        Core.inRange(hsv, new Scalar(hue, sat, val), new Scalar(hue + del, sat + del, val + del), inrange); //you know what inrange is lmao
        Imgproc.morphologyEx(inrange, inrange, Imgproc.MORPH_CLOSE, element); //this basically closes the gaps between the segments of the cryptobox at far distances. basically, it dilates and then erodes the inranged image.  a dilation means that for each pixel, imagine the structuring element (a tall rectangle) - if there's a pixel of high value in there, the whole bit takes a high value. otherwise the whole bit takes a low value.  erosion is the opposite. the result is that small holes are closed (like the ones between cryptobox segments) but not large ones (like the ones between cryptobox columns) - that's why it's a tall rectangle, so that only vertical distances are closed and not horizontal ones
//            Imgproc.erode(inrange, inrange, element);
        robot.safeCall(robot.getCTelemetry().sendImage("InRange", inrange), false);
        Mat lines = new Mat();
        long timestamp = System.currentTimeMillis();
        Imgproc.HoughLines(inrange, lines, 1, Math.PI / 180, 75); //look at the hough lines documentation - the only things you should really change are the 3rd and last parameter. the last parameter is how hard it is to find a line - the higher it is, the more confident it has to be in the line. the 3rd is the resolution of the distance from the center that the line is. you might want to lower the resolution and change the cutoff to a double/making it smaller OR making the image bigger. when the bot gets further away than the blue line from the cryptobox, the columns start to be less than 40 pixels apart, meaning that it starts ignoring them. the point is, if you want the CV to work from further away, either make the image bigger or the resolution + cutoff smaller.
        Log.d("Hough Complete", Long.toString(System.currentTimeMillis() - timestamp) + "ms");
        Log.d("Lines", lines.toString());
        int positions[] = new int[4];
        int lineCount = 0;
//            Log.d("Columns", Integer.toString(lines.cols()));
        for (int i = 0; i < lines.rows() && lineCount < 4; i++) { //for each line detected and while the amount of accepted lines is less than 4
            double[] currentLine = lines.get(i, 0); //get a detected line
            if (currentLine[1] > tolerance && currentLine[1] < (Math.PI - tolerance))
                continue; //check if it's vertical enough. currentLine[1] is theta - the angle of the line perpendicular to the detected line, and running from the detected line to the center.
            boolean interferes = false;
            for (int j = 0; j < lineCount; j++) {
                int dist = (int) Math.abs(Math.abs(currentLine[0]) - positions[j]); //loop through each already accepted line and find the distance between the current line and the accepted one. currentLine[0] is rho - the distance from the center to the detected line
                if (dist < cutoff) {
                    interferes = true; //if it's too close to an already accepted line, then it interferes.
                    break;
                }
            }
            if (!interferes) {
                positions[lineCount] = (int) Math.abs(currentLine[0]); //if it doesnt interfere then add it to the accepted lines
                lineCount++; //notice that because of the for loop condition, there will never be more than 4 lines... but there can be less than 4!
            }
        }
        Arrays.sort(positions, 0, lineCount); //sort the positions from leftmost to right most
        int avgSpace = 0;
//            int avgPos = 0;
        for (int i = 0; i < lineCount; i++) {
//                avgPos += positions[i];
            if (i < lineCount - 1) avgSpace += positions[i + 1] - positions[i];
        }
//            if (lineCount > 0) avgPos /= lineCount;
//            else avgPos = 0;
        if (lineCount > 1) avgSpace /= (lineCount - 1);
        else avgSpace = 0; //simple enough calculation of the average space between columns
        boolean safe = true; //this variable is god, it basically says whether the program could fill in the positions of the rest of the cryptobox. the way i extrapolate the off-screen cryptobox positions is done the way it is because i ran out of time and didnt implement a way for it to track the position of the cryptobox over time and predict movement. actually that would no longer work now that this would be a command. i guess this is the best way to do it. it should work - usually. if not, we can use the bot's previous movements to guess at which one is off-screen. there are few situations where it's unsafe though. also, random note that i cant explain right now bc im running out of time, up there ^^^^ in the hough function, i would recommend you reduce the rho resolution. (3rd parameter)
        if (lineCount > 1) {
            //this should be fairly self-explanatory, not going to comment it
            if (lineCount == 3) {
                boolean leftOff = positions[0] < avgSpace;
                boolean rightOff = positions[2] > (img.cols() - avgSpace) - widthOfStupidBarThatsInTheWay;
                if (leftOff && rightOff) {
                    safe = false;
                } else if (leftOff) {
                    positions[3] = positions[2];
                    positions[2] = positions[1];
                    positions[1] = positions[0];

                    positions[0] = positions[1] - avgSpace;
                } else if (rightOff) {
                    positions[3] = positions[2] + avgSpace;
                } else safe = false;
            } else if (lineCount == 2) {
                boolean leftOff = positions[0] < avgSpace;
                boolean rightOff = positions[1] > (img.cols() - avgSpace) - widthOfStupidBarThatsInTheWay;
                if (leftOff && rightOff) {
                    positions[2] = positions[1];
                    positions[1] = positions[0];

                    positions[3] = positions[2] + avgSpace;
                    positions[0] = positions[1] - avgSpace;
                } else if (leftOff && !rightOff) {
                    positions[3] = positions[1];
                    positions[2] = positions[0];

                    positions[1] = positions[2] - avgSpace;
                    positions[0] = positions[1] - avgSpace;
                } else if (!leftOff && rightOff) {
                    positions[2] = positions[1] + avgSpace;
                    positions[3] = positions[2] + avgSpace;
                } else safe = false;
            } else if (lineCount == 1) safe = false;
        }
//            THIS NEXT PART IS ACTUAL <BLACK MAGIC WIZARDRY> INVENTED BY THE OPENCV TEAM TO PERPETUATE THE NEW WORLD ORDER
//            IDK HOW IT WORKS BUT IT DOES, IT RENDERS LINES IT DETECTS, SO THAT IT CAN SEND TO CTELEMETRY
//            NO TOUCHY PLS
//========================================================================================
        Mat hough = new Mat();
        Imgproc.cvtColor(img, hough, Imgproc.COLOR_RGB2BGR);
        if (safe) {
            for (int i = 0; i < 4; i++) {
                double rho = positions[i], theta = 0; //lmao
                if (rho < 0 || rho > img.cols()) continue;
//            cout << rho << "\t";
//            if (i != lineCount - 1) cout << positions[i + 1] - positions[i] << '\t';
//            if (i != lineCount - 1) avgSpace += (positions[i + 1] - positions[i]);
                Point pt1 = new Point(), pt2 = new Point();
                double a = Math.cos(theta), b = Math.sin(theta);
                double x0 = a * rho, y0 = b * rho;
                pt1.x = (int) (x0 + 2000 * (-b));
                pt1.y = (int) (y0 + 2000 * (a));
                pt2.x = (int) (x0 - 1000 * (-b));
                pt2.y = (int) (y0 - 1000 * (a));
                //Target lines are green, others are red;
                Imgproc.line(hough, pt1, pt2, new Scalar(0, i == target || i == target + 1 ? 255 : 0, i == target || i == target + 1 ? 0 : 255), 3, Core.LINE_AA, 0);
            }
//            </BLACK MAGIC WIZARDRY>
            robot.safeCall(robot.getCTelemetry().sendImage("Hough", hough), false);

        }
//        robot.addToTelemetry("Positions", Arrays.toString(positions));
//            Log.d("Safe", (avgSpace != 0 && safe + "");
        if (avgSpace != 0 && safe) { // ok now here i calculate the offset and stuff
//                cutoff = avgSpace - 10;
            int left, right;
            left = positions[target];
            right = positions[target + 1]; //find the left and right target lines
//                if (left >= 0 && right >= 0) {
            int avg = (left + right) / 2; // find the center between them
//                avg = avg + (int)(avgSpace * 1.5);
            int offset = avg - (img.cols() / 2); //negative is left, positive is right - distance from the center
            offset -= (int) (avgSpace * 1.5); //take into account the offset of the phone

//            robot.addToTelemetry("Offset", offset);
            if (inAuton) {
                currentThread.offset.set(offset); //kms,l
            } else
                this.offset.set(offset);

//            robot.addToTelemetry("Avg Space", avgSpace);
//                strafe(0.5 * Math.signum(offset)); //idk why this strafe function works but it does - what you need to do with this command is instead of strafing after this is done, set a global offset variable or something idk
//            img.release();
//            hsv.release();
//            inrange.release();
//            lines.release();
//            hough.release();
//                }
//                Thread.sleep(200);
//========================================================================================
//      HOPE THAT MADE SENSE LMAO, HAVE FUN WARREN (if you want you can wait for me)
//========================================================================================
//            } else {
//                robot.stopMotors();
        }
//            runtimes++;
//        }
        return false;
    }

    private void strafe(double power) { //positive is left, negative is right
        Log.d("Strafe", "triggered");
        robot.getLFMotor().setPower(-power);
        robot.getLBMotor().setPower(power);
        robot.getRFMotor().setPower(power);
        robot.getRBMotor().setPower(-power);
    }
}
