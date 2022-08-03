package com.pocketcocktails.pocketbar.di

import com.pocketcocktails.pocketbar.data.network.ApiService
import dagger.Module
import dagger.Provides
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Module
object ApiModule {

    private const val API_KEY = "1"
    private const val BASE_URL = "https://www.thecocktaildb.com/api/json/v1/1/"

    private var logInterceptor = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)

    private var requestInterceptor = Interceptor { chain ->
        val request = chain.request().newBuilder()
        val originalHttpUrl = chain.request().url
        val newUrl = originalHttpUrl.newBuilder().addQueryParameter("api_key", API_KEY).build()
        request.url(newUrl)
        chain.proceed(request.build())
    }

    private var okHttpClient = OkHttpClient.Builder()
        .addInterceptor(logInterceptor)
        .addInterceptor(requestInterceptor)

    private fun getRetrofit(): Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(okHttpClient.build())
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    @Provides
    fun provideApiService(): ApiService = getRetrofit().create(ApiService::class.java)

}
