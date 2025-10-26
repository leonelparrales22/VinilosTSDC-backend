# VinilosTSDC-backend

## Descripción

Esta aplicación es un backend para la plataforma VinilosTSDC, implementada en Android utilizando Kotlin y Jetpack Compose. Permite a los usuarios seleccionar roles (Visitante o Coleccionista), navegar por menús, y gestionar álbumes musicales con funcionalidades de listado y detalle.

## Arquitectura

La aplicación sigue los patrones de diseño MVVM (Model-View-ViewModel), Repository y Service Adapter para una separación clara de responsabilidades:

- **MVVM**: Separa la lógica de presentación (View) de la lógica de negocio (ViewModel) y los datos (Model).
- **Repository**: Abstrae el acceso a datos, proporcionando una interfaz unificada para operaciones de datos.
- **Service Adapter**: Adapta las llamadas a servicios externos (API) utilizando Retrofit.

## Tecnologías Utilizadas

- **Lenguaje**: Kotlin
- **UI Framework**: Jetpack Compose
- **Navegación**: Navigation Compose
- **Gestión de Estado**: ViewModel con StateFlow
- **Red**: Retrofit con OkHttp y Gson
- **Inyección de Dependencias**: Manual (planeado migrar a Hilt)
- **Build Tool**: Gradle (Kotlin DSL)

## Instalación y Ejecución

1. Clona el repositorio:

   ```bash
   git clone https://github.com/leonelparrales22/VinilosTSDC-backend.git
   ```

2. Abre el proyecto en Android Studio.
3. Sincroniza Gradle.
4. Ejecuta la aplicación en un emulador o dispositivo.

Asegúrate de tener configurado el `BASE_URL` en `build.gradle.kts` para apuntar al servidor API.

## Estructura del Proyecto

```text
app/src/main/java/com/example/vinilostsdc_frontend/
├── data/
│   ├── model/          # Modelos de datos (Album, etc.)
│   ├── repository/     # Repositorios (AlbumRepository)
│   └── service/        # Servicios API (ApiService)
├── di/                 # Módulos de inyección de dependencias (AppModule)
├── presentation/
│   ├── screen/         # Pantallas Compose (RoleSelectionScreen, etc.)
│   └── viewmodel/      # ViewModels (AlbumViewModel)
└── MainActivity.kt     # Actividad principal con navegación
```

## API Endpoints

- `GET /albums`: Obtiene la lista de álbumes.
- `GET /albums/{id}`: Obtiene detalles de un álbum específico.
- (Otros endpoints para artistas y coleccionistas están definidos en ApiService.kt)

## Mejoras Pendientes

- Migrar a Hilt para inyección de dependencias.
- Añadir pruebas unitarias para ViewModels y repositorios.
- Mejorar manejo de errores para casos edge.
- Verificar fuente única de verdad en la lógica de datos.

## Contribución

Para contribuir, crea un pull request con tus cambios. Asegúrate de seguir las convenciones de código y añadir tests si es necesario.

## Licencia

[Especifica la licencia si aplica]
