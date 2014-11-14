RedNeuronal
===========

Programa en java que modela una red neuronal de un número indeterminado de entradas, 
una capa oculta con un número indeterminado de nodos, y un único nodo de salida.

Este programa lee los archivos directamente `.net` exportados por _JavaNNS_ para crear la red neuronal.

## Modo de uso
```java
// Creamos la red
RedNeuronal redNeuronal = new RedNeuronal("fichero_red.net");

// Capturamos la salida de la ejecución
RedNeuronal.Salida salidaRed = redNeuronal.ejecutar("fichero_muestras.pat");

// Vemos la salida
System.out.println(salidaRed);
```
