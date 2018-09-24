package virtualRobot.commands;

/**
 * Created by david on 1/25/18.
 */

public class ShittyPause extends Command {

    private int millis;

    public ShittyPause(int millis) {

        this.millis = millis;
    }
    @Override
    public boolean changeRobotState() throws InterruptedException {
        long orig = System.currentTimeMillis();
        while (System.currentTimeMillis() - orig < millis) {


        }
        return false;
    }
}
