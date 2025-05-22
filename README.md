# Proyecto Compiladores

Implementación de un compilador con análisis léxico, sintáctico y semántico utilizando JFlex y CUP, con interfaz web basada en Spring Boot.

## Documentación

La documentación técnica completa del proyecto está disponible en:
- [Documentación Técnica (Google Docs)](https://docs.google.com/document/d/1wCsX7QTosweIXoAQjfZ-XbHks8ktVM1PW624gMSpsoo/edit?usp=sharing) 

## Aplicación desplegada

La aplicación está disponible en:
- [Analizador Web (Render)](https://proyecto-compiladores-gr9y.onrender.com) 

## Estructura del Proyecto

El proyecto está organizado en los siguientes componentes:

- **Análisis Léxico**: Implementado con JFlex
- **Análisis Sintáctico**: Implementado con CUP
- **Análisis Semántico**: Verificación de tipos y manejo de ámbitos
- **Interfaz Web**: Aplicación Spring Boot con Thymeleaf

## Requisitos previos

- Java 17 o superior
- Apache Maven instalado en el sistema

## Cómo ejecutar localmente

```bash
# Clonar el repositorio
git clone [URL_DEL_REPOSITORIO]

# Navegar al directorio del proyecto
cd Proyecto-Compiladores

# Compilar el proyecto
mvn clean install

# Ejecutar la aplicación
mvn spring-boot:run
```

La aplicación estará disponible en `http://localhost:8080`

## Ejemplos de código

En la carpeta `test-files` se encuentran ejemplos de programas que pueden ser procesados por el compilador.

