package org.firstinspires.ftc.teamcode;

import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.imgcodecs.Imgcodecs;

import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.Random;

public class FileWrite {
    public static void recordImg(Mat mat, String name) throws IOException {
        Random rand = new Random();
//        File file = new File("/sdcard/8405Logging/",Integer.toString(rand.nextInt())+".jpeg");
        final MatOfByte buf = new MatOfByte();
        Imgcodecs.imencode(".jpeg", mat, buf);
        FileOutputStream out = new FileOutputStream("/storage/emulated/0/sdcard/8405Logging/"+new Date(System.currentTimeMillis()).toString()+name+".jpeg");
        out.write(buf.toArray());
        out.close();
    }
}
