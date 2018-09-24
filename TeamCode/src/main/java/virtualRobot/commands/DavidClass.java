package virtualRobot.commands;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Matrix;
import android.os.Environment;
import virtualRobot.utils.BetterLog;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import virtualRobot.utils.Vector2i;

/**
 * Created by DOSullivan on 11/4/15.
 * Analyzes the picture and determines whether or not red is left
 */

@Deprecated
public class DavidClass {
    public final static Mode currentMode = Mode.MIDSPLIT;
    public volatile static double startXPercent = 0;
    public volatile static double endXPercent = 1;
    public volatile static double startYPercent = 0.135;
    public volatile static double endYPercent = 1;
    public volatile static double start1XPercent = 0;
    public volatile static double end1XPercent = -1;
    public volatile static double start1YPercent = 0;
    public volatile static double end1YPercent = -1;
    public volatile static double start2XPercent = 0.66;
    public volatile static double end2XPercent = 1;
    public volatile static double start2YPercent = 0;
    public volatile static double end2YPercent = 0.8353;
    private static double TOLERANCE = 135;


    public static final long RED = Color.red(Color.RED); //note that Color.RED is negative
    //returns true if left is red (right is blue)
    //returns false if left if blue (right is red)
    public static void setPercents(double startX, double endX, double startY, double endY) {
        startXPercent = startX;
        endXPercent = endX;
        startYPercent = startY;
        endYPercent = endY;

    }
    public static boolean analyzePic2(Bitmap bmp) {
        BetterLog.d("DavidClass", "Color.RED: " + Long.toString(RED));
        Bitmap image= Bitmap.createScaledBitmap(bmp, bmp.getWidth() / 2, bmp.getHeight() / 2, true);
//        image= Bitmap.createScaledBitmap(bmp, image.getWidth() / 2, image.getHeight() / 2, true);
        Matrix matrix = new Matrix();
        matrix.postRotate(180);
//        Bitmap scaledBitmap = Bitmap.createScaledBitmap(image, image.getWidth(), image.getHeight(), true);
        image = Bitmap.createBitmap(image, 0, 0, image.getWidth(), image.getHeight(), matrix, true);



       // int[] pixels = new int[image.getWidth() * image.getHeight()];

        int height = image.getHeight(), width = image.getWidth();
        boolean result;
      //  image.getPixels(pixels, 0, image.getWidth(), 0, 0, image.getWidth(), image.getHeight()); //gets pixels in pixel array
        if (currentMode == Mode.MIDSPLIT) {
            final int startX = (int) ((startXPercent) * width); //.4
            final int endX = (int) (endXPercent * width); //.9
            final int startY = (int) (startYPercent * height); //.55
            final int endY = (int) (endYPercent * height); //.77

            final int midX = (startX + endX) / 2;

            //int lNum = 0;
            //int rNum = 0;
            long lSum = 0;
            long rSum = 0;
            long lAvg, rAvg;
            for (int i = startY; i < endY; i++) {
                for (int j = startX; j < midX; j++) {
                    lSum += Color.red(image.getPixel(j, i));
                    //lSum += Color.red(pixels[width*i + j]);
                    // lNum++;

                }

                for (int j = midX; j < endX; j++) {
                    rSum += Color.red(image.getPixel(j, i));
                    //rSum+= Color.red(pixels[width*i + j]);
                    //rNum++;


                }

            }
            BetterLog.d("DavidClass", "Width: "+ width);
            BetterLog.d("DavidClass", "Height: " + height);
            BetterLog.d("DavidClass","Mid: " + midX);
            //lAvg = roundUp(lSum, lNum);
            //rAvg = roundUp(rSum, rNum);
            BetterLog.d("DavidClass", "Pic Size: " + Long.toString(midX - startX) + " by " + Long.toString(endX - midX));

            lAvg = roundUp(lSum, ((midX - startX) * (endY - startY)));
            rAvg = roundUp(rSum, ((endX - midX) * (endY - startY)));
            BetterLog.d("DavidClass", "Left: " + Long.toString(lAvg) + " Right: " + Long.toString(rAvg));
            result = (lAvg-RED > rAvg-RED);
        } else {
            Vector2i start1 = new Vector2i((int) DavidClass.start1XPercent*width, (int) DavidClass.startYPercent*height);
            Vector2i end1 = new Vector2i((int) DavidClass.startXPercent*width,(int) DavidClass.startYPercent*height);
            Vector2i start2 = new Vector2i((int) DavidClass.start2XPercent*width, (int) DavidClass.start2YPercent*height);
            Vector2i end2  = new Vector2i((int) DavidClass.end2XPercent*width, (int) DavidClass.end2YPercent*height);

            //int lNum = 0;
            //int rNum = 0;
            long lSum = 0;
            long rSum = 0;
            long lAvg, rAvg;
            for (int i = start1.y; i < end1.y; i++) {
                for (int j = start1.x; j < end1.x; j++) {
                    lSum += Color.red(image.getPixel(j, i));
                    //lSum += Color.red(pixels[width*i + j]);
                    // lNum++;

                }
            }
            for (int i = start2.y; i < end2.y; i++) {
                for (int j = start2.x; j < end2.x; j++) {
                    rSum += Color.red(image.getPixel(j, i));
                    //lSum += Color.red(pixels[width*i + j]);
                    // lNum++;

                }
            }

            //lAvg = roundUp(lSum, lNum);
            //rAvg = roundUp(rSum, rNum);
            BetterLog.d("DavidClass", "Pic1 Size: " + Long.toString(end1.x-start1.x) + " by " + Long.toString(end1.y - start1.y));
            BetterLog.d("DavidClass", "Pic2 Size: " + Long.toString(end2.x-start2.x) + " by " + Long.toString(end2.y - start2.y));

            lAvg = roundUp(lSum, ((end1.x-start1.x) * (end1.y - start1.y)));
            rAvg = roundUp(rSum, ((end2.x - start2.x) * (end2.y - start2.y)));
            BetterLog.d("DavidClass", "Left: " + Long.toString(lAvg) + " Right: " + Long.toString(rAvg));
            result = (lAvg-RED > rAvg-RED);
        }
        /*BetterLog.d("qqq", Long.toString(lAvg) + " " + Long.toString(rAvg));



        OutputStream fos = null;
        try {
            fos = new FileOutputStream(new File(Environment.getExternalStorageDirectory() + "/FIRST/" + Boolean.toString(lAvg>rAvg)+Long.toString(System.currentTimeMillis()) + ".jpg"));
            image.compress(Bitmap.CompressFormat.JPEG, 100, fos);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }*/

        return result;
        }
    public static boolean[] checkIfAllRed(Bitmap bmp) {
        Bitmap image= Bitmap.createScaledBitmap(bmp, bmp.getWidth() / 2, bmp.getHeight() / 2, true);
        Matrix matrix = new Matrix();
        matrix.postRotate(90);
//        Bitmap scaledBitmap = Bitmap.createScaledBitmap(image, image.getWidth(), image.getHeight(), true);
        image = Bitmap.createBitmap(image, 0, 0, image.getWidth(), image.getHeight(), matrix, true);



        // int[] pixels = new int[image.getWidth() * image.getHeight()];

        int height = image.getHeight(), width = image.getWidth();
        //  image.getPixels(pixels, 0, image.getWidth(), 0, 0, image.getWidth(), image.getHeight()); //gets pixels in pixel array

        final int startX = (int) ((startXPercent) * width); //.4
        final int endX = (int) (endXPercent*width); //.9
        final int startY = (int) (startYPercent*height); //.55
        final int endY = (int) (endYPercent*height); //.77

        final int midX = (startX + endX) / 2;

        //int lNum = 0;
        //int rNum = 0;
        long lSum = 0;
        long rSum = 0;
        long lAvg, rAvg;

        for (int i = startY; i < endY; i++) {
            for (int j = startX; j < midX; j++) {
                lSum += Color.red(image.getPixel(j,i));
            }
            for (int j = midX; j < endX; j++) {
                rSum += Color.red(image.getPixel(j,i));

            }
        }

        lAvg = roundUp(lSum, ((midX-startX)*(endY-startY)));
        rAvg = roundUp(rSum, ((endX-midX)*(endY-startY)));

        BetterLog.d("qqq", Long.toString(lAvg) + " " + Long.toString(rAvg));

        Command.ROBOT.addToProgress("LAVG, RAVG, LAVG+RAVG/2: " + Long.toString(lAvg) + " " + Long.toString(rAvg) + " " + Long.toString((rAvg+lAvg)/2));


        boolean[] info = {(lAvg+rAvg)/2 >TOLERANCE, lAvg>rAvg};
        return (info);
    }















    /*public static boolean analyzePic(Bitmap bmp) {
        BetterLog.d("zzz", Long.toString(RED));
        Bitmap image= bmp;
        image= Bitmap.createScaledBitmap(bmp, image.getWidth() / 2, image.getHeight() / 2, true);
        Matrix matrix = new Matrix();
        matrix.postRotate(90);
        Bitmap scaledBitmap = Bitmap.createScaledBitmap(image, image.getWidth(), image.getHeight(), true);
        image = Bitmap.createBitmap(scaledBitmap, 0, 0, scaledBitmap.getWidth(), scaledBitmap.getHeight(), matrix, true);
        int[] pixels = new int[image.getWidth() * image.getHeight()];

        int height = image.getHeight(), width = image.getWidth();
        image.getPixels(pixels, 0, image.getWidth(), 0, 0, image.getWidth(), image.getHeight()); //gets pixels in pixel array

        final int startX = roundUp(width,2)-1;
        final int endX = width;
        final int endY = roundUp(height, 100/77)-1;
        final int startY = roundUp(height, 100/55)-1;
        final int widthX = endX - startX;
        final int heightY = endY - startY;

        int MidX = roundUp(widthX, 2)-1, Midy = roundUp(heightY,2)-1;
        int Q1x = roundUp(MidX, 2), Q3x = roundUp(MidX+widthX, 2), Q1y = roundUp(Midy,2), Q3y = roundUp(Midy+heightY,2);
        List<Integer> leftPixels = new ArrayList<Integer>(), rightPixels = new ArrayList<Integer>();
        /*for (int i = Q1y; i < Q1y+Q3y; i++){
            int z1 = (widthX * i)+1;
            int z2 = (widthX*i)+Midx+1;
            for (int x = z1; x<(z1+Q1x); x++){
                leftPixels.add(pixels[x]);
            }
            for (int x = z2; x<(z2+(widthX-Q3x));x++) {
                rightPixels.add(pixels[x]);
            }
        }
        for (int i = startY; i < endY; i++) {
            int z1 = (width * i) +1+startX;
            int z2 = z1+MidX;
           for (int x = z1; x < z2; x++) {
                leftPixels.add(pixels[x]);
            }
            for (int x = z2; x < (z2+(widthX-MidX-1)); x++) {
                rightPixels.add(pixels[x]);
            }
        }
        int lNum = leftPixels.size(), rNum = rightPixels.size();
        long lSum = 0, rSum = 0;
        long lAvg, rAvg;

        for (int i = 0; i < lNum;i++){
            lSum+= Color.red(leftPixels.get(i));
        }
        for (int i = 0; i <rNum;i++) {
            rSum+=  Color.red(rightPixels.get(i));
        }
        lAvg = roundUp(lSum, lNum);
        rAvg = roundUp(rSum, rNum);
        BetterLog.d("qqq", Long.toString(lAvg) + " " + Long.toString(rAvg));
        return (lAvg-RED > rAvg-RED);





    }*/
    //ceiling division, assumes both numbers are positive
    private static int roundUp(int num, int divisor) {
        return (num + divisor - 1) / divisor;
    }
    private static long roundUp(long num, long divisor) {
        return (num + divisor - 1) / divisor;
    }

    public enum Mode{
        MIDSPLIT, TWORECTANGLES
    }
}
/* NON-ANDROID VERSION: (USES IMAGEIO)
public class DavidClass {
    public static final long RED = Color.RED.getRed();
    //returns true if left is red (right is blue)
    //returns false if left if blue (right is red)
    public static boolean analyzePic(File f) throws IOException {
       BufferedImage image;
        image = ImageIO.read(f);
        int[] pixels = null;
        int height = image.getHeight(), width = image.getWidth();
       pixels = image.getRGB(0, 0, image.getWidth(), image.getHeight(), null, 0, image.getWidth()); //gets pixels in pixel array

        int Midx = roundUp(width, 2)-1, Midy = roundUp(height,2)-1;
        int Q1x = roundUp(Midx, 2), Q3x = roundUp(Midx+width, 2), Q1y = roundUp(Midy,2), Q3y = roundUp(Midy+height,2);
        List<Integer> leftPixels = new ArrayList<Integer>(), rightPixels = new ArrayList<Integer>();
        for (int i = Q1y; i < Q1y+Q3y; i++){
            int z1 = (width * i)+1;
            int z2 = (width*i)+Midx+1;
            for (int x = z1; x<(z1+Q1x); x++){
               leftPixels.add(pixels[x]);
            }
            for (int x = z2; x<(z2+(width-Q3x));x++) {
                rightPixels.add(pixels[x]);
            }
        }
        int lNum = leftPixels.size(), rNum = rightPixels.size();
        long lSum = 0, rSum = 0;
        long lAvg, rAvg;

        for (int i = 0; i < lNum;i++){
           lSum+= new Color(leftPixels.get(i)).getRed();
        }
        for (int i = 0; i <rNum;i++) {
            rSum+= new Color(rightPixels.get(i)).getRed();
        }
        lAvg = roundUp(lSum, lNum);
        rAvg = roundUp(rSum, rNum);
       return (lAvg-RED > rAvg-RED);

 */