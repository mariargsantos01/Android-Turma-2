package com.example.contatos.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.contatos.model.Contact
import com.example.contatos.repository.ContactRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ContactViewModel : ViewModel() {

    private val repository = ContactRepository()

    // Lista de contatos
    private val _contacts = MutableStateFlow<List<Contact>>(emptyList())
    val contacts: StateFlow<List<Contact>> = _contacts.asStateFlow()

    // Contato selecionado (para edição)
    private val _selectedContact = MutableStateFlow<Contact?>(null)
    val selectedContact: StateFlow<Contact?> = _selectedContact.asStateFlow()

    // Estado de loading
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    // Mensagem de erro
    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    // Sucesso na operação
    private val _operationSuccess = MutableStateFlow(false)
    val operationSuccess: StateFlow<Boolean> = _operationSuccess.asStateFlow()

    // Campos do formulário
    private val _nome = MutableStateFlow("")
    val nome: StateFlow<String> = _nome.asStateFlow()

    private val _email = MutableStateFlow("")
    val email: StateFlow<String> = _email.asStateFlow()

    private val _telefone = MutableStateFlow("")
    val telefone: StateFlow<String> = _telefone.asStateFlow()

    private val _nascimento = MutableStateFlow("")
    val nascimento: StateFlow<String> = _nascimento.asStateFlow()

    private val _cep = MutableStateFlow("")
    val cep: StateFlow<String> = _cep.asStateFlow()

    private val _bairro = MutableStateFlow("")
    val bairro: StateFlow<String> = _bairro.asStateFlow()

    private val _logradouro = MutableStateFlow("")
    val logradouro: StateFlow<String> = _logradouro.asStateFlow()

    private val _numero = MutableStateFlow("")
    val numero: StateFlow<String> = _numero.asStateFlow()

    private val _estado = MutableStateFlow("")
    val estado: StateFlow<String> = _estado.asStateFlow()

    private val _cidade = MutableStateFlow("")
    val cidade: StateFlow<String> = _cidade.asStateFlow()

    // Loading do CEP
    private val _isCepLoading = MutableStateFlow(false)
    val isCepLoading: StateFlow<Boolean> = _isCepLoading.asStateFlow()

    init {
        loadContacts()
    }

    fun loadContacts() {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null
            val result = repository.getContacts()
            result.onSuccess { _contacts.value = it }
            result.onFailure { _errorMessage.value = it.message }
            _isLoading.value = false
        }
    }

    fun loadContact(id: String) {
        viewModelScope.launch {
            _isLoading.value = true
            val result = repository.getContact(id)
            result.onSuccess { contact ->
                _selectedContact.value = contact
                _nome.value = contact.nome
                _email.value = contact.email
                _telefone.value = contact.telefone
                _nascimento.value = contact.nascimento
                _cep.value = contact.cep
                _bairro.value = contact.bairro
                _logradouro.value = contact.logradouro
                _numero.value = contact.numero
                _estado.value = contact.estado
                _cidade.value = contact.cidade
            }
            result.onFailure { _errorMessage.value = it.message }
            _isLoading.value = false
        }
    }

    fun saveContact() {
        if (!validateForm()) return

        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null

            val contact = Contact(
                nome = _nome.value.trim(),
                email = _email.value.trim(),
                telefone = _telefone.value.trim(),
                nascimento = _nascimento.value.trim(),
                cep = _cep.value.trim(),
                bairro = _bairro.value.trim(),
                logradouro = _logradouro.value.trim(),
                numero = _numero.value.trim(),
                estado = _estado.value.trim(),
                cidade = _cidade.value.trim()
            )

            val selectedId = _selectedContact.value?.id
            val result = if (selectedId != null) {
                repository.updateContact(selectedId, contact)
            } else {
                repository.createContact(contact)
            }

            result.onSuccess {
                _operationSuccess.value = true
                loadContacts()
            }
            result.onFailure { _errorMessage.value = it.message }
            _isLoading.value = false
        }
    }

    fun deleteContact(id: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null
            val result = repository.deleteContact(id)
            result.onSuccess { loadContacts() }
            result.onFailure { _errorMessage.value = it.message }
            _isLoading.value = false
        }
    }

    fun searchCep(cep: String) {
        val cleanCep = cep.replace("-", "").replace(".", "").trim()
        if (cleanCep.length != 8) return

        viewModelScope.launch {
            _isCepLoading.value = true
            val result = repository.searchCep(cleanCep)
            result.onSuccess { response ->
                _estado.value = response.uf
                _cidade.value = response.cidade
                _bairro.value = response.bairro
                _logradouro.value = response.logradouro
            }
            result.onFailure { _errorMessage.value = it.message }
            _isCepLoading.value = false
        }
    }

    // Setters para campos do formulário
    fun onNomeChange(value: String) { _nome.value = value }
    fun onEmailChange(value: String) { _email.value = value }
    fun onTelefoneChange(value: String) { _telefone.value = value }
    fun onNascimentoChange(value: String) { _nascimento.value = value }
    fun onCepChange(value: String) {
        _cep.value = value
        val clean = value.replace("-", "").replace(".", "").trim()
        if (clean.length == 8) {
            searchCep(clean)
        }
    }
    fun onBairroChange(value: String) { _bairro.value = value }
    fun onLogradouroChange(value: String) { _logradouro.value = value }
    fun onNumeroChange(value: String) { _numero.value = value }
    fun onEstadoChange(value: String) { _estado.value = value }
    fun onCidadeChange(value: String) { _cidade.value = value }

    fun clearForm() {
        _selectedContact.value = null
        _nome.value = ""
        _email.value = ""
        _telefone.value = ""
        _nascimento.value = ""
        _cep.value = ""
        _bairro.value = ""
        _logradouro.value = ""
        _numero.value = ""
        _estado.value = ""
        _cidade.value = ""
        _errorMessage.value = null
        _operationSuccess.value = false
    }

    fun clearError() {
        _errorMessage.value = null
    }

    fun resetOperationSuccess() {
        _operationSuccess.value = false
    }

    private fun validateForm(): Boolean {
        if (_nome.value.isBlank()) {
            _errorMessage.value = "Nome é obrigatório"
            return false
        }
        if (_email.value.isBlank()) {
            _errorMessage.value = "Email é obrigatório"
            return false
        }
        if (_telefone.value.isBlank()) {
            _errorMessage.value = "Telefone é obrigatório"
            return false
        }
        return true
    }
}

