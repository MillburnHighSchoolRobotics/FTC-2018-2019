package org.firstinspires.ftc.teamcode.watchdog;

import android.util.Log;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.Hardware;

import org.firstinspires.ftc.robotcore.internal.system.AppUtil;

public abstract class Watchdog extends Thread {
    protected final Thread parentThread;
    protected final HardwareMap hardwareMap;
    public Watchdog(Thread parentThread, HardwareMap hardwareMap) {
        this.parentThread = parentThread;
        this.hardwareMap = hardwareMap;




    }
    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted() && !parentThread.isInterrupted()) {
            if (!(WatchdogManager.getInstance().getCurrentAuton() == null || (WatchdogManager.getInstance().getCurrentAuton().opModeIsActive() || !WatchdogManager.getInstance().getCurrentAuton().isStarted()))) {
                WatchdogManager.getInstance().clean();
                break;
            }
            loop();
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                Log.e("Watchdog", e.getMessage(), e);
                break;
            }
        }
    }
    protected abstract void loop();
}
