package com.bridge.androidtechnicaltest.network

import com.bridge.androidtechnicaltest.db.PupilUploadDto
import com.bridge.androidtechnicaltest.db.PupilList
import io.reactivex.Completable
import io.reactivex.Single
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface PupilApi {
    @GET("pupils")
    fun getPupils(@Query("page") page: Int = 1): Single<PupilList>

    @POST("pupils")
    fun addPupil(@Body pupil: PupilUploadDto): Completable

    @DELETE("pupils/{id}")
    fun deletePupil(@Path("id") pupilId: Int): Completable
}