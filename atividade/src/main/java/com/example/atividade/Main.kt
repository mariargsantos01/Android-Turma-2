package com.example.atividade

import java.util.Scanner

/**
 * Projeto: Introdução ao Desenvolvimento com Kotlin
 * Tema: Cadastro de Produtos (Console + POO + Scanner)
 *
 * Requisitos cobertos:
 * 1) Classe de modelo (data class Produto)
 * 2) Estrutura de lista para gerenciar objetos (encapsulada no CRUD)
 * 3) Funcionalidades: Cadastrar, Listar, Pesquisar, Alterar, Remover, Finalizar
 * 4) Uso de Null Safety (Double? + safe calls)
 * 5) Uso de Elvis Operator ( ?: )
 * 6) POO (classe abstrata Crud<T> + ProdutoCRUD com encapsulamento da lista)
 * 7) Validações (nome obrigatório, parsing seguro)
 * 8) Interação com usuário usando Scanner
 */

data class Produto(
    var id: Int,
    var nome: String,
    var preco: Double?
)

/** ===== CLASSE ABSTRATA ===== */
abstract class Crud<T> {

    abstract fun cadastrar(scanner: Scanner)
    abstract fun listar()
    abstract fun pesquisar(scanner: Scanner)
    abstract fun alterar(scanner: Scanner)
    abstract fun remover(scanner: Scanner)

    protected fun lerInt(scanner: Scanner, texto: String): Int {
        while (true) {
            print(texto)
            val entrada = scanner.nextLine().trim()
            val n = entrada.toIntOrNull()
            if (n != null) return n
            println("Entrada inválida. Digite um número inteiro.")
        }
    }
}

/** ===== ProdutoCRUD  ===== */
class ProdutoCRUD : Crud<Produto>() {

    private val itens = mutableListOf<Produto>()

    private fun reindexarIds() {
        itens.forEachIndexed { index, produto ->
            produto.id = index + 1
        }
    }

    /** Cadastrar */
    override fun cadastrar(scanner: Scanner) {
        println("\n--- CADASTRAR PRODUTO ---")

        val id = itens.size + 1
        println("ID gerado automaticamente: $id")

        print("Nome: ")
        val nome = scanner.nextLine()
        if (nome.isBlank()) {
            println("Nome não pode ser vazio.")
            return
        }

        print("Preço (pode deixar vazio; use vírgula ou ponto): ")
        val precoInput = scanner.nextLine()

        val preco: Double = precoInput
            .replace(',', '.')
            .toDoubleOrNull() ?: 0.0

        val novo = Produto(id = id, nome = nome.trim(), preco = preco)
        itens.add(novo)
        println("Produto cadastrado com sucesso: $novo")
    }

    /**  Listar */
    override fun listar() {
        println("\n--- LISTAR PRODUTOS ---")
        if (itens.isEmpty()) {
            println("Nenhum produto cadastrado.")
            return
        }
        itens.forEach { produto ->
            val precoFormatado = produto.preco?.let { "R$ %.2f".format(it) } ?: "sem preço"
            println("ID: ${produto.id} | Nome: ${produto.nome} | Preço: $precoFormatado")
        }
    }

    /** Pesquisar  */
    override fun pesquisar(scanner: Scanner) {
        println("\n--- PESQUISAR POR NOME ---")
        print("Digite o texto do nome para buscar: ")
        val termo = scanner.nextLine().trim()

        if (termo.isBlank()) {
            println("O termo de busca não pode ser vazio.")
            return
        }

        val resultados = itens.filter { it.nome.contains(termo, ignoreCase = true) }
        if (resultados.isEmpty()) {
            println("Nenhum produto encontrado contendo \"$termo\".")
            return
        }

        println("✔ Encontrados ${resultados.size} produto(s) contendo \"$termo\":")
        resultados.forEach { produto ->
            val precoFormatado = produto.preco?.let { "R$ %.2f".format(it) } ?: "sem preço"
            println("ID: ${produto.id} | Nome: ${produto.nome} | Preço: $precoFormatado")
        }
    }

    /** Alterar  */
    override fun alterar(scanner: Scanner) {
        println("\n--- ALTERAR PRODUTO ---")
        val id = lerInt(scanner, "Informe o ID para alterar: ")

        // Atenção: como IDs são reindexados, eles sempre vão de 1..N
        val produto = itens.find { it.id == id }
        if (produto == null) {
            println("Produto com ID $id não encontrado.")
            return
        }

        print("Novo nome (atual: ${produto.nome}) — deixe vazio para manter: ")
        val novoNome = scanner.nextLine()
        if (novoNome.isNotBlank()) {
            produto.nome = novoNome.trim()
        }

        val precoAtualFmt = produto.preco?.let { "R$ %.2f".format(it) } ?: "sem preço"
        print("Novo preço (atual: $precoAtualFmt) — deixe vazio para manter: ")
        val novoPrecoInput = scanner.nextLine()

        if (novoPrecoInput.isNotBlank()) {
            val novoPreco = novoPrecoInput.replace(',', '.').toDoubleOrNull()
            if (novoPreco != null) {
                produto.preco = novoPreco
            } else {
                println("Valor inválido; preço mantido.")
            }
        }

        println("Produto atualizado: $produto")
    }

    /** Remover */
    override fun remover(scanner: Scanner) {
        println("\n--- REMOVER PRODUTO ---")
        val id = lerInt(scanner, "Informe o ID para remover: ")

        val removido = itens.removeIf { it.id == id }
        if (removido) {
            reindexarIds()
            println("Produto removido. IDs reindexados de 1..${itens.size}.")
        } else {
            println("Produto com ID $id não encontrado.")
        }
    }
}

/** ===== TELA INICIAL ===== */
fun telaInicial(scanner: Scanner): Boolean {
    println("====================================")
    println("   ✨ Bem-vindo à Loja Pixel Games ✨")
    println("====================================")
    println("1 - Entrar")
    println("2 - Sair")

    while (true) {
        print("Escolha uma opção: ")
        val opcao = scanner.nextLine().trim().toIntOrNull()
        when (opcao) {
            1 -> return true
            2 -> return false
            else -> println("Opção inválida! Tente novamente.\n")
        }
    }
}

/** ===== MAIN ===== */
fun main() {
    val scanner = Scanner(System.`in`)
    val crud: Crud<Produto> = ProdutoCRUD()

    val continuar = telaInicial(scanner)
    if (!continuar) {
        println("Saindo... Até a próxima! 👋")
        return
    }

    var opcao: Int
    do {
        println("\n==============================")
        println("        MENU DE PRODUTOS      ")
        println("==============================")
        println("1 - Cadastrar")
        println("2 - Listar")
        println("3 - Pesquisar por NOME")
        println("4 - Alterar")
        println("5 - Remover")
        println("6 - Finalizar")

        print("Escolha uma opção: ")
        opcao = scanner.nextLine().trim().toIntOrNull() ?: -1

        when (opcao) {
            1 -> crud.cadastrar(scanner)
            2 -> crud.listar()
            3 -> crud.pesquisar(scanner)
            4 -> crud.alterar(scanner)
            5 -> crud.remover(scanner)
            6 -> println("Finalizando... Obrigado por usar o sistema.")
            else -> println("Opção inválida! Tente novamente.")
        }
    } while (opcao != 6)
}