package com.example.ecloset.di

import com.example.ecloset.user.remote.RetrofitClient
import com.example.ecloset.user.remote.api.AuthApi
import com.example.ecloset.user.remote.api.ClothesApi
import com.example.ecloset.user.remote.api.OutfitsApi
import com.example.ecloset.user.remote.api.UserApi
import com.example.ecloset.user.remote.datasource.AuthRemoteDataSource
import com.example.ecloset.user.remote.datasource.ClothesRemoteDataSource
import com.example.ecloset.user.remote.datasource.OutfitsRemoteDataSource
import com.example.ecloset.user.remote.datasource.UserRemoteDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

	@Provides @Singleton
	fun provideClothesApi(): ClothesApi =
		RetrofitClient.retrofit.create(ClothesApi::class.java)

	@Provides @Singleton
	fun provideOutfitsApi(): OutfitsApi =
		RetrofitClient.retrofit.create(OutfitsApi::class.java)

	@Provides @Singleton
	fun provideUserApi(): UserApi =
		RetrofitClient.retrofit.create(UserApi::class.java)

	@Provides @Singleton
	fun provideClothesRemoteDS(api: ClothesApi) = ClothesRemoteDataSource(api)

	@Provides @Singleton
	fun provideOutfitsRemoteDS(api: OutfitsApi) = OutfitsRemoteDataSource(api)

	@Provides @Singleton
	fun provideUserRemoteDS(api: UserApi) = UserRemoteDataSource(api)

	@Provides @Singleton
	fun provideAuthApi(): AuthApi = RetrofitClient.retrofit.create(AuthApi::class.java)

	@Provides @Singleton
	fun provideAuthRemoteDS(api: AuthApi) = AuthRemoteDataSource(api)
}
