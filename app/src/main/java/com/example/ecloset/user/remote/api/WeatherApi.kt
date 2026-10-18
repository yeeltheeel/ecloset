package com.example.ecloset.user.remote.api

import com.example.ecloset.user.remote.data.WttrResponse
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import java.util.concurrent.TimeUnit

interface WttrApi {
	@GET("{location}")
	suspend fun getCurrentWeather(
		@Path("location") location: String,
		@Query("format") format: String = "j2"
	): WttrResponse
}

object WttrApiClient {
	private val okHttpClient = OkHttpClient.Builder()
		.connectTimeout(15, TimeUnit.SECONDS)
		.readTimeout(30, TimeUnit.SECONDS)
		.addInterceptor { chain ->
			val request = chain.request().newBuilder()
				.header("User-Agent", "Wget/1.21") // forces wttr.in to return json, not text/plain
				.header("Accept", "application/json")
				.build()
			chain.proceed(request)
		}
		.build()

	val api: WttrApi by lazy {
		Retrofit.Builder()
			.baseUrl("https://wttr.in/")
			.client(okHttpClient)
			.addConverterFactory(GsonConverterFactory.create())
			.build()
			.create(WttrApi::class.java)
	}
}
