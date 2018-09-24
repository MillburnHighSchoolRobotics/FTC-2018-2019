package org.firstinspires.ftc.teamcode.TestingOpModes;

import android.annotation.TargetApi;
import android.content.Context;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraManager;
import android.os.Build;
import android.util.Size;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

/**
 * Created by Ethan Mak on 11/24/2017.
 */
@Disabled
public class DualCameraTest extends OpMode {
    Context context;

    String frontId = "";
    String backId = "";

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void init() {
        context = hardwareMap.appContext;

        CameraManager manager = (CameraManager) context.getSystemService(Context.CAMERA_SERVICE);

        try {
            for (String cameraId : manager.getCameraIdList()) {
                switch (manager.getCameraCharacteristics(cameraId).get(CameraCharacteristics.LENS_FACING)) {
                    case CameraCharacteristics.LENS_FACING_BACK:
                        backId = cameraId;
                        break;
                    case CameraCharacteristics.LENS_FACING_FRONT:
                        frontId = cameraId;
                        break;
                }
            }

            Size cameraSize = new Size(1080, 1920);


        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void loop() {

    }
}
