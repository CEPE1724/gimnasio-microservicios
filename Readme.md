## UNIVERSIDAD TECNOLÓGICA ISRAEL

Facultad de Ciencias de la Ingeniería

## ACTIVIDAD AUTÓNOMA

## Seguridad en Microservicios: Autenticación y Autorización con JWT, OAuth2/OIDC y Keycloak

| Asignatura |   | Microservicios |
| --- | --- | --- |
| Docente |   | MSc. Viviana Flores |
| Tema |   | Sistema de Gestión de Gimnasio (Membresías y Clases) |
| Modalidad |   | Trabajo autónomo individual o en parejas |

## Datos de entrega
 EDISON BERNABE CEPEDA CAIZAGUANO

## Repositorio GitHub:
https://github.com/CEPE1724/gimnasio-microservicios.git

## Link del video de funcionamiento ( Drive )
https://drive.google.com/drive/folders/1sFz4ErpiiC0qWheU2e6nNQEyc3JyMA2G?usp=sharing

## 1. Introducción y objetivo

Esta actividad autónoma tiene como propósito que usted replique, de forma independiente, el flujo completo de autenticación y autorización con JWT, OAuth2/OIDC y Keycloak trabajado en la sesión de clase, aplicándolo a un dominio distinto al utilizado como ejemplo guiado.

Objetivo de aprendizaje: implementar un microservicio Spring Boot protegido por Keycloak, capaz de distinguir correctamente entre autenticación, autorización por rol y autorización a nivel de dato, siguiendo las mismas buenas prácticas revisadas en clase (arquitectura en capas, uso de DTOs, extracción segura de identidad desde el token, y separación de responsabilidades entre el microservicio y el Authorization Server).

Esta actividad debe desarrollarse de manera individual (o en parejas, según indique el docente), sin reutilizar directamente el código del ejercicio guiado en clase. Se espera que usted razone y adapte cada paso al nuevo escenario.

## 2. Escenario: Sistema de Gestión de Gimnasio

Un gimnasio requiere digitalizar el proceso de reserva de cupos en sus clases grupales (spinning, yoga, crossfit, funcional, entre otras). El sistema debe permitir que los socios reserven cupo, que los entrenadores gestionen la asistencia de sus propias clases, y que el personal administrativo tenga control total sobre todas las reservas.

## 2.1 Roles del sistema

| Rol en Keycloak |   | Descripción |   | Permisos principales |   |
| --- | --- | --- | --- | --- | --- |
| MEMBER |   | Socio del gimnasio que asiste a clases |   | Ver y reservar cupo únicamente en sus propias clases |   |
| TRAINER |   | Entrenador que dicta una o más clases |   | Ver los inscritos en las clases que dicta y marcar asistencia |   |
| ADMIN |   | Personal administrativo del gimnasio |   | Acceso total: ver todas las reservas del sistema y eliminar cualquier registro |   |


## UNIVERSIDAD TECNOLÓGICA ISRAEL

Facultad de Ciencias de la Ingeniería

## 2.2 Entidad principal: ClassBooking (Reserva de clase)

| Campo |   | Tipo sugerido |   | Descripción |   |
| --- | --- | --- | --- | --- | --- |
| id |   | Long |   | Identificador único del registro, autogenerado |   |
| memberUsername |   | String |   | Username del socio, obtenido del JWT — nunca del cuerpo de la petición |   |
| trainerUsername |   | String |   | Username del entrenador asignado a la clase |   |
| className |   | String |   | Nombre de la clase reservada (ej. Spinning, Yoga, CrossFit) |   |
| classDate |   | LocalDateTime |   | Fecha y hora programada de la clase |   |
| status |   | Enum |   | RESERVED, ATTENDED o CANCELLED |   |

## 3. Instrucciones paso a paso

A continuación se detallan los lineamientos que debe seguir. No se proporciona código: usted debe diseñar e implementar cada componente aplicando lo aprendido en el laboratorio guiado.

## Parte A — Configuración de Keycloak

- 1. Cree un nuevo Realm llamado gym-system.

- 2. Cree un Client llamado gym-app, configurado como confidential client (Client authentication: ON), con Standard Flow y Direct Access Grants habilitados.

- 3. Configure las Valid Redirect URIs necesarias para pruebas con Postman.

- 4. Cree los 3 roles de realm: MEMBER, TRAINER y ADMIN (en inglés y en mayúsculas, siguiendo la convención usada en clase).

- 5. Cree 3 usuarios de prueba, uno por cada rol, con contraseñas no temporales, y asígneles su rol correspondiente. Sugerencia de nomenclatura: member.test, trainer.test, admin.test.

## Parte B — Microservicio Spring Boot

Genere un nuevo proyecto Spring Boot 3.x (Maven, Java 17) con las dependencias: Spring Web, Spring Security, OAuth2 Resource Server, Spring Data JPA, PostgreSQL Driver, Lombok y Validation.

Implemente la arquitectura en capas completa: Entity, Repository, Service (interfaz + implementación) y Controller, utilizando DTOs de entrada y salida — la entidad JPA nunca debe exponerse directamente en las respuestas de la API.

Los endpoints que debe implementar son los siguientes:

| Método Ruta |   |   |   | Rol requerido |   | Descripción funcional |   |
| --- | --- | --- | --- | --- | --- | --- | --- |
| POST |   | /api/bookings |   | MEMBER |   | Crea una nueva reserva de clase a nombre del socio autenticado (el username se obtiene del JWT, no del cuerpo de la petición) |   |


## UNIVERSIDAD TECNOLÓGICA ISRAEL

Facultad de Ciencias de la Ingeniería

| GET | /api/bookings/my-bookings MEMBER |   | Devuelve únicamente las reservas del socio autenticado |
| --- | --- | --- | --- |
| GET | /api/bookings/my-classes | TRAINER | Devuelve las reservas de las clases asignadas al entrenador autenticado |
| PATCH | /api/bookings/{id}/attend | TRAINER | Marca una reserva como asistida (ATTENDED); solo válido si estaba en estado RESERVED |
| GET | /api/bookings | ADMIN | Devuelve todas las reservas del sistema, sin restricción de propietario |
| DELETE | /api/bookings/{id} | ADMIN | Elimina cualquier reserva del sistema |

Recuerde: el username del socio o del entrenador debe obtenerse siempre desde el claim preferred_username del JWT ya validado — nunca debe confiarse en un valor enviado por el cliente en el cuerpo de la petición.

## Parte C — Configuración de seguridad

- Configure el microservicio como OAuth2 Resource Server, apuntando al realm gym-system.

- Implemente un JwtAuthenticationConverter que extraiga los roles desde el claim realm_access.roles del token de Keycloak.

- Configure las reglas de autorización por ruta y rol, replicando la lógica de la tabla de endpoints de la sección anterior.

- Mantenga la convención de nomenclatura: identificadores de código en inglés, comentarios en español, cada línea de código comentada.

## Parte D — Gateway con Nginx (opcional / bono)

Como actividad complementaria, configure Nginx como reverse proxy delante de su microservicio, reenviando las peticiones que lleguen a /api/ y preservando el header Authorization sin modificarlo.

## Parte E — Pruebas con Postman

Debe documentar, con capturas de pantalla, los siguientes 6 escenarios de prueba (equivalentes a los realizados en el laboratorio guiado en clase):

|   |   | N° Escenario |   | Usuario / Rol |   | Resultado esperado |   |
| --- | --- | --- | --- | --- | --- | --- | --- |
| 1 |   | Crear una reserva de clase |   | MEMBER |   | 201 Created — la reserva se crea con el memberUsername tomado del token |   |
| 2 |   | Intentar ver todas las reservas del sistema |   | MEMBER |   | 403 Forbidden — no tiene el rol ADMIN |   |
| 3 |   | Consultar las clases propias asignadas |   | TRAINER |   | 200 OK — solo ve las reservas de sus clases |   |
| 4 |   | Consultar todas las reservas del sistema ADMIN |   |   |   | 200 OK — acceso completo sin restricción |   |
| 5 |   | Marcar una reserva como asistida |   | TRAINER |   | 200 OK — el estado cambia de RESERVED a ATTENDED |   |


## UNIVERSIDAD TECNOLÓGICA ISRAEL

Facultad de Ciencias de la Ingeniería

| 6 | Eliminar una reserva | ADMIN | 204 No Content — la reserva se elimina correctamente |
| --- | --- | --- | --- |

## 4. Entregables

- Proyecto Spring Boot completo (código fuente), junto con la configuración de Docker/docker-compose utilizada para Keycloak y la base de datos.

- Colección de Postman exportada en formato .json, con las 6 pruebas de la sección anterior documentadas (nombre descriptivo por request).

- Video de funcionamiento: grabación de pantalla (duración sugerida de 5 a 10 minutos) donde se muestre el flujo completo: creación del realm y usuarios en Keycloak, obtención del token desde Postman, y ejecución de los 6 escenarios de prueba con sus respectivos códigos de respuesta.
