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
    CTelemetry ctel;


    public SahilClass(VuforiaLocalizerImplSubclass vuforiaInstance) {
        this(vuforiaInstance, 2000);
    }

    public SahilClass(VuforiaLocalizerImplSubclass vuforiaInstance, int length) {
        this.vuforiaInstance = vuforiaInstance;
        widthCamera = vuforiaInstance.rgb.getBufferWidth();
        heightCamera = vuforiaInstance.rgb.getHeight();
        ctel = new Retrofit.Builder()
                .baseUrl("http://localhost:3000")
                .addConverterFactory(MatConverterFactory.create())
                .build()
                .create(CTelemetry.class);
        this.length = length;
    }

    public int getPosition() {
        ElapsedTime time = new ElapsedTime();
        int timesRun = 0;
        int totalX = 0;
        int totalY = 0;
        int totalMax = 0;
        int totalMin = 0;



        while (time.milliseconds() < length && !Thread.currentThread().isInterrupted()) {
            Mat img = new Mat();
            Bitmap bm = Bitmap.createBitmap(widthCamera, heightCamera, Bitmap.Config.RGB_565);
            bm.copyPixelsFromBuffer(vuforiaInstance.rgb.getPixels());
            Utils.bitmapToMat(bm, img);
            Mat hsv = new Mat();
            Imgproc.cvtColor(img, hsv, Imgproc.COLOR_RGB2HSV);
            Mat rgb = new Mat();
            Imgproc.cvtColor(img, rgb, Imgproc.COLOR_BGR2RGB);
            img.release();

            Mat black = new Mat();
            Core.inRange(hsv, lowerBlack, upperBlack, black);
            Mat erodeBlack = new Mat();
            Imgproc.erode(black, erodeBlack, Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(10, 10)));
            black.release();
            Size blackSize = erodeBlack.size();

            int min = 0;
            int max = 0;
            for (int w = 0; w < blackSize.width; w++) {
                int h = (int) Math.round(blackSize.height/2);
                double[] data = erodeBlack.get(h,w);
                Log.d("min color values", w + "," + h + " - " + Arrays.toString(data));
                if (data[0] > 0) {
                    min = w;
                    Log.d("good", "minimum");
                    break;
                }
            }
            for (int w = (int)(Math.round(blackSize.width-1))-1; w >= 0; w--) {
                int h = (int) Math.round(blackSize.height / 2);
                double[] data = erodeBlack.get(h, w);
                Log.d("max color values", w + "," + h + " - " + Arrays.toString(data));
                if (data[0] > 0) {
                    max = w;
                    Log.d("good", "maximum");
                    break;
                }
            }

            if (max <= min) {
                hsv.release();
                rgb.release();
                erodeBlack.release();
                continue;
            }

            totalMin += min;
            totalMax += max;

            Mat gold = new Mat();
            Core.inRange(hsv, lowerG, upperG, gold);
            Mat cropped = gold.submat(0,heightCamera,min,max);
            gold.release();
            Mat erode = new Mat();
            Imgproc.erode(cropped, erode, Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(5, 5)));
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

            Mat croppedImage = rgb.submat(0, heightCamera, min, max);
            Imgproc.circle(croppedImage,centroid, 80, new Scalar(255,0,0), 80);

            try {
                ctel.sendImage("Camera Image", rgb).execute();
            } catch (IOException e) {
                e.printStackTrace();
                Log.e("CTelemetry", "failed camera img");
            }
            try {
                ctel.sendImage("Cropped Image", croppedImage).execute();
            } catch (IOException e) {
                e.printStackTrace();
                Log.e("CTelemetry", "failed cropped img");
            }
            try {
                ctel.sendImage("Camera Outline", erodeBlack).execute();
            } catch (IOException e) {
                e.printStackTrace();
                Log.e("CTelemetry", "failed black detection");
            }
            try {
                ctel.sendImage("Mineral Detection", erode).execute();
            } catch (IOException e) {
                e.printStackTrace();
                Log.e("CTelemetry", "failed gold detection");
            }

            rgb.release();
            croppedImage.release();
            erodeBlack.release();
            erode.release();
        }

        int position = 0;
        double max = totalMax/(double)timesRun;
        double min = totalMin/(double)timesRun;
        double widthImage = max-min+1;
        Point centroid = new Point(totalX/(double)timesRun, totalY/(double)timesRun);
        Log.d("Width Range", "Width: " + min + " - " + max);
        Log.d("Centroid", "Centroid: " + centroid.toString());

        if (max <= min) {
            if ((centroid.x >= 0) && (centroid.x < (widthImage/3))) {
                position = 1;
            } else if ((centroid.x >= (widthImage/3)) && (centroid.x < (2*(widthImage/3)))) {
                position = 2;
            } else if (centroid.x >= (2*(widthImage/3))) {
                position = 3;
            }
            Log.d("Position", "Position: " + position);
        } else {
            Log.d("Position", "uh oh we got a big error determining the max and min");
        }
        return position;
    }
}
