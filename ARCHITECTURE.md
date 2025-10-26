# Vinilos TSDC - Aplicación Android

Esta aplicación Android implementa una interfaz para la gestión de álbumes, artistas y coleccionistas de vinilos, utilizando los patrones arquitectónicos **MVVM**, **Repository** y **Service Adapter**.

## Arquitectura Implementada

### 📋 Patrones Arquitectónicos

#### 1. **MVVM (Model-View-ViewModel)**
- **View**: Pantallas de Jetpack Compose (`presentation/screen/`)
- **ViewModel**: Manejo del estado de la UI (`presentation/viewmodel/`)
- **Model**: Entidades de datos (`data/model/`)

#### 2. **Repository Pattern**
- Abstrae el acceso a datos
- Ubicación: `data/repository/`
- Proporciona una interfaz única para múltiples fuentes de datos

#### 3. **Service Adapter Pattern**
- Adapta servicios externos (API REST)
- Ubicación: `data/service/`
- Interfaz: `ApiService.kt`

## 🏗️ Estructura del Proyecto

```
app/src/main/java/com/example/vinilostsdc_frontend/
├── data/
│   ├── model/          # Entidades de datos
│   │   ├── Album.kt
│   │   ├── Artist.kt
│   │   └── Collector.kt
│   ├── repository/     # Patrón Repository
│   │   ├── AlbumRepository.kt
│   │   ├── ArtistRepository.kt
│   │   └── CollectorRepository.kt
│   └── service/        # Service Adapter
│       └── ApiService.kt
├── di/                 # Inyección de dependencias
│   └── AppModule.kt
├── presentation/
│   ├── screen/         # Pantallas (Views)
│   │   ├── AlbumListScreen.kt
│   │   ├── ArtistListScreen.kt
│   │   ├── CollectorListScreen.kt
│   │   └── CrearAlbumScreen.kt
│   └── viewmodel/      # ViewModels
│       ├── AlbumViewModel.kt
│       ├── ArtistViewModel.kt
│       └── CollectorViewModel.kt
├── ui/theme/           # Tema de la aplicación
└── MainActivity.kt     # Actividad principal
```

## 🔧 Tecnologías Utilizadas

- **Kotlin**: Lenguaje de programación
- **Jetpack Compose**: UI moderna y declarativa
- **ViewModel**: Gestión del estado de la UI
- **Retrofit**: Cliente HTTP para APIs REST
- **OkHttp**: Cliente HTTP con interceptores
- **Gson**: Serialización/deserialización JSON
- **Coroutines & Flow**: Programación asíncrona reactiva
- **Navigation Compose**: Navegación entre pantallas

## 📱 Funcionalidades

### Pantallas Principales:
1. **Pantalla Principal**: Menú con opciones de navegación
2. **Catálogo de Álbumes**: Lista de álbumes disponibles
3. **Listado de Artistas**: Lista de músicos
4. **Listado de Coleccionistas**: Lista de coleccionistas
5. **Crear Álbum**: Formulario para agregar nuevos álbumes

### Características:
- ✅ Estados de carga y error
- ✅ Manejo de llamadas a API
- ✅ Validación de formularios
- ✅ Navegación fluida
- ✅ Arquitectura escalable

## 🌐 API Integration

La aplicación se conecta a: `https://back-vynils-qa.herokuapp.com/`

### Endpoints utilizados:
- `GET /albums` - Obtener lista de álbumes
- `GET /albums/{id}` - Obtener álbum específico
- `POST /albums` - Crear nuevo álbum
- `GET /musicians` - Obtener lista de artistas
- `GET /collectors` - Obtener lista de coleccionistas

## 🔄 Flujo de Datos

```
View (Compose) → ViewModel → Repository → Service Adapter → API
                    ↓
                UI State ← Flow ← Resource ← Response
```

### Manejo de Estados:
- **Loading**: Mientras se realizan las peticiones
- **Success**: Datos cargados correctamente
- **Error**: Manejo de errores con mensajes descriptivos

## 💉 Gestión de Dependencias

El proyecto utiliza un patrón de inyección manual a través de objetos singleton en `di/AppModule.kt`:

- **NetworkModule**: Configuración de Retrofit, OkHttp y Gson
- **RepositoryModule**: Instancias de repositorios

## 🚀 Compilación y Ejecución

```bash
# Compilar la aplicación
./gradlew assembleDebug

# Ejecutar tests
./gradlew test

# Instalar en dispositivo
./gradlew installDebug
```

## 📋 Próximas Mejoras

- [ ] Implementar Hilt para inyección de dependencias
- [ ] Agregar pantallas de detalle
- [ ] Implementar caché local con Room
- [ ] Agregar tests unitarios e integración
- [ ] Mejorar manejo de errores
- [ ] Implementar offline-first approach

## 🎯 Beneficios de la Arquitectura

1. **Separación de responsabilidades**: Cada capa tiene un propósito específico
2. **Testabilidad**: Fácil testing unitario de cada componente
3. **Mantenibilidad**: Código organizado y fácil de modificar
4. **Escalabilidad**: Arquitectura preparada para crecimiento
5. **Reutilización**: Componentes reutilizables entre pantallas