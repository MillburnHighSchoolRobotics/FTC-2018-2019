package virtualRobot.commands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import virtualRobot.Condition;
import virtualRobot.LogicThread;

/**
 * Created by DOSullivan on 10/28/15.
 * this command will create a new thread happening at the same time as the other commands in the queue of the logic thread
 */
public class SpawnNewThread extends Command {
	private List<LogicThread> logic;
    private List<Thread> t;

    private Condition condition;

    public SpawnNewThread() {
    	logic = new ArrayList<LogicThread>();
    	t = new ArrayList<Thread>();
    	
    	condition = new Condition() {
    		public boolean isConditionMet() {
    			return false;
    		}
    	};
    }

    public SpawnNewThread(LogicThread... threads){
        this();
        logic.addAll(Arrays.asList(threads));
    }
    
    public SpawnNewThread(List<LogicThread> l) {
    	this();
    	logic = l;
    }

    @Override
    public boolean changeRobotState() {
        int i = 0;
        boolean isInterrupted = false;
        while (!condition.isConditionMet() && i < logic.size() ) {
        	Thread temp = new Thread(logic.get(i++));
        	temp.start();
        	t.add(temp);
        	
        	if (Thread.currentThread().isInterrupted()) {
        		isInterrupted = true;
        		break;
        	}
        }
        
        return isInterrupted;
    }
    
    public List<Thread> getThreads() {
        return t;
    }
    
    public void addLogicThread(LogicThread l) {
    	logic.add(l);
    }
    public void addCondition(Condition e) {
        condition = e;
    }
}
