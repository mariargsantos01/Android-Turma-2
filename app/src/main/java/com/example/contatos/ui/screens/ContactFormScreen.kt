package com.example.contatos.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.PinDrop
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.widthIn
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.contatos.ui.components.CustomTextField
import com.example.contatos.ui.components.DatePickerField
import com.example.contatos.ui.components.LoadingView
import com.example.contatos.ui.components.MaskedTextField
import com.example.contatos.ui.components.PrimaryButton
import com.example.contatos.utils.CepMask
import com.example.contatos.utils.DateMask
import com.example.contatos.utils.EmailValidator
import com.example.contatos.utils.PhoneMask
import com.example.contatos.viewmodel.ContactViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContactFormScreen(
    viewModel: ContactViewModel,
    contactId: String?,
    onBack: () -> Unit
) {
    val nome by viewModel.nome.collectAsStateWithLifecycle()
    val email by viewModel.email.collectAsStateWithLifecycle()
    val telefone by viewModel.telefone.collectAsStateWithLifecycle()
    val nascimento by viewModel.nascimento.collectAsStateWithLifecycle()
    val cep by viewModel.cep.collectAsStateWithLifecycle()
    val bairro by viewModel.bairro.collectAsStateWithLifecycle()
    val logradouro by viewModel.logradouro.collectAsStateWithLifecycle()
    val numero by viewModel.numero.collectAsStateWithLifecycle()
    val estado by viewModel.estado.collectAsStateWithLifecycle()
    val cidade by viewModel.cidade.collectAsStateWithLifecycle()
    val isLoading by viewModel.isLoading.collectAsStateWithLifecycle()
    val isCepLoading by viewModel.isCepLoading.collectAsStateWithLifecycle()
    val errorMessage by viewModel.errorMessage.collectAsStateWithLifecycle()
    val operationSuccess by viewModel.operationSuccess.collectAsStateWithLifecycle()

    val isEditMode = contactId != null
    val snackbarHostState = remember { SnackbarHostState() }
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current
    val nameFocusRequester = remember { FocusRequester() }

    var showValidation by rememberSaveable(contactId) { mutableStateOf(false) }

    val nomeError = if (showValidation && nome.isBlank()) "Nome é obrigatório" else null
    val emailError = when {
        showValidation && email.isBlank() -> "E-mail é obrigatório"
        email.isNotBlank() && !EmailValidator.isValid(email) -> "E-mail inválido"
        else -> null
    }
    val telefoneError = when {
        showValidation && telefone.isBlank() -> "Telefone é obrigatório"
        telefone.isNotBlank() && !PhoneMask.isValid(telefone) -> "Telefone inválido"
        else -> null
    }
    val nascimentoError = when {
        nascimento.isNotBlank() && !DateMask.isValid(nascimento) -> "Data inválida"
        else -> null
    }
    val cepError = when {
        cep.isNotBlank() && !CepMask.isValid(cep) -> "CEP inválido"
        else -> null
    }

    val hasValidationError = listOf(
        nomeError,
        emailError,
        telefoneError,
        nascimentoError,
        cepError
    ).any { it != null }

    LaunchedEffect(contactId) {
        showValidation = false
        if (contactId != null) {
            viewModel.loadContact(contactId)
        } else {
            viewModel.clearForm()
            nameFocusRequester.requestFocus()
        }
    }

    LaunchedEffect(operationSuccess) {
        if (operationSuccess) {
            keyboardController?.hide()
            focusManager.clearFocus(force = true)
            viewModel.resetOperationSuccess()
            onBack()
        }
    }

    LaunchedEffect(errorMessage) {
        errorMessage?.let {
            snackbarHostState.showSnackbar(it)
            viewModel.clearError()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = if (isEditMode) "Editar contato" else "Novo contato",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = if (isEditMode) {
                                "Atualize os dados e mantenha sua agenda sempre em dia"
                            } else {
                                "Preencha as informações e crie um novo contato"
                            },
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Voltar"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    titleContentColor = MaterialTheme.colorScheme.onSurface
                )
            )
        },
        snackbarHost = {
            SnackbarHost(snackbarHostState) { data ->
                Snackbar(
                    snackbarData = data,
                    shape = MaterialTheme.shapes.medium,
                    containerColor = MaterialTheme.colorScheme.inverseSurface,
                    contentColor = MaterialTheme.colorScheme.inverseOnSurface
                )
            }
        }
    ) { paddingValues ->
        if (isLoading && isEditMode && nome.isBlank()) {
            LoadingView(
                modifier = Modifier.padding(paddingValues),
                title = "Carregando contato",
                message = "Buscando os dados para edição."
            )
        } else {
            Column(
                modifier = Modifier
                    .padding(paddingValues)
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .imePadding()
                    .navigationBarsPadding()
                    .padding(horizontal = 16.dp, vertical = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                AnimatedVisibility(
                    visible = isLoading,
                    enter = fadeIn() + expandVertically(),
                    exit = fadeOut() + shrinkVertically()
                ) {
                    LinearProgressIndicator(
                        modifier = Modifier.fillMaxWidth(),
                        strokeCap = androidx.compose.ui.graphics.StrokeCap.Round
                    )
                }

                AnimatedVisibility(
                    visible = showValidation && hasValidationError,
                    enter = fadeIn() + expandVertically(),
                    exit = fadeOut() + shrinkVertically()
                ) {
                    ElevatedCard(
                        shape = MaterialTheme.shapes.large,
                        colors = CardDefaults.elevatedCardColors(
                            containerColor = MaterialTheme.colorScheme.errorContainer
                        )
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = "Revise os campos destacados",
                                style = MaterialTheme.typography.titleSmall,
                                color = MaterialTheme.colorScheme.onErrorContainer
                            )
                            Text(
                                text = "Corrija os dados obrigatórios e os formatos inválidos antes de salvar.",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onErrorContainer,
                                modifier = Modifier.padding(top = 4.dp)
                            )
                        }
                    }
                }

                SectionCard(
                    title = "Dados pessoais",
                    subtitle = "Informações principais para identificação do contato"
                ) {
                    CustomTextField(
                        value = nome,
                        onValueChange = viewModel::onNomeChange,
                        label = "Nome",
                        modifier = Modifier.focusRequester(nameFocusRequester),
                        leadingIcon = Icons.Default.Person,
                        placeholder = "Ex.: Maria Souza",
                        enabled = !isLoading,
                        isError = nomeError != null,
                        errorMessage = nomeError,
                        imeAction = ImeAction.Next,
                        keyboardActions = KeyboardActions(
                            onNext = { focusManager.moveFocus(FocusDirection.Down) }
                        )
                    )

                    CustomTextField(
                        value = email,
                        onValueChange = viewModel::onEmailChange,
                        label = "E-mail",
                        leadingIcon = Icons.Default.Email,
                        placeholder = "contato@exemplo.com",
                        keyboardType = KeyboardType.Email,
                        enabled = !isLoading,
                        isError = emailError != null,
                        errorMessage = emailError,
                        imeAction = ImeAction.Next,
                        keyboardActions = KeyboardActions(
                            onNext = { focusManager.moveFocus(FocusDirection.Down) }
                        )
                    )

                    MaskedTextField(
                        value = telefone,
                        onValueChange = viewModel::onTelefoneChange,
                        label = "Telefone",
                        mask = PhoneMask,
                        leadingIcon = Icons.Default.Phone,
                        placeholder = "(83) 99999-9999",
                        enabled = !isLoading,
                        isError = telefoneError != null,
                        errorMessage = telefoneError,
                        imeAction = ImeAction.Next,
                        keyboardActions = KeyboardActions(
                            onNext = { focusManager.moveFocus(FocusDirection.Down) }
                        )
                    )

                    DatePickerField(
                        value = nascimento,
                        onValueChange = viewModel::onNascimentoChange,
                        label = "Nascimento",
                        placeholder = "dd/MM/aaaa",
                        enabled = !isLoading,
                        isError = nascimentoError != null,
                        errorMessage = nascimentoError
                    )
                }

                SectionCard(
                    title = "Endereço",
                    subtitle = "Ao informar o CEP, os campos podem ser preenchidos automaticamente"
                ) {
                    MaskedTextField(
                        value = cep,
                        onValueChange = viewModel::onCepChange,
                        label = "CEP",
                        mask = CepMask,
                        leadingIcon = Icons.Default.PinDrop,
                        placeholder = "58000-000",
                        enabled = !isLoading,
                        isError = cepError != null,
                        errorMessage = cepError,
                        imeAction = ImeAction.Next,
                        keyboardActions = KeyboardActions(
                            onNext = { focusManager.moveFocus(FocusDirection.Down) }
                        )
                    )

                    AnimatedVisibility(
                        visible = isCepLoading,
                        enter = fadeIn(),
                        exit = fadeOut()
                    ) {
                        Text(
                            text = "Consultando endereço pelo CEP...",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }

                    CustomTextField(
                        value = logradouro,
                        onValueChange = viewModel::onLogradouroChange,
                        label = "Logradouro",
                        leadingIcon = Icons.Default.Home,
                        placeholder = "Rua, avenida, praça...",
                        enabled = !isLoading,
                        imeAction = ImeAction.Next,
                        keyboardActions = KeyboardActions(
                            onNext = { focusManager.moveFocus(FocusDirection.Down) }
                        )
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        CustomTextField(
                            value = numero,
                            onValueChange = { value -> viewModel.onNumeroChange(value.filter(Char::isDigit)) },
                            label = "Número",
                            modifier = Modifier
                                .weight(1.1f)
                                .widthIn(min = 108.dp),
                            keyboardType = KeyboardType.Number,
                            leadingIcon = Icons.Default.PinDrop,
                            placeholder = "123",
                            enabled = !isLoading,
                            imeAction = ImeAction.Next,
                            keyboardActions = KeyboardActions(
                                onNext = { focusManager.moveFocus(FocusDirection.Down) }
                            )
                        )

                        CustomTextField(
                            value = bairro,
                            onValueChange = viewModel::onBairroChange,
                            label = "Bairro",
                            modifier = Modifier.weight(1.9f),
                            leadingIcon = Icons.Default.LocationOn,
                            placeholder = "Bairro",
                            enabled = !isLoading,
                            imeAction = ImeAction.Next,
                            keyboardActions = KeyboardActions(
                                onNext = { focusManager.moveFocus(FocusDirection.Down) }
                            )
                        )
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        CustomTextField(
                            value = cidade,
                            onValueChange = viewModel::onCidadeChange,
                            label = "Cidade",
                            modifier = Modifier.weight(2f),
                            leadingIcon = Icons.Default.LocationOn,
                            placeholder = "Cidade",
                            enabled = !isLoading,
                            imeAction = ImeAction.Next,
                            keyboardActions = KeyboardActions(
                                onNext = { focusManager.moveFocus(FocusDirection.Down) }
                            )
                        )

                        CustomTextField(
                            value = estado,
                            onValueChange = { viewModel.onEstadoChange(it.take(2).uppercase()) },
                            label = "UF",
                            modifier = Modifier.weight(1f),
                            placeholder = "PB",
                            enabled = !isLoading,
                            imeAction = ImeAction.Done,
                            keyboardActions = KeyboardActions(
                                onDone = {
                                    keyboardController?.hide()
                                    focusManager.clearFocus()
                                }
                            )
                        )
                    }
                }

                PrimaryButton(
                    text = if (isEditMode) "Atualizar contato" else "Salvar contato",
                    onClick = {
                        showValidation = true
                        if (!hasValidationError) {
                            keyboardController?.hide()
                            focusManager.clearFocus(force = true)
                            viewModel.saveContact()
                        }
                    },
                    enabled = !isLoading,
                    isLoading = isLoading,
                    leadingIcon = Icons.Default.Check
                )

                Spacer(modifier = Modifier.height(12.dp))
            }
        }
    }
}

@Composable
private fun SectionCard(
    title: String,
    subtitle: String,
    content: @Composable () -> Unit
) {
    ElevatedCard(
        shape = MaterialTheme.shapes.large,
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            content()
        }
    }
}
