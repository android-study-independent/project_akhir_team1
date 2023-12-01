package com.example.tanify.data.response.profile

import com.example.tanify.data.response.login.errordata
import com.google.gson.annotations.SerializedName

data class EditPasswordResponse (
    @SerializedName("msg")
    val message: String? = null,
    @SerializedName("errors")
    val error: errorEditPassword? = null,
)

data class errorEditPassword(
    @SerializedName("oldPassword")
    val oldPassword: errordata? = null,
    @SerializedName("newPassword")
    val newPassword: errordata? = null,
    @SerializedName("confirmPassword")
    val confirmPassword: errordata? = null,
)
