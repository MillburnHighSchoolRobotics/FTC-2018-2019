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
    private int width;
    private int height;
    private int kSize = 9;
    private int sigmaX = 0;
    private int length;
    private Scalar lowerG = new Scalar(10, 193, 95);
    private Scalar upperG = new Scalar(32, 255, 255);
    CTelemetry ctel;


    public SahilClass(VuforiaLocalizerImplSubclass vuforiaInstance) {
        this(vuforiaInstance, 2000);
    }

    public SahilClass(VuforiaLocalizerImplSubclass vuforiaInstance, int length) {
        this.vuforiaInstance = vuforiaInstance;
        width = vuforiaInstance.rgb.getBufferWidth();
        height = vuforiaInstance.rgb.getHeight();
        ctel = new Retrofit.Builder()
                .baseUrl("http://localhost:3000")
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
            Bitmap bm = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
            bm.copyPixelsFromBuffer(vuforiaInstance.rgb.getPixels());
            Utils.bitmapToMat(bm, img);
            Mat rgb = new Mat();
            Imgproc.cvtColor(img, rgb, Imgproc.COLOR_BGR2RGB);
            Mat hsv = new Mat();
            Imgproc.cvtColor(img, hsv, Imgproc.COLOR_RGB2HSV);
//        bgr.release();
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
                    return (int) Math.signum(Imgproc.contourArea(matOfPoint) - Imgproc.contourArea(s));
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
            erode.release();
            img.release();
            rgb.release();
        }
//        int radius = 2;

        int position = 0;
        Point centroid = new Point(totalX/(double)timesRun, totalY/(double)timesRun);
        Log.d("SahilClass", "Centroid: " + centroid.toString() + ", (" + width + ", " + height + ")");
        if ((centroid.x >= 0) && (centroid.x < (width/3))) {
            position = 1;
        } else if ((centroid.x >= (width/3)) && (centroid.x < ((2*width)/3))) {
            position = 2;
        } else if (centroid.x >= (2*width)/3) {
            position = 3;
        } else {
            position = -1;
        }
        Log.d("SahilClass", "Position: " + position);
        return position;
    }
}
