package com.example.contatos.repository

import com.example.contatos.model.Contact
import com.example.contatos.model.ViaCepResponse
import com.example.contatos.network.RetrofitInstance
import com.example.contatos.utils.Constants
import java.net.ConnectException
import java.net.NoRouteToHostException
import java.net.SocketTimeoutException
import java.net.UnknownServiceException
import java.net.UnknownHostException
import java.io.IOException
import javax.net.ssl.SSLException

class ContactRepository {

    private val contactApi = RetrofitInstance.contactApi
    private val viaCepApi = RetrofitInstance.viaCepApi

    private fun isSuccessResponse(success: Boolean?) = success == true

    private fun handleNetworkException(e: Exception, defaultMsg: String): Exception {
        return when (e) {
            is UnknownHostException -> Exception(Constants.ERRO_SEM_INTERNET)
            is ConnectException,
            is NoRouteToHostException,
            is SSLException,
            is UnknownServiceException -> Exception(Constants.ERRO_SERVIDOR_OFFLINE)
            is SocketTimeoutException -> Exception(Constants.ERRO_CONEXAO_TIMEOUT)
            is IOException -> Exception(Constants.ERRO_REDE_GENERICO)
            else -> Exception(if (defaultMsg.isNotBlank()) "$defaultMsg ${Constants.ERRO_INESPERADO}" else Constants.ERRO_INESPERADO)
        }
    }

    suspend fun getContacts(): Result<List<Contact>> {
        return try {
            val response = contactApi.getContacts()
            val apiResponse = response.body()
            if (response.isSuccessful && isSuccessResponse(apiResponse?.success)) {
                Result.success(apiResponse?.data ?: emptyList())
            } else {
                Result.failure(Exception("${Constants.ERRO_BUSCAR_CONTATOS} Código: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(handleNetworkException(e, Constants.ERRO_BUSCAR_CONTATOS))
        }
    }

    suspend fun getContact(id: String): Result<Contact> {
        return try {
            val response = contactApi.getContact(id)
            val apiResponse = response.body()
            val contact = apiResponse?.data
            if (response.isSuccessful && isSuccessResponse(apiResponse?.success) && contact != null) {
                Result.success(contact)
            } else {
                Result.failure(Exception("${Constants.ERRO_BUSCAR_CONTATO} Código: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(handleNetworkException(e, Constants.ERRO_BUSCAR_CONTATO))
        }
    }

    suspend fun createContact(contact: Contact): Result<Contact> {
        return try {
            val response = contactApi.createContact(contact)
            val apiResponse = response.body()
            val createdContact = apiResponse?.data
            if (response.isSuccessful && isSuccessResponse(apiResponse?.success) && createdContact != null) {
                Result.success(createdContact)
            } else {
                Result.failure(Exception("${Constants.ERRO_CRIAR_CONTATO} Código: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(handleNetworkException(e, Constants.ERRO_CRIAR_CONTATO))
        }
    }

    suspend fun updateContact(id: String, contact: Contact): Result<Contact> {
        return try {
            val response = contactApi.updateContact(id, contact)
            val apiResponse = response.body()
            val updatedContact = apiResponse?.data
            if (response.isSuccessful && isSuccessResponse(apiResponse?.success) && updatedContact != null) {
                Result.success(updatedContact)
            } else {
                Result.failure(Exception("${Constants.ERRO_ATUALIZAR_CONTATO} Código: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(handleNetworkException(e, Constants.ERRO_ATUALIZAR_CONTATO))
        }
    }

    suspend fun deleteContact(id: String): Result<Unit> {
        return try {
            val response = contactApi.deleteContact(id)
            val apiResponse = response.body()
            if (response.isSuccessful && (apiResponse == null || isSuccessResponse(apiResponse.success))) {
                Result.success(Unit)
            } else {
                Result.failure(Exception("${Constants.ERRO_EXCLUIR_CONTATO} Código: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(handleNetworkException(e, Constants.ERRO_EXCLUIR_CONTATO))
        }
    }

    suspend fun searchCep(cep: String): Result<ViaCepResponse> {
        return try {
            val response = viaCepApi.getCep(cep)
            if (response.isSuccessful && response.body() != null) {
                val body = response.body()!!
                if (body.erro) {
                    Result.failure(Exception(Constants.ERRO_CEP_NAO_ENCONTRADO))
                } else {
                    Result.success(body)
                }
            } else {
                Result.failure(Exception("${Constants.ERRO_BUSCAR_CEP} Código: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(handleNetworkException(e, Constants.ERRO_BUSCAR_CEP))
        }
    }
}
