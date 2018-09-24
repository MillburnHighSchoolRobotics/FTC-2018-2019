package virtualRobot.utils;

import android.app.Activity;
import android.support.annotation.NonNull;

import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.navigation.RelicRecoveryVuMark;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackable;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackables;
import org.firstinspires.ftc.teamcode.ReflectionUpdateThread;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.PrintWriter;

import virtualRobot.SallyJoeBot;
import virtualRobot.VuforiaLocalizerImplSubclass;

import static org.firstinspires.ftc.robotcore.external.navigation.RelicRecoveryVuMark.*;

/**
 * Created by ethan on 9/20/17.
 */

public class GlobalUtils {
    private static Activity currentActivity = null;
    public static VuforiaLocalizerImplSubclass vuforiaInstance = null;
    public static RelicRecoveryVuMark forcedVumark = LEFT;
    public static VuforiaTrackables relicTrackables = null;
    public static VuforiaTrackable relicTemplate = null;
    private static int vumarkNum = 0;
    public static ReflectionUpdateThread currentUpdateThread = null;
    public static ElapsedTime runtime = new ElapsedTime();
    public static ThreadGroup updateThreadGroup = null;
    public static PrintWriter crashLogger = null;
    public static final boolean withoutVumark = false;
    public static SallyJoeBot.Team currentTeam;
    public static boolean isCVLoaded = false;

    public static Activity getCurrentActivity() {
        if (currentActivity == null)
            throw new ExceptionInInitializerError("Failed to set current activity");
        return currentActivity;
    }

    public static void setCurrentActivity(@NonNull Activity activity) {
        currentActivity = activity;
    }

    public static void incrementVumark() {
        vumarkNum = (vumarkNum + 1) % 3;
        forcedVumark = new RelicRecoveryVuMark[] {LEFT, CENTER, RIGHT}[vumarkNum];
    }
}
