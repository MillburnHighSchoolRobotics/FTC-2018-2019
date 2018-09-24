package org.firstinspires.ftc.teamcode.TestingOpModes;

import com.qualcomm.hardware.lynx.LynxModule;
import com.qualcomm.hardware.lynx.LynxNackException;
import com.qualcomm.hardware.lynx.LynxUsbDevice;
import com.qualcomm.hardware.lynx.commands.standard.LynxDiscoveryCommand;
import com.qualcomm.hardware.lynx.commands.standard.LynxSetModuleLEDColorCommand;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.configuration.LynxUsbDeviceConfiguration;

/**
 * Created by Ethan Mak on 8/29/2017.
 *
 * Used to test the LynxModule object and the command system for the module
 */
@Disabled
@Autonomous(name="Shenanigans", group = "Testing")
public class ShenanigansOpMode extends OpMode {
    byte purple = (byte) 153;
    byte black = (byte) 0;

    LynxUsbDevice root;
    LynxModule child;

    LynxDiscoveryCommand colorCommand;
    @Override
    public void init() {
        root = (LynxUsbDevice) hardwareMap.get("Expansion Hub Portal 1");
        child = new LynxModule(root, 2, true);
        colorCommand = new LynxDiscoveryCommand(child);
    }

    @Override
    public void loop() {
        try
        {
            colorCommand.send();
        }
        catch (InterruptedException | LynxNackException e)
        {
            e.printStackTrace();
        }
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
