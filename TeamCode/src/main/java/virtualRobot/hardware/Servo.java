package virtualRobot.hardware;

import virtualRobot.utils.MathUtils;

/**
 * Created by Alex on 9/30/2015.
 * The virtual servo component
 */
public class Servo {
    private volatile double position = 0;

    public synchronized double getPosition() {
        return position;
    }

    public synchronized void setPosition(double position) {
        if (Double.isNaN(position))
            position = 0;
        this.position = MathUtils.clamp(position,0,1);
    }
}
