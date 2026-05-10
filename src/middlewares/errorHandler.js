const errorHandler = (err, req, res, next) => {
  const statusCode = err.statusCode || 500;
  const message = err.message || 'Erro interno do servidor';
  const details = err.details || null;

  console.error({
    statusCode,
    message,
    method: req.method,
    path: req.originalUrl,
    stack: process.env.NODE_ENV === 'production' ? undefined : err.stack,
  });

  const response = {
    success: false,
    message,
  };

  if (details) {
    response.details = details;
  }

  res.status(statusCode).json(response);
};

module.exports = errorHandler;
