package com.anyline.di

import com.anyline.api.ApiService
import com.anyline.repository.GithubRepository
import com.anyline.repository.GithubRepositoryImpl
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit


@Module
class AppModule {

    @Provides
    fun provideGithubRepository(retrofit: Retrofit): GithubRepository {
        return GithubRepositoryImpl(retrofit.create(ApiService::class.java))
    }
}