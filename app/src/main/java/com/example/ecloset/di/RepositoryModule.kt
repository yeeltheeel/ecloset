package com.example.ecloset.di

import android.content.Context
import android.content.SharedPreferences
import com.example.ecloset.user.data.ConnectionModeManager
import com.example.ecloset.user.data.repo.AuthRepository
import com.example.ecloset.user.data.repo.ClothesRepository
import com.example.ecloset.user.data.repo.OutfitsRepository
import com.example.ecloset.user.data.repo.UserRepository
import com.example.ecloset.user.local.datasource.AuthLocalDataSource
import com.example.ecloset.user.local.datasource.LocalDataSource
import com.example.ecloset.user.remote.datasource.AuthRemoteDataSource
import com.example.ecloset.user.remote.datasource.ClothesRemoteDataSource
import com.example.ecloset.user.remote.datasource.OutfitsRemoteDataSource
import com.example.ecloset.user.remote.datasource.UserRemoteDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import dagger.hilt.android.qualifiers.ApplicationContext

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {
	@Provides @Singleton
	fun provideSharedPreferences(
		@ApplicationContext context: Context
	): SharedPreferences =
		context.getSharedPreferences("ecloset_prefs", Context.MODE_PRIVATE)

	@Provides @Singleton
	fun provideConnectionModeManager(
		prefs: SharedPreferences
	): ConnectionModeManager = ConnectionModeManager(prefs)

	@Provides @Singleton
	fun provideLocalDataSource() = LocalDataSource()

	@Provides @Singleton
	fun provideClothesRepository(
		remote: ClothesRemoteDataSource,
		local: LocalDataSource,
		mode: ConnectionModeManager
	) = ClothesRepository(remote, local, mode)

	@Provides @Singleton
	fun provideOutfitsRepository(
		remote: OutfitsRemoteDataSource,
		local: LocalDataSource,
		mode: ConnectionModeManager
	) = OutfitsRepository(remote, local, mode)

	@Provides @Singleton
	fun provideUserRepository(
		remote: UserRemoteDataSource,
		local: LocalDataSource,
		mode: ConnectionModeManager
	) = UserRepository(remote, local, mode)

	@Provides @Singleton
	fun provideAuthLocalDataSource() = AuthLocalDataSource()

	@Provides @Singleton
	fun provideAuthRepository(
		remote: AuthRemoteDataSource,
		local: AuthLocalDataSource,
		mode: ConnectionModeManager
	) = AuthRepository(remote, local, mode)
}
