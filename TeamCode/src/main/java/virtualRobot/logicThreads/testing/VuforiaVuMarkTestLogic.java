package virtualRobot.logicThreads.testing;

import android.provider.Settings;
import android.util.Log;

import org.firstinspires.ftc.robotcore.external.navigation.RelicRecoveryVuMark;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackable;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackables;

import virtualRobot.VuforiaLocalizerImplSubclass;
import virtualRobot.logicThreads.AutonomousLogicThread;
import virtualRobot.utils.GlobalUtils;

/**
 * Created by david on 1/24/18.
 */

public class VuforiaVuMarkTestLogic extends AutonomousLogicThread {
    @Override
    protected void realRun() throws InterruptedException {
        VuforiaLocalizerImplSubclass vuforia = GlobalUtils.vuforiaInstance;
        VuforiaTrackables relicTrackables = vuforia.loadTrackablesFromAsset("RelicVuMark");
        VuforiaTrackable relicTemplate = relicTrackables.get(0);
        relicTemplate.setName("relicVuMarkTemplate");
        relicTrackables.activate();
        int meme = 0;
        while (true) {
            RelicRecoveryVuMark vuMark = RelicRecoveryVuMark.from(relicTemplate);
            Log.d("VuMarkNum", vuMark.name() + " " + meme++);
            if (vuMark != RelicRecoveryVuMark.UNKNOWN) {
                robot.addToTelemetry("VuMark", vuMark.name());
            }
        }
    }
}
