package org.firstinspires.ftc.teamcode.TestingOpModes;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.SerialNumber;
import com.vuforia.CameraDevice;
import com.vuforia.CameraField;

import org.firstinspires.ftc.robotcore.external.function.Consumer;
import org.firstinspires.ftc.robotcore.external.function.Continuation;
import org.firstinspires.ftc.robotcore.external.hardware.camera.CameraCharacteristics;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;

import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.internal.system.Deadline;
import org.firstinspires.ftc.teamcode.JeffBot;
import org.firstinspires.ftc.teamcode.SahilClass;
//import org.firstinspires.ftc.teamcode.R;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.Scalar;
import org.opencv.videoio.VideoCapture;

import virtualRobot.VuforiaLocalizerImplSubclass;
import virtualRobot.utils.BetterLog;

@Autonomous(name = "Sahil Test", group = "Testing")
//@Disabled
public class SahilTest extends LinearOpMode{

    static {
        if(OpenCVLoader.initDebug()) {
            Log.d("opencv","yay it works");
        } else {
            Log.d("opencv","nope it doesnt work");
        }
    }

    @Override
    public void runOpMode() throws InterruptedException {



        VuforiaLocalizer.Parameters params = new VuforiaLocalizer.Parameters(hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName()));

        params.cameraDirection = VuforiaLocalizer.CameraDirection.BACK;
//        params.cameraName=hardwareMap.get(WebcamName.class, "Whatever the webcam is called");

        params.vuforiaLicenseKey = JeffBot.vuforiaKey;
        telemetry.addData("Vuforia Status: ", "Loading...");
        telemetry.update();
        VuforiaLocalizerImplSubclass vuforiaInstance = new VuforiaLocalizerImplSubclass(params);
        int n = CameraDevice.getInstance().getNumFields();
        for (int x = 0; x < n; x++) {
            CameraField cameraField = new CameraField();
            CameraDevice.getInstance().getCameraField(x,cameraField);
            Log.d("SahilClass", "type: " + cameraField.getType());
            Log.d("SahilClass", "key: " + cameraField.getKey());
        }
//        Log.d("SahilClass", "Field String: " + CameraDevice.getInstance().getFieldString("android.control.aeExposureCompensation"));
//        Log.d("SahilClass","Value: " + realVal);
//        Log.d("SahilClass", "Field String: " + CameraDevice.getInstance().getFieldString("iso"));
//        CameraDevice.getInstance().setField("iso","1600");

        float[] stepWrapper = new float[1];
        CameraDevice.getInstance().getFieldFloat("android.control.aeCompensationStep", stepWrapper);
        float step = stepWrapper[0];
        Log.d("SahilClass","Step: " + step);
        CameraDevice.getInstance().setField("android.control.aeCompensationStep", 0.5f);
        float[] stepWrapper1 = new float[1];
        CameraDevice.getInstance().getFieldFloat("android.control.aeCompensationStep", stepWrapper1);
        float step1 = stepWrapper1[0];
        Log.d("SahilClass","Step1: " + step1);

        long[] valWrapper = new long[1];
        CameraDevice.getInstance().getFieldInt64("android.control.aeExposureCompensation", valWrapper);
        long realVal = valWrapper[0];
        Log.d("SahilClass","Value: " + realVal);
        CameraDevice.getInstance().setField("android.control.aeExposureCompensation",-3);
        long[] valWrapper1 = new long[1];
        CameraDevice.getInstance().getFieldInt64("android.control.aeExposureCompensation", valWrapper1);
        long realVal1 = valWrapper1[0];
        Log.d("SahilClass","Value1: " + realVal1);
        Thread.sleep(3000);
        telemetry.addData("Vuforia Status: ", "Loaded!");
        telemetry.update();
        waitForStart();
        SahilClass sahilClass = new SahilClass(vuforiaInstance, 1000); //this only loops once after 1000 millis but keep this constraint just in case
        int pos = sahilClass.getThreeMineralPosition();
        telemetry.addData("Position", pos + "");
        telemetry.update();
//        }
    }
}
