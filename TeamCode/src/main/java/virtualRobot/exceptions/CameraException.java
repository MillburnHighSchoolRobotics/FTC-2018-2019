package virtualRobot.exceptions;

/**
 * Created by Ethan Mak on 8/31/2017.
 */

public class CameraException extends RuntimeException {
    public CameraException(String s) {
        super(s);
    }

    public CameraException() {
        super();
    }
}
