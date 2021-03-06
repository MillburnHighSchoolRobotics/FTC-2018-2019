package org.firstinspires.ftc.teamcode;

import android.graphics.Bitmap;
import android.util.Log;

import org.firstinspires.ftc.teamcode.TestingOpModes.Circle;
import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfDouble;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Retrofit;
import virtualRobot.VuforiaLocalizerImplSubclass;
import virtualRobot.telemetry.CTelemetry;
import virtualRobot.telemetry.MatConverterFactory;

public class SahilClassButPartTwo {
    static {
        OpenCVLoader.initDebug();
    }

    VuforiaLocalizerImplSubclass vuforiaInstance;
    private int widthCamera;
    private int widthCameraCropped;
    private int heightCamera;
    private int heightCameraCropped;
    private final double croppingConstant = 0.25 ;

    private final int blackConstant = 25;
    private Mat display = new Mat();
    private Mat original = new Mat();
    private Mat crop = new Mat();
    private Mat mainGold = new Mat();
    private Mat mainSilver = new Mat();
    private Mat maskGold = new Mat();
    private Mat maskBlack = new Mat();
    private Mat hierarchy = new Mat();
    private Rect gold;
    private Circle silver;
    private int silverCount;

    CTelemetry ctel;

    public SahilClassButPartTwo(VuforiaLocalizerImplSubclass vuforiaInstance) {
        this.vuforiaInstance = vuforiaInstance;
        widthCamera = vuforiaInstance.rgb.getBufferWidth();
        heightCamera = vuforiaInstance.rgb.getHeight();
        heightCameraCropped = (int) Math.round(croppingConstant*heightCamera);
//        ctel = new Retrofit.Builder()
//            .baseUrl(BuildConfig.CTELEM_SERVER_IP)
//            .addConverterFactory(MatConverterFactory.create())
//            .build()
//            .create(CTelemetry.class);
        ctel = null;
    }
    public void getImage() {
        Mat img = new Mat();
        heightCameraCropped = (int) Math.round(heightCamera*croppingConstant);
        Bitmap bm = Bitmap.createBitmap(widthCamera, heightCameraCropped, Bitmap.Config.RGB_565);
        bm.copyPixelsFromBuffer(vuforiaInstance.rgb.getPixels());
        Utils.bitmapToMat(bm, img);
        Imgproc.cvtColor(img, original, Imgproc.COLOR_BGR2RGB);
        img.release();
    }
    public void getImageOld() {
        Mat img = new Mat();
        heightCameraCropped = (int) Math.round(heightCamera*croppingConstant);
        Bitmap bm = Bitmap.createBitmap(widthCamera, heightCameraCropped, Bitmap.Config.RGB_565);
        bm.copyPixelsFromBuffer(vuforiaInstance.rgb.getPixels());
        Utils.bitmapToMat(bm, img);
        Imgproc.cvtColor(img, original, Imgproc.COLOR_BGR2RGB);
        img.release();
        crop();
    }
    public void crop() {
        Mat hsv = new Mat();
        Imgproc.cvtColor(original, hsv, Imgproc.COLOR_RGB2HSV);
        Mat black = new Mat();
        Core.inRange(hsv, new Scalar(0, 0, 0), new Scalar(255, 255, blackConstant), black);
        Imgproc.erode(black, maskBlack, Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(10, 10)));
        black.release();
        hsv.release();


        int min = 0;
        for (int w = 0; w < widthCamera; w++) {
            int h = (int) Math.round((int)(heightCameraCropped)/2);
            double[] data = maskBlack.get(h,w);
            if (data[0] == 0) {
                min = w;
                break;
            }
        }

        int max = 0;
        for (int w = widthCamera-1; w >= 0; w--) {
            int h = (int) Math.round((int)(heightCameraCropped)/2);
            double[] data = maskBlack.get(h,w);
            if (data[0] == 0) {
                max = w;
                break;
            }
        }

        Log.d("SahilClass2", "minimum - " + min);
        Log.d("SahilClass2", "maximum - " + max);

        widthCameraCropped = max - min + 1;

        crop = original.clone().submat(0,(int)(heightCameraCropped),min,max);
    }
    public int getPosition() {
        getImage();
        display = original.clone();
        crop = original.clone();
        getGoldPosition();
//        getSilverPosition();
        sendImages();

        int position = 0;
        if (gold == null) {
            position =  0;
//            if (silverCount >= 2) {
//                Log.d("SahilClass2", "Position " + position + " - no silver detected");
//            }
        } else if (gold.x > 0 && gold.x <= (widthCamera/2)) {
            position = 1;
//            if (!(silver.x > (widthCamera/2) && gold.x <= widthCamera)) {
//                Log.d("SahilClass2", "Position " + position + " - no silver detected");
//            }
        } else if (gold.x > (widthCamera/2) && gold.x <= widthCamera) {
            position = 2;
//            if (!(gold.x > 0 && gold.x <= (widthCamera/2))) {
//                Log.d("SahilClass2", "Position " + position + " - no silver detected");
//            }
        }
        Log.d("SahilClass2", "Position: " + position);
        return position;
    }
    public int getPositionOld() {
        getImageOld();
        display = crop.clone();
        getGoldPosition();
//        getSilverPosition();
        Log.d("SahilClass2", "Cropped Width: " + widthCameraCropped);
        int position = 0;

        Imgproc.line(display, new Point((widthCameraCropped / 3), 0), new Point((widthCameraCropped / 3), (int) (heightCameraCropped)), new Scalar(0, 0, 255), 5);
        Imgproc.line(display, new Point(2 * (widthCameraCropped / 3), 0), new Point(2 * (widthCameraCropped / 3), (int) (heightCameraCropped)), new Scalar(0, 0, 255), 5);
        sendImagesOld();

        try {
            Log.d("SahilClass2", "Rectangle Position: (" + gold.x + "," + gold.y + ")");

            if ((gold.x >= 0) && (gold.x < (widthCameraCropped / 3))) {
                position = 2;
            } else if ((gold.x >= (widthCameraCropped / 3)) && (gold.x < (2 * (widthCameraCropped / 3)))) {
                position = 1;
            } else if (gold.x >= (2 * (widthCameraCropped / 3))) {
                position = 0;
            }
            Log.d("SahilClass2", "Position: " + position);
        } catch (Exception ex) {
            position = -1;
            Log.d("SahilClass2", "Error determining the position");
        }
        return position;
    }
    public void getGoldPosition() {
        mainGold = crop.clone();
        Imgproc.GaussianBlur(mainGold,mainGold,new Size(5,5),0);

        filterYellow(mainGold.clone());

        Rect rect = bestScoreGold();
        showLocationGold(rect);
    }
    public void getSilverPosition() {
        mainSilver = crop.clone();
        Mat temp = new Mat();
        Imgproc.bilateralFilter(mainSilver, temp, 5, 175, 175);
        temp.copyTo(mainSilver);
        temp.release();
        Imgproc.cvtColor(mainSilver, mainSilver, Imgproc.COLOR_RGB2Lab);

        Imgproc.erode(mainSilver, mainSilver, Imgproc.getStructuringElement(Imgproc.MORPH_ELLIPSE, new Size(3,3)));
        Imgproc.GaussianBlur(mainSilver, mainSilver, new Size(3,3), 0);

        Circle circle = bestScoreSilver();
        showLocationSilver(circle);
    }
    public void filterYellow(Mat input) {
        double threshold = 70;
        List<Mat> channels = new ArrayList<>();

        Imgproc.cvtColor(input, input, Imgproc.COLOR_BGR2YUV);
        Imgproc.GaussianBlur(input,input,new Size(3,3),0);
        Core.split(input, channels);
        if(channels.size() > 0){
            Imgproc.threshold(channels.get(1), maskGold, threshold, 255, Imgproc.THRESH_BINARY_INV);
        }
        for(int i=0;i<channels.size();i++){
            channels.get(i).release();
        }
        input.release();
    }
    public Rect bestScoreGold() {
        List<MatOfPoint> contours = new ArrayList<>();
        Imgproc.findContours(maskGold, contours, hierarchy, Imgproc.RETR_TREE, Imgproc.CHAIN_APPROX_SIMPLE);
        Imgproc.drawContours(display,contours,-1,new Scalar(230,70,70),2);

        Rect bestRect = null;
        double bestDiffrence = Double.MAX_VALUE;
        for(MatOfPoint c : contours){
            double score = getRatioScore(c) + getAreaScore(c);

            Rect rect = Imgproc.boundingRect(c);
            Imgproc.rectangle(display, rect.tl(), rect.br(), new Scalar(0,0,255),2); // Draw rect

            Log.d("SahilClass2","Rectangle Score: " + score);
            if(score < bestDiffrence){
                bestDiffrence = score;
                bestRect = rect;
            }
        }
        return bestRect;
    }
    public Circle bestScoreSilver() {
        double sensitivity = 1.4; // sensitivity of circle detector - 1.2 to 2.1;
        double minDistance = 60; // minimum distance between circles
        Mat circles = new Mat();
        List<Mat> channels = new ArrayList<Mat>();
        Core.split(mainSilver, channels);
        Imgproc.HoughCircles(channels.get(0), circles, Imgproc.CV_HOUGH_GRADIENT, sensitivity, minDistance); //Applies the Hough Circular transformation to find circles in the image

        Circle bestCircle = null;
        double bestDifference = Double.MAX_VALUE;
        for (int i = 0; i < circles.width(); i++) {
            Circle circle = new Circle(circles.get(0,i)[0],circles.get(0,i)[1],circles.get(0,i)[2]);
            Mat mask = Mat.zeros(mainSilver.size(), CvType.CV_8UC1);
            Imgproc.circle(mask, new Point((int) circle.x, (int) circle.y), (int) circle.radius, new Scalar(255), -1);
            Mat masked = new Mat((int) heightCamera, (int) widthCamera, CvType.CV_8UC3);
            mainSilver.copyTo(masked, mask);
            double score = getColorDeviationScore(masked);
            mask.release();
            masked.release();
            Imgproc.circle(display, new Point(circle.x, circle.y), (int) circle.radius, new Scalar(0,0,255),2);
            Log.d("SahilClass2","Circle Score: " + score);
            if(score < bestDifference){
                bestDifference = score;
                bestCircle = circle;
            }
            silverCount++;
        }
        return bestCircle;
    }
    public double getRatioScore(Mat input) {
        if(!(input instanceof MatOfPoint)) return Double.MAX_VALUE;
        double weight = 5;
        double perfectRatio = 1;
        Rect rect = Imgproc.boundingRect((MatOfPoint)input);
        double w = rect.width;
        double h = rect.height;

        double cubeRatio = Math.max(Math.abs(h/w), Math.abs(w/h)); // Get the ratio. We use max in case h and w get swapped??? it happens when u account for rotation
        double ratioDiffrence = Math.abs(cubeRatio - perfectRatio);
        return ratioDiffrence * weight;
    }
    public double getAreaScore(Mat input) {
        if(!(input instanceof MatOfPoint)) return Double.MAX_VALUE;
        double weight = 0.005;
        double area = Imgproc.contourArea((MatOfPoint)input);
        return -area * weight;
    }
    public double getColorDeviationScore(Mat input) {
        MatOfDouble std = new MatOfDouble();
        MatOfDouble mean = new MatOfDouble();
        Core.meanStdDev(input, mean, std);
        double[] list = (std.get(0,0));
        if(list.length == 0) {
            return 0;
        }
        double sum = 0;
        for (int i = 0; i < list.length; i++) {
            sum += list[i];
        }
        return sum / list.length;
    }
    public void showLocationGold(Rect bestRect) {
        if (bestRect != null) {
            Imgproc.rectangle(display, bestRect.tl(), bestRect.br(), new Scalar(255, 0, 0), 4);
            Imgproc.putText(display, "Chosen", bestRect.tl(), 0, 1, new Scalar(255, 255, 255));

            gold = bestRect;
            Imgproc.putText(display,"Result: " + gold.x +"/"+gold.y,new Point(10,heightCamera - 30),0,1, new Scalar(255,255,0),1);
        } else {
            gold = null;
        }
    }
    public void showLocationSilver(Circle circle) {
        if(circle != null) {
            silver = circle;
            Imgproc.circle(display, new Point(silver.x, silver.y), (int) silver.radius, new Scalar(255, 0, 0), 4);
            Imgproc.putText(display, "Chosen", new Point(silver.x, silver.y), 0, .8, new Scalar(255, 255, 255));
        } else {
            silver = null;
        }
    }
    public void sendImages() {
//        if (ctel != null) {
//        try {
//            ctel.sendImage("Display", display).execute();
//        } catch (IOException e) {
//            e.printStackTrace();
//            Log.e("CTelemetry", "failed display");
//        }
//        try {
//            ctel.sendImage("Gold", maskGold).execute();
//        } catch (IOException e) {
//            e.printStackTrace();
//            Log.e("CTelemetry", "failed gold");
//        }
//        } else {
            try {
                FileWrite.recordImg(display, "Display Image");
                FileWrite.recordImg(maskGold, "Gold Mask");
            } catch (IOException e) {
                Log.e("CTelemetry", "failed image logging", e);
            }
//        }

        display.release();
        mainGold.release();
        mainSilver.release();
        maskGold.release();
        hierarchy.release();
    }
    public void sendImagesOld() {
        try {
            FileWrite.recordImg(display, "Display Image");
            FileWrite.recordImg(maskGold, "Gold Mask");
            FileWrite.recordImg(maskBlack, "Black Mask");
        } catch (IOException e) {
            Log.e("CTelemetry", "failed image logging", e);
        }
        display.release();
        mainGold.release();
        mainSilver.release();
        maskGold.release();
        maskBlack.release();
        hierarchy.release();

    }
}