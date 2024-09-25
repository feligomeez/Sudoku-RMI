# Sudoku Distribuido con RPC

Este proyecto consiste en la implementación de una versión distribuida del juego Sudoku, utilizando llamadas a procedimientos remotos (RPC).

## Descripción del Juego

El Sudoku es un juego de lógica que consiste en un tablero de 9x9, dividido en 9 bloques de 3x3. Cada posición del tablero puede contener un número entre 1 y 9. El objetivo del juego es rellenar todo el tablero siguiendo estas reglas:

	•	Cada fila debe contener los números del 1 al 9 sin repetición.
	•	Cada columna debe contener los números del 1 al 9 sin repetición.
	•	Cada bloque 3x3 debe contener los números del 1 al 9 sin repetición.

El juego comienza con un tablero parcialmente lleno, y el jugador debe completar el resto de las celdas siguiendo las reglas mencionadas. El juego termina cuando el tablero está correctamente completado.

## Funcionalidades del Proyecto

Este proyecto utiliza una arquitectura cliente-servidor en la que el servidor centralizado gestiona múltiples juegos de Sudoku para diferentes clientes. Las principales características son:

1. Gestión de Juegos (servidor)

	•	Crear Juego: El servidor crea un nuevo Sudoku basado en un nivel de dificultad proporcionado por el usuario. Los Sudokus se almacenan tanto en el servidor como en una lista en el cliente.
	•	Borrar Juego: El servidor permite eliminar Sudokus creados previamente, ya sea el Sudoku activo o cualquier otro en la lista de juegos del cliente.
	•	Seleccionar Juego: Los clientes pueden seleccionar uno de los Sudokus almacenados en el servidor para activar y continuar jugando.

2. Jugar Sudoku (cliente)

	•	Poner un valor: El cliente puede seleccionar una fila, columna y número para colocarlo en el tablero del Sudoku activo.
	•	Borrar un valor: El cliente puede eliminar el valor en una posición específica del tablero.
	•	Ayuda: El cliente puede pedir una sugerencia sobre qué número puede colocarse en una celda vacía, basada en los valores posibles.

3. Salir

	•	El cliente se desconecta del servidor al terminar la sesión de juego.

## Arquitectura del Sistema

La aplicación se divide en dos componentes principales:

	•	Servidor de Sudoku: Almacena múltiples juegos de Sudoku y permite su gestión. Los clientes se conectan al servidor mediante RPC para interactuar con los juegos.
	•	Cliente de Sudoku: Permite a los usuarios jugar y gestionar sus juegos de Sudoku. Los clientes se conectan al servidor para obtener, modificar y almacenar Sudokus.

## Tecnologías Utilizadas

	•	Java: Lenguaje de programación base para la implementación del Sudoku.
	•	RPC (Remote Procedure Call): Para la comunicación entre el cliente y el servidor.
	•	Sockets: Se utilizan para gestionar la conexión cliente-servidor.
