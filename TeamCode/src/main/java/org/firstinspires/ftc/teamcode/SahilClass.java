package org.firstinspires.ftc.teamcode;

import android.graphics.Bitmap;
import android.util.Log;

import com.qualcomm.robotcore.util.ElapsedTime;

import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.core.Point;
import org.opencv.imgproc.Imgproc;
import org.opencv.imgproc.Moments;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import retrofit2.Retrofit;
import virtualRobot.VuforiaLocalizerImplSubclass;
import virtualRobot.telemetry.CTelemetry;
import virtualRobot.telemetry.MatConverterFactory;
import virtualRobot.utils.GlobalUtils;

public class SahilClass {
    VuforiaLocalizerImplSubclass vuforiaInstance;
    private int widthCamera;
    private int heightCamera;
    private int kSize = 9;
    private int sigmaX = 0;
    private int length;
    private Scalar lowerG = new Scalar(10, 193, 95);
    private Scalar upperG = new Scalar(32, 255, 255);
    private Scalar lowerBlack = new Scalar(0, 0, 0);
    private Scalar upperBlack = new Scalar(255, 255, 1);
    private int areaBoundary = 10;
    private int max = -1;
    private int min = -1;
    CTelemetry ctel;


    public SahilClass(VuforiaLocalizerImplSubclass vuforiaInstance) {
        this(vuforiaInstance, 2000);
    }

    public SahilClass(VuforiaLocalizerImplSubclass vuforiaInstance, int length) {
        this.vuforiaInstance = vuforiaInstance;
        widthCamera = vuforiaInstance.rgb.getBufferWidth();
        heightCamera = vuforiaInstance.rgb.getHeight();
        ctel = new Retrofit.Builder()
                .baseUrl("http://127.0.0.1:3000")
                .addConverterFactory(MatConverterFactory.create())
                .build()
                .create(CTelemetry.class);
        this.length = length;
    }

    //TODO: In case of any mistake, return -1
    public int getPosition() {
        ElapsedTime time = new ElapsedTime();
        int timesRun = 0;
        int totalX = 0;
        int totalY = 0;
        while (time.milliseconds() < length && !Thread.currentThread().isInterrupted()) {
            Mat img = new Mat();
            Bitmap bm = Bitmap.createBitmap(widthCamera, heightCamera, Bitmap.Config.RGB_565);
            bm.copyPixelsFromBuffer(vuforiaInstance.rgb.getPixels());
            Utils.bitmapToMat(bm, img);
            Mat rgb = new Mat();
            Imgproc.cvtColor(img, rgb, Imgproc.COLOR_BGR2RGB);
            Mat hsv = new Mat();
            Imgproc.cvtColor(img, hsv, Imgproc.COLOR_RGB2HSV);
//        bgr.release();




            Mat black = new Mat();
            Core.inRange(hsv, lowerBlack, upperBlack, black);
            Mat erodeBlack = new Mat();
            Imgproc.erode(black, erodeBlack, Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(10, 10)));
            black.release();

            Size blackSize = erodeBlack.size();
            loop: for (int w = 0; w < blackSize.width; w++) {
                int h = (int) Math.round(blackSize.height/2);
                double[] data = erodeBlack.get(h,w);
                Log.d("color values",data[0] + " " + data[1] + " " + data[2]);
                if ((data[2] > 1) && ((max == -1) || (min == -1))) {
                    if (min == -1) {
                        min = w;
                    } else {
                        max = w;
                        break loop;
                    }
                }
            }


//            Mat blurBlack = new Mat();
//            Imgproc.GaussianBlur(erodeBlack, blurBlack, new Size(kSize, kSize), sigmaX);
//            List<MatOfPoint> blackContours = new LinkedList<>();
//            Imgproc.findContours(blurBlack, blackContours, new Mat(), Imgproc.RETR_CCOMP, Imgproc.CHAIN_APPROX_NONE);
//            blurBlack.release();
//            Collections.sort(blackContours, new Comparator<MatOfPoint>() {
//                @Override
//                public int compare(MatOfPoint matOfPoint, MatOfPoint s) {
//                    Moments moments1 = Imgproc.moments(matOfPoint);
//                    Moments moments2 = Imgproc.moments(s);
//                    if ((moments1.get_m00() != 0) && (moments2.get_m00() != 0)) {
//                        return Double.compare((moments1.get_m01() / moments1.get_m00()) , (moments2.get_m01() / moments2.get_m00()));
//                    }
//                    return 0;
//                }
//            });
//            for (MatOfPoint pt : blackContours) {
//                pt.release();
//
//            }

//            Point centroidBlack = new Point();
//            centroidBlack.x = 0;
//            centroidBlack.y = 0;
//            if (blackContours.size() > 0) {
//                Moments moments = Imgproc.moments(blackContours.get(0));
//                if (moments.get_m00() != 0) {
//                    centroidBlack.x = moments.get_m10() / moments.get_m00();
//                    centroidBlack.y = moments.get_m01() / moments.get_m00();
//                }
//                totalX += centroidBlack.x;
//                totalY += centroidBlack.y;
//                timesRun++;
//            }
//            for (MatOfPoint mat : blackContours) {
//                mat.release();
//            }




            Mat gold = new Mat();
            Core.inRange(hsv, lowerG, upperG, gold);
            Mat erode = new Mat();
            Imgproc.erode(gold, erode, Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(5, 5)));
            gold.release();
//        Log.d("Color", Arrays.toString(hsv.get(100, 100)));
            hsv.release();
            Mat blur = new Mat();
            Imgproc.GaussianBlur(erode, blur, new Size(kSize, kSize), sigmaX);
            List<MatOfPoint> contours = new LinkedList<>();
            Imgproc.findContours(blur, contours, new Mat(), Imgproc.RETR_CCOMP, Imgproc.CHAIN_APPROX_NONE);
            blur.release();
            Collections.sort(contours, new Comparator<MatOfPoint>() {
                @Override
                public int compare(MatOfPoint matOfPoint, MatOfPoint s) {
                    return (int) Math.signum(Imgproc.contourArea(s) - Imgproc.contourArea(matOfPoint));
                }
            });
            Point centroid = new Point();
            centroid.x = 0;
            centroid.y = 0;
            if (contours.size() > 0) {
                Moments moments = Imgproc.moments(contours.get(0));
                if (moments.get_m00() != 0) {
                    centroid.x = moments.get_m10() / moments.get_m00();
                    centroid.y = moments.get_m01() / moments.get_m00();
                }
                totalX += centroid.x;
                totalY += centroid.y;
                timesRun++;
            }
            for (MatOfPoint mat : contours) {
                mat.release();
            }

            Imgproc.circle(rgb,centroid, 80, new Scalar(255,0,0), 80);





            try {
                ctel.sendImage("Black", erodeBlack).execute();
            } catch (IOException e) {
                e.printStackTrace();
                Log.e("CTelemetry", "failed black erode");
            }
            try {
                ctel.sendImage("Erode", erode).execute();
            } catch (IOException e) {
                e.printStackTrace();
                Log.e("CTelemetry", "failed erode");
            }
            try {
                ctel.sendImage("Image", rgb).execute();
            } catch (IOException e) {
                e.printStackTrace();
                Log.e("CTelemetry", "failed img");
            }
            erodeBlack.release();
            erode.release();
            img.release();
            rgb.release();
        }
//        int radius = 2;

        int position = 0;
        int widthImage = max-min;
        Point centroid = new Point(totalX/(double)timesRun, totalY/(double)timesRun);
        Log.d("SahilClass", "Centroid: " + centroid.toString() + ", (" + widthCamera + ", " + heightCamera + ")");
        if (!((max == -1) || (min == -1))) {
            if ((centroid.x >= 0) && (centroid.x < ((widthImage/3)+min))) {
                position = 1;
            } else if ((centroid.x >= ((widthImage/3)+min)) && (centroid.x < ((2*(widthImage/3))+min))) {
                position = 2;
            } else if (centroid.x >= ((2*(widthImage/3))+min)) {
                position = 3;
            } else {
                position = -1;
            }
        } else {
            position = -1;
        }
        Log.d("SahilClass", "Position: " + position);
        return position;
    }
}
