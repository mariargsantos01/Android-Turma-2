const { v4: uuidv4 } = require('uuid');
const { readJSON, writeJSON } = require('../utils/fileHandler');

class ContactService {
  async getAll() {
    return await readJSON();
  }

  async getById(id) {
    const contacts = await readJSON();
    const contact = contacts.find((c) => c.id === id);
    if (!contact) {
      const error = new Error('Contato não encontrado');
      error.statusCode = 404;
      throw error;
    }
    return contact;
  }

  async create(data) {
    this.validate(data);

    const contacts = await readJSON();
    const now = new Date().toISOString();

    const newContact = {
      id: uuidv4(),
      nome: data.nome,
      email: data.email,
      telefone: data.telefone,
      nascimento: data.nascimento || null,
      cep: data.cep || null,
      bairro: data.bairro || null,
      logradouro: data.logradouro || null,
      numero: data.numero || null,
      estado: data.estado || null,
      cidade: data.cidade || null,
      createdAt: now,
      updatedAt: now,
    };

    contacts.push(newContact);
    await writeJSON(contacts);
    return newContact;
  }

  async update(id, data) {
    this.validate(data);

    const contacts = await readJSON();
    const index = contacts.findIndex((c) => c.id === id);

    if (index === -1) {
      const error = new Error('Contato não encontrado');
      error.statusCode = 404;
      throw error;
    }

    const updated = {
      ...contacts[index],
      nome: data.nome,
      email: data.email,
      telefone: data.telefone,
      nascimento: data.nascimento ?? contacts[index].nascimento,
      cep: data.cep ?? contacts[index].cep,
      bairro: data.bairro ?? contacts[index].bairro,
      logradouro: data.logradouro ?? contacts[index].logradouro,
      numero: data.numero ?? contacts[index].numero,
      estado: data.estado ?? contacts[index].estado,
      cidade: data.cidade ?? contacts[index].cidade,
      updatedAt: new Date().toISOString(),
    };

    contacts[index] = updated;
    await writeJSON(contacts);
    return updated;
  }

  async remove(id) {
    const contacts = await readJSON();
    const index = contacts.findIndex((c) => c.id === id);

    if (index === -1) {
      const error = new Error('Contato não encontrado');
      error.statusCode = 404;
      throw error;
    }

    contacts.splice(index, 1);
    await writeJSON(contacts);
  }

  validate(data) {
    const errors = [];
    if (!data.nome || !data.nome.trim()) errors.push('Nome é obrigatório');
    if (!data.email || !data.email.trim()) errors.push('Email é obrigatório');
    if (!data.telefone || !data.telefone.trim()) errors.push('Telefone é obrigatório');

    if (errors.length > 0) {
      const error = new Error(errors.join(', '));
      error.statusCode = 400;
      throw error;
    }
  }
}

module.exports = new ContactService();
