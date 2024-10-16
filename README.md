# Prueba-tecnica-Java-Mid-Chakray
Prueba tecnica para la consultora chakray para evaluar nivel de Java 8-17 con Sprign Boot

# User Management API

Esta API permite la gestión de usuarios y direcciones, proporcionando operaciones CRUD sobre los usuarios y sus direcciones. Implementada con **Spring Boot**.

## Endpoints

### 1. Obtener usuarios ordenados (`GET /users`)

#### URL:
`GET /users`

#### Parámetros de consulta:
- `sortedBy` (opcional): Parámetro que define por qué atributo ordenar la lista de usuarios.
    - Valores admitidos:
        - `id`: Ordena por ID de usuario.
        - `email`: Ordena por correo electrónico.
        - `name`: Ordena por nombre de usuario.
        - `created_at`: Ordena por fecha de creación.

#### Respuesta:
- **200 OK**: Devuelve los usuarios ordenados.
- **400 BAD REQUEST**: Si el valor de `sortedBy` no es válido.

#### Ejemplo:

```GET /users?sortedBy=email```

### 2. Obtener direcciones de un usuario (GET /users/{user_id}/addresses)
URL:

```GET /users/{user_id}/addresses```

Parámetros:

```user_id```: El ID del usuario cuyas direcciones se desean obtener.

Respuesta:

200 OK: Devuelve las direcciones del usuario.

204 NO CONTENT: Si el usuario no es encontrado.

500 INTERNAL SERVER ERROR: Si ocurre un error en el servidor.


Ejemplo:

```GET /users/1/addresses```


### 3. Actualizar una dirección de un usuario (PUT /users/{user_id}/addresses/{address_id})
URL:

```PUT /users/{user_id}/addresses/{address_id}```

Parámetros:

```user_id```: El ID del usuario.

```address_id```: El ID de la dirección que se quiere actualizar.

Cuerpo de la solicitud:

JSON con los campos de la dirección que se desean actualizar:

```name```: Nombre de la dirección (ej: trabajo, casa).

```street```: Calle de la dirección.

```country_code```: Código de país de la dirección.

Respuesta:

200 OK: Si la dirección se actualiza exitosamente.

409 CONFLICT: Si el usuario o la dirección no son encontrados.

500 INTERNAL SERVER ERROR: Si ocurre un error en el servidor.

Ejemplo:

```PUT /users/1/addresses/2```

`{
    "name": "newWorkAddress",
    "street": "newStreet 123",
    "country_code": "US"
}`


### 4. Crear un nuevo usuario (POST /users)
URL:
```POST /users```

Cuerpo de la solicitud:

JSON con la información del usuario:

```name```: Nombre del usuario.

```email```: Correo electrónico.

```password```: Contraseña del usuario.

```addresses```: Lista de direcciones (nombre, calle y código de país).

Respuesta:

200 OK: Si el usuario se crea exitosamente.

400 BAD REQUEST: Si los datos son inválidos.

500 INTERNAL SERVER ERROR: Si ocurre un error en el servidor.

Ejemplo:


`POST /users
{
    "name": "newUser",
    "email": "newuser@mail.com",
    "password": "password123",
    "addresses": [
        {
            "name": "home",
            "street": "123 Main St",
            "country_code": "US"
        }
    ]
}`


### 5. Actualizar un usuario (PATCH /users/{id})

`URL:
PATCH /users/{id}`

Parámetros de ruta:

`id`: El ID del usuario a actualizar.

Cuerpo de la solicitud:

JSON con los campos que se desean actualizar:

`name`: Nombre del usuario.

`email`: Correo electrónico.

`password`: Contraseña del usuario.

Respuesta:

200 OK: Si el usuario se actualiza exitosamente.

409 CONFLICT: Si el usuario no es encontrado.

500 INTERNAL SERVER ERROR: Si ocurre un error en el servidor.

Ejemplo:

`PATCH /users/1
{
    "name": "updatedUserName",
    "email": "updatedemail@mail.com",
    "password": "newpassword123"
}`


### 6. Eliminar un usuario (DELETE /users/{id})

`URL:
DELETE /users/{id}`

Parámetros de ruta:

`id`: El ID del usuario a eliminar.

Respuesta:

200 OK: Si el usuario se elimina exitosamente.

409 CONFLICT: Si el usuario no es encontrado.

500 INTERNAL SERVER ERROR: Si ocurre un error en el servidor.

Ejemplo:


`DELETE /users/1`

Ejecución del Proyecto

Clona el repositorio:


`git clone https://github.com/usuario/user-management-api.git`

Importa el proyecto en tu IDE preferido (Eclipse, IntelliJ, etc.).

Ejecuta el proyecto desde la clase principal DemoApplication.

Usa una herramienta como Postman o cURL para interactuar con los endpoints.

O utiliza Swagger UI que se genera automaticamente en el proyecto: `http://localhost:8080/doc/swagger-ui/index.html#/user-controller/sortedBy`

Notas
Todas las respuestas tienen un formato JSON que incluye un campo code, un message descriptivo y un campo users_info con los datos relevantes.
Asegúrate de incluir los encabezados de Content-Type: application/json en las solicitudes que requieran un cuerpo JSON.
El servicio de usuario incluye codificación de contraseñas con el método userService.encodePassword.



