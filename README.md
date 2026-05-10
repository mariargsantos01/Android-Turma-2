# Contatos Android App

Aplicativo Android para gerenciamento de contatos com integração REST própria e busca de endereço via ViaCEP.

## Tecnologias utilizadas

- Kotlin 2.0.21
- Jetpack Compose (BOM 2024.09.00)
- Material 3 + Material Icons Extended
- MVVM
- Retrofit 2.9.0
- OkHttp 4.12.0 (com Logging Interceptor)
- Gson Converter
- Coroutines 1.7.3
- StateFlow / MutableStateFlow
- Navigation Compose 2.7.7
- ViaCEP

## Funcionalidades

- **CRUD completo** de contatos (criar, listar, editar, excluir)
- **Busca automática de endereço por CEP** via ViaCEP — ao digitar 8 dígitos, os campos de logradouro, bairro, cidade e UF são preenchidos automaticamente
- **Máscaras de entrada** para telefone `(XX) XXXXX-XXXX`, CEP `XXXXX-XXX` e data `dd/MM/aaaa`
- **Validação em tempo real** de formulário com destaque visual nos campos com erro
- **DatePicker nativo** do Material 3 para seleção de data de nascimento
- **Avatar com inicial** do nome no card de contato
- **Animações** de loading, transições de tela e exibição de erros
- **Tratamento robusto de erros de rede** com mensagens amigáveis (sem internet, timeout, servidor offline, etc.)
- **Estado vazio** com ilustração e instruções quando não há contatos
- **Snackbar** para feedback de erros e operações

## Arquitetura

O projeto utiliza arquitetura **MVVM** com separação clara de responsabilidades:

```text
app/src/main/java/com/example/contatos/
├── model/
│   ├── Contact.kt
│   └── ViaCepResponse.kt
├── network/
│   ├── ContactApiService.kt
│   ├── RetrofitInstance.kt
│   └── ViaCepService.kt
├── repository/
│   └── ContactRepository.kt
├── viewmodel/
│   └── ContactViewModel.kt
├── ui/
│   ├── components/
│   │   ├── CustomTextField.kt
│   │   ├── DatePickerField.kt
│   │   ├── EmptyListView.kt
│   │   ├── ErrorText.kt
│   │   ├── LoadingView.kt
│   │   ├── MaskedTextField.kt
│   │   ├── ModernContactCard.kt
│   │   └── PrimaryButton.kt
│   └── screens/
│       ├── ContactFormScreen.kt
│       └── ContactListScreen.kt
├── navigation/
│   └── AppNavigation.kt
├── utils/
│   ├── CepMask.kt
│   ├── Constants.kt
│   ├── DateMask.kt
│   ├── EmailValidator.kt
│   ├── MaskVisualTransformation.kt
│   └── PhoneMask.kt
└── MainActivity.kt
```

### Camadas

- `model`: modelos de dados da aplicação e respostas da API
- `network`: serviços Retrofit (contatos + ViaCEP) e configuração HTTP com OkHttp
- `repository`: comunicação com API, tratamento de erros de rede e integração ViaCEP
- `viewmodel`: gerenciamento de estado, loading, validação e ações de CRUD
- `ui/components`: componentes reutilizáveis (campos de texto, botões, cards, estados visuais)
- `ui/screens`: telas principais (lista de contatos e formulário)
- `navigation`: rotas e fluxo de navegação
- `utils`: constantes, máscaras de entrada, validações e transformações visuais

## Modelo de dados

```kotlin
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
```

## Integração com API

A aplicação consome um backend hospedado no **Render**.

URL base configurada em `Constants.kt`:

```kotlin
const val BASE_URL = "https://android-turma-2.onrender.com/"
```

### Endpoints

| Método   | Rota              | Descrição             |
|----------|-------------------|-----------------------|
| `GET`    | `/contacts`       | Listar todos          |
| `GET`    | `/contacts/{id}`  | Buscar por ID         |
| `POST`   | `/contacts`       | Criar novo contato    |
| `PUT`    | `/contacts/{id}`  | Atualizar contato     |
| `DELETE` | `/contacts/{id}`  | Excluir contato       |

### Formato esperado da API

```json
{
  "success": true,
  "data": []
}
```

ou

```json
{
  "success": true,
  "data": {
    "id": "uuid",
    "nome": "Maria",
    "email": "maria@email.com",
    "telefone": "(83) 99999-9999",
    "nascimento": "01/01/2000",
    "cep": "58000-000",
    "bairro": "Centro",
    "logradouro": "Rua Principal",
    "numero": "100",
    "estado": "PB",
    "cidade": "João Pessoa"
  }
}
```

### ViaCEP

Busca de endereço por CEP via API pública:

```text
https://viacep.com.br/ws/{cep}/json/
```

O preenchimento automático é disparado ao digitar os 8 dígitos do CEP no formulário.

## Tratamento de erros de rede

O app possui tratamento amigável para diferentes cenários:

| Exceção                  | Mensagem exibida                                                        |
|--------------------------|-------------------------------------------------------------------------|
| `UnknownHostException`   | Sem internet no momento. Verifique sua conexão e tente novamente.       |
| `ConnectException`       | Não foi possível acessar o servidor agora. Tente novamente em instantes.|
| `SocketTimeoutException` | A conexão demorou mais do que o esperado. Tente novamente.              |
| `IOException`            | Ocorreu um problema de rede. Confira sua conexão e tente novamente.     |
| Outros                   | Ocorreu um erro inesperado. Tente novamente.                            |

Configurações de rede:
- Timeout de conexão, leitura e escrita: **30 segundos**
- Retry automático em falha de conexão
- Logging de requisições HTTP em modo debug

## Componentes reutilizáveis

| Componente           | Descrição                                                    |
|----------------------|--------------------------------------------------------------|
| `CustomTextField`    | Campo de texto com ícone, placeholder e mensagem de erro     |
| `MaskedTextField`    | Campo com máscara dinâmica (telefone, CEP)                   |
| `DatePickerField`    | Seletor de data com DatePickerDialog do Material 3           |
| `ModernContactCard`  | Card de contato com avatar, dados e ações (editar/excluir)   |
| `PrimaryButton`      | Botão primário com estado de loading                         |
| `LoadingView`        | Tela de carregamento animada                                 |
| `EmptyListView`      | Estado vazio com ilustração e orientações                    |
| `ErrorText`          | Texto de erro com animação de entrada/saída                  |

## Dependências principais

As dependências estão configuradas em:

- `gradle/libs.versions.toml`
- `app/build.gradle.kts`

Principais bibliotecas:

- `androidx.compose.material3`
- `androidx.compose.material:material-icons-extended`
- `androidx.navigation:navigation-compose:2.7.7`
- `com.squareup.retrofit2:retrofit:2.9.0`
- `com.squareup.retrofit2:converter-gson:2.9.0`
- `com.squareup.okhttp3:logging-interceptor:4.12.0`
- `org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3`
- `androidx.lifecycle:lifecycle-viewmodel-compose:2.6.1`
- `androidx.lifecycle:lifecycle-runtime-compose:2.6.1`

## Como executar

### Requisitos

- Android Studio atualizado
- JDK compatível com o projeto
- SDK Android instalado
- Backend online acessível no Render

### Passos

1. Abra o projeto no Android Studio.
2. Verifique a URL em `Constants.kt`.
3. Faça sync do Gradle.
4. Execute em emulador ou celular físico.

### Compilar debug

```powershell
Set-Location "C:\Users\msanto13\AndroidStudioProjects\contatos"
.\gradlew.bat :app:assembleDebug --console=plain
```

## Pronto para demonstração

O projeto está preparado para demonstração usando backend online, sem depender de:

- `10.0.2.2`
- IP local
- mesma rede Wi-Fi
- localhost da máquina

