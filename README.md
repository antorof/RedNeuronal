RedNeuronal
===========

Programa en java que modela una red neuronal de un número indeterminado de entradas, 
una capa oculta con un número indeterminado de nodos, y un único nodo de salida.

Este programa lee los archivos directamente `.net` exportados por _JavaNNS_ para crear la red neuronal.

## Modo de uso
```java
RedNeuronal redNeuronal = new RedNeuronal("fichero_red.net");
redNeuronal.ejecutar("fichero_muestras.pat");
```
