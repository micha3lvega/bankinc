# Sistema de Emisión y Gestión de Tarjetas Bank Inc 
![Quality Gate Status](https://next.sonarqube.com/sonarqube/api/project_badges/measure?project=sonarqube&metric=alert_status&token=d95182127dd5583f57578d769b511660601a8547)

El proyecto "Sistema de Emisión y Gestión de Tarjetas Bank Inc" tiene como objetivo desarrollar una aplicación que permita asignar a los clientes de Bank Inc tarjetas de débito o crédito para realizar transacciones.

## Descripción del Proyecto

La aplicación "Bank Inc" proporciona funcionalidades para la emisión, gestión y uso de tarjetas bancarias. Los clientes pueden solicitar una nueva tarjeta, activarla, recargar saldo, realizar transacciones de compra, consultar el saldo y la información de las transacciones, así como anular transacciones.

# Funcionalidades
A continuación se describen las principales funcionalidades del proyecto:
## Generar número de tarjeta
Cada vez que se requiera una nueva tarjeta, el sistema realiza un proceso de emisión de tarjeta que incluye los siguientes pasos:

1. Se genera un cliente con nombres y apellidos aleatorios utilizando la librería Faker para generar datos al azar.

2. A partir del identificador de producto, se genera un número de tarjeta de 16 dígitos.

3. La tarjeta se genera en un estado inactivo y requerirá ser activada posteriormente.

4. El saldo inicial de la tarjeta es cero pesos.

5. Se genera una fecha de vencimiento que es 3 años posterior a la fecha actual.

## Activar tarjeta
El proceso de activación cambia el estado de la tarjeta de "inactiva" a "activa", permitiendo su uso para realizar transacciones.

## Recargar saldo
El proceso de recarga de saldo permite añadir un valor específico al saldo de la tarjeta. Este proceso solo es posible si la tarjeta se encuentra activa.

## Bloquear tarjeta
El proceso de bloqueo de tarjeta cambia el estado de la tarjeta de "activa" a "bloqueada". Una tarjeta bloqueada no puede realizar nuevas transacciones.

## Consulta de saldo
Este proceso permite consultar el saldo actual de la tarjeta y muestra su estado (activa, bloqueada, etc.).

## Transaccion de compra
Al realizar una transacción de compra, se llevan a cabo las siguientes acciones:

- Se valida que el saldo de la tarjeta sea suficiente para cubrir el valor de la transacción.
- El sistema asigna un identificador único a la transacción, que puede utilizarse posteriormente para consultar o anular la transacción.
- Se descuenta el valor de la transacción del saldo de la tarjeta.

## Consultar transacción
Este proceso permite consultar la información detallada de una transacción específica, utilizando su identificador único.

## Anulación de transacción
La anulación de una transacción de compra implica:

- Se valida que la transacción no haya pasado más de 24 horas desde su creación.
- Se cambia el estado de la transacción a "anulada".
El valor de la transacción se devuelve al saldo de la tarjeta.

## Tecnologías Utilizadas

- Java
- Faker
- Spring Framework
- Spring JPA

## Requisitos del Sistema

- Java 17 o superior
- Base de datos MySQL

# Diagramas
## Modelo entidad relacion
![Modelo entidad relacion](https://github.com/micha3lvega/bankinc/blob/despliegue/images/MER.png)

## Instalación y Configuración

1. Clona el repositorio del proyecto.
2. Configura la conexión a la base de datos en el archivo de configuración `application.properties`.
## Imagen docker: construir y correr

Build:
```
    mvn clean install -U
```
Ejecucion
```
    docker-compose up -d
```
## Abrir aplicacion

En un navegador de internet, ir a http://localhost:8080/swagger-ui/index.html

![swagger](https://github.com/micha3lvega/bankinc/blob/despliegue/images/swagger-ui.png)

# Postman
Para ver la documentacion postman ingrese aqui
https://documenter.getpostman.com/view/4294452/2s93zB4M1m

# Pruebas unitarias
Sonar

![Pruebas unitarias](https://github.com/micha3lvega/bankinc/blob/despliegue/images/sonar.png)