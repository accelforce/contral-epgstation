package net.accelf.contral.epgstation.api

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import okhttp3.HttpUrl
import okhttp3.HttpUrl.Companion.toHttpUrl
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.create
import retrofit2.http.GET
import retrofit2.http.Query

internal interface EPGStation {

    @Serializable
    data class ListRecordedResponse(
        @SerialName("records") val records: List<Recorded>,
        @SerialName("total") val total: Int,
    )

    @GET("api/recorded")
    suspend fun listRecorded(
        @Query("ruleId") ruleId: Int? = null,
        @Query("isHalfWidth") isHalfWidth: Boolean = true,
        @Query("offset") offset: Int = 0,
        @Query("limit") limit: Int = 50,
    ): ListRecordedResponse

    @GET("api/version")
    suspend fun getVersion(): Version

    companion object {
        private val json = Json {
            ignoreUnknownKeys = true
        }

        internal fun baseUrl(secure: Boolean, domain: String) =
            (if (domain.contains("://")) domain else "http://${domain}")
                .toHttpUrl()
                .newBuilder()
                .scheme(if (secure) "https" else "http")
                .build()

        internal fun create(secure: Boolean, domain: String) = create(baseUrl(secure, domain))

        @OptIn(ExperimentalSerializationApi::class)
        internal fun create(baseUrl: HttpUrl): EPGStation =
            Retrofit.Builder()
                .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
                .client(
                    OkHttpClient.Builder()
                        .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BASIC))
                        .build()
                )
                .baseUrl(baseUrl)
                .build()
                .create()
    }
}
