package org.firstinspires.ftc.teamcode;

import android.util.Log;

import com.qualcomm.robotcore.hardware.HardwareMap;

import java.util.HashMap;
import java.util.Map;

public class WatchdogManager {
    private HashMap<String, Watchdog> watchdogs;
    private HashMap<String, Object> values;
    private HashMap<String, Integer> owners;
    private HardwareMap hardwareMap;
    private static final String TAG = "WatchdogManager";

    public WatchdogManager() {
        this.watchdogs = new HashMap<>();
        this.values = new HashMap<>();
        this.owners = new HashMap<>();
        this.hardwareMap = null;
    }

    public synchronized void clean() {
        for (Map.Entry<String, Watchdog> watchdog : watchdogs.entrySet()) {
            if (watchdog.getValue().isAlive()) {
                watchdog.getValue().interrupt();
                Log.d(TAG, "Killed Watchdog " + watchdog.getKey());
            }
        }
        watchdogs.clear();
        values.clear();
        owners.clear();
    }

    public synchronized void provision(String name, Class<? extends Watchdog> watchdogClass, Object... args) {
        Object[] passedArgs = new Object[args.length + 2];
        passedArgs[0] = Thread.currentThread();
        passedArgs[1] = hardwareMap;
        System.arraycopy(args, 0, passedArgs, 2, args.length);
        Class argTypes[] = new Class[args.length];
        for (int i = 0; i < passedArgs.length; i++) {
            argTypes[i] = passedArgs[i].getClass();
        }
        Watchdog wd;
        try {
            wd = watchdogClass.getDeclaredConstructor(argTypes).newInstance(passedArgs);
        } catch (Exception e) {
            Log.e(TAG, e.getMessage(), e);
            throw new RuntimeException();
        }
        watchdogs.put(name, wd);
        wd.start();
    }

    public synchronized void setValue(String name, Object obj) {
        if (!values.containsKey(name)) {
            values.put(name, obj);
            owners.put(name, Thread.currentThread().hashCode());
        } else if (owners.get(name) == Thread.currentThread().hashCode()) {
            values.put(name, obj);
        } else {
            Log.e(TAG, "Unable to set value for " + name + ": Not owned by thread " + Thread.currentThread().getName());
        }
    }

    public synchronized Object getValue(String name) {
        return values.get(name);
    }

    public synchronized void remove(String name) {
        Watchdog wd = watchdogs.get(name);
        wd.interrupt();
    }

    private static final WatchdogManager INSTANCE = new WatchdogManager();
    public static synchronized WatchdogManager getInstance() {
        return INSTANCE;
    }

    public void setHardwareMap(HardwareMap hardwareMap) {
        this.hardwareMap = hardwareMap;
    }
}
