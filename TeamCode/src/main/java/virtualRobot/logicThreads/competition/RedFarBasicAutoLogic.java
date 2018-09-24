package virtualRobot.logicThreads.competition;

import virtualRobot.commands.GetVuMarkSide;
import virtualRobot.commands.RotateEncoder;
import virtualRobot.commands.Translate;
import virtualRobot.logicThreads.AutonomousLogicThread;
import virtualRobot.utils.GlobalUtils;

import static org.firstinspires.ftc.robotcore.external.navigation.RelicRecoveryVuMark.LEFT;
import static org.firstinspires.ftc.robotcore.external.navigation.RelicRecoveryVuMark.UNKNOWN;

/**
 * Created by ethan on 9/22/17.
 */

public class RedFarBasicAutoLogic extends AutonomousLogicThread {
    private double power = 0.5;

    @Override
    protected void realRun() throws InterruptedException {
        int rot = 0;
        int push = 0;

        if (GlobalUtils.withoutVumark)
            currentVuMark = GlobalUtils.forcedVumark;
        else
            runCommand(new GetVuMarkSide(3000));

        robot.addToTelemetry("Current VuMark: ", currentVuMark);

        robot.moveJewelServo(true);
        Thread.sleep(1000);

        int red = robot.getColorSensor().getRed();
        int blue = robot.getColorSensor().getBlue();
        if ((red != 0 || blue != 0)) {
            double rat = red / (double) blue;
            robot.addToTelemetry("CS", red + " " + blue + " " + rat);
            if (rat >= 1.5) {
                robot.moveJewelRotater(1);
                Thread.sleep(500);
                robot.moveJewelRotater(0);
            } else if (rat <= 0.6) {
                robot.moveJewelRotater(-1);
                Thread.sleep(500);
                robot.moveJewelRotater(0);
            }
        }
        robot.moveJewelServo(false);
        Thread.sleep(500);

        rot = 0;
        if (currentVuMark == UNKNOWN) {
            currentVuMark = LEFT;
        }
        switch (currentVuMark) {
            case RIGHT:
                rot = 180-15; //1926
                push = 700;
                break;
            case CENTER:
                rot = 180-37; //2126
                push = 850;
                break;
            case LEFT:
                rot = 180-60;
                push = 1000;
                break;
        }
        runCommand(new Translate(1200, Translate.Direction.FORWARD,0,power));
        runCommand(new RotateEncoder(rot,power));
        robot.moveFlipper(true);
        Thread.sleep(500);
        runCommand(new Translate(push, Translate.Direction.BACKWARD, 0, power));
        runCommand(new Translate(300, Translate.Direction.FORWARD, 0, power));
    }
}
