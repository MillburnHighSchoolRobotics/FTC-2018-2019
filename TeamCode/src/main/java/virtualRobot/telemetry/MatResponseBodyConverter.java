package virtualRobot.telemetry;

import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.imgcodecs.Imgcodecs;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Converter;

/**
 * Created by david on 11/7/17.
 */

public class MatResponseBodyConverter implements Converter<ResponseBody, Mat> {
    @Override
    public Mat convert(ResponseBody responseBody) throws IOException {
        return Imgcodecs.imdecode(new MatOfByte(responseBody.bytes()), Imgcodecs.CV_LOAD_IMAGE_UNCHANGED);
    }
}
