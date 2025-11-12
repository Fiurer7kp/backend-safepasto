-- Datos iniciales para desarrollo
INSERT INTO users (username, email, password, first_name, last_name, created_at, updated_at)
VALUES
('admin', 'admin@safepasto.com', '$2a$10$TuPasswordHasheado', 'Admin', 'SafePasto', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('usuario1', 'usuario1@email.com', '$2a$10$TuPasswordHasheado', 'Juan', 'Pérez', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Nota: Las contraseñas deben ser hasheadas con BCrypt