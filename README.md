RedNeuronal
===========

Programa en java que modela una red neuronal de un número indeterminado de entradas, 
una capa oculta con un número indeterminado de nodos, y un único nodo de salida.

Este programa lee los archivos directamente `.net` exportados por _JavaNNS_ para crear la red neuronal.

## Errores comunes
Para que el programa lea correctamente los `.net`, éste archivo debe contener los __pesos de 
manera inversa__ (hay veces que JavaNNS los exporta de manera inversa y otras en orden):
```
  ...
  
target | site | source:weight
-------|------|----------------------------------------------------------------------------------------------------------
     9 |      |  8: 5.28718, 7:-2.38443,  6:-5.48300,  5: 6.01719, 4:10.07148, 3:-4.20824, 2:-3.02101, 1:-0.45850
    10 |      |  8: 2.44861, 7:-17.23550, 6:-32.90749, 5:16.97792, 4: 1.53458, 3: 6.07847, 2:-8.81917, 1: 0.21718
    
  ...
```

Si no tuvieran puestos así hay que comentar la línea 100 de `RedNeuronal.java` y descomentar la línea 101.

## Modo de uso
```java
// Creamos la red
RedNeuronal redNeuronal = new RedNeuronal("fichero_red.net", false /*true o false dependiendo de si la red es normalizada o no*/);

// Capturamos la salida de la ejecución
RedNeuronal.Salida salidaRed = redNeuronal.ejecutar("fichero_muestras.pat");

// Vemos la salida
System.out.println(salidaRed);
```
