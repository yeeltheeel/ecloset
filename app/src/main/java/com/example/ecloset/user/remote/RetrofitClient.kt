package com.example.ecloset.user.remote

import com.example.ecloset.BuildConfig
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonDeserializer
import com.google.gson.JsonPrimitive
import com.google.gson.JsonSerializer
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.time.LocalDate
import java.util.concurrent.TimeUnit

object RetrofitClient {
	private const val BASE_URL = BuildConfig.BASE_URL

	private val gson: Gson = GsonBuilder()
		.registerTypeAdapter(
			LocalDate::class.java,
			JsonDeserializer<LocalDate> { json, _, _ ->
				LocalDate.parse(json.asString)
			}
		)
		.registerTypeAdapter(
			LocalDate::class.java,
			JsonSerializer<LocalDate> { src, _, _ ->
				JsonPrimitive(src.toString())
			}
		)
		.create()

	private val okHttpClient: OkHttpClient = OkHttpClient.Builder()
		.connectTimeout(10, TimeUnit.SECONDS)
		.readTimeout(10, TimeUnit.SECONDS)
		.writeTimeout(10, TimeUnit.SECONDS)
		.addInterceptor(
			HttpLoggingInterceptor().apply {
				level = if (BuildConfig.DEBUG)
					HttpLoggingInterceptor.Level.BODY
				else
					HttpLoggingInterceptor.Level.NONE
			}
		)
		.build()

	val retrofit: Retrofit by lazy {
		Retrofit.Builder()
			.baseUrl(BASE_URL)
			.client(okHttpClient)
			.addConverterFactory(GsonConverterFactory.create(gson))
			.build()
	}
}