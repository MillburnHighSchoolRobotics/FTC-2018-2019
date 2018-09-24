package virtualRobot.commands;

import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;

//import com.qualcomm.ftcrobotcontroller.FtcRobotControllerActivity;

import java.io.IOException;

import virtualRobot.utils.GlobalUtils;

/**
 * Created by shant on 1/29/2016.
 * Plays a Music file
 */
public class PlayMusic extends Command {
    private String fileName;
    private boolean async;

    public PlayMusic(String fileName) {
        this(fileName, true);
    }

    public PlayMusic (String fileName, boolean async) {
        this.fileName = fileName;
        this.async = async;
    }

    @Override
    public boolean changeRobotState() throws InterruptedException {
        final MediaPlayer mp = new MediaPlayer();

        if (mp.isPlaying()) {
            mp.stop();
        }



        try {
            mp.reset();
            AssetFileDescriptor afd;
           afd = GlobalUtils.getCurrentActivity().getAssets().openFd(fileName);
            mp.setDataSource(afd.getFileDescriptor(),afd.getStartOffset(),afd.getLength());
            mp.prepare();
            mp.start();
            while (mp.isPlaying()) {
                Thread.sleep(20);
            }
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
}
