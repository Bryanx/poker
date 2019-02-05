package be.kdg.mobile_client.services;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Generates a service that can be used to send requests to.
 * Constructor expects an interface with Retrofit methods.
 */
public class ServiceGenerator {
    public static final String API_BASE_URL = "https://poker-user-service.herokuapp.com";
    private static Retrofit.Builder builder = new Retrofit.Builder()
            .baseUrl(API_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create());
    private static Retrofit retrofit = builder.build();

    public static <S> S createService(Class<S> serviceClass) {
        return retrofit.create(serviceClass);
    }
}
