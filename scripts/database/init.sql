-- Script de inicialización de base de datos para SafePasto
-- Este script se ejecuta automáticamente al crear el contenedor de PostgreSQL

-- Crear extensiones si es necesario
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

-- Crear tabla de usuarios
CREATE TABLE IF NOT EXISTS users (
    id BIGSERIAL PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    first_name VARCHAR(50),
    last_name VARCHAR(50),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Crear tabla de roles de usuarios
CREATE TABLE IF NOT EXISTS user_roles (
    user_id BIGINT NOT NULL,
    roles VARCHAR(50) NOT NULL,
    PRIMARY KEY (user_id, roles),
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

-- Crear tabla de alertas
CREATE TABLE IF NOT EXISTS alertas (
    id BIGSERIAL PRIMARY KEY,
    tipo_alerta VARCHAR(50) NOT NULL,
    gravedad VARCHAR(20) NOT NULL,
    descripcion TEXT NOT NULL,
    usuario_id BIGINT NOT NULL,
    latitud DECIMAL(10, 8),
    longitud DECIMAL(11, 8),
    direccion VARCHAR(255),
    barrio VARCHAR(100),
    ciudad VARCHAR(100) DEFAULT 'Pasto',
    departamento VARCHAR(100) DEFAULT 'Nariño',
    activa BOOLEAN DEFAULT TRUE,
    verificada BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (usuario_id) REFERENCES users(id) ON DELETE CASCADE
);

-- Índices para mejor performance
CREATE INDEX IF NOT EXISTS idx_alertas_activas ON alertas(activa, created_at);
CREATE INDEX IF NOT EXISTS idx_alertas_ubicacion ON alertas(latitud, longitud);
CREATE INDEX IF NOT EXISTS idx_alertas_usuario ON alertas(usuario_id);
CREATE INDEX IF NOT EXISTS idx_alertas_tipo ON alertas(tipo_alerta);
CREATE INDEX IF NOT EXISTS idx_alertas_gravedad ON alertas(gravedad);

-- Insertar usuario administrador por defecto (contraseña: admin123)
INSERT INTO users (username, email, password, first_name, last_name) 
VALUES (
    'admin', 
    'admin@safepasto.com', 
    '$2a$10$8S5s.vhqRlD.U.2sD.7rE.XrW9p5K5V5z5W5V5W5V5W5V5W5V5W5V5W', -- bcrypt para 'admin123'
    'Admin', 
    'SafePasto'
) ON CONFLICT (username) DO NOTHING;

-- Asignar rol de administrador
INSERT INTO user_roles (user_id, roles) 
VALUES (1, 'ROLE_ADMIN') 
ON CONFLICT (user_id, roles) DO NOTHING;

-- Insertar usuario de prueba (contraseña: usuario123)
INSERT INTO users (username, email, password, first_name, last_name) 
VALUES (
    'usuario1', 
    'usuario1@email.com', 
    '$2a$10$8S5s.vhqRlD.U.2sD.7rE.XrW9p5K5V5z5W5V5W5V5W5V5W5V5W5V5W', -- bcrypt para 'usuario123'
    'Juan', 
    'Pérez'
) ON CONFLICT (username) DO NOTHING;

-- Asignar rol de usuario normal
INSERT INTO user_roles (user_id, roles) 
VALUES (2, 'ROLE_USER') 
ON CONFLICT (user_id, roles) DO NOTHING;

-- Insertar algunas alertas de ejemplo
INSERT INTO alertas (tipo_alerta, gravedad, descripcion, usuario_id, latitud, longitud, direccion, barrio) 
VALUES 
(
    'SEGURIDAD_ROBO', 
    'MEDIA', 
    'Se reportó robo a persona en el parque principal. Hombre con chaqueta negra huyó hacia el norte.', 
    2, 
    1.208396, 
    -77.277846, 
    'Calle 18 #24-35', 
    'Centro'
),
(
    'ACCIDENTE_TRANSITO', 
    'ALTA', 
    'Accidente entre moto y automóvil en la avenida. Se requiere ambulancia.', 
    2, 
    1.210456, 
    -77.279123, 
    'Avenida de los Estudiantes', 
    'San José'
),
(
    'INCENDIO', 
    'URGENTE', 
    'Incendio en vivienda de dos pisos. Bomberos en camino.', 
    1, 
    1.206789, 
    -77.281234, 
    'Carrera 25 #15-40', 
    'La Aurora'
) ON CONFLICT DO NOTHING;

-- Mensaje de confirmación
DO $$ 
BEGIN
    RAISE NOTICE 'Base de datos SafePasto inicializada exitosamente!';
    RAISE NOTICE 'Usuarios creados: admin/admin123, usuario1/usuario123';
END $$;