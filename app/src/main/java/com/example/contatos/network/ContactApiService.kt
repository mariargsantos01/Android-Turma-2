package com.example.contatos.network

import com.example.contatos.model.ApiResponse
import com.example.contatos.model.Contact
import retrofit2.Response
import retrofit2.http.*

interface ContactApiService {

    @GET("contacts")
    suspend fun getContacts(): Response<ApiResponse<List<Contact>>>

    @GET("contacts/{id}")
    suspend fun getContact(@Path("id") id: String): Response<ApiResponse<Contact>>

    @POST("contacts")
    suspend fun createContact(@Body contact: Contact): Response<ApiResponse<Contact>>

    @PUT("contacts/{id}")
    suspend fun updateContact(@Path("id") id: String, @Body contact: Contact): Response<ApiResponse<Contact>>

    @DELETE("contacts/{id}")
    suspend fun deleteContact(@Path("id") id: String): Response<ApiResponse<Unit>>
}

