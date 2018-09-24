package virtualRobot.hardware;

/**
 * Created by Alex on 10/1/2015.
 * Virtual ContinousRotationServo. Set Position
 */
public class ContinuousRotationServo {

    double speed = 0;

    public synchronized double getSpeed () {
    	double retVal = 0;
    	synchronized (this) {
    		retVal = speed;
    	}
        return retVal;
    }

    public synchronized void setSpeed (double speed) {
        speed = Math.max (Math.min(speed, 1), -1);
    	synchronized (this) {
    		this.speed = speed;
    	}
    }
}
