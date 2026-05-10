const { Router } = require('express');
const contactController = require('../controllers/ContactController');

const router = Router();

router.get('/contacts', (req, res, next) => contactController.getAll(req, res, next));
router.get('/contacts/:id', (req, res, next) => contactController.getById(req, res, next));
router.post('/contacts', (req, res, next) => contactController.create(req, res, next));
router.put('/contacts/:id', (req, res, next) => contactController.update(req, res, next));
router.delete('/contacts/:id', (req, res, next) => contactController.remove(req, res, next));

module.exports = router;
