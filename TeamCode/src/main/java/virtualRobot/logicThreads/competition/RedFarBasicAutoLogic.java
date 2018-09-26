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
    protected void realRun() throws InterruptedException {}
}
