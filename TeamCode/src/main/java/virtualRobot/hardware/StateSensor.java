package virtualRobot.hardware;

import virtualRobot.SallyJoeBot;
import virtualRobot.utils.MathUtils;
import virtualRobot.utils.Matrix;
import virtualRobot.utils.Vector3f;

/**
 * Created by ethachu19 on 12/1/16
 * StateSensor detects robots velocity and position
 */
public class StateSensor extends Sensor {
    private Vector3f position;
    private Vector3f velocity;
    private SallyJoeBot robot = null;

    public StateSensor() {
        position = new Vector3f();
        velocity = new Vector3f();
    }

    public StateSensor setRobot(SallyJoeBot robot) {
        this.robot = robot;
        return this;
    }

    public synchronized Vector3f getPosition() {
        return new Vector3f(position);
    }

    public synchronized Vector3f getVelocity() {
        return new Vector3f(velocity);
    }

    public synchronized void setPosition(Vector3f v) { this.position = v; }

    public synchronized void setVelocity(Vector3f v) { this.velocity = v; }
}
