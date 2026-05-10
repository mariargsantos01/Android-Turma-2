package com.example.contatos.model

import com.google.gson.annotations.SerializedName

data class ViaCepResponse(
    val cep: String = "",
    val logradouro: String = "",
    val complemento: String = "",
    val bairro: String = "",
    @SerializedName("localidade")
    val cidade: String = "",
    val uf: String = "",
    val erro: Boolean = false
)

