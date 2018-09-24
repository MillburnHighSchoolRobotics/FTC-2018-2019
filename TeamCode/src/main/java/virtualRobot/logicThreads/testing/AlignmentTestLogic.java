package virtualRobot.logicThreads.testing;

import android.graphics.Bitmap;
import android.util.Log;

import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

import java.util.Arrays;

import virtualRobot.LogicThread;
import virtualRobot.VuforiaLocalizerImplSubclass;
import virtualRobot.utils.BetterLog;
import virtualRobot.utils.GlobalUtils;

/**
 * Created by david on 2/12/18.
 */

public class AlignmentTestLogic extends LogicThread {
    private int width, height;

    private VuforiaLocalizerImplSubclass vuforiaInstance;

    final int hue = 86; //107
    final int sat = 120; //143
    final int val = 12; //84
    final int del = 150; //128
    final double tolerance = 0.4;
    final int widthOfStupidBarThatsInTheWay = 100; //TODO: tune this stupid value

    @Override
    protected void realRun() throws InterruptedException {
//        robot.initCTelemetry();
        vuforiaInstance = GlobalUtils.vuforiaInstance;
        width = vuforiaInstance.rgb.getWidth();
        height = vuforiaInstance.rgb.getHeight();
        int target = (int)(Math.random() * 3);
        robot.addToTelemetry("Target", target);
        int cutoff = 40;
        //Start CV
        while (true) {
//            robot.stopMotors();
            robot.addToTelemetry("Cutoff", cutoff);
            Mat img = new Mat();
            Bitmap bm = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
            bm.copyPixelsFromBuffer(vuforiaInstance.rgb.getPixels());
            Utils.bitmapToMat(bm, img);
            Imgproc.resize(img, img, new Size(0,0), 0.3, 0.3, Imgproc.INTER_LINEAR);
            Mat hsv = new Mat();
            Imgproc.GaussianBlur(img, hsv, new Size(5, 5), 0);
            Imgproc.cvtColor(hsv, hsv, Imgproc.COLOR_RGB2HSV);
            Mat element = Imgproc.getStructuringElement(Imgproc.CV_SHAPE_RECT, new Size(3, 10));
            Mat inrange = new Mat();
            Core.inRange(hsv, new Scalar(hue, sat, val), new Scalar(hue + del, sat + del, val + del), inrange);
            Imgproc.morphologyEx(inrange, inrange, Imgproc.MORPH_CLOSE, element);
//            Imgproc.erode(inrange, inrange, element);
//            try {
//                robot.getCTelemetry().sendImage("InRange", inrange).execute();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
            Mat lines = new Mat();
            long timestamp = System.currentTimeMillis();
            Imgproc.HoughLines(inrange, lines, 1, Math.PI/180, 200/2);
            Log.d("Hough Complete", Long.toString(System.currentTimeMillis() - timestamp) + "ms");
            Log.d("Lines", lines.toString());
            int positions[] = new int[4];
            int lineCount = 0;
//            Log.d("Columns", Integer.toString(lines.cols()));
            for (int i = 0; i < lines.rows() && lineCount < 4; i++) {
                double[] currentLine = lines.get(i, 0);
                if (currentLine[1] > tolerance && currentLine[1] < (Math.PI - tolerance)) continue;
                boolean interferes = false;
                for (int j = 0; j < lineCount; j++) {
                    int dist = (int)Math.abs(Math.abs(currentLine[0]) - positions[j]);
                    if (dist < cutoff) {
                        interferes = true;
                        break;
                    }
                }
                if (!interferes) {
                    positions[lineCount] = (int)Math.abs(currentLine[0]);
                    lineCount++;
                }
            }
            Arrays.sort(positions, 0, lineCount);
            int avgSpace = 0;
//            int avgPos = 0;
            for (int i = 0; i < lineCount; i++) {
//                avgPos += positions[i];
                if (i < lineCount - 1) avgSpace += positions[i + 1] - positions[i];
            }
//            if (lineCount > 0) avgPos /= lineCount;
//            else avgPos = 0;
            if (lineCount > 1) avgSpace /= (lineCount - 1);
            else avgSpace = 0;
            boolean safe = true;
            if (avgSpace != 0) {
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
//            Mat hough = new Mat();
//            Imgproc.cvtColor(img, hough, Imgproc.COLOR_RGB2BGR);
//            if (safe) {
//                for (int i = 0; i < 4; i++) {
//                    double rho = positions[i], theta = 0; //lmao
//                    if (rho < 0 || rho > img.cols()) continue;
////            cout << rho << "\t";
////            if (i != lineCount - 1) cout << positions[i + 1] - positions[i] << '\t';
////            if (i != lineCount - 1) avgSpace += (positions[i + 1] - positions[i]);
//                    Point pt1 = new Point(), pt2 = new Point();
//                    double a = Math.cos(theta), b = Math.sin(theta);
//                    double x0 = a * rho, y0 = b * rho;
//                    pt1.x = (int) (x0 + 2000 * (-b));
//                    pt1.y = (int) (y0 + 2000 * (a));
//                    pt2.x = (int) (x0 - 1000 * (-b));
//                    pt2.y = (int) (y0 - 1000 * (a));
//                    //Target lines are green, others are red;
//                    Imgproc.line(hough, pt1, pt2, new Scalar(0, i == target || i == target + 1 ? 255 : 0, i == target || i == target + 1 ? 0 : 255), 3, Core.LINE_AA, 0);
//                }
//                try {
//                    robot.getCTelemetry().sendImage("Hough", hough).execute();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
            robot.addToTelemetry("Positions", Arrays.toString(positions));
//            Log.d("Safe", (avgSpace != 0 && safe + "");
            if (avgSpace != 0 && safe) {
//                cutoff = avgSpace - 10;
                int left, right;
                left = positions[target];
                right = positions[target + 1];
//                if (left >= 0 && right >= 0) {
                int avg = (left + right) / 2;
//                avg = avg + (int)(avgSpace * 1.5);
                int offset = avg - (img.cols() / 2);
                offset -= (int)(avgSpace * 1.5);
                robot.addToTelemetry("Offset", offset);
                robot.addToTelemetry("Avg Space", avgSpace);
                strafe(0.5 * Math.signum(offset));
                img.release();
                hsv.release();
                inrange.release();
                lines.release();
//                hough.release();
//                }
//                Thread.sleep(200);
            } else {
                robot.stopMotors();
            }
        }
    }

    private void strafe(double power) { //positive is left, negative is right
        Log.d("Strafe", "triggered");
        robot.getLFMotor().setPower(-power);
        robot.getLBMotor().setPower(power);
        robot.getRFMotor().setPower(-power);
        robot.getRBMotor().setPower(power);
    }
}
