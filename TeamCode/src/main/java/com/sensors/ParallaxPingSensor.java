package com.sensors;

import virtualRobot.utils.BetterLog;

import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.DigitalChannelController;

/**
 * Created by ethachu19 on 10/31/2016.
 */

@Deprecated
public class ParallaxPingSensor {

    DigitalChannel channel;

    public ParallaxPingSensor(DigitalChannel channel) {
        this.channel = channel;
    }

    public double getResponseTime() {
        channel.setMode(DigitalChannelController.Mode.OUTPUT);
        try {
            channel.setState(false);
            Thread.sleep(0, 2000);
            channel.setState(true);
            Thread.sleep(0, 5000);
            channel.setState(false);
        } catch (InterruptedException ex) {
            return -1;
        }

        long start = System.nanoTime();
        channel.setMode(DigitalChannelController.Mode.INPUT);
        while (!channel.getState()) {
            BetterLog.d("ChannelLoop", "In Loop Channel");
        }
        return (System.nanoTime() - start)/1000;
    }

    public double getDistanceCM() {
        return getResponseTime() / 29 / 2;
    }

    public double getDistanceInches() {
        return getResponseTime() / 74 / 2;
    }
}
