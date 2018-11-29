package virtualRobot.telemetry;

import org.opencv.android.OpenCVLoader;
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
        OpenCVLoader.initDebug();
        return new MatConverterFactory();
    }

    @Override
    public Converter<ResponseBody, Mat> responseBodyConverter(Type type, Annotation[] annotations, Retrofit retrofit) {
        if (type == Mat.class)
            return new MatResponseBodyConverter();
        return null;
    }

    @Override
    public Converter<Mat, RequestBody> requestBodyConverter(Type type, Annotation[] parameterAnnotations, Annotation[] methodAnnotations, Retrofit retrofit) {
        if (type == Mat.class)
            return new MatRequestBodyConverter();
        return null;
    }
}
