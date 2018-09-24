package virtualRobot.telemetry;

import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okio.BufferedSink;
import retrofit2.Converter;

/**
 * Created by david on 11/7/17.
 */

public class MatRequestBodyConverter implements Converter<Mat, RequestBody> {
    @Override
    public RequestBody convert(Mat mat) throws IOException {
        final MatOfByte buf = new MatOfByte();
        Imgcodecs.imencode(".jpeg", mat, buf);
        return new RequestBody() {
            @Override
            public MediaType contentType() {
                return MediaType.parse("image/jpeg");
            }

            @Override
            public void writeTo(BufferedSink sink) throws IOException {
                sink.write(buf.toArray());
            }
        };
    }
}
