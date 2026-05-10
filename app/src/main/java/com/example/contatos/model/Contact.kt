package com.example.contatos.model


data class Contact(
    val id: String? = null,
    val nome: String = "",
    val email: String = "",
    val telefone: String = "",
    val nascimento: String = "",
    val cep: String = "",
    val bairro: String = "",
    val logradouro: String = "",
    val numero: String = "",
    val estado: String = "",
    val cidade: String = ""
)

