package org.firstinspires.ftc.teamcode;

import android.graphics.Bitmap;
import android.util.Log;

import com.qualcomm.robotcore.util.ElapsedTime;

import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.core.Point;
import org.opencv.imgproc.Imgproc;
import org.opencv.imgproc.Moments;

import java.io.IOException;
import java.util.ArrayList;
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
    private double ratioDeviation = 0.35; //for 0.2, the size range is form 0.8 to 1.2 exclusive
//    private Scalar lowerG = new Scalar(0, 49, 210); //this is for webcam
//    private Scalar upperG = new Scalar(44, 255, 255); //this is for webcam
    private Scalar lowerG = new Scalar(10, 200, 160); //ew Scalar(10, 193, 95);
    private Scalar upperG = new Scalar(32, 255, 255);
    private Scalar lowerW = new Scalar(14, 56, 126);
    private Scalar upperW = new Scalar(36, 223, 255);
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
                .baseUrl(BuildConfig.CTELEM_SERVER_IP)
                .addConverterFactory(MatConverterFactory.create())
                .build()
                .create(CTelemetry.class);
//        ctel = null;
        this.length = length;
    }

    public int getThreeMineralPosition() {
        int[] data = getMineralLocation();
        int totalMax = data[0];
        int totalMin = data[1];
        int totalX = data[2];
        int totalY = data[3];
        int timesRun = data[4];

        int position = -1;
        double max = totalMax/(double)timesRun;
        double min = totalMin/(double)timesRun;
        double widthImage = max-min+1;
        Point centroid = new Point(totalX/(double)timesRun, totalY/(double)timesRun);
        Log.d("Width Range", "Width: " + min + " - " + max);
        Log.d("Centroid", "Centroid: " + centroid.toString());

        if (max > min) {
            if ((centroid.x >= 0) && (centroid.x < (widthImage/3))) {
                position = 2;
            } else if ((centroid.x >= (widthImage/3)) && (centroid.x < (2*(widthImage/3)))) {
                position = 1;
            } else if (centroid.x >= (2*(widthImage/3))) {
                position = 0;
            }
            Log.d("Position", "Position: " + position);
        } else {
            Log.d("Position", "uh oh we got a big error determining the max and min");
        }
        return position;
    }


    public int getThreeWebMineralPosition() {
        int[] data = getWebMineralLocation();
        int totalMax = data[0];
        int totalMin = data[1];
        int totalX = data[2];
        int totalY = data[3];
        int timesRun = data[4];

        int position = -1;
        double max = totalMax/(double)timesRun;
        double min = totalMin/(double)timesRun;
        double widthImage = max-min+1;
        Point centroid = new Point(totalX/(double)timesRun, totalY/(double)timesRun);
        Log.d("Width Range", "Width: " + min + " - " + max);
        Log.d("Centroid", "Centroid: " + centroid.toString());

        if ((centroid.x >= 0) && (centroid.x < (widthImage/3))) {
            position = 0;
        } else if ((centroid.x >= (widthImage/3)) && (centroid.x < (2*(widthImage/3)))) {
            position = 1;
        } else if (centroid.x >= (2*(widthImage/3))) {
            position = 2;
            Log.d("Position", "Position: " + position);
        } else {
            Log.d("Position", "uh oh we got a big error determining the max and min");
        }
        return position;
    }

    public int getTwoMineralPosition() {
        int[] data = getMineralLocation();
        int totalMax = data[0];
        int totalMin = data[1];
        int totalX = data[2];
        int totalY = data[3];
        int timesRun = data[4];
        int detected = data[5];

        int position = -1;
        double max = totalMax/(double)timesRun;
        double min = totalMin/(double)timesRun;
        double widthImage = max-min+1;
        Point centroid = new Point(totalX/(double)timesRun, totalY/(double)timesRun);
        Log.d("Width Range", "Width: " + min + " - " + max);
        Log.d("Centroid", "Centroid: " + centroid.toString());

        if (detected == -1) {
            position = 2;
            Log.d("Position", "Position: " + position);
        } else if (max > min) {
            if ((centroid.x >= 0) && (centroid.x < (widthImage/2))) {
                position = 0;
            } else if (centroid.x >= ((widthImage/2))) {
                position = 1;
            }
        } else {
            position = 2;
//            Log.d("Position", "uh oh we got a big error determining the max and min");
        }
        Log.d("Position", "Position: " + position);
        return position;
    }
    public int getTwoWebMineralPosition() {
        int[] data = getWebMineralLocation();
        int totalMax = data[0];
        int totalMin = data[1];
        int totalX = data[2];
        int totalY = data[3];
        int timesRun = data[4];
        int detected = data[5];

        int position = 2;
        double max = totalMax/(double)timesRun;
        double min = totalMin/(double)timesRun;
        double widthImage = max-min+1;
        Log.d("Width of Imaage", "Width: " + widthImage);
        Point centroid = new Point(totalX/(double)timesRun, totalY/(double)timesRun);
//        Log.d("Width Range", "Width: " + min + " - " + max);
        Log.d("Centroid", "Centroid: " + centroid.toString());

        if (detected == -1) {
            position = 2;
            Log.d("Position", "Position: " + position);
        } else{// if (max > min) {
            if ((centroid.x >= 0) && (centroid.x < (widthImage/2))) {
                position = 0;
            } else if (centroid.x >= ((widthImage/2))) {
                position = 1;
            }
        } //else {
        //    position = 2;
        //    position = 2;
//            Log.d("Position", "uh oh we got a big error determining the max and min");
     //   }
        Log.d("Position", "Position: " + position);
        return position;
    }
    private int[] getMineralLocation() {
        ElapsedTime time = new ElapsedTime();
        int timesRun = 0;
        int totalX = 0;
        int totalY = 0;
        int totalMax = 0;
        int totalMin = 0;
        int detected = -1;
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

            int minPrev = -1;
            int maxPrev = -1;
            ArrayList<int[]> minRanges = new ArrayList<>();
            ArrayList<int[]> maxRanges = new ArrayList<>();

            for (int w = 0; w < widthCamera; w++) {
                int h = (int) Math.round((int)(heightCamera*0.45)/2);
                double[] data = erodeBlack.get(h,w);
                if ((data[0] > 0) && (minPrev == -1)) {
                    minPrev = w;
                } else if ((data[0] == 0)  && (minPrev != -1)) {
                    minRanges.add(new int[] {minPrev, w});
                    minPrev = -1;
                }
            }
            int minLength = 0;
            int min = 0;
            for (int a = 0; a < minRanges.size(); a++) {
                int minLengthNew = Math.abs(minRanges.get(a)[1] - minRanges.get(a)[0]);
                if (minLengthNew > minLength) {
                    minLength = minLengthNew;
                    min = minRanges.get(a)[1];
                }
            }


            for (int w = (int)(Math.round(widthCamera-1))-1; w >= 0; w--) {
                int h = (int) Math.round((int)(heightCamera*0.45)/2);
                double[] data = erodeBlack.get(h,w);
                if ((data[0] == 0) && (maxPrev == -1)) {
                    maxPrev = w;
                } else if ((data[0] > 0)  && (maxPrev != -1)) {
                    maxRanges.add(new int[] {maxPrev, w});
                    maxPrev = -1;
                }
            }
            int maxLength = 0;
            int max = 0;
            for (int a = 0; a < maxRanges.size(); a++) {
                int maxLengthNew = Math.abs(maxRanges.get(a)[0] - maxRanges.get(a)[1]);
                if (maxLengthNew > maxLength) {
                    maxLength = maxLengthNew;
                    max = maxRanges.get(a)[0];
                }
            }

            Log.d("good", "minimum - " + min);
            Log.d("good", "maximum - " + max);

            if (max <= min) {
                hsv.release();
                rgb.release();
                erodeBlack.release();
                continue;
            }

            totalMin += min;
            totalMax += max;

            Mat gold = new Mat();
            Mat goldNotCropped = new Mat();
            Core.inRange(hsv, lowerG, upperG, goldNotCropped);
            Core.inRange(hsv, lowerG, upperG, gold);
            Mat cropped = gold.submat(0,(int)(heightCamera*0.45),min,max);
            gold.release();
            Mat erode = new Mat();
            Imgproc.erode(cropped, erode, Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(5, 5)));
            hsv.release();
            Mat blur = new Mat();
            Imgproc.GaussianBlur(erode, blur, new Size(kSize, kSize), sigmaX);
            List<MatOfPoint> contoursUnbound = new LinkedList<>();
            Imgproc.findContours(blur, contoursUnbound, new Mat(), Imgproc.RETR_CCOMP, Imgproc.CHAIN_APPROX_NONE);
            blur.release();
            List<MatOfPoint> contours = new LinkedList<>();
            for (int a = 0; a < contoursUnbound.size(); a++) {
                Rect r = Imgproc.boundingRect(contoursUnbound.get(a));
                double ratio = Math.max(r.width,r.height)/Math.min(r.width,r.height);
                if ((ratio < (1+ratioDeviation)) && (ratio > (1-ratioDeviation))) {
                    contours.add(contoursUnbound.get(a));
                }
            }
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
                detected = 1;
            }
            for (MatOfPoint mat : contours) {
                mat.release();
            }

            Mat croppedImage = rgb.submat(0, (int)(heightCamera*0.45), min, max);
            Imgproc.circle(croppedImage,centroid, 80, new Scalar(255,0,0), 80);
            Imgproc.line(rgb, new Point(min,0), new Point(min,(int)(heightCamera*0.45)), new Scalar(255,0,0), 5);
            Imgproc.line(rgb, new Point(max,0), new Point(max,(int)(heightCamera*0.45)), new Scalar(0,255,0), 5);
            if (ctel != null) {
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
                try {
                    ctel.sendImage("Test Gold Detection", goldNotCropped).execute();
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.e("CTelemetry", "failed non cropped gold detection");
                }
            }

            rgb.release();
            croppedImage.release();
            erodeBlack.release();
            erode.release();
            goldNotCropped.release();
        }
        return new int[] {totalMax, totalMin, totalX, totalY, timesRun, detected};
    }
    private int[] getWebMineralLocation() {
        ElapsedTime time = new ElapsedTime();
        int timesRun = 0;
        int totalX = 0;
        int totalY = 0;
//        int totalMax = 0;
//        int totalMin = 0;
        int detected = -1;
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

//            Mat black = new Mat();
//            Core.inRange(hsv, lowerBlack, upperBlack, black);
//            Mat erodeBlack = new Mat();
//            Imgproc.erode(black, erodeBlack, Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(10, 10)));
//            black.release();

//            int minPrev = -1;
//            int maxPrev = -1;
//            ArrayList<int[]> minRanges = new ArrayList<>();
//            ArrayList<int[]> maxRanges = new ArrayList<>();

//            for (int w = 0; w < widthCamera; w++) {
//                int h = (int) Math.round(heightCamera/2);
//                double[] data = erodeBlack.get(h,w);
//                if ((data[0] > 0) && (minPrev == -1)) {
//                    minPrev = w;
//                } else if ((data[0] == 0)  && (minPrev != -1)) {
//                    minRanges.add(new int[] {minPrev, w});
//                    minPrev = -1;
//                }
//            }
//            int minLength = 0;
//            int min = 0;
//            for (int a = 0; a < minRanges.size(); a++) {
//                int minLengthNew = Math.abs(minRanges.get(a)[1] - minRanges.get(a)[0]);
//                if (minLengthNew > minLength) {
//                    minLength = minLengthNew;
//                    min = minRanges.get(a)[1];
//                }
//            }


//            for (int w = (int)(Math.round(widthCamera-1))-1; w >= 0; w--) {
//                int h = (int) Math.round(heightCamera/2);
//                double[] data = erodeBlack.get(h,w);
//                if ((data[0] == 0) && (maxPrev == -1)) {
//                    maxPrev = w;
//                } else if ((data[0] > 0)  && (maxPrev != -1)) {
//                    maxRanges.add(new int[] {maxPrev, w});
//                    maxPrev = -1;
//                }
 //           }
//            int maxLength = 0;
//            int max = 0;
//            for (int a = 0; a < maxRanges.size(); a++) {
//                int maxLengthNew = Math.abs(maxRanges.get(a)[0] - maxRanges.get(a)[1]);
//                if (maxLengthNew > maxLength) {
//                    maxLength = maxLengthNew;
//                    max = maxRanges.get(a)[0];
//                }
//            }

//            Log.d("good", "minimum - " + min);
//            Log.d("good", "maximum - " + max);

//            if (max <= min) {
//                hsv.release();
//                rgb.release();
//                erodeBlack.release();
//                continue;
//            }

//            totalMin += min;
//            totalMax += max;

            Mat gold = new Mat();
//            Mat goldNotCropped = new Mat();
//            Core.inRange(hsv, lowerW, upperW, goldNotCropped);
            Core.inRange(hsv, lowerW, upperW, gold);
//            Mat cropped = gold;
            Mat erode = new Mat();
            Imgproc.erode(gold, erode, Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(5, 5)));
            gold.release();
            hsv.release();
            Mat blur = new Mat();
            Imgproc.GaussianBlur(erode, blur, new Size(kSize, kSize), sigmaX);
            List<MatOfPoint> contoursUnbound = new LinkedList<>();
            Imgproc.findContours(blur, contoursUnbound, new Mat(), Imgproc.RETR_CCOMP, Imgproc.CHAIN_APPROX_NONE);
            blur.release();
            List<MatOfPoint> contours = new LinkedList<>();
            for (int a = 0; a < contoursUnbound.size(); a++) {
                Rect r = Imgproc.boundingRect(contoursUnbound.get(a));
                double ratio = Math.max(r.width,r.height)/Math.min(r.width,r.height);
                if ((ratio < (1+ratioDeviation)) && (ratio > (1-ratioDeviation))) {
                    contours.add(contoursUnbound.get(a));
                }
            }
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

                Log.d("area", "" + Imgproc.contourArea(contours.get(0)));
                Moments moments = Imgproc.moments(contours.get(0));
                if (moments.get_m00() != 0) {
                    centroid.x = moments.get_m10() / moments.get_m00();
                    centroid.y = moments.get_m01() / moments.get_m00();
                }
                totalX += centroid.x;
                totalY += centroid.y;
                timesRun++;
                detected = 1;
            } else {
                Log.d("area", "lol theres no area");
            }
            for (MatOfPoint mat : contours) {
                mat.release();
            }

            Mat rgbcopy = new Mat();
            rgb.copyTo(rgbcopy);
            Imgproc.circle(rgbcopy,centroid, 80, new Scalar(255,0,0), 80);
//            //Imgproc.line(rgb, new Point(min,0), new Point(min,heightCamera), new Scalar(255,0,0), 5);
//            //Imgproc.line(rgb, new Point(max,0), new Point(max,heightCamera), new Scalar(0,255,0), 5);

//            try {
//                ctel.sendImage("Camera Image", rgb).execute();
//            } catch (IOException e) {
//                e.printStackTrace();
//                Log.e("CTelemetry", "failed camera img");
//            }
//            try {
//                ctel.sendImage("Cropped Image", rgbcopy).execute();
//            } catch (IOException e) {
//                e.printStackTrace();
//                Log.e("CTelemetry", "failed cropped img");
//            }
////            try {
////                ctel.sendImage("Camera Outline", erodeBlack).execute();
////            } catch (IOException e) {
////                e.printStackTrace();
////                Log.e("CTelemetry", "failed black detection");
////            }
//            try {
//                ctel.sendImage("Mineral Detection", erode).execute();
//            } catch (IOException e) {
//                e.printStackTrace();
//                Log.e("CTelemetry", "failed gold detection");
//            }
//            try {
//                ctel.sendImage("Test Gold Detection", goldNotCropped).execute();
//            } catch (IOException e) {
//                e.printStackTrace();
//                Log.e("CTelemetry", "failed non cropped gold detection");
//            }

            rgb.release();
//            croppedImage.release();
//            erodeBlack.release();
            erode.release();
//            goldNotCropped.release();
        }
        return new int[] {widthCamera*timesRun, 0, totalX, totalY, timesRun, detected};
    }
}
