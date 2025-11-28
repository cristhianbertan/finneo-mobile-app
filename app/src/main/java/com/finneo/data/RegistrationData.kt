package com.finneo.data

import java.time.LocalDate

@kotlinx.serialization.Serializable
data class RegistrationData(
    val email: String = "",
    val password: String = "",
    val passwordConfirmationValue: String = "",

    val name: String = "",
    val lastName: String = "",
    val document: String = "",
    val gender: String = "",

    val streetAddress: String = "",
    val number: String = "",
    val district: String = "",
    val state: String = "",
    val city: String = ""
)