# 🚨 SafePasto Backend - Sistema de Alertas Comunitarias

Backend desarrollado en Java Spring Boot para el sistema de alertas comunitarias de Pasto, Nariño.

## 🚀 Características

- ✅ Autenticación JWT segura
- ✅ Gestión de usuarios y roles
- ✅ Sistema de alertas en tiempo real
- ✅ WebSocket para notificaciones instantáneas
- ✅ Geolocalización de alertas
- ✅ API RESTful documentada
- ✅ Configuración para producción en Render

## 🛠️ Tecnologías

- **Java 17** + **Spring Boot 3.2.0**
- **PostgreSQL** - Base de datos
- **JWT** - Autenticación
- **WebSocket** - Comunicación en tiempo real
- **Docker** - Contenedores
- **Maven** - Gestión de dependencias

## 📋 Prerrequisitos

- Java 17 o superior
- Maven 3.6+
- PostgreSQL 14+
- Docker (opcional)

## 🏃‍♂️ Ejecución Local

### 1. Configuración de Base de Datos
```bash
# Crear base de datos
createdb safepasto_db

# O usar Docker
docker-compose up postgres -d