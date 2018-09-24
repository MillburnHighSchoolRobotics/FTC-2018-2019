package virtualRobot.monitorThreads;

import java.util.Vector;

import virtualRobot.MonitorThread;
import virtualRobot.utils.Vector3f;

/**
 * Created by ethachu19 on 12/5/2016.
 */

public class CollisionMonitor extends MonitorThread {
    Vector3f lastAccel = new Vector3f();
    private final double COLLISION_THRESHOLD_DELTA_G = 0.5;

    public CollisionMonitor() {
        lastAccel = robot.getImu().getLinearAccel();
    }

    @Override
    public boolean setStatus() {
        Vector3f accel = robot.getImu().getLinearAccel();
        double curr_world_linear_accel_x = accel.x;
        double currentJerkX = curr_world_linear_accel_x - lastAccel.x;
        lastAccel.x = curr_world_linear_accel_x;
        double curr_world_linear_accel_y = accel.y;
        double currentJerkY = curr_world_linear_accel_y - lastAccel.y;
        lastAccel.y = curr_world_linear_accel_y;

        return !((Math.abs(currentJerkX) > COLLISION_THRESHOLD_DELTA_G) ||
                (Math.abs(currentJerkY) > COLLISION_THRESHOLD_DELTA_G));
    }
}
