#!/bin/bash

# Script de despliegue para SafePasto Backend
echo "🚀 Iniciando despliegue de SafePasto Backend..."

# Colores para output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

# Función para imprimir mensajes
print_info() {
    echo -e "${GREEN}✅ $1${NC}"
}

print_warning() {
    echo -e "${YELLOW}⚠️ $1${NC}"
}

print_error() {
    echo -e "${RED}❌ $1${NC}"
}

# Verificar que estamos en el directorio correcto
if [ ! -f "pom.xml" ]; then
    print_error "No se encontró pom.xml. Asegúrate de estar en el directorio raíz del proyecto."
    exit 1
fi

# Verificar variables de entorno para producción
if [ "$1" = "prod" ]; then
    print_info "Modo: Producción"
    
    if [ -z "$DATABASE_URL" ]; then
        print_error "DATABASE_URL no está configurada"
        exit 1
    fi

    if [ -z "$JWT_SECRET" ]; then
        print_warning "JWT_SECRET no está configurada, usando valor por defecto"
    fi
else
    print_info "Modo: Desarrollo"
fi

# Limpiar y compilar la aplicación
print_info "Compilando aplicación..."
mvn clean package -DskipTests

if [ $? -ne 0 ]; then
    print_error "Error en la compilación"
    exit 1
fi

# Verificar que el JAR se creó correctamente
if [ ! -f "target/safepasto-backend-1.0.0.jar" ]; then
    print_error "JAR no encontrado después de la compilación"
    exit 1
fi

print_info "Compilación exitosa"

# Opción para Docker
if [ "$1" = "docker" ]; then
    print_info "Construyendo imagen Docker..."
    docker build -t safepasto-backend .
    
    print_info "Ejecutando con Docker Compose..."
    docker-compose up -d
    
    print_info "Aplicación desplegada en Docker"
    echo "📊 Health Check: http://localhost:8080/api/health"
    echo "🔗 API Base: http://localhost:8080/api"
else
    print_info "Ejecutando aplicación directamente..."
    java -jar target/safepasto-backend-1.0.0.jar
fi

print_info "Despliegue completado exitosamente! 🎉"