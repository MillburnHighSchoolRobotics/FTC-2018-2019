package virtualRobot.logicThreads;

import org.firstinspires.ftc.robotcore.external.navigation.RelicRecoveryVuMark;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import virtualRobot.LogicThread;
import virtualRobot.commands.Translate;

/**
 * Created by Ethan Mak on 9/10/2017.
 */

public abstract class AutonomousLogicThread extends LogicThread {
    public volatile RelicRecoveryVuMark currentVuMark = RelicRecoveryVuMark.UNKNOWN;
    public AtomicBoolean redIsLeft = new AtomicBoolean(false);
    public AtomicInteger offset = new AtomicInteger(0);


//    protected void depositGlyph() throws InterruptedException {
//        runCommand(new Translate(100, Translate.Direction.FORWARD, 0));
//        //TODO: RUN BOX TO DEPOSIT GLYPH
//        runCommand(new Translate(100, Translate.Direction.BACKWARD, 0));
//    }
}
