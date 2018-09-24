package virtualRobot.telemetry;

import org.opencv.core.Mat;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Retrofit;

/**
 * Created by david on 11/7/17.
 */

public class MatConverterFactory extends Converter.Factory {
    public static MatConverterFactory create() {
        return new MatConverterFactory();
    }

    @Override
    public Converter<ResponseBody, Mat> responseBodyConverter(Type type, Annotation[] annotations, Retrofit retrofit) {
        return new MatResponseBodyConverter();
    }

    @Override
    public Converter<Mat, RequestBody> requestBodyConverter(Type type, Annotation[] parameterAnnotations, Annotation[] methodAnnotations, Retrofit retrofit) {
        return new MatRequestBodyConverter();
    }
}
