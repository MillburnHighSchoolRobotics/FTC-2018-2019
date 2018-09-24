package virtualRobot.commands;

import android.graphics.Bitmap;
import virtualRobot.utils.BetterLog;

import java.util.concurrent.atomic.AtomicBoolean;

import virtualRobot.Condition;
import virtualRobot.VuforiaLocalizerImplSubclass;
/**
 * Takes a Picture Using Vuforia
 */

public class FTCTakePicture extends Command{
    AtomicBoolean redisLeft;
    AtomicBoolean isRed;
    AtomicBoolean isRedAndRedIsLeft;
    VuforiaLocalizerImplSubclass vuforia;
    Mode mode;
    public FTCTakePicture (Mode mode, AtomicBoolean red, VuforiaLocalizerImplSubclass vuforia) {
        this.mode=mode;
        this.redisLeft = red;
        this.isRed = red;
        this.vuforia = vuforia;
    }
    public FTCTakePicture (Mode mode, AtomicBoolean red, AtomicBoolean isRedAndRedIsLeft, VuforiaLocalizerImplSubclass vuforia) {
        this.mode=mode;
        this.redisLeft = red;
        this.isRed = red;
        this.isRedAndRedIsLeft = isRedAndRedIsLeft;
        this.vuforia = vuforia;
    }

    public boolean changeRobotState() throws InterruptedException {

        //Converts VuforiaLocalizerImplSubclass' picture to a bitmap for analysis by DavidClass
        if (vuforia.rgb != null){
            Bitmap bm =  Bitmap.createBitmap(vuforia.rgb.getWidth(), vuforia.rgb.getHeight(), Bitmap.Config.RGB_565);
            bm.copyPixelsFromBuffer(vuforia.rgb.getPixels());
            if (mode==Mode.TAKING_PICTURE){
                boolean analyzed = DavidClass.analyzePic2(bm);
                BetterLog.d("DavidClass","Analyzed: " + analyzed + " ");
                redisLeft.set(analyzed);
            }
            else {
                boolean[] analyzed = DavidClass.checkIfAllRed(bm);
                BetterLog.d("DavidClass","Check: " + analyzed[0] + " isAllRedAndRedIsLeft " + analyzed[1]);
                isRed.set(analyzed[0]);
                isRedAndRedIsLeft.set(analyzed[1]);
            }
       }

        return Thread.currentThread().isInterrupted();
    }

    public enum Mode{
       TAKING_PICTURE,
        CHECKING_PICTURE
    }
}