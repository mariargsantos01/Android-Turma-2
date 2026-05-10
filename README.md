# Contatos Android App

Aplicativo Android para gerenciamento de contatos com integração REST própria e busca de endereço via ViaCEP.

## Tecnologias utilizadas

- Kotlin
- Jetpack Compose
- Material 3
- MVVM
- Retrofit
- Gson Converter
- Coroutines
- StateFlow / MutableStateFlow
- Navigation Compose
- ViaCEP

## Arquitetura

O projeto utiliza arquitetura **MVVM** com separação clara de responsabilidades:

```text
app/src/main/java/com/example/contatos/
├── model
├── network
├── repository
├── viewmodel
├── ui
│   ├── components
│   └── screens
├── navigation
└── utils
```

### Camadas

- `model`: modelos de dados da aplicação e respostas da API
- `network`: serviços Retrofit e configuração HTTP
- `repository`: comunicação com API, tratamento de erros e integração ViaCEP
- `viewmodel`: gerenciamento de estado, loading e ações de CRUD
- `ui`: telas e componentes Compose
- `navigation`: rotas e fluxo de navegação
- `utils`: constantes, máscaras, validações e utilitários auxiliares

## Integração com API

A aplicação está preparada para consumir um backend online hospedado no **Render**.

A URL base fica centralizada em:

- `app/src/main/java/com/example/contatos/utils/Constants.kt`

Exemplo atual:

```kotlin
const val BASE_URL = "https://seu-backend.onrender.com/"
```

> Substitua `https://seu-backend.onrender.com/` pela URL real do seu backend publicado no Render.

### Endpoints esperados

- `GET /contacts`
- `GET /contacts/{id}`
- `POST /contacts`
- `PUT /contacts/{id}`
- `DELETE /contacts/{id}`

### Formato esperado da API

O app espera respostas no formato:

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
    "nome": "Maria"
  }
}
```

### ViaCEP

A busca de CEP continua funcionando normalmente via:

```text
https://viacep.com.br/ws/{cep}/json/
```

## Tratamento de erro de rede

O app já possui tratamento amigável para:

- falta de internet
- servidor offline
- timeout
- erro inesperado

As mensagens são exibidas sem crash da aplicação, preservando a experiência em:

- celular físico
- backend online
- conexões instáveis

## Dependências principais

As dependências estão configuradas em:

- `gradle/libs.versions.toml`
- `app/build.gradle.kts`

Principais bibliotecas:

- `androidx.compose.material3`
- `androidx.navigation:navigation-compose`
- `com.squareup.retrofit2:retrofit`
- `com.squareup.retrofit2:converter-gson`
- `org.jetbrains.kotlinx:kotlinx-coroutines-android`
- `androidx.lifecycle:lifecycle-viewmodel-compose`
- `androidx.lifecycle:lifecycle-runtime-compose`

## Como executar

### Requisitos

- Android Studio atualizado
- JDK compatível com o projeto
- SDK Android instalado
- backend online acessível no Render

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

