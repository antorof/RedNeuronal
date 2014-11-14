RedNeuronal
===========

Programa en java que modela una red neuronal de un número indeterminado de entradas, 
una capa oculta con un número indeterminado de nodos, y un único nodo de salida.

Este programa lee los archivos directamente `.net` exportados por _JavaNNS_ para crear la red neuronal.

**Nota**: Para que el programa lea correctamente los `.net`, éste archivo debe contener los pesos de 
manera inversa (`8: 1,89164,  7: 0,03796,  6:-0,47484,  5:-0,23870, etc.`)

## Modo de uso
```java
// Creamos la red
RedNeuronal redNeuronal = new RedNeuronal("fichero_red.net");

// Capturamos la salida de la ejecución
RedNeuronal.Salida salidaRed = redNeuronal.ejecutar("fichero_muestras.pat");

// Vemos la salida
System.out.println(salidaRed);
```
