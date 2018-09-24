package org.firstinspires.ftc.teamcode.TestingOpModes;

import android.graphics.Bitmap;
import android.util.Log;

import retrofit2.Retrofit;
import virtualRobot.utils.BetterLog;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
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
import org.opencv.videoio.VideoCapture;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

import virtualRobot.VuforiaLocalizerImplSubclass;
import virtualRobot.telemetry.CTelemetry;
import virtualRobot.telemetry.MatConverterFactory;

/**
 * Created by Ethan Mak on 8/31/2017.
 */
@Disabled
@Autonomous(name = "Testing: OpenCV Test", group = "Testing")
public class OpenCVTest extends LinearOpMode {
    ElapsedTime time = new ElapsedTime();
//    CameraDevice cap;
    VuforiaLocalizerImplSubclass vuforiaInstance;
    VideoCapture cap;
    private int width, height;
    Scalar redUpper = new Scalar(255, 100, 100, 255);
    Scalar redLower = new Scalar(50, 0, 0, 0);
    Scalar blueUpper = new Scalar(50, 150, 255, 255);
    Scalar blueLower = new Scalar(0, 0, 50, 0);

    static {
        if(!OpenCVLoader.initDebug()){
            BetterLog.d("OpenCV", "OpenCV not loaded");
        } else {
            BetterLog.d("OpenCV", "OpenCV loaded");
        }
    }

    @Override
    public void runOpMode() throws InterruptedException {
        CTelemetry ctel = new Retrofit.Builder()
                .baseUrl("http://172.20.95.207:8080/")
                .addConverterFactory(MatConverterFactory.create())
                .build()
                .create(CTelemetry.class);

        VuforiaLocalizer.Parameters params = new VuforiaLocalizer.Parameters(hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName()));
        params.cameraDirection = VuforiaLocalizer.CameraDirection.BACK;
        params.vuforiaLicenseKey = "AdVGalv/////AAAAGYhiDIdk+UI+ivt0Y7WGvUJnm5cKX/lWesW2pH7gnK3eOLTKThLekYSO1q65ttw7X1FvNhxxhdQl3McS+mzYjO+HkaFNJlHxltsI5+b4giqNQKWhyKjzbYbNw8aWarI5YCYUFnyiPPjH39/CbBzzFk3G2RWIzNB7cy4AYhjwYRKRiL3k33YvXv0ZHRzJRkMpnytgvdv5jEQyWa20DIkriC+ZBaj8dph8/akyYfyD1/U19vowknmzxef3ncefgOZoI9yrK82T4GBWazgWvZkIz7bPy/ApGiwnkVzp44gVGsCJCUFERiPVwfFa0SBLeCrQMrQaMDy3kOIVcWTotFn4m1ridgE5ZP/lvRzEC4/vcuV0";
        vuforiaInstance = new VuforiaLocalizerImplSubclass(params);

        waitForStart();

        width = vuforiaInstance.rgb.getBufferWidth();
        height = vuforiaInstance.rgb.getHeight();

        Bitmap bm = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);

        while (opModeIsActive()) {
            Mat img = new Mat();

            //vuforiaInstance.rgb.getPixels().array().length +
            bm.copyPixelsFromBuffer(vuforiaInstance.rgb.getPixels());
            Utils.bitmapToMat(bm, img);
            Mat bgrimg = new Mat();
            Imgproc.cvtColor(img, bgrimg, Imgproc.COLOR_RGB2BGR);
            try {
                ctel.sendImage("Image", bgrimg).execute();
            } catch (IOException err) {
                telemetry.addData("ImageError", err.getMessage());
            }
            bgrimg.release();
            telemetry.addData("Center: ", Arrays.toString(img.get(img.rows()/2, img.cols()/2)));
//            img.release();
//            telemetry.addData("Vuforia size: ", "" + " " + height + " " + width);
//
//            telemetry.addData("Mat Size: ", img.cols() + " " + img.rows());
//            Imgproc.cvtColor(img, img, Imgproc.COLOR_RGB2BGR);
//
////        if (cap.read(img)) {
////            throw new CameraException("Camera failed to get image");
////        }
////        telemetry.addData("IMG: ", img.cols());
            Mat blur = new Mat();
            Imgproc.GaussianBlur(img, blur, new Size(7,7), 0);
            img.release();
//
            Mat red = new Mat();
            // inRange - Checks if array elements lie between the elements of two other arrays
            // -- in this case the two other arrays are redLower and redUpper which will be determined experimentally
            Core.inRange(blur.clone(), redLower, redUpper, red);
            try {
                ctel.sendImage("Red", red).execute();
            } catch (IOException err) {
                telemetry.addData("RedError", err.getMessage());
            }
            List<MatOfPoint> redContours = new LinkedList<>();
            // findContours - Finds contours (essentially edges) of every physical object in 'red'
            // 'contours' List stores all detected contours as a vector of points
            // The new Mat object is an output vector
            // RETR_EXTERNAL is a mode which retrieves only the most external contours
            // CHAIN_APPROX_SIMPLE compresses horizontal, vertical, and diagonal segments and leaves only their end points ???
            Imgproc.findContours(red.clone(), redContours, new Mat(), Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE);
            red.release();

            // Sorts based on the signs (- or +) of the contourAreas of consecutive matOfPoint objects in the contours List
            Collections.sort(redContours, new Comparator<MatOfPoint>() {
                @Override
                public int compare(MatOfPoint matOfPoint, MatOfPoint t1) {
                    return (int) Math.signum(Imgproc.contourArea(matOfPoint) - Imgproc.contourArea(t1));
                }
            });

            if (redContours.size() <= 0) {
                logset("redIsLeft: ", "Not seen");
                telemetry.addData("redIsLeft: ", "Not Seen Red");
                telemetry.update();
                blur.release();
//                return;
                continue;
            }
            MatOfPoint2f cont = new MatOfPoint2f(redContours.get(redContours.size() - 1).toArray());
            Point centerRed = new Point();
            float[] radiusRed = new float[1];
            // minEnclosingCircle finds a circle of minimum area enclosing a 2D point set, in this case;
            Imgproc.minEnclosingCircle(cont,centerRed,radiusRed);
            cont.release();
            for (MatOfPoint mat : redContours) {
                mat.release();
            }

            if (radiusRed[0] < 150) {
                logset("redIsLeft: ", "Not seen");
                telemetry.addData("redIsLeft: ", "Red Too Small");
                telemetry.update();
                blur.release();
//                return;
                continue;
            }

            telemetry.addData("Circle Red ", centerRed.toString() + " " + radiusRed[0]);
            logset("Circle Red ", centerRed.toString() + " " + radiusRed[0]);

            Mat blue = new Mat();
            Core.inRange(blur.clone(), blueLower, blueUpper, blue);
            try {
                ctel.sendImage("Blue", blue).execute();
            } catch (IOException err) {
                telemetry.addData("BlueError", err.getMessage());
            }
            LinkedList<MatOfPoint> blueContours = new LinkedList<>();
            Imgproc.findContours(blue.clone(), blueContours, new Mat(), Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE);
            blue.release();
            blur.release();
            Collections.sort(blueContours, new Comparator<MatOfPoint>() {
                @Override
                public int compare(MatOfPoint matOfPoint, MatOfPoint t1) {
                    return (int) Math.signum(Imgproc.contourArea(matOfPoint) - Imgproc.contourArea(t1));
                }
            });
            if (blueContours.size() <= 0) {
                logset("redIsLeft: ", "Not seen");
                telemetry.addData("redIsLeft: ", "Not Seen Blue");
                telemetry.update();
//                return;
                continue;
            }
            cont = new MatOfPoint2f(blueContours.get(blueContours.size() - 1).toArray());
            Point centerBlue = new Point();
            float[] radiusBlue = new float[1];
            Imgproc.minEnclosingCircle(cont,centerBlue,radiusBlue);
            cont.release();
            for (MatOfPoint mat : blueContours) {
                mat.release();
            }

            if (radiusBlue[0] < 150) {
                logset("redIsLeft: ", "Not seen");
                telemetry.addData("redIsLeft: ", "Blue Too Small");
                telemetry.update();
//                blur.release();
//                return;
                continue;
            }

            telemetry.addData("Circle Blue ", centerBlue.toString() + " " + radiusBlue[0]);

            logset("redIsLeft: ", "" + (centerRed.x < centerBlue.x));
            telemetry.addData("redIsLeft: ", "" + (centerRed.x < centerBlue.x));
            telemetry.update();
        }
    }

    public void logset(String x, String y) {
        Log.d(x, y);
    }

//    @Override
//    public void init() {
//        VuforiaLocalizer.Parameters params = new VuforiaLocalizer.Parameters(hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName()));
//        params.cameraDirection = VuforiaLocalizer.CameraDirection.BACK;
//        params.vuforiaLicenseKey = "AdVGalv/////AAAAGYhiDIdk+UI+ivt0Y7WGvUJnm5cKX/lWesW2pH7gnK3eOLTKThLekYSO1q65ttw7X1FvNhxxhdQl3McS+mzYjO+HkaFNJlHxltsI5+b4giqNQKWhyKjzbYbNw8aWarI5YCYUFnyiPPjH39/CbBzzFk3G2RWIzNB7cy4AYhjwYRKRiL3k33YvXv0ZHRzJRkMpnytgvdv5jEQyWa20DIkriC+ZBaj8dph8/akyYfyD1/U19vowknmzxef3ncefgOZoI9yrK82T4GBWazgWvZkIz7bPy/ApGiwnkVzp44gVGsCJCUFERiPVwfFa0SBLeCrQMrQaMDy3kOIVcWTotFn4m1ridgE5ZP/lvRzEC4/vcuV0";
//        vuforiaInstance = new VuforiaLocalizerImplSubclass(params);
//        width = vuforiaInstance.rgb.getBufferWidth();
//        height = vuforiaInstance.rgb.getHeight();
////        cap = new VideoCapture(Videoio.CV_CAP_ANDROID_BACK);
////        if (!cap.isOpened())
////            throw new CameraException("Camera failed to open");
////        cap.set(Videoio.CAP_PROP_FRAME_WIDTH,320);
////        cap.set(Videoio.CAP_PROP_FRAME_HEIGHT,240);
//    }
//
//    @Override
//    public void loop() {
////        height, width, CV_8UC1
//        Mat img = new Mat(height, width, CV_8UC1);
//        img.put(0,0,vuforiaInstance.rgb.getPixels().array());
//        Imgproc.cvtColor(img, img, Imgproc.COLOR_RGB2BGR);
////        if (cap.read(img)) {
////            throw new CameraException("Camera failed to get image");
////        }
////        telemetry.addData("IMG: ", img.cols());
//        Mat blur = new Mat();
//        Imgproc.GaussianBlur(img, blur, new Size(7,7), 0);
//
//        Mat red = new Mat();
//        // inRange - Checks if array elements lie between the elements of two other arrays
//        // -- in this case the two other arrays are redLower and redUpper which will be determined experimentally
//        Core.inRange(img, redLower, redUpper, red);
//        List<MatOfPoint> contours = new LinkedList<>();
//        // findContours - Finds contours (essentially edges) of every physical object in 'red'
//        // 'contours' List stores all detected contours as a vector of points
//        // The new Mat object is an output vector
//        // RETR_EXTERNAL is a mode which retrieves only the most external contours
//        // CHAIN_APPROX_SIMPLE compresses horizontal, vertical, and diagonal segments and leaves only their end points ???
//        Imgproc.findContours(red.clone(), contours, new Mat(), Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE);
//
//        // Sorts based on the signs (- or +) of the contourAreas of consecutive matOfPoint objects in the contours List
//        Collections.sort(contours, new Comparator<MatOfPoint>() {
//            @Override
//            public int compare(MatOfPoint matOfPoint, MatOfPoint t1) {
//                return (int) Math.signum(Imgproc.contourArea(matOfPoint) - Imgproc.contourArea(t1));
//            }
//        });
//
//        if (contours.size() <= 0) {
//            telemetry.addData("redIsLeft: ", "Not seen");
//            return;
//        }
//        Point centerRed = new Point();
//        float[] radiusRed = new float[1];
//        // minEnclosingCircle finds a circle of minimum area enclosing a 2D point set, in this case;
//        Imgproc.minEnclosingCircle(new MatOfPoint2f(contours.get(contours.size() - 1).toArray()),centerRed,radiusRed);
//
//        Mat blue = new Mat();
//        Core.inRange(img, blueLower, blueUpper, blue);
//        contours = new LinkedList<>();
//        Imgproc.findContours(red.clone(), contours, new Mat(), Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE);
//        Collections.sort(contours, new Comparator<MatOfPoint>() {
//            @Override
//            public int compare(MatOfPoint matOfPoint, MatOfPoint t1) {
//                return (int) Math.signum(Imgproc.contourArea(matOfPoint) - Imgproc.contourArea(t1));
//            }
//        });
//        if (contours.size() <= 0) {
//            telemetry.addData("redIsLeft: ", "Not seen");
//            return;
//        }
//        Point centerBlue = new Point();
//        float[] radiusBlue = new float[1];
//        Imgproc.minEnclosingCircle(new MatOfPoint2f(contours.get(contours.size() - 1).toArray()),centerBlue,radiusBlue);
//        telemetry.addData("redIsLeft: ", centerRed.x < centerBlue.x);
//    }




//    @Override
//    public void stop() {
//        cap.release();
//    }
}
