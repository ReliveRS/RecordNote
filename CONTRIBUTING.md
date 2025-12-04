# Guía de Contribución

¡Gracias por tu interés en contribuir a RecordNote! 🎉

## Código de Conducta

Este proyecto adhiere al [Contributor Covenant Code of Conduct](CODE_OF_CONDUCT.md). Al participar, se espera que mantengas este código.

## ¿Cómo Contribuir?

### Reportar Bugs

1. Verifica que el bug no haya sido reportado previamente
2. Usa la plantilla de issue para bugs
3. Incluye toda la información solicitada
4. Añade logs y screenshots si es posible

### Proponer Features

1. Abre un issue describiendo el feature
2. Explica por qué sería útil
3. Proporciona ejemplos de uso
4. Espera feedback antes de empezar a codear

### Pull Requests

1. Fork el repositorio
2. Crea una rama desde `develop`
3. Haz tus cambios
4. Escribe o actualiza tests
5. Asegúrate de que todos los tests pasen
6. Actualiza la documentación
7. Envía el PR

## Estándares de Código

### Kotlin

- Seguir [Kotlin Coding Conventions](https://kotlinlang.org/docs/coding-conventions.html)
- Usar nombres descriptivos
- Documentar funciones públicas con KDoc
- Máximo 100 caracteres por línea

### Compose

- Componentes pequeños y reutilizables
- Separar lógica de UI
- Usar state hoisting apropiadamente
- Seguir guías de Material Design 3

### Commits

Formato de commits:
tipo(scope): descripción corta

Descripción detallada del cambio

Fixes #123

text

Tipos:
- `feat`: Nuevo feature
- `fix`: Bug fix
- `docs`: Documentación
- `style`: Formato, punto y coma, etc.
- `refactor`: Refactorización
- `test`: Tests
- `chore`: Tareas de mantenimiento

## Tests

- Unit tests para lógica de negocio
- UI tests para flows críticos
- Mantener cobertura > 70%
- Tests deben ser rápidos y determinísticos

## Documentación

- Actualizar README si es necesario
- Documentar APIs públicas
- Añadir comentarios para código complejo
- Actualizar CHANGELOG

## Proceso de Review

1. CI debe pasar (build, tests, lint)
2. Al menos 1 aprobación requerida
3. Código debe seguir estándares
4. Tests deben estar incluidos
5. Documentación debe estar actualizada

## Preguntas

Si tienes preguntas, abre un issue o contacta a maintainers@recordnote.com