# Contacts API

API REST simples para gerenciamento de contatos com Node.js, Express e armazenamento em arquivo JSON local. O projeto foi preparado para integração com aplicativo Android Kotlin e funciona em plataformas Node.js como Render e Railway.

## Tecnologias

- Node.js
- Express
- JavaScript
- CORS
- dotenv
- uuid
- File System (`fs`)

## Requisitos

- Node.js 18 ou superior
- npm 9 ou superior

## Estrutura do projeto

```text
contacts-api/
├── database/
│   └── contacts.json
├── src/
│   ├── controllers/
│   ├── middlewares/
│   ├── routes/
│   ├── services/
│   ├── utils/
│   └── server.js
├── .env
├── .env.example
├── .gitignore
├── package.json
└── README.md
```

## Modelo do contato

Cada contato possui os campos:

- `id`
- `nome`
- `email`
- `telefone`
- `nascimento`
- `cep`
- `bairro`
- `logradouro`
- `numero`
- `estado`
- `cidade`
- `createdAt`
- `updatedAt`

## Respostas da API

### Sucesso

```json
{
  "success": true,
  "data": []
}
```

### Erro

```json
{
  "success": false,
  "message": "Contato não encontrado"
}
```

### Rota inexistente

```json
{
  "success": false,
  "message": "Rota não encontrada"
}
```

## Endpoints

Base URL em produção:

```text
https://android-turma-2.onrender.com/
```

### `GET /`

Retorna o status da API.

**Resposta:**

```json
{
  "success": true,
  "message": "API running"
}
```

### `GET /contacts`

Lista todos os contatos.

### `GET /contacts/:id`

Busca um contato pelo ID.

### `POST /contacts`

Cria um novo contato.

**Body de exemplo:**

```json
{
  "nome": "Maria Souza",
  "email": "maria@email.com",
  "telefone": "11999999999",
  "nascimento": "1995-08-21",
  "cep": "01001000",
  "bairro": "Centro",
  "logradouro": "Rua Exemplo",
  "numero": "123",
  "estado": "SP",
  "cidade": "São Paulo"
}
```

### `PUT /contacts/:id`

Atualiza um contato existente.

### `DELETE /contacts/:id`

Remove um contato.

Exemplos em produção:

- `GET https://android-turma-2.onrender.com/`
- `GET https://android-turma-2.onrender.com/contacts`
- `POST https://android-turma-2.onrender.com/contacts`

## Como rodar localmente

### 1. Instalar dependências

```bash
npm install
```

### 2. Criar variáveis de ambiente

Copie o arquivo `.env.example` para `.env` e ajuste se necessário.

Exemplo:

```env
PORT=3000
NODE_ENV=development
```

### 3. Iniciar em desenvolvimento

```bash
npm run dev
```

### 4. Iniciar em produção/local simples

```bash
npm start
```

A API ficará disponível em:

```text
http://localhost:3000
```

Em produção, a API ficará disponível em:

```text
https://android-turma-2.onrender.com/
```

## Armazenamento em JSON

Os dados são gravados em `database/contacts.json`.

Características da persistência:

- cria o arquivo automaticamente se ele não existir
- lê contatos diretamente do JSON
- grava alterações com escrita atômica usando arquivo temporário
- mantém `createdAt` e `updatedAt`
- continua funcionando sem banco SQL
- valida se o conteúdo do arquivo é uma lista válida
- falha com erro controlado se o JSON estiver corrompido

## Comportamento do servidor

- usa `process.env.PORT || 3000`
- escuta em `0.0.0.0` para compatibilidade com hospedagens Node.js online
- expõe `GET /` como health check
- desabilita o header `x-powered-by`
- aceita JSON com limite de `1mb`
- libera CORS para integração com o app Android
- trata `unhandledRejection` e `uncaughtException`

## Compatibilidade com hospedagens online

O projeto está pronto para uso em hospedagens Node.js online, incluindo Render e Railway, porque:

- usa `process.env.PORT`
- sobe com `npm start`
- escuta em `0.0.0.0`
- não depende de banco externo
- usa apenas armazenamento local em JSON
- possui endpoint raiz útil para health check

## Importante sobre persistência online

Este projeto usa arquivo local JSON. Em plataformas com sistema de arquivos efêmero, os dados podem ser perdidos após reinício ou novo deploy.

A API continua funcionando normalmente nessas plataformas, mas o arquivo `database/contacts.json` pode ser recriado vazio após reinicializações da infraestrutura.

## Scripts disponíveis

```json
{
  "dev": "nodemon src/server.js",
  "start": "node src/server.js"
}
```

## Observações finais

- CORS está liberado para integração com Android
- a API responde em JSON padronizado
- há tratamento global de erros
- o endpoint raiz pode ser usado como health check
- o projeto está pronto para deploy simples
- o arquivo `contacts.json` é criado automaticamente no primeiro start, se necessário
