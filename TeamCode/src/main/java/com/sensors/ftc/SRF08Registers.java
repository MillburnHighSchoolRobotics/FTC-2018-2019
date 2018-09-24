package com.sensors.ftc;

/**
 * Created by ethachu19 on 11/2/2016.
 */

public class SRF08Registers {

    /***************************
     * Who Am I
     ***************************/
    public static byte SOFTWARE_REVISION = 0x00;

    /*********************
     * Commands (VERY IMPORTANT)
     **********************/
    public static byte COMMAND = 0x00;

    /*******************************
     * Limits
     ********************************/
    public static byte MAXGAIN = 0x01;
    public static byte RANGING_DATA = 0x02;

    /******************************
     * Sensors
     *******************************/
    public static byte LIGHTSENSOR = 0x01;
    public static byte FIRST_ECHO = 0x02;

}
