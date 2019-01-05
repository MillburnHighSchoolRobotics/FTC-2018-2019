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
    static {
        OpenCVLoader.initDebug();
    }
    VuforiaLocalizerImplSubclass vuforiaInstance;
    private int widthCamera;
    private int heightCamera;
    private int heightCameraOriginal;
    private int kSize = 9;
    private int sigmaX = 0;
    private int length;
    private double ratioDeviation = 0.35; //for 0.2, the size range is form 0.8 to 1.2 exclusive
//    private Scalar lowerG = new Scalar(0, 49, 210); //this is for webcam
//    private Scalar upperG = new Scalar(44, 255, 255); //this is for webcam
    private Scalar lowerG = new Scalar(10, 193, 95); //(10, 200, 160);
    private Scalar upperG = new Scalar(32, 255, 255);
    private Scalar lowerW = new Scalar(190, 0, 0);
    private Scalar upperW = new Scalar(255, 256, 256);
    private Scalar lowerBlack = new Scalar(0, 0, 0);
    private Scalar upperBlack = new Scalar(255, 255, 25);
    private static final double croppingConstant = 0.3;
    CTelemetry ctel;


    public SahilClass(VuforiaLocalizerImplSubclass vuforiaInstance) {
        this(vuforiaInstance, 2000);
    }

    public SahilClass(VuforiaLocalizerImplSubclass vuforiaInstance, int length) {
        this.vuforiaInstance = vuforiaInstance;
        widthCamera = vuforiaInstance.rgb.getBufferWidth();
        heightCameraOriginal = vuforiaInstance.rgb.getHeight();
        heightCamera = (int) Math.round(croppingConstant*heightCameraOriginal);
//        ctel = new Retrofit.Builder()
//                .baseUrl(BuildConfig.CTELEM_SERVER_IP)
//                .addConverterFactory(MatConverterFactory.create())
//                .build()
//                .create(CTelemetry.class);
        ctel = null;
        this.length = length;
    }

    public int getThreeMineralPosition() {
        int[] data = getMineralLocationNotCorrected();
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

//        if (max > min) {
            if ((centroid.x >= 0) && (centroid.x < (widthImage/3))) {
                position = 2;
            } else if ((centroid.x >= (widthImage/3)) && (centroid.x < (2*(widthImage/3)))) {
                position = 1;
            } else if (centroid.x >= (2*(widthImage/3))) {
                position = 0;
            }
            Log.d("Position", "Position: " + position);
//        } else {
//            Log.d("Position", "uh oh we got a big error determining the max and min");
//        }
        return position;
    }
    public int getThreeMineralPositionCorrected() {
        double[] data = getMineralLocationCorrected();
        int min = (int) data[0];
        int max = (int) data[1];
        double x = data[2];
        double y = data[3];

        int position = -1;
        double widthImage = max-min+1;
        Point centroid = new Point(x,y);

        if (max > min) {
            if ((centroid.x >= 0) && (centroid.x < (widthImage/3))) {
                position = 2;
            } else if ((centroid.x >= (widthImage/3)) && (centroid.x < (2*(widthImage/3)))) {
                position = 1;
            } else if (centroid.x >= (2*(widthImage/3))) {
                position = 0;
            }
            Log.d("SahilClass", "Position: " + position);
        } else {
            Log.d("SahilClass", "uh oh we got a big error determining the max and min");
        }
        return position;
    }


    private int[] getMineralLocationNotCorrected() {
        ElapsedTime time = new ElapsedTime();
        int timesRun = 0;
        int totalX = 0;
        int totalY = 0;
        int totalMax = 0;
        int totalMin = 0;
        int detected = -1;
        while (time.milliseconds() < length && !Thread.currentThread().isInterrupted()) {
            Mat img = new Mat();
            Bitmap bm = Bitmap.createBitmap(widthCamera, heightCameraOriginal, Bitmap.Config.RGB_565);
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
                int h = (int) Math.round((int)(heightCameraOriginal*croppingConstant)/2);
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
                int h = (int) Math.round((int)(heightCameraOriginal*croppingConstant)/2);
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
//                hsv.release();
//                rgb.release();
//                erodeBlack.release();
//                continue;
                max = widthCamera-1;
                min = 0;
                Log.d("good", "fucjka sidogfhasklejfgnv");
            }

            totalMin += min;
            totalMax += max;

            Mat gold = new Mat();
            Mat goldNotCropped = new Mat();
            Core.inRange(hsv, lowerG, upperG, goldNotCropped);
            Core.inRange(hsv, lowerG, upperG, gold);
            Mat cropped = gold.submat(0,(int)(heightCameraOriginal*croppingConstant),min,max);
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

            Mat croppedImage = rgb.submat(0, (int)(heightCameraOriginal*croppingConstant), min, max);
            Imgproc.circle(croppedImage,centroid, 80, new Scalar(255,0,0), 80);
            Imgproc.line(rgb, new Point(min,0), new Point(min,(int)(heightCameraOriginal*croppingConstant)), new Scalar(255,0,0), 5);
            Imgproc.line(rgb, new Point(max,0), new Point(max,(int)(heightCameraOriginal*croppingConstant)), new Scalar(0,255,0), 5);
            Imgproc.line(rgb, new Point(((max-min)/3)+min,0), new Point(((max-min)/3)+min,(int)(heightCameraOriginal*croppingConstant)), new Scalar(0,0,255), 5);
            Imgproc.line(rgb, new Point((2*(max-min)/3)+min,0), new Point((2*(max-min)/3)+min,(int)(heightCameraOriginal*croppingConstant)), new Scalar(0,0,255), 5);
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
            } else {
                try {
                    FileWrite.recordImg(rgb, "Camera Image");
                    FileWrite.recordImg(croppedImage, "Cropped Image");
                    FileWrite.recordImg(erodeBlack, "Camera Outline");
                    FileWrite.recordImg(erode, "Mineral Detection");
                    FileWrite.recordImg(goldNotCropped, "Test Gold Detection");
                } catch (IOException e) {
                    Log.e("CTelemetry", "failed image logging", e);
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
    private Object[] crop(Mat camera) {
        Mat hsvNotBalanced = new Mat();
        Imgproc.cvtColor(camera, hsvNotBalanced, Imgproc.COLOR_RGB2HSV);

        Mat black = new Mat();
        Core.inRange(hsvNotBalanced, lowerBlack, upperBlack, black);
        Mat erodeBlack = new Mat();
        Imgproc.erode(black, erodeBlack, Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(10, 10)));
        black.release();
        hsvNotBalanced.release();


        int minPrev = -1;
        int maxPrev = -1;
        ArrayList<int[]> minRanges = new ArrayList<>();
        ArrayList<int[]> maxRanges = new ArrayList<>();

        for (int w = 0; w < widthCamera; w++) {
            int h = (int) Math.round(heightCamera/2);
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
            int h = (int) Math.round(heightCamera/2);
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

        Log.d("SahilClass", "minimum - " + min);
        Log.d("SahilClass", "maximum - " + max);

        return new Object[] {min,max,erodeBlack};
    }
    private double[] getMineralLocationCorrected() {
        ElapsedTime time = new ElapsedTime();
        Mat camera = new Mat();
        Bitmap bm = Bitmap.createBitmap(widthCamera, heightCameraOriginal, Bitmap.Config.RGB_565);
        bm.copyPixelsFromBuffer(vuforiaInstance.rgb.getPixels());
        Utils.bitmapToMat(bm, camera);
        Log.d("SahilClass", "widthCamera - " + widthCamera);
        Log.d("SahilClass", "heightCamera - " + heightCameraOriginal);
        Log.d("SahilClass", "heightCamera Scaled - " + heightCamera);

        Object cropData[] = crop(camera);
        int min = (int) cropData[0];
        int max = (int) cropData[1];

        Mat erodeBlack = (Mat) cropData[2];
        Mat rgbCamera = new Mat();
        Imgproc.cvtColor(camera, rgbCamera, Imgproc.COLOR_BGR2RGB);
        Mat rgbCropped = rgbCamera.submat(0,heightCamera,min,max);
        Mat bgrCropped = new Mat();
        Imgproc.cvtColor(rgbCropped, bgrCropped, Imgproc.COLOR_RGB2BGR);

        Imgproc.line(rgbCamera, new Point(min,0), new Point(min,heightCamera), new Scalar(255,0,0), 5);
        Imgproc.line(rgbCamera, new Point(max,0), new Point(max,heightCamera), new Scalar(0,255,0), 5);

        Mat[] whiteData = trueWhite(bgrCropped);
        Mat bgr = whiteData[0];
        Mat white = whiteData[1];
        Mat hsv = new Mat();
        Imgproc.cvtColor(bgr, hsv, Imgproc.COLOR_RGB2HSV);
        Mat rgb = new Mat();
        Imgproc.cvtColor(bgr, rgb, Imgproc.COLOR_BGR2RGB);
        Mat balanced = new Mat();
        Imgproc.cvtColor(bgr, balanced, Imgproc.COLOR_BGR2RGB);

        Mat gold = new Mat();
        Core.inRange(hsv, lowerG, upperG, gold);
        Mat erodeGold = new Mat();
        Imgproc.erode(gold, erodeGold, Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(5, 5)));
        Mat blurGold = new Mat();
        Imgproc.GaussianBlur(erodeGold, blurGold, new Size(kSize, kSize), sigmaX);
        List<MatOfPoint> contoursUnbound = new LinkedList<>();
        Imgproc.findContours(blurGold, contoursUnbound, new Mat(), Imgproc.RETR_CCOMP, Imgproc.CHAIN_APPROX_NONE);
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
        }
        for (MatOfPoint mat : contours) {
            mat.release();
        }

        Imgproc.circle(rgb,centroid, 80, new Scalar(255,0,0), 80);
        Log.d("SahilClass", "Centroid: " + centroid.toString());
        if (ctel != null) {
            try {
                ctel.sendImage("Main Image", rgb).execute();
            } catch (IOException e) {
                e.printStackTrace();
                Log.e("CTelemetry", "failed main image");
            }
            try {
                ctel.sendImage("Camera Image", rgbCamera).execute();
            } catch (IOException e) {
                e.printStackTrace();
                Log.e("CTelemetry", "failed camera image");
            }
            try {
                ctel.sendImage("Cropped Image", rgbCropped).execute();
            } catch (IOException e) {
                e.printStackTrace();
                Log.e("CTelemetry", "failed cropped image");
            }
            try {
                ctel.sendImage("Lens Overlap", erodeBlack).execute();
            } catch (IOException e) {
                e.printStackTrace();
                Log.e("CTelemetry", "failed lens overlap");
            }
            try {
                ctel.sendImage("White Detection", white).execute();
            } catch (IOException e) {
                e.printStackTrace();
                Log.e("CTelemetry", "failed white detection");
            }
            try {
                ctel.sendImage("Balanced Image", balanced).execute();
            } catch (IOException e) {
                e.printStackTrace();
                Log.e("CTelemetry", "failed balanced image");
            }
            try {
                ctel.sendImage("Mineral Detection", erodeGold).execute();
            } catch (IOException e) {
                e.printStackTrace();
                Log.e("CTelemetry", "failed mineral detection");
            }
        } else {
            try {
                FileWrite.recordImg(rgb, "Main Image");
                FileWrite.recordImg(rgbCamera, "Camera Image");
                FileWrite.recordImg(erodeBlack, "Lens Overlap");
                FileWrite.recordImg(rgbCropped, "Cropped Image");
                FileWrite.recordImg(white, "White Detection");
                FileWrite.recordImg(balanced, "Balanced Image");
                FileWrite.recordImg(erodeGold, "Mineral Detection");
            } catch (IOException e) {
                Log.e("CTelemetry", "failed image logging", e);
            }
        }
        camera.release();
        erodeBlack.release();
        rgbCamera.release();
        rgbCropped.release();
        bgrCropped.release();
        white.release();
        bgr.release();
        hsv.release();
        rgb.release();
        balanced.release();
        gold.release();
        erodeGold.release();
        blurGold.release();
        return new double[] {min, max, centroid.x, centroid.y};
    }

    private Mat grayWorldAssumption(Mat img) {
        Mat lab = new Mat(); //no pun intended
        Imgproc.cvtColor(img, lab, Imgproc.COLOR_BGR2Lab);
        double a = 0;
        double b = 0;
        int count = 0;
        for (int x = 0; x < lab.rows(); x++) {
            for (int y = 0; y < lab.cols(); y++) {
                a += lab.get(x,y)[1];
                b += lab.get(x,y)[2];
                count++;
            }
        }
        a /= count;
        b /= count;
        Log.d("SahilClass", "a - " + a);
        Log.d("SahilClass", "b - " + b);
        for (int x = 0; x < lab.rows(); x++) {
            for (int y = 0; y < lab.cols(); y++) {
                double[] data = lab.get(x,y);
                double[] newData = new double[3];
                newData[0] = data[0];
                newData[1] = data[1] - Math.round((a-128)*(data[0]/255.0));
                newData[2] = data[2] - Math.round((b-128)*(data[0]/255.0));
                lab.put(x,y,newData);
            }
        }
        Mat balancedImage = new Mat();
        Imgproc.cvtColor(lab, balancedImage, Imgproc.COLOR_Lab2BGR);
        Log.d("SahilClass", "balanced!");
        return balancedImage;
    }
    private Mat[] trueWhite(Mat img) {
        Mat lab = new Mat();
        Imgproc.cvtColor(img, lab, Imgproc.COLOR_BGR2Lab);
        Mat gray = new Mat();
        Core.inRange(lab, lowerW, upperW, gray);
        Mat erode = new Mat();
        Imgproc.erode(gray, erode, Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(5, 5)));
        Mat blur = new Mat();
        Imgproc.GaussianBlur(erode, blur, new Size(kSize, kSize), sigmaX);

        List<MatOfPoint> contoursUnbound = new LinkedList<>();
        Imgproc.findContours(blur, contoursUnbound, new Mat(), Imgproc.RETR_CCOMP, Imgproc.CHAIN_APPROX_NONE);
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
        }
        for (MatOfPoint mat : contours) {
            mat.release();
        }
        double[] white = lab.get((int)Math.round(centroid.y),(int)Math.round(centroid.x)).clone();
        for (int y = 0; y < lab.rows(); y++) {
            for (int x = 0; x < lab.row(y).cols(); x++) {
                double[] data = lab.get(y,x);
                double[] newData = new double[3];
                newData[0] = data[0];
                newData[1] = Math.round(data[1] - ((white[1]-128)*(data[0]/255.0)));
                newData[2] = Math.round(data[2] - ((white[2]-128)*(data[0]/255.0)));
                lab.put(y,x,newData);
            }
        }
        Mat bgr = new Mat();
        Imgproc.cvtColor(lab, bgr, Imgproc.COLOR_Lab2BGR);
        Imgproc.circle(bgr,centroid, 10, new Scalar(255,0,0), 5);
        Log.d("SahilClass", "balanced!");
        return new Mat[] {bgr, gray};
    }
//    public int getThreeWebMineralPosition() {
//        int[] data = getWebMineralLocation();
//        int totalMax = data[0];
//        int totalMin = data[1];
//        int totalX = data[2];
//        int totalY = data[3];
//        int timesRun = data[4];
//
//        int position = -1;
//        double max = totalMax/(double)timesRun;
//        double min = totalMin/(double)timesRun;
//        double widthImage = max-min+1;
//        Point centroid = new Point(totalX/(double)timesRun, totalY/(double)timesRun);
//        Log.d("Width Range", "Width: " + min + " - " + max);
//        Log.d("Centroid", "Centroid: " + centroid.toString());
//
//        if ((centroid.x >= 0) && (centroid.x < (widthImage/3))) {
//            position = 0;
//        } else if ((centroid.x >= (widthImage/3)) && (centroid.x < (2*(widthImage/3)))) {
//            position = 1;
//        } else if (centroid.x >= (2*(widthImage/3))) {
//            position = 2;
//            Log.d("Position", "Position: " + position);
//        } else {
//            Log.d("Position", "uh oh we got a big error determining the max and min");
//        }
//        return position;
//    }
//    public int getTwoMineralPosition() {
//        int[] data = getMineralLocation();
//        int totalMax = data[0];
//        int totalMin = data[1];
//        int totalX = data[2];
//        int totalY = data[3];
//        int timesRun = data[4];
//        int detected = data[5];
//
//        int position = -1;
//        double max = totalMax/(double)timesRun;
//        double min = totalMin/(double)timesRun;
//        double widthImage = max-min+1;
//        Point centroid = new Point(totalX/(double)timesRun, totalY/(double)timesRun);
//        Log.d("Width Range", "Width: " + min + " - " + max);
//        Log.d("Centroid", "Centroid: " + centroid.toString());
//
//        if (detected == -1) {
//            position = 2;
//            Log.d("Position", "Position: " + position);
//        } else if (max > min) {
//            if ((centroid.x >= 0) && (centroid.x < (widthImage/2))) {
//                position = 0;
//            } else if (centroid.x >= ((widthImage/2))) {
//                position = 1;
//            }
//        } else {
//            position = 2;
////            Log.d("Position", "uh oh we got a big error determining the max and min");
//        }
//        Log.d("Position", "Position: " + position);
//        return position;
//    }
//    public int getTwoWebMineralPosition() {
//        int[] data = getWebMineralLocation();
//        int totalMax = data[0];
//        int totalMin = data[1];
//        int totalX = data[2];
//        int totalY = data[3];
//        int timesRun = data[4];
//        int detected = data[5];
//
//        int position = 2;
//        double max = totalMax/(double)timesRun;
//        double min = totalMin/(double)timesRun;
//        double widthImage = max-min+1;
//        Log.d("Width of Imaage", "Width: " + widthImage);
//        Point centroid = new Point(totalX/(double)timesRun, totalY/(double)timesRun);
////        Log.d("Width Range", "Width: " + min + " - " + max);
//        Log.d("Centroid", "Centroid: " + centroid.toString());
//
//        if (detected == -1) {
//            position = 2;
//            Log.d("Position", "Position: " + position);
//        } else{// if (max > min) {
//            if ((centroid.x >= 0) && (centroid.x < (widthImage/2))) {
//                position = 0;
//            } else if (centroid.x >= ((widthImage/2))) {
//                position = 1;
//            }
//        } //else {
//        //    position = 2;
//        //    position = 2;
////            Log.d("Position", "uh oh we got a big error determining the max and min");
//        //   }
//        Log.d("Position", "Position: " + position);
//        return position;
//    }
//    private int[] getWebMineralLocation() {
//        ElapsedTime time = new ElapsedTime();
//        int timesRun = 0;
//        int totalX = 0;
//        int totalY = 0;
////        int totalMax = 0;
////        int totalMin = 0;
//        int detected = -1;
//        while (time.milliseconds() < length && !Thread.currentThread().isInterrupted()) {
//            Mat img = new Mat();
//            Bitmap bm = Bitmap.createBitmap(widthCamera, heightCamera, Bitmap.Config.RGB_565);
//            bm.copyPixelsFromBuffer(vuforiaInstance.rgb.getPixels());
//            Utils.bitmapToMat(bm, img);
//            Mat hsv = new Mat();
//            Imgproc.cvtColor(img, hsv, Imgproc.COLOR_RGB2HSV);
//            Mat rgb = new Mat();
//            Imgproc.cvtColor(img, rgb, Imgproc.COLOR_BGR2RGB);
//            img.release();
//
////            Mat black = new Mat();
////            Core.inRange(hsv, lowerBlack, upperBlack, black);
////            Mat erodeBlack = new Mat();
////            Imgproc.erode(black, erodeBlack, Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(10, 10)));
////            black.release();
//
////            int minPrev = -1;
////            int maxPrev = -1;
////            ArrayList<int[]> minRanges = new ArrayList<>();
////            ArrayList<int[]> maxRanges = new ArrayList<>();
//
////            for (int w = 0; w < widthCamera; w++) {
////                int h = (int) Math.round(heightCamera/2);
////                double[] data = erodeBlack.get(h,w);
////                if ((data[0] > 0) && (minPrev == -1)) {
////                    minPrev = w;
////                } else if ((data[0] == 0)  && (minPrev != -1)) {
////                    minRanges.add(new int[] {minPrev, w});
////                    minPrev = -1;
////                }
////            }
////            int minLength = 0;
////            int min = 0;
////            for (int a = 0; a < minRanges.size(); a++) {
////                int minLengthNew = Math.abs(minRanges.get(a)[1] - minRanges.get(a)[0]);
////                if (minLengthNew > minLength) {
////                    minLength = minLengthNew;
////                    min = minRanges.get(a)[1];
////                }
////            }
//
//
////            for (int w = (int)(Math.round(widthCamera-1))-1; w >= 0; w--) {
////                int h = (int) Math.round(heightCamera/2);
////                double[] data = erodeBlack.get(h,w);
////                if ((data[0] == 0) && (maxPrev == -1)) {
////                    maxPrev = w;
////                } else if ((data[0] > 0)  && (maxPrev != -1)) {
////                    maxRanges.add(new int[] {maxPrev, w});
////                    maxPrev = -1;
////                }
// //           }
////            int maxLength = 0;
////            int max = 0;
////            for (int a = 0; a < maxRanges.size(); a++) {
////                int maxLengthNew = Math.abs(maxRanges.get(a)[0] - maxRanges.get(a)[1]);
////                if (maxLengthNew > maxLength) {
////                    maxLength = maxLengthNew;
////                    max = maxRanges.get(a)[0];
////                }
////            }
//
////            Log.d("good", "minimum - " + min);
////            Log.d("good", "maximum - " + max);
//
////            if (max <= min) {
////                hsv.release();
////                rgb.release();
////                erodeBlack.release();
////                continue;
////            }
//
////            totalMin += min;
////            totalMax += max;
//
//            Mat gold = new Mat();
////            Mat goldNotCropped = new Mat();
////            Core.inRange(hsv, lowerW, upperW, goldNotCropped);
//            Core.inRange(hsv, lowerW, upperW, gold);
////            Mat cropped = gold;
//            Mat erode = new Mat();
//            Imgproc.erode(gold, erode, Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(5, 5)));
//            gold.release();
//            hsv.release();
//            Mat blur = new Mat();
//            Imgproc.GaussianBlur(erode, blur, new Size(kSize, kSize), sigmaX);
//            List<MatOfPoint> contoursUnbound = new LinkedList<>();
//            Imgproc.findContours(blur, contoursUnbound, new Mat(), Imgproc.RETR_CCOMP, Imgproc.CHAIN_APPROX_NONE);
//            blur.release();
//            List<MatOfPoint> contours = new LinkedList<>();
//            for (int a = 0; a < contoursUnbound.size(); a++) {
//                Rect r = Imgproc.boundingRect(contoursUnbound.get(a));
//                double ratio = Math.max(r.width,r.height)/Math.min(r.width,r.height);
//                if ((ratio < (1+ratioDeviation)) && (ratio > (1-ratioDeviation))) {
//                    contours.add(contoursUnbound.get(a));
//                }
//            }
//            Collections.sort(contours, new Comparator<MatOfPoint>() {
//                @Override
//                public int compare(MatOfPoint matOfPoint, MatOfPoint s) {
//                    return (int) Math.signum(Imgproc.contourArea(s) - Imgproc.contourArea(matOfPoint));
//                }
//            });
//            Point centroid = new Point();
//            centroid.x = 0;
//            centroid.y = 0;
//            if (contours.size() > 0) {
//
//                Log.d("area", "" + Imgproc.contourArea(contours.get(0)));
//                Moments moments = Imgproc.moments(contours.get(0));
//                if (moments.get_m00() != 0) {
//                    centroid.x = moments.get_m10() / moments.get_m00();
//                    centroid.y = moments.get_m01() / moments.get_m00();
//                }
//                totalX += centroid.x;
//                totalY += centroid.y;
//                timesRun++;
//                detected = 1;
//            } else {
//                Log.d("area", "lol theres no area");
//            }
//            for (MatOfPoint mat : contours) {
//                mat.release();
//            }
//
//            Mat rgbcopy = new Mat();
//            rgb.copyTo(rgbcopy);
//            Imgproc.circle(rgbcopy,centroid, 80, new Scalar(255,0,0), 80);
////            //Imgproc.line(rgb, new Point(min,0), new Point(min,heightCamera), new Scalar(255,0,0), 5);
////            //Imgproc.line(rgb, new Point(max,0), new Point(max,heightCamera), new Scalar(0,255,0), 5);
//
////            try {
////                ctel.sendImage("Camera Image", rgb).execute();
////            } catch (IOException e) {
////                e.printStackTrace();
////                Log.e("CTelemetry", "failed camera img");
////            }
////            try {
////                ctel.sendImage("Cropped Image", rgbcopy).execute();
////            } catch (IOException e) {
////                e.printStackTrace();
////                Log.e("CTelemetry", "failed cropped img");
////            }
//////            try {
//////                ctel.sendImage("Camera Outline", erodeBlack).execute();
//////            } catch (IOException e) {
//////                e.printStackTrace();
//////                Log.e("CTelemetry", "failed black detection");
//////            }
////            try {
////                ctel.sendImage("Mineral Detection", erode).execute();
////            } catch (IOException e) {
////                e.printStackTrace();
////                Log.e("CTelemetry", "failed gold detection");
////            }
////            try {
////                ctel.sendImage("Test Gold Detection", goldNotCropped).execute();
////            } catch (IOException e) {
////                e.printStackTrace();
////                Log.e("CTelemetry", "failed non cropped gold detection");
////            }
//
//            rgb.release();
////            croppedImage.release();
////            erodeBlack.release();
//            erode.release();
////            goldNotCropped.release();
//        }
//        return new int[] {widthCamera*timesRun, 0, totalX, totalY, timesRun, detected};
//    }
}
