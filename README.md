# Contacts API

API REST simples para gerenciamento de contatos com Node.js, Express e armazenamento em arquivo JSON local. O projeto foi preparado para integração com aplicativo Android Kotlin e para deploy em plataformas Node.js como Render e Railway.

## Tecnologias

- Node.js
- Express
- JavaScript
- CORS
- dotenv
- uuid
- File System (`fs`)

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

## Endpoints

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

## Armazenamento em JSON

Os dados são gravados em `database/contacts.json`.

Características da persistência:

- cria o arquivo automaticamente se ele não existir
- lê contatos diretamente do JSON
- grava alterações com escrita atômica usando arquivo temporário
- mantém `createdAt` e `updatedAt`
- continua funcionando sem banco SQL

## Deploy no Render

### 1. Criar conta

Crie uma conta em https://render.com.

### 2. Conectar GitHub

No painel do Render, conecte sua conta do GitHub e autorize o repositório do projeto.

### 3. Escolher o projeto backend

- Clique em **New +**
- Escolha **Web Service**
- Selecione o repositório deste backend

### 4. Configurar o serviço

Preencha com:

- **Environment**: `Node`
- **Build Command**: `npm install`
- **Start Command**: `npm start`

### 5. Configurar variáveis de ambiente

Adicione no painel do Render:

```env
PORT=10000
NODE_ENV=production
```

Observação: o Render normalmente injeta `PORT` automaticamente. A API já está preparada para usar `process.env.PORT || 3000`.

### 6. Publicar

Finalize a criação do serviço e aguarde o deploy.

Quando o deploy terminar, sua API ficará disponível em uma URL pública do Render.

## Compatibilidade com Railway e outras hospedagens Node.js

O projeto também está pronto para plataformas como Railway e similares porque:

- usa `process.env.PORT`
- sobe com `npm start`
- escuta em `0.0.0.0`
- não depende de banco externo
- usa apenas armazenamento local em JSON

## Importante sobre persistência online

Este projeto usa arquivo local JSON. Em plataformas com sistema de arquivos efêmero, como deploy padrão sem disco persistente, os dados podem ser perdidos após reinício ou novo deploy.

Para persistência real no Render, use uma destas opções:

- anexar um disco persistente ao serviço, quando disponível no seu plano
- manter este backend em uma hospedagem com filesystem persistente

Se o serviço for executado sem disco persistente, a API continuará funcionando, mas o arquivo `database/contacts.json` poderá ser recriado vazio após reinicializações da infraestrutura.

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
