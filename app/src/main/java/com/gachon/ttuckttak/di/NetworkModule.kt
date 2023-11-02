package com.gachon.ttuckttak.di

import com.gachon.ttuckttak.BuildConfig
import com.gachon.ttuckttak.data.local.AuthManager
import com.gachon.ttuckttak.data.remote.annotation.NeedToken
import com.gachon.ttuckttak.data.remote.service.AuthService
import com.gachon.ttuckttak.data.remote.service.MemberService
import com.gachon.ttuckttak.data.remote.service.SolutionService
import com.gachon.ttuckttak.data.remote.service.ViewService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Invocation
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    private const val BASE_URL = "https://ttukttak.store/api/"

    @Provides
    @Singleton
    fun provideOkHttpClient(authManager: AuthManager): OkHttpClient {
        // 토큰을 주입하는 Interceptor 생성
        val tokenInterceptor = Interceptor { chain ->
            var request = chain.request()

            // 현재 요청이 NeedToken 어노테이션을 가지고 있는지 확인한다.
            if (request.tag(Invocation::class.java)?.method()?.isAnnotationPresent(NeedToken::class.java) == true) {
                // 만약 어노테이션이 있다면, 새로운 Request Builder를 생성하여 Authorization 헤더에 토큰 값을 추가하고,
                // 이를 다시 요청 객체로 빌드한다.
                val newRequestBuilder = request.newBuilder()
                newRequestBuilder.addHeader("Authorization", authManager.getAccessToken()!!)
                request = newRequestBuilder.build()
            }

            // 변경된 요청으로 네트워크 호출을 진행한다.
            chain.proceed(request)
        }

        return if (BuildConfig.DEBUG) {
            val loggingInterceptor = HttpLoggingInterceptor()
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)

            OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)  // 로깅 인터셉터 추가 (디버그 모드일 때만)
                .addNetworkInterceptor(tokenInterceptor)  // 토큰 인터셉터 추가
                .build()

        } else {
            OkHttpClient.Builder().addNetworkInterceptor(tokenInterceptor).build()  // 토큰 인터셉터만 추가
        }
    }

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideAuthService(retrofit: Retrofit): AuthService {
        return retrofit.create(AuthService::class.java)
    }

    @Provides
    @Singleton
    fun provideMemberService(retrofit: Retrofit): MemberService {
        return retrofit.create(MemberService::class.java)
    }

    @Provides
    @Singleton
    fun provideViewService(retrofit: Retrofit): ViewService {
        return retrofit.create(ViewService::class.java)
    }

    @Provides
    @Singleton
    fun provideSolutionService(retrofit: Retrofit): SolutionService {
        return retrofit.create(SolutionService::class.java)
    }
}