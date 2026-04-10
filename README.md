# Financas App (Atividade 2)

Aplicativo Android em Kotlin + Jetpack Compose para gestao de despesas pessoais.

## Funcionalidades
- Home com saldo automatico (ganhos - gastos)
- Cadastro de ganhos por mes/ano (adicionar, editar, remover)
- Cadastro de gastos por mes/ano (adicionar, editar, remover)
- Cadastro de sonhos financeiros com avaliacao de atingibilidade e progresso
- Persistencia local com Room

## Arquitetura
- MVVM
- UDF: `UI -> Evento -> ViewModel -> Estado -> UI`
- `StateFlow` exposto pelos ViewModels
- Estados imutaveis (`UiState`) e eventos em `sealed class`
- Camadas: `ui`, `viewmodel`, `data/local`, `data/repository`, `data/model`, `navigation`

## Persistencia
Foi usado **Room** porque o app precisa de listas relacionais e consultas filtradas (por tipo, mes/ano) com operacoes de edicao/remocao, o que fica mais adequado do que um armazenamento chave-valor.

## Como executar
```powershell
cd "C:\Users\msanto13\AndroidStudioProjects\Atividade2"
.\gradlew.bat assembleDebug
```

