package virtualRobot.logicThreads.competition;

import org.firstinspires.ftc.robotcore.external.navigation.RelicRecoveryVuMark;

import virtualRobot.commands.GetVuMarkSide;
import virtualRobot.commands.RotateEncoder;
import virtualRobot.commands.ShustinClass;
import virtualRobot.commands.Translate;
import virtualRobot.logicThreads.AutonomousLogicThread;
import virtualRobot.utils.GlobalUtils;

import static org.firstinspires.ftc.robotcore.external.navigation.RelicRecoveryVuMark.LEFT;
import static org.firstinspires.ftc.robotcore.external.navigation.RelicRecoveryVuMark.UNKNOWN;

/**
 * Created by ethan on 9/22/17.
 */

public class BlueNearBasicAutoLogic extends AutonomousLogicThread {
    private double power = 0.5;

    @Override
    protected void realRun() throws InterruptedException {
        int dist = 0;

        if (GlobalUtils.withoutVumark)
            currentVuMark = GlobalUtils.forcedVumark;
        else
            runCommand(new GetVuMarkSide(3000));

        robot.addToTelemetry("Current VuMark: ", currentVuMark);


        Thread.sleep(500);

        dist = 0;
        if (currentVuMark == UNKNOWN) {
            currentVuMark = LEFT;
        }
        switch (currentVuMark) {
            case LEFT:
                dist = 1200; //1926
                break;
            case CENTER:
                dist = 1500; //2126
                break;
            case RIGHT:
                dist = 1800;
                break;
        }

        runCommand(new Translate(dist, Translate.Direction.BACKWARD,0,power));
        runCommand(new RotateEncoder(-90,power));
        Thread.sleep(300);
        runCommand(new Translate(700, Translate.Direction.BACKWARD, 0, power));
//        runCommand(new Translate(300, Translate.Direction.FORWARD, 0, power));
// MULTIGLYPH

        Thread.sleep(500);
        RelicRecoveryVuMark mark = currentVuMark == RelicRecoveryVuMark.CENTER ? RelicRecoveryVuMark.RIGHT : RelicRecoveryVuMark.CENTER;
        Thread.sleep(2000);
        runCommand(new Translate(1400, Translate.Direction.FORWARD,0,0.5f));
        Thread.sleep(3000);
        Thread.sleep(500);
        runCommand(new RotateEncoder(-90,1));
        Thread.sleep(500);
        runCommand(new Translate(100, Translate.Direction.BACKWARD, 0, 0.5f));
        int lastOffset = -1;
        offset.set(20);
        while (true) {
//            runCommand(new ShustinClass(mark));
            offset.set(offset.get() - 10);
            Thread.sleep(500);
            robot.addToTelemetry("Offset", offset.get());
            if (Math.abs(offset.get()) < 7) {
                break;
            }
            if (lastOffset >= 0 && Math.abs(offset.get()) - Math.abs(lastOffset) > 75) continue;
            lastOffset = Math.abs(offset.get());
            runCommand(new Translate(offset.get() * 10, Translate.Direction.LEFT, 0, 0.9));
            runCommand(new RotateEncoder(-90,0.9));
        }
        runCommand(new Translate(1000, Translate.Direction.BACKWARD, 0, 0.5f));
        Thread.sleep(3000);
        runCommand(new Translate(500, Translate.Direction.BACKWARD, 0, 0.5f));
        Thread.sleep(1000);
        runCommand(new Translate(500, Translate.Direction.FORWARD, 0, 0.5f));
    }
}
