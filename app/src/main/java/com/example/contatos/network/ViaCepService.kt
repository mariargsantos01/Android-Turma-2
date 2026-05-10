package com.example.contatos.network

import com.example.contatos.model.ViaCepResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface ViaCepService {

    @GET("ws/{cep}/json/")
    suspend fun getCep(@Path("cep") cep: String): Response<ViaCepResponse>
}

