package virtualRobot.commands;

import java.util.ArrayList;

import virtualRobot.Condition;
import virtualRobot.hardware.Servo;

/**
 * Created by shant on 10/9/2015.
 * Moves a Servo
 */

//Just directly use the robot object in LogicThread, there is no need for this command
@Deprecated
public class MoveServo extends Command {
    private Condition condition;
    private ArrayList<Object[]> servos = new ArrayList<Object[]>();

    public MoveServo() {
        condition = new Condition() {
            public boolean isConditionMet() {
                return false;
            }
        };

        servos = new ArrayList<Object[]>();
    }
    public MoveServo(Condition e) {
        condition = e;

        servos = new ArrayList<Object[]>();
    }

    public MoveServo(Servo[] servo, double[] newPosition) {
        this();

        for (int i = 0; i < servo.length; i++) {
            servos.add(new Object[] {servo[i], newPosition[i]});
        }
    }
    public MoveServo(Servo[] servo, double[] newPosition, Condition e) {
        this(e);

        for (int i = 0; i < servo.length; i++) {
            servos.add(new Object[] {servo[i], newPosition[i]});
        }

    }



    public void addServoChange(Servo servo, double newPosition) {
        servos.add(new Object[] {servo, new Double(newPosition)});
    }

    public void removeServoChange(Servo servo) {
        for (int i = 0; i < servos.size(); i++) {
            if (servos.get(i)[0] == servo) {
                servos.remove(i);
                break;
            }
        }
    }

    public void addCondition(Condition e) {
        condition = e;
    }

    public Condition getCondition() {
        return condition;
    }

    @Override
    public boolean changeRobotState() {
        int i = 0;
        boolean isInterrupted = false;
        MainLoop: while (!isInterrupted && i < servos.size()) {
            switch (checkConditionals()) {
                case BREAK:
                    break MainLoop;
                case END:
                    return isInterrupted;
            }

            ((Servo)servos.get(i)[0]).setPosition(((Double)servos.get(i)[1]));
            i++;
            
            if (Thread.currentThread().isInterrupted()) {
            	isInterrupted = true;
            	break;
            }
        }
        
        return isInterrupted;
    }

}
