package virtualRobot.commands;

/**
 * Created by ethachu19 on 3/31/2017.
 */

public final class TestCommand extends Command {

    private TestCommand() {
        throw new UnsupportedOperationException("Cannot instantiate this class");
    }

    double tp = 1;
    @Override
    protected int activate(String s) {
        switch(s) {
            case "BREAK":
                return BREAK;
            case "END":
                return END;
            case "SLOW":
                tp = 0.15;
                break;
            case "SPEED":
                tp = 1.0;
                break;
        }
        return NO_CHANGE;
    }

    @Override
    public boolean changeRobotState() throws InterruptedException {
        boolean isInterrupted = false;
        MainLoop: while(!isInterrupted) {
            switch (checkConditionals()) {
                case BREAK:
                    break MainLoop;
                case END:
                    return isInterrupted;
            }

            if (Thread.currentThread().isInterrupted()) {
                isInterrupted = true;
                break;
            }
            try {
                Thread.sleep(5);
            } catch (InterruptedException ex) {
                isInterrupted = true;
                break;
            }
        }
        return isInterrupted;
    }
}
