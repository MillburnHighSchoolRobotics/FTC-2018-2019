package org.firstinspires.ftc.teamcode.TestingOpModes;

import android.graphics.Bitmap;
import virtualRobot.utils.BetterLog;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.vuforia.HINT;
import com.vuforia.Vuforia;

import org.firstinspires.ftc.robotcore.external.matrices.OpenGLMatrix;
import org.firstinspires.ftc.robotcore.external.matrices.VectorF;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackable;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackableDefaultListener;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackables;
import org.firstinspires.ftc.teamcode.R;

import java.util.concurrent.atomic.AtomicBoolean;

import virtualRobot.VuforiaLocalizerImplSubclass;
import virtualRobot.commands.DavidClass;

/**
 * Created by mehme_000 on 10/7/2016.
 */

@Disabled
@Autonomous(name ="Sensor: CameraNoBackend", group="Sensor")
public class TakePictureTest extends LinearOpMode {
    //private TakePictureTestGod tp;
    private AtomicBoolean redisLeft = new AtomicBoolean();

    @Override
    public void runOpMode() throws InterruptedException {
        VuforiaLocalizer.Parameters params = new VuforiaLocalizer.Parameters(R.id.cameraMonitorViewId);
        params.cameraDirection = VuforiaLocalizer.CameraDirection.BACK; //Default
        params.vuforiaLicenseKey = "AcXbD9X/////AAAAGVpq1gdfDkIPp+j5hv1iV5RZXLWAWV4F7je9gks+8lHhZb6mwCj7xy9mapHP6sKO9OrPv5kVQDXhB+T+Rn7V7GUm4Ub4rmCanqv4frx8gT732qJUnTEj9POMufR9skjlXSEODbpThxrLCPqobHeAeSA5dUmUik3Rck0lcwhElw5yOBN45iklYnvC9GpPRv128ALcgt9Zpw/shit0erKmuyrT62NRUKgoHNMm5xV/Xqj8Vgwke8ESap+nK7v+6lx35vDZ6ISNDVMMM8h0VqeL0745MNPJoI1vgiNRo30R7WwtPYME44koOrWMUIxMXghtqxq7AfFxb6sbin0i5KSUJWtLsqmZOrAXxjxdUwY8f8tw";
        params.cameraMonitorFeedback = VuforiaLocalizer.Parameters.CameraMonitorFeedback.AXES; //Default

        //VuforiaLocalizer vuforia = new VuforiaLocalizerImplSubclass(params);
        VuforiaLocalizerImplSubclass vuforia = new VuforiaLocalizerImplSubclass(params);
        Vuforia.setHint(HINT.HINT_MAX_SIMULTANEOUS_IMAGE_TARGETS, 5);

        VuforiaTrackables beacons = vuforia.loadTrackablesFromAsset("Beacons"); //xml file in FtcRobotController/src/main/assets
        VuforiaTrackables beaconsT = vuforia.loadTrackablesFromAsset("FTC_2016-17");

        beacons.get(0).setName("RedIsLeftSample");
        beacons.get(1).setName("BlueIsLeftSample");
        beacons.get(2).setName("RedIsLeft");
        beacons.get(3).setName("BlueIsLeft");
        beacons.get(4).setName("RedIsLeft3D");

        beaconsT.get(3).setName("Gears");

        beacons.activate();
        beaconsT.activate();

        waitForStart();

        while (opModeIsActive()) {
            for (VuforiaTrackable beac : beacons) {
                OpenGLMatrix pose = ((VuforiaTrackableDefaultListener) beac.getListener()).getPose();

                if (pose != null) {
                    VectorF translation = pose.getTranslation();
                    telemetry.addData(beac.getName() + "-Translate", translation);
                    //double degreesToTurn = Math.toDegrees(Math.atan2(translation.get(1), translation.get(2))); //0, 2 for landscape; 1, 2 for portrait
                    //telemetry.addData(beac.getName() + "-Degrees", degreesToTurn);
                }
                else
                    telemetry.addData("Tracking Object " + beac.getName() + ": ", false);
            }

            for (VuforiaTrackable beac : beaconsT) {
                OpenGLMatrix pose = ((VuforiaTrackableDefaultListener) beac.getListener()).getPose();

                if (pose != null) {
                    VectorF translation = pose.getTranslation();
                    telemetry.addData(beac.getName() + "-Translate", translation);
                    //double degreesToTurn = Math.toDegrees(Math.atan2(translation.get(1), translation.get(2))); //0, 2 for landscape; 1, 2 for portrait
                    //telemetry.addData(beac.getName() + "-Degrees", degreesToTurn);
                }
                else
                    telemetry.addData("Tracking Object " + beac.getName() + ": ", false);
            }

            telemetry.update();
        }

        if (vuforia.rgb != null) {
            Bitmap bm = Bitmap.createBitmap(vuforia.rgb.getWidth(), vuforia.rgb.getHeight(), Bitmap.Config.RGB_565);
            bm.copyPixelsFromBuffer(vuforia.rgb.getPixels());
            boolean analyzed = DavidClass.analyzePic2(bm);
            telemetry.addData("cameraReturn", analyzed + " ");
            telemetry.update();
            BetterLog.d("cameraReturn ", analyzed + " ");
            redisLeft.set(analyzed);
        }
    }
}
