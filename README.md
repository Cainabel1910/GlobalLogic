# Proyecto de Autenticación con JWT - Usuario BCI

## Descripción

Este es un proyecto de ejemplo para la autenticación de usuarios utilizando JWT (JSON Web Tokens) en una aplicación Spring Boot. El proyecto permite crear usuarios y loguearse con JWT para acceder a recursos protegidos. Utiliza una base de datos H2 en memoria y está configurado para ser ejecutado fácilmente en cualquier entorno de desarrollo local.

El sistema está diseñado para ser simple y eficiente, con pruebas unitarias que garantizan el correcto funcionamiento de las funcionalidades.

## Tecnologías

- **Java**: 11
- **Spring Boot**: 2.5.14
- **JWT**: Para autenticación y autorización
- **Maven**: Gestión de dependencias y construcción del proyecto
- **JUnit**: Para pruebas unitarias
- **Mockito**: Para crear mocks en las pruebas unitarias
- **H2**: Base de datos en memoria

## Documentacion
Se ha generado un swagger que documenta los endpoint de auth. 

http://localhost:9090/swagger-ui/index.htm

## Test
Se han incluido los siguientes Test:
- testRegister -> Registro exitoso
- testRegisterBadEmail -> El email no cumple con el formato requerido.
- testRegisterBadPassword -> La contraseña no cumple con el formato requerido.
- testRegisterEmailRegistrado -> El Email ya se encuentra registrado.
- testLogin_CredencialesIncorrectas -> Login con credenciales incorrectas.

## Requisitos

- **Java 11** o superior.
- **Maven**: Para construir y administrar el proyecto.
- **IDE**:  IntelliJ IDEA.

## Instalación

### Clonación del Repositorio

* Clona este repositorio en tu máquina local:

   ```bash
   git clone https://github.com/usuario/proyecto-jwt.git
   
### Construcción del Proyecto
* Compilación: Utiliza Maven para construir el proyecto y sus dependencias. 

### Ejecución del Proyecto
* Abrir el proyecto en IntelliJ IDEA.



