const contactService = require('../services/ContactService');

class ContactController {
  async getAll(req, res, next) {
    try {
      const contacts = await contactService.getAll();
      res.json({ success: true, data: contacts });
    } catch (error) {
      next(error);
    }
  }

  async getById(req, res, next) {
    try {
      const contact = await contactService.getById(req.params.id);
      res.json({ success: true, data: contact });
    } catch (error) {
      next(error);
    }
  }

  async create(req, res, next) {
    try {
      const contact = await contactService.create(req.body);
      res.status(201).json({ success: true, data: contact });
    } catch (error) {
      next(error);
    }
  }

  async update(req, res, next) {
    try {
      const contact = await contactService.update(req.params.id, req.body);
      res.json({ success: true, data: contact });
    } catch (error) {
      next(error);
    }
  }

  async remove(req, res, next) {
    try {
      await contactService.remove(req.params.id);
      res.json({ success: true, message: 'Contato removido com sucesso' });
    } catch (error) {
      next(error);
    }
  }
}

module.exports = new ContactController();
