package com.example.foodie.di

import com.example.foodie.BASE_URL
import com.example.foodie.FoodRecipesApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class) // previously we used the now-deprecated ApplicationComponent, SingletonComponent works the same way, @InstallIn annotation tells Hilt where we want to use our bindings, here  we're telling it we want to use them inside the whole application scope.
object NetworkModule {

    @Singleton
    @Provides
    fun provideOkHttpClient(): OkHttpClient{
        return OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)) // we could've (and might as well should've) supplied HttpLoggingInterceptor in the parameters as a dependency to the provideOkHttpClient() binding, and created its own binding for it.
            .readTimeout(15, TimeUnit.SECONDS) // The read timeout is the break during which the client will wait to read data. It defines a maximum time of inactivity between two data packets when waiting for the server's response. The default timeout of 10 seconds,  a zero value indicates no timeout.
            .connectTimeout(15, TimeUnit.SECONDS) // The connection timeout value determines the maximum amount of time the client will wait for a connection to be established with the server. This value is only used when making an initial connection. By default, for the OkHttpClient, this timeout is set to 10 seconds.  a zero value indicates no timeout. A value of zero means no timeout at all.
            .build()
    }

    @Singleton
    @Provides
    fun provideGsonConverterFactory(): GsonConverterFactory = GsonConverterFactory.create()

    @Singleton
    @Provides
    fun provideRetrofit(
        okHttpClient: OkHttpClient,
        gsonConverterFactory: GsonConverterFactory
    ): Retrofit{
       return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(gsonConverterFactory)
            .build()
    }

    @Singleton // here we're telling Dagger-Hilt that we only want ONE instance of FoodRecipesApi created throughout the whole specified component's lifecycle (application component in our case). If we didn't use the @Singleton annotation, every time a class injects FoodRecipesApi as a dependency, Hilt will create new instances of it, instead of reusing the same one, objects creation is an expensive operation, so that would be no bueno, and bad memory resources management, and frankly useless since one instance is enough.
    @Provides // since we're using a type (FoodRecipesApi) that is from an external library (i.e. Retrofit lib will create its implementation) as a dependency, we use @Provides annotation for this binding. If we instead had an interface type we defined ourselves that we want to inject as a dependency, we use @Binds annotation.
    // in parameters we define the needed dependencies for the creation of this binding, here we receive Retrofit dependency as a parameter, and thus we must create another binding that provides its implementation, else Hilt won't know from where to get a Retrofit instance.
    fun provideFoodRecipesApi(retrofit: Retrofit): FoodRecipesApi{ // here, the return type is important, it tells Hilt "whenever something needs FoodRecipesApi as a dependency to be injected, give them this one".
        return retrofit.create(FoodRecipesApi::class.java)
    }
}