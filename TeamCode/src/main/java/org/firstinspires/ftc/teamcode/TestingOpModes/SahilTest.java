package org.firstinspires.ftc.teamcode.TestingOpModes;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.SerialNumber;

import org.firstinspires.ftc.robotcore.external.function.Consumer;
import org.firstinspires.ftc.robotcore.external.function.Continuation;
import org.firstinspires.ftc.robotcore.external.hardware.camera.CameraCharacteristics;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;

import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.internal.system.Deadline;
import org.firstinspires.ftc.teamcode.JeffBot;
import org.firstinspires.ftc.teamcode.SahilClass;
import org.firstinspires.ftc.teamcode.R;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.Scalar;
import org.opencv.videoio.VideoCapture;

import virtualRobot.VuforiaLocalizerImplSubclass;
import virtualRobot.utils.BetterLog;

@Autonomous(name = "Sahil Test", group = "Testing")
public class SahilTest extends LinearOpMode {

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
        VuforiaLocalizerImplSubclass vuforiaInstance = new VuforiaLocalizerImplSubclass(params);
        waitForStart();
        SahilClass sahilClass = new SahilClass(vuforiaInstance, 1000); //this only loops once after 1000 millis but keep this constraint just in case
        int pos = sahilClass.getThreeMineralPosition();
        telemetry.addData("Position", pos + "");
        telemetry.update();
//        }
    }
}
