package com.bridge.androidtechnicaltest.db

data class PupilUploadDto(
    val name: String,
    val country: String,
    val longitude: Int,
    val latitude: Int,
    val image: String,
    val pupilId: Int,
) {
    companion object {
        fun fromPupil(pupil: Pupil) = PupilUploadDto(
            pupilId = pupil.pupilId.toInt(),
            name = pupil.name,
            country = pupil.country,
            longitude = pupil.longitude.toInt(),
            latitude = pupil.latitude.toInt(),
            image = pupil.image,
        )
    }
}
