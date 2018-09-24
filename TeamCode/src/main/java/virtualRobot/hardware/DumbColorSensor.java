package virtualRobot.hardware;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by david on 11/3/17.
 */

public class DumbColorSensor {
//    private AtomicInteger red;
//    private AtomicInteger green;
//    private AtomicInteger blue;
    private int red;
    private int green;
    private int blue;

//	public DumbColorSensor() {
//		red = new AtomicInteger();
//		green = new AtomicInteger();
//		blue = new AtomicInteger();
//	}

    public synchronized int getRed() {
        return red;
    }

    public synchronized void setRed(int red) {
        this.red = red;
    }

    public synchronized int getGreen() {
        return green;
    }

    public synchronized void setGreen(int green) {
        this.green = green;
    }

    public synchronized int getBlue() {
        return blue;
    }

    public synchronized void setBlue(int blue) {
        this.blue = blue;
    }


}
