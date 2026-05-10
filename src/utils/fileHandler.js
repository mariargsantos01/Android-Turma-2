const fs = require('fs').promises;
const path = require('path');

const DB_PATH = path.join(__dirname, '..', '..', 'database', 'contacts.json');
const DB_DIR = path.join(__dirname, '..', '..', 'database');
const TEMP_DB_PATH = path.join(DB_DIR, 'contacts.tmp.json');

async function ensureDatabaseFile() {
  await fs.mkdir(DB_DIR, { recursive: true });

  try {
    await fs.access(DB_PATH);
  } catch (error) {
    if (error.code === 'ENOENT') {
      await fs.writeFile(DB_PATH, '[]', 'utf-8');
      return;
    }

    throw error;
  }
}

async function readJSON() {
  await ensureDatabaseFile();

  try {
    const data = await fs.readFile(DB_PATH, 'utf-8');
    const parsedData = JSON.parse(data);

    if (!Array.isArray(parsedData)) {
      const error = new Error('O arquivo de contatos está inválido');
      error.statusCode = 500;
      throw error;
    }

    return parsedData;
  } catch (error) {
    if (error.code === 'ENOENT') {
      await ensureDatabaseFile();
      return [];
    }

    if (error instanceof SyntaxError) {
      const parseError = new Error('Falha ao ler o arquivo de contatos');
      parseError.statusCode = 500;
      throw parseError;
    }

    throw error;
  }
}

async function writeJSON(data) {
  if (!Array.isArray(data)) {
    const error = new Error('Os dados de contatos precisam estar em formato de lista');
    error.statusCode = 500;
    throw error;
  }

  await ensureDatabaseFile();
  await fs.writeFile(TEMP_DB_PATH, JSON.stringify(data, null, 2), 'utf-8');
  await fs.rename(TEMP_DB_PATH, DB_PATH);
}

module.exports = { ensureDatabaseFile, readJSON, writeJSON };
