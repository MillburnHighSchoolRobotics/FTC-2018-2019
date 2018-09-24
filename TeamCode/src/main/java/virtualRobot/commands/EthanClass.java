package virtualRobot.commands;

import android.graphics.Bitmap;

import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

import virtualRobot.SallyJoeBot;
import virtualRobot.VuforiaLocalizerImplSubclass;
import virtualRobot.logicThreads.AutonomousLogicThread;
import virtualRobot.utils.BetterLog;
import virtualRobot.utils.GlobalUtils;

/**
 * Created by ethan on 9/22/17.
 */

public class EthanClass extends Command {
    VuforiaLocalizerImplSubclass vuforiaInstance;
    AutonomousLogicThread currentThread;

    private int width, height;
    private int kSize = 11;
    Scalar redUpper = new Scalar(255, 100, 100, 255);
    Scalar redLower = new Scalar(50, 0, 0, 0);
    Scalar blueUpper = new Scalar(50, 150, 255, 255);
    Scalar blueLower = new Scalar(0, 0, 50, 0);
    private static final SallyJoeBot robot = Command.ROBOT;
    static {
        if(!OpenCVLoader.initDebug()){
            BetterLog.d("OpenCV", "OpenCV not loaded");
        } else {
            BetterLog.d("OpenCV", "OpenCV loaded");
        }
    }
    public EthanClass() {
        vuforiaInstance = GlobalUtils.vuforiaInstance;
        width = vuforiaInstance.rgb.getBufferWidth();
        height = vuforiaInstance.rgb.getHeight();
        robot.initCTelemetry();
    }

    @Override
    public boolean changeRobotState() throws InterruptedException {
        int point = 0;
//        Log.d("EthanClass", Integer.toString(point++));
        if (parentThread instanceof AutonomousLogicThread)
            currentThread = ((AutonomousLogicThread) parentThread);
        else
            throw new RuntimeException("Was not called in Autonomous?");
//        Log.d("EthanClass", Integer.toString(point++));
        Mat img = new Mat();
        Bitmap bm = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
        bm.copyPixelsFromBuffer(vuforiaInstance.rgb.getPixels());
        Utils.bitmapToMat(bm, img);
        Mat imgbgr = new Mat();
        Imgproc.cvtColor(img, imgbgr, Imgproc.COLOR_RGB2BGR);
//        Log.d("EthanClass", Integer.toString(point++));
        try {
            robot.getCTelemetry().sendImage("Image", imgbgr).execute();
        } catch (IOException e) {
            robot.addToTelemetry("Image", e.getMessage());
        }
        imgbgr.release();
//        Log.d("EthanClass", Integer.toString(point++));
        Mat blur = new Mat();
        Imgproc.GaussianBlur(img, blur, new Size(kSize,kSize), 0);
        Mat red = new Mat();
        Core.inRange(img, redLower, redUpper, red);
//        Log.d("EthanClass", Integer.toString(point++));
        try {
            robot.getCTelemetry().sendImage("Red", red).execute();
        } catch (IOException e) {
            robot.addToTelemetry("RedImage", e.getMessage());
        }
//        Log.d("EthanClass", Integer.toString(point++));
        List<MatOfPoint> contours = new LinkedList<>();
        Imgproc.findContours(red.clone(), contours, new Mat(), Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE);
        Collections.sort(contours, new Comparator<MatOfPoint>() {
            @Override
            public int compare(MatOfPoint matOfPoint, MatOfPoint t1) {
                return (int) Math.signum(Imgproc.contourArea(matOfPoint) - Imgproc.contourArea(t1));
            }
        });
//        Log.d("EthanClass", Integer.toString(point++));
        if (contours.size() <= 0) {
            currentThread.redIsLeft.set(false);
            return Thread.currentThread().isInterrupted();
        }
        Point centerRed = new Point();
        float[] radiusRed = new float[1];
        Imgproc.minEnclosingCircle(new MatOfPoint2f(contours.get(contours.size() - 1).toArray()),centerRed,radiusRed);
        Mat blue = new Mat();
        Core.inRange(img, blueLower, blueUpper, blue);
//        Log.d("EthanClass", Integer.toString(point++));
        try {
            robot.getCTelemetry().sendImage("Blue", blue).execute();
        } catch (IOException e) {
            robot.addToTelemetry("BlueImage", e.getMessage());
        }
//        Log.d("EthanClass", Integer.toString(point++));
        contours = new LinkedList<>();
        Imgproc.findContours(blue.clone(), contours, new Mat(), Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE);
        Collections.sort(contours, new Comparator<MatOfPoint>() {
            @Override
            public int compare(MatOfPoint matOfPoint, MatOfPoint t1) {
                return (int) Math.signum(Imgproc.contourArea(matOfPoint) - Imgproc.contourArea(t1));
            }
        });
//        Log.d("EthanClass", Integer.toString(point++));
        if (contours.size() <= 0) {
            currentThread.redIsLeft.set(false);
            return Thread.currentThread().isInterrupted();
        }
        Point centerBlue = new Point();
        float[] radiusBlue = new float[1];
        Imgproc.minEnclosingCircle(new MatOfPoint2f(contours.get(contours.size() - 1).toArray()),centerBlue,radiusBlue);
        currentThread.redIsLeft.set(centerRed.x < centerBlue.x);
//        Log.d("EthanClass", Integer.toString(point++));
        return Thread.currentThread().isInterrupted();
    }
}
