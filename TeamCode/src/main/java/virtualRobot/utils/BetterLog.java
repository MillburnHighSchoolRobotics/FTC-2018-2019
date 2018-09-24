package virtualRobot.utils;

import android.util.Log;

import virtualRobot.utils.BetterLog;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ethan on 9/20/17.
 */

public class BetterLog {
    private static List<String> exceptions = new ArrayList<>();

    static {

    }

    public static void d(String tag, String msg){
        if (!exceptions.contains(tag))
            Log.d(tag, msg);
    }
}
