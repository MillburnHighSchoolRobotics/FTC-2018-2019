package org.firstinspires.ftc.teamcode;

import virtualRobot.utils.BetterLog;

import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;

import java.util.ArrayList;

import virtualRobot.LogicThread;
import virtualRobot.VuforiaLocalizerImplSubclass;
//import virtualRobot.LogicThreads.deprecated.FPSLogicThread;
//import virtualRobot.LogicThreads.deprecated.FireBallsLogicThread;
//import virtualRobot.LogicThreads.deprecated.PIDTesterLogicThread;
//import virtualRobot.LogicThreads.deprecated.RotateAutoPIDGod;
//import virtualRobot.LogicThreads.deprecated.TeleopLogicThread;
//import virtualRobot.LogicThreads.deprecated.TranslateAutoPIDGod;
//import virtualRobot.LogicThreads.deprecated.TranslateTeleopPIDGod;
//import virtualRobot.LogicThreads.deprecated.testingTranslateLogicThread;

/**
 * Created by 17osullivand on 11/9/16.
 */

public class CreateVuforia implements Runnable {
    static final ArrayList<Class<? extends LogicThread>> exception = new ArrayList<>();
    Class<? extends LogicThread> LogicThread;
    LogicThread vuforiaEverywhere;
    Thread t;
    private boolean good = false;

    static {
//        exception.add(TeleopLogicThread.class);
//        //exception.add(PIDLineFollowerGod.class);
//        exception.add(PIDTesterLogicThread.class);
//        exception.add(RotateAutoPIDGod.class);
//        exception.add(FPSLogicThread.class);
//        exception.add(testingTranslateLogicThread.class);
//        exception.add(FireBallsLogicThread.class);
//        exception.add(TranslateAutoPIDGod.class);
//        exception.add(TranslateTeleopPIDGod.class);
    }

    public CreateVuforia(Class<? extends LogicThread> g,LogicThread vuforiaEverywhere, Thread t) {
        LogicThread = g;
//        this.vuforiaEverywhere = vuforiaEverywhere;
        this.t = t;
    }


    @Override
    public void run() {
//        try {
            if (!exception.contains(LogicThread)){//&& !LogicThread.equals(PIDTesterLogicThread.class) && !LogicThread.equals(PIDLineFollowerGod.class)) {
                VuforiaLocalizer.Parameters params = new VuforiaLocalizer.Parameters(R.id.cameraMonitorViewId);
                params.cameraDirection = VuforiaLocalizer.CameraDirection.BACK;
                params.vuforiaLicenseKey = "AdVGalv/////AAAAGYhiDIdk+UI+ivt0Y7WGvUJnm5cKX/lWesW2pH7gnK3eOLTKThLekYSO1q65ttw7X1FvNhxxhdQl3McS+mzYjO+HkaFNJlHxltsI5+b4giqNQKWhyKjzbYbNw8aWarI5YCYUFnyiPPjH39/CbBzzFk3G2RWIzNB7cy4AYhjwYRKRiL3k33YvXv0ZHRzJRkMpnytgvdv5jEQyWa20DIkriC+ZBaj8dph8/akyYfyD1/U19vowknmzxef3ncefgOZoI9yrK82T4GBWazgWvZkIz7bPy/ApGiwnkVzp44gVGsCJCUFERiPVwfFa0SBLeCrQMrQaMDy3kOIVcWTotFn4m1ridgE5ZP/lvRzEC4/vcuV0";
                BetterLog.d("VuforiaCamera", "Vuforia Initialization");
                VuforiaLocalizerImplSubclass vuforia = new VuforiaLocalizerImplSubclass(params);
//                vuforiaEverywhere = LogicThread.newInstance();
//                vuforiaEverywhere.setVuforia(vuforia);
                t = new Thread(vuforiaEverywhere);
            } else {
//                t = new Thread(LogicThread.newInstance());
                BetterLog.d("VuforiaCamera", "Only creating LogicThread");
            }
//        } catch (InstantiationException e) {
//            return;
//        } catch (IllegalAccessException e) {
//            return;
//        }
        good = true;
    }

    public boolean getGood() { //son
        return good;
    }
}