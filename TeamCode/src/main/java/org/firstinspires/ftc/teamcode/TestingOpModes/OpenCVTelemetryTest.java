package org.firstinspires.ftc.teamcode.TestingOpModes;

import android.graphics.Bitmap;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.Mat;

import java.io.IOException;

import retrofit2.Retrofit;
import virtualRobot.VuforiaLocalizerImplSubclass;
import virtualRobot.telemetry.CTelemetry;
import virtualRobot.utils.BetterLog;
import virtualRobot.telemetry.MatConverterFactory;

/**
 * Created by david on 11/7/17.
 */
@Disabled
@TeleOp( name="OpenCVTelemetryTest", group="Testing" )
public class OpenCVTelemetryTest extends LinearOpMode {

    VuforiaLocalizerImplSubclass vuforiaInstance;
    private int width, height;
    static {
        if(!OpenCVLoader.initDebug()){
            BetterLog.d("OpenCV", "OpenCV not loaded");
        } else {
            BetterLog.d("OpenCV", "OpenCV loaded");
        }
    }

    @Override
    public void runOpMode() throws InterruptedException {
        VuforiaLocalizer.Parameters params = new VuforiaLocalizer.Parameters(hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName()));
        params.cameraDirection = VuforiaLocalizer.CameraDirection.BACK;
        params.vuforiaLicenseKey = "AdVGalv/////AAAAGYhiDIdk+UI+ivt0Y7WGvUJnm5cKX/lWesW2pH7gnK3eOLTKThLekYSO1q65ttw7X1FvNhxxhdQl3McS+mzYjO+HkaFNJlHxltsI5+b4giqNQKWhyKjzbYbNw8aWarI5YCYUFnyiPPjH39/CbBzzFk3G2RWIzNB7cy4AYhjwYRKRiL3k33YvXv0ZHRzJRkMpnytgvdv5jEQyWa20DIkriC+ZBaj8dph8/akyYfyD1/U19vowknmzxef3ncefgOZoI9yrK82T4GBWazgWvZkIz7bPy/ApGiwnkVzp44gVGsCJCUFERiPVwfFa0SBLeCrQMrQaMDy3kOIVcWTotFn4m1ridgE5ZP/lvRzEC4/vcuV0";
        vuforiaInstance = new VuforiaLocalizerImplSubclass(params);

        CTelemetry ctel = new Retrofit.Builder()
                .baseUrl("http://172.20.95.70:8080/")
                .addConverterFactory(MatConverterFactory.create())
                .build()
                .create(CTelemetry.class);

        waitForStart();

        width = vuforiaInstance.rgb.getBufferWidth();
        height = vuforiaInstance.rgb.getHeight();

        Bitmap bm = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);

        boolean firstPress = true;

        while (opModeIsActive()) {
            Mat img = new Mat();
            bm.copyPixelsFromBuffer(vuforiaInstance.rgb.getPixels());
            Utils.bitmapToMat(bm, img);

            if (firstPress && gamepad1.a) {
                firstPress = false;
                telemetry.addData("ran", "ran");
                try {
                    ctel.sendImage("Image", img).execute();
                } catch (IOException err) {
                    telemetry.addData("Error", err.getMessage());
                }
            } else {
                firstPress = true;
            }
            img.release();
            telemetry.update();
        }
    }
}
