package virtualRobot.commands;

/*import static com.qualcomm.ftcrobotcontroller.FtcRobotControllerActivity.imageByteData;
import static com.qualcomm.ftcrobotcontroller.FtcRobotControllerActivity.imageLock;
import static com.qualcomm.ftcrobotcontroller.FtcRobotControllerActivity.imageParameters;
import static com.qualcomm.ftcrobotcontroller.FtcRobotControllerActivity.mCamera;*/


/**
 * Created by DOSullivan on 11/25/15.
 * DEPRECATED: takes a picture using janky means
 */
@Deprecated
public class TakePicture extends Command {

    @Override
    public boolean changeRobotState() throws InterruptedException {
        return false;
    }
    /*//boolean[] redIsLeft;
    ArrayList<Command> commands;
    boolean isRed;
    AtomicBoolean redisLeft;

    private Condition condition;
    private boolean isInterrupted;

    public TakePicture (AtomicBoolean redisLeft) {
        this.redisLeft = redisLeft;
    }

    public TakePicture(ArrayList<Command> commands, String color) {
        
        condition = new Condition() {
            @Override
            public boolean isConditionMet() {
                return false;
            }
        };
        
        this.commands = commands;

        isRed = color.equals("red");
    }
    public void addCondition (Condition e) {
        condition = e;
    }
    public boolean changeRobotState() throws InterruptedException {
        
        imageLock.lock();

        mCamera.setPreviewCallback(new Camera.PreviewCallback() {

            @Override
            public void onPreviewFrame(byte[] data, Camera camera) {
                imageLock.lock();
                imageByteData = data;
                imageLock.unlock();
            }
        });

        mCamera.startPreview();

        imageLock.unlock();

        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            return true;
        }

        imageLock.lock();

        YuvImage yuvImage = new YuvImage(imageByteData, imageParameters.getPreviewFormat(), imageParameters.getPreviewSize().width, imageParameters.getPreviewSize().height, null);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        Rect rect = new Rect(0, 0, imageParameters.getPreviewSize().width, imageParameters.getPreviewSize().height);
        yuvImage.compressToJpeg(rect, 75, byteArrayOutputStream);
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPurgeable = true;
        options.inInputShareable = true;
        Bitmap mBitmap = BitmapFactory.decodeByteArray(byteArrayOutputStream.toByteArray(), 0, byteArrayOutputStream.size(), options);

        boolean analyzed = DavidClass.analyzePic2(mBitmap);
        BetterLog.d("cameraReturn", analyzed + " ");
        redisLeft.set(analyzed);

        OutputStream fos = null;
        try {
            fos = new FileOutputStream(new File(Environment.getExternalStorageDirectory() + "/FIRST/" + Boolean.toString(analyzed)+Long.toString(System.currentTimeMillis())));
            mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        imageLock.unlock();

        mCamera.stopPreview();
        
        return false;
    }*/
}


