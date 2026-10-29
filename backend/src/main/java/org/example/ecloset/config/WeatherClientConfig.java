package org.example.ecloset.config;

import lombok.RequiredArgsConstructor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import org.example.ecloset.remote.WttrApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.util.concurrent.TimeUnit;

@Configuration
@RequiredArgsConstructor
public class WeatherClientConfig {

    private final WeatherProperties props;

    @Bean
    public WttrApi wttrApi() {
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(props.getConnectTimeoutSec(), TimeUnit.SECONDS)
                .readTimeout(props.getReadTimeoutSec(), TimeUnit.SECONDS)
                .addInterceptor(chain -> {
                    Request request = chain.request().newBuilder()
                            .header("User-Agent", props.getUserAgent())
                            .header("Accept", "application/json")
                            .build();
                    return chain.proceed(request);
                })
                .build();

        return new Retrofit.Builder()
                .baseUrl(props.getBaseUrl())
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(WttrApi.class);
    }
}