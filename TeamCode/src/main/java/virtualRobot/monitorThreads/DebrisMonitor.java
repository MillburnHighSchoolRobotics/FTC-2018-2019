package virtualRobot.monitorThreads;

import virtualRobot.utils.BetterLog;

import virtualRobot.SallyJoeBot;
import virtualRobot.MonitorThread;

/**
 * Created by shant on 1/10/2016.
 * Automatically stops robot if it's tilted at an angle (it's caught on debris)
 */
public class DebrisMonitor extends MonitorThread {


    @Override
    public boolean setStatus() {
        double totalAngle = Math.sqrt(Math.pow(robot.getImu().getRoll(), 2) + Math.pow(robot.getImu().getPitch(), 2));
        if (totalAngle > 2.5) {
            BetterLog.d("RoboAngle", robot.getImu().getRoll() + " " + robot.getImu().getPitch() + " " + totalAngle);
            BetterLog.d("RoboAngle", "Robot died in debris thread");
            return false;
        }
        BetterLog.d("RoboAngle", "we still in here " + robot.getImu().getRoll() + " " + robot.getImu().getPitch());
        return true;
    }

}
