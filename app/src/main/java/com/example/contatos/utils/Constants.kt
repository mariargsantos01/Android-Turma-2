package com.example.contatos.utils

object Constants {
    // URL pública do backend hospedado no Render.
    // Substitua pelo domínio real do seu backend quando necessário.
    const val BASE_URL = "https://seu-backend.onrender.com/"

    const val VIACEP_URL = "https://viacep.com.br/"
    const val CEP_LENGTH = 8
    const val NETWORK_TIMEOUT_SECONDS = 30L

    // Mensagens de erro amigáveis para uso online
    const val ERRO_SEM_INTERNET = "Sem internet no momento. Verifique sua conexão e tente novamente."
    const val ERRO_SERVIDOR_OFFLINE = "Não foi possível acessar o servidor agora. Tente novamente em instantes."
    const val ERRO_CONEXAO_TIMEOUT = "A conexão demorou mais do que o esperado. Tente novamente em alguns segundos."
    const val ERRO_REDE_GENERICO = "Ocorreu um problema de rede. Confira sua conexão e tente novamente."
    const val ERRO_INESPERADO = "Ocorreu um erro inesperado. Tente novamente."
    const val ERRO_BUSCAR_CONTATOS = "Erro ao buscar contatos."
    const val ERRO_BUSCAR_CONTATO = "Erro ao buscar contato."
    const val ERRO_CRIAR_CONTATO = "Erro ao criar contato."
    const val ERRO_ATUALIZAR_CONTATO = "Erro ao atualizar contato."
    const val ERRO_EXCLUIR_CONTATO = "Erro ao excluir contato."
    const val ERRO_BUSCAR_CEP = "Erro ao buscar CEP."
    const val ERRO_CEP_NAO_ENCONTRADO = "CEP não encontrado."
}
