package redes;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Clase que modela una red neuronal de un número indeterminado de 
 * entradas, una capa oculta con un número indeterminado de nodos
 * y una única salida
 * 
 * @author Antonio Toro
 */
public class RedNeuronal 
{	
	public String name,
	              netFile;
	public int inputCount  = 0,
	           hiddenCount = 0;
	public final boolean normalized;
	
	public ArrayList<ArrayList<Double>> pesos;
	public ArrayList<Double> bias;
	
	/**
	 * Constructor de una red neuronal de una única salida.
	 * @param netFile Archivo {@code .net} que describe la red
	 */
	public RedNeuronal(String netFile, boolean normalized) {
		this.netFile = netFile;
		this.normalized = normalized;
		
		pesos = new ArrayList<ArrayList<Double>>();
		bias = new ArrayList<Double>();
		
		FileReader fr = null;
		BufferedReader br = null;
		String linea;
		ArrayList<String> lineasBias = new ArrayList<String>();
		
		try {
			fr = new FileReader (netFile);
			br = new BufferedReader(fr);
			
			// Saltamos las 3 primeras lineas
			saltarLineas(br,3);
			
			// Cogemos el nombre de la red
			linea = br.readLine();
			this.name = linea.split(":\\s+")[1];

			// Saltamos otras 3 lineas
			saltarLineas(br,23);
			
			// Almacenamos las lineas que contienen los BIAS y contamos el numero de nodos
			// de entrada y de capa oculta
			boolean biasLeidos = false;
			do {
				linea = br.readLine();
				
				String[] arr = linea.split("\\|"); 
				if(arr[5].trim().equals("i")) {
					inputCount++;
				}
				else if (arr[5].trim().equals("h")) {
					hiddenCount++;
					lineasBias.add(linea);
				}
				else if (arr[5].trim().equals("o")) {
					biasLeidos = true;
					lineasBias.add(linea);
				}
				
			} while (!biasLeidos);
			
			// Ahora extraemos los BIAS de las lineas que hemos guardado
			for (int i = 0; i < lineasBias.size(); i++) {
				String str = lineasBias.get(i);
				
				String[] arr = str.split("\\|"); 
				
				bias.add( Double.parseDouble(arr[4].trim().replace(',','.')) );
			}
			
			// Saltamos 7 lineas
			saltarLineas(br, 7);
			
			// Ahora leemos los pesos de la capa oculta y del nodo de salida
			for (int i = 0; i < hiddenCount+1; i++) { // hiddenCount+1 por el nodo de salida
				linea = br.readLine();
				String[] arr = linea.split(",*\\s*[0-9]+:\\s*"); 
				
				pesos.add(new ArrayList<Double>());
				for (int j = arr.length-1; j > 0; j--) {
					String peso = arr[j].trim();
					pesos.get(i).add(Double.parseDouble(peso.trim().replace(',','.')));
				}
			}
			
			
		} catch (IOException e) {
			System.err.println("No se ha podido crear la red");
			e.printStackTrace();
		} 
	}
	
	/**
	 * Ejecuta la red neuronal sobre los datos de un fichero.
	 * @param patternFile Archivo de datos sobre el que lanzar la red neuronal
	 */
	public Salida ejecutar(String patternFile) {
		String linea;
		FileReader fr = null;
		BufferedReader br = null;
		int numEjemplos, numInputs;
		ArrayList<Double> salidasComp = new ArrayList<Double>();
		ArrayList<Double> salidas = new ArrayList<Double>();
		double[] inputValues  = new double[inputCount];
		double[] hiddenValues = new double[hiddenCount];
		double   outputValue,
		         computedOutput,
		         error_2 = 0.0;
		Salida salida = new Salida(this.name);
		
		try {
			fr = new FileReader (patternFile);
			br = new BufferedReader(fr);

			// Saltamos 3 lineas
			saltarLineas(br,3);

			// Cogemos el numero de muestras
			linea = br.readLine();
			numEjemplos = Integer.parseInt( linea.split("\\s+")[4] );
			
			// Cogemos el numero de entradas
			linea = br.readLine();
			numInputs = Integer.parseInt( linea.split("\\s+")[5] );

			// Si no coinciden el numero de entradas de la red y del archivo de muestras se sale
			if (numInputs != this.inputCount) {
				System.err.println("Number of inputs in pattern file ("+numInputs+") does "
						+ "not match with number of inputs of the net ("+inputCount+")");
				System.exit(1);
			}
			
			// Salta 2 lineas
			saltarLineas(br,2);
			
			// Leemos las muestras
			while( (linea=br.readLine()) != null )
			{
				String[] lineaSeparada = linea.split("\\t");
				computedOutput = 0.0;
				
				// Leemos las entradas
				for (int i = 0; i < inputCount; i++) {
					inputValues[i] = Double.parseDouble(lineaSeparada[i]);
				}
				// Leemos la salida
				outputValue = Double.parseDouble(lineaSeparada[lineaSeparada.length-1]);
//				salidas.add(outputValue);

				// Calculamos el valor computado de salida
				for (int i = 0; i < hiddenCount; i++) {
					double tmpHiddenValue = 0.0;
					for (int j = 0; j < inputValues.length; j++) {
//						if(i==1) System.out.println(inputValues[j]);
//						if(i==1) System.out.print(pesos.get(i).get(j)+" ");
						tmpHiddenValue += inputValues[j] * pesos.get(i).get(j);
//						if(i==0 && j==0) {
//							System.out.print("ent: ");
//							for (double d : inputValues) {
//								System.out.print(d+" ");
//							} 
//							System.out.print("\npesos: ");
//							for (double d : pesos.get(i)) {
//								System.out.print(d+" ");
//							} 
							
//							System.out.println("sum: "+tmpHiddenValue);
//							System.out.print("ent: "+inputValues[j]);
//							System.out.println("pesos: "+pesos.get(i).get(j));
//						}
					}
//					if(i==0) 
//						System.out.println("\nsum: "+tmpHiddenValue);
//					if(i==1) System.out.println("");
//					if(i==1) 
//						System.out.println(outputValue);
					
					hiddenValues[i] = tmpHiddenValue + bias.get(i);
					
					hiddenValues[i] = 1.0/(1.0+Math.exp(-hiddenValues[i]));
					
//					System.out.println("fin: "+hiddenValues[i]);
					
					computedOutput += hiddenValues[i] * pesos.get(pesos.size()-1).get(i);

//					if(i==0) 
//						for (double d : hiddenValues) {
//							System.out.println(d+" ");
//						} 
				}
//				System.out.println("fin: "+computedOutput);
				// Sumamos el BIAS de la salida si la red es normalizada
				if (normalized) {
					computedOutput += bias.get(bias.size()-1);
					computedOutput = 1.0/(1.0+Math.exp(-computedOutput));
				}

//				System.out.println("output: "+outputValue);
				// Sumamos el error
//				salidasComp.add(computedOutput);
				error_2 += Math.pow(computedOutput-outputValue,2);
			}

			salida.error = Math.sqrt(error_2);
			salida.error_2 = error_2;
			salida.samples = numEjemplos;
			
//			salida.error_2 = 0.0;
//			for (int k = 0; k < numEjemplos; k++) {
//				System.out.println(salidasComp.get(k)+"\t-\t"+salidas.get(k));
//				salida.error_2 += Math.pow(salidasComp.get(k)-salidas.get(k),2);
//			}
			
			System.out.println("Error cuadrático medio: "+error_2/numEjemplos);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return salida;
	}
	
	/**
	 * Salta un n&uacute;mero de l&iacute;neas de un {@link BufferedReader}
	 * @param br     Buffer del que se quiere saltar l&iacute;neas
	 * @param numero N&uacute;mero de lineas a saltar
	 */
	private void saltarLineas(BufferedReader br, int numero) {
		try {
			for (int i = 0; i < numero; i++) {
				br.readLine();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Imprime por pantalla los pesos de la red
	 */
	public void printPesos() {
		for (ArrayList<Double> arrayList : pesos) {
			for (Double db : arrayList) {
				System.out.print(db+" ");
			}
			System.out.println("");
		}
	}
	
	/**
	 * Imprime por pantalla los bias de la red
	 */
	public void printBias() {
		for (Double db : bias) {
			System.out.print(db+" ");
		}
		System.out.println("");
	}
	
	/**
	 * Clase que modela la salida de la ejecución de la red neuronal sobre
	 * un conjunto de datos.
	 * 
	 * @author Antonio Toro
	 */
	public class Salida {
		public double error   = 0.0,
		              error_2 = 0.0;
		public int    samples = 0;
		public String name;
		
		public Salida(String name) {
			this.name = name;
		}
		
		@Override
		public String toString() {
			String str = "";

			str += this.name + "\n";
			str += " Error total: " + error + "\n";
			str += " Error total medio: " + error/samples + "\n";
			str += " Error cuadrático: " + error_2 + "\n";
			str += " Error cuadrático medio: " + error_2/samples + "\n";
			
			return str;
		}
		
		public Salida desnormalizar(double min, double max) {
			Salida out = new Salida(this.name+"_desnormalizada");

			out.error   = this.error * (max-min) + min;
			out.error_2 = this.error_2 * (max-min) + min;
			out.samples = this.samples;
			
			return out;
		}
	}

}
