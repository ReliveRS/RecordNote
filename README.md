# 🎙️ RecordNote

**RecordNote** es una aplicación Android moderna para la gestión de notas de voz con transcripción automática. Construida con las últimas tecnologías de Android y siguiendo las mejores prácticas de Clean Architecture.

<p align="center">
  <img src="docs/screenshots/logo.png" alt="RecordNote Logo" width="200"/>
</p>

## ✨ Características

### 🎤 Grabación de Audio
- Grabación de audio de alta calidad
- Control de pausa/reanudar durante la grabación
- Visualizador de forma de onda en tiempo real
- Indicador de duración y nivel de audio
- Soporte para grabaciones largas (hasta 60 minutos)

### 📝 Gestión de Notas
- Crear, editar y eliminar notas
- Organización con etiquetas personalizables
- Colores personalizados para cada nota
- Sistema de favoritos
- Búsqueda rápida por título, contenido o etiquetas
- Filtros avanzados

### 🗣️ Transcripción de Voz
- Transcripción automática de audio a texto
- Soporte para múltiples idiomas
- Integración con Google Cloud Speech-to-Text
- Opción de transcripción manual bajo demanda

### ☁️ Sincronización
- Sincronización automática con el servidor
- Trabajo offline con sincronización posterior
- Resolución inteligente de conflictos
- Indicador de estado de sincronización

### 💾 Backup y Seguridad
- Backups automáticos locales
- Exportación de notas en múltiples formatos (JSON, TXT, CSV, Markdown, HTML)
- Encriptación de datos sensibles
- Restauración fácil desde backup

### 🎨 Interfaz y Experiencia
- Material Design 3
- Tema claro y oscuro
- Animaciones fluidas
- Interfaz intuitiva y moderna
- Soporte completo para Jetpack Compose

## 📱 Requisitos

- **Android:** 8.0 (API 26) o superior
- **Espacio:** Mínimo 50 MB
- **Permisos:**
    - Micrófono (para grabación de audio)
    - Almacenamiento (para guardar grabaciones)
    - Internet (para sincronización y transcripción)
    - Notificaciones (para alertas y estado de grabación)

## 🏗️ Arquitectura

RecordNote está construida siguiendo **Clean Architecture** con el patrón **MVVM** (Model-View-ViewModel).

📦 com.recordnote
┣ 📂 data # Capa de Datos
┃ ┣ 📂 local # Base de datos local (Room)
┃ ┃ ┣ 📂 dao # Data Access Objects
┃ ┃ ┣ 📂 database # Configuración de BD
┃ ┃ ┗ 📂 entidades # Entidades de Room
┃ ┣ 📂 remote # API REST
┃ ┃ ┣ 📂 api # Servicios de Retrofit
┃ ┃ ┣ 📂 dto # Data Transfer Objects
┃ ┃ ┗ 📂 interceptor # Interceptores HTTP
┃ ┗ 📂 repositories # Implementación de repositorios
┣ 📂 di # Inyección de Dependencias (Hilt)
┣ 📂 domain # Capa de Dominio
┃ ┣ 📂 model # Modelos de dominio
┃ ┣ 📂 repository # Interfaces de repositorios
┃ ┗ 📂 usecase # Casos de uso
┣ 📂 presentation # Capa de Presentación
┃ ┣ 📂 ui # Pantallas de Compose
┃ ┃ ┣ 📂 auth # Login y Registro
┃ ┃ ┣ 📂 configuracion # Configuración
┃ ┃ ┣ 📂 grabacion # Grabación de audio
┃ ┃ ┣ 📂 inicio # Pantalla principal
┃ ┃ ┣ 📂 notas # Detalle y edición de notas
┃ ┃ ┗ 📂 splash # Splash screen
┃ ┗ 📂 navigation # Navegación de Compose
┣ 📂 theme # Temas y estilos
┣ 📂 utils # Utilidades
┗ 📂 workers # Background tasks (WorkManager)

text

## 🛠️ Stack Tecnológico

### Core
- **Kotlin** - Lenguaje principal
- **Jetpack Compose** - UI moderna y declarativa
- **Material Design 3** - Sistema de diseño

### Arquitectura
- **Clean Architecture** - Separación de responsabilidades
- **MVVM** - Patrón de presentación
- **Coroutines** - Programación asíncrona
- **Flow** - Streams reactivos

### Inyección de Dependencias
- **Hilt** - DI basado en Dagger

### Base de Datos
- **Room** - Persistencia local
- **DataStore** - Preferencias

### Networking
- **Retrofit** - Cliente HTTP
- **OkHttp** - Gestión de red
- **Gson** - Serialización JSON

### Background Tasks
- **WorkManager** - Tareas en segundo plano
- **Foreground Services** - Grabación continua

### Testing
- **JUnit** - Unit tests
- **Mockk** - Mocking
- **Espresso** - UI tests
- **Truth** - Assertions

### Otros
- **Timber** - Logging
- **Coil** - Carga de imágenes
- **Navigation Compose** - Navegación

## 🚀 Instalación

### Clonar el Repositorio

git clone https://github.com/tu-usuario/recordnote.git
cd recordnote

text

### Configurar el Proyecto

1. **Abrir en Android Studio:**
    - Android Studio Hedgehog | 2023.1.1 o superior
    - Gradle 8.2 o superior

2. **Configurar local.properties:**

sdk.dir=/ruta/a/tu/Android/sdk

text

3. **Configurar API Keys (opcional):**
   Si quieres usar transcripción con servicios externos, añade en `local.properties`:

OPENAI_API_KEY=tu_clave_aqui
GOOGLE_CLOUD_API_KEY=tu_clave_aqui

text

### Compilar y Ejecutar

./gradlew assembleDebug

text

O desde Android Studio: **Run > Run 'app'**

## 📦 Builds

### Debug

./gradlew assembleDebug

text

### Release

./gradlew assembleRelease

text

### Tests

Unit tests

./gradlew test
Instrumentation tests

./gradlew connectedAndroidTest
Todos los tests

./gradlew testDebugUnitTest connectedDebugAndroidTest

text

## 📸 Screenshots

<p align="center">
  <img src="docs/screenshots/home.png" width="200" alt="Home"/>
  <img src="docs/screenshots/recording.png" width="200" alt="Recording"/>
  <img src="docs/screenshots/note_detail.png" width="200" alt="Note Detail"/>
  <img src="docs/screenshots/settings.png" width="200" alt="Settings"/>
</p>

## 🗺️ Roadmap

### Versión 1.1 (Próximamente)
- [ ] Widget para inicio rápido de grabación
- [ ] Compartir notas directamente
- [ ] Recordatorios y alarmas
- [ ] Carpetas para organización
- [ ] Búsqueda por voz

### Versión 1.2
- [ ] Sincronización con Google Drive
- [ ] Exportación a PDF
- [ ] Temas personalizables
- [ ] Modo de bajo consumo
- [ ] Estadísticas de uso

### Versión 2.0
- [ ] Reconocimiento de múltiples hablantes
- [ ] Resúmenes automáticos con IA
- [ ] Traducción automática
- [ ] Integración con calendarios
- [ ] Versión web

## 🤝 Contribuir

Las contribuciones son bienvenidas. Por favor:

1. Fork el proyecto
2. Crea una rama para tu feature (`git checkout -b feature/AmazingFeature`)
3. Commit tus cambios (`git commit -m 'Add some AmazingFeature'`)
4. Push a la rama (`git push origin feature/AmazingFeature`)
5. Abre un Pull Request

### Guía de Estilo

- Seguir las convenciones de Kotlin
- Documentar código público con KDoc
- Escribir tests para nueva funcionalidad
- Mantener cobertura de tests > 70%

## 📝 Licencia

Este proyecto está bajo la Licencia MIT - ver el archivo [LICENSE](LICENSE) para más detalles.

## 👥 Autores

- **Tu Nombre** - *Desarrollo inicial* - [tu-github](https://github.com/tu-usuario)

## 🙏 Agradecimientos

- [Material Design](https://m3.material.io/) por las guías de diseño
- [Android Developers](https://developer.android.com/) por la excelente documentación
- Comunidad de Kotlin y Android
- Todos los contribuidores del proyecto

## 📧 Contacto

- **Email:** support@recordnote.com
- **Website:** [recordnote.com](https://recordnote.com)
- **Twitter:** [@RecordNoteApp](https://twitter.com/RecordNoteApp)

## 📊 Estado del Proyecto

![Build Status](https://img.shields.io/badge/build-passing-brightgreen)
![Version](https://img.shields.io/badge/version-1.0.0-blue)
![License](https://img.shields.io/badge/license-MIT-green)
![Platform](https://img.shields.io/badge/platform-Android-green)
![API](https://img.shields.io/badge/API-26%2B-brightgreen)
![Kotlin](https://img.shields.io/badge/Kotlin-1.9.20-purple)
![Compose](https://img.shields.io/badge/Compose-1.5.4-blue)

## 🔐 Seguridad

Si encuentras alguna vulnerabilidad de seguridad, por favor envía un email a security@recordnote.com en lugar de usar el issue tracker.

## 📚 Documentación Adicional

- [Guía de Usuario](docs/USER_GUIDE.md)
- [API Documentation](docs/API.md)
- [Arquitectura Detallada](docs/ARCHITECTURE.md)
- [Guía de Contribución](CONTRIBUTING.md)
- [Changelog](CHANGELOG.md)

## ⚙️ Configuración Avanzada

### Variables de Entorno

Para producción

export RECORDNOTE_ENV=production
export RECORDNOTE_API_URL=https://api.recordnote.com
Para desarrollo

export RECORDNOTE_ENV=development
export RECORDNOTE_API_URL=http://localhost:3000

text

### Flavors de Build

El proyecto soporta múltiples flavors:

- **dev** - Entorno de desarrollo
- **staging** - Pre-producción
- **production** - Producción

./gradlew assembleDevDebug
./gradlew assembleStagingRelease
./gradlew assembleProductionRelease

text

## 🐛 Reportar Bugs

Usa el [issue tracker](https://github.com/tu-usuario/recordnote/issues) de GitHub para reportar bugs.

Por favor incluye:
- Versión de Android
- Versión de la app
- Pasos para reproducir
- Logs relevantes
- Screenshots si aplica

## 💡 Solicitar Features

¿Tienes una idea para mejorar RecordNote? Abre un [feature request](https://github.com/tu-usuario/recordnote/issues/new?template=feature_request.md).

---

<p align="center">
  Hecho con ❤️ usando Kotlin y Jetpack Compose
</p>

<p align="center">
  <a href="https://recordnote.com">Website</a> •
  <a href="https://twitter.com/RecordNoteApp">Twitter</a> •
  <a href="https://github.com/tu-usuario/recordnote">GitHub</a>
</p>