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
	
	public ArrayList<ArrayList<Double>> pesos;
	public ArrayList<Double> bias;
	
	/**
	 * Constructor de una red neuronal de una única salida.
	 * @param netFile Archivo {@code .net} que describe la red
	 */
	public RedNeuronal(String netFile) {
		this.netFile = netFile;
		
		pesos = new ArrayList<ArrayList<Double>>();
		bias = new ArrayList<Double>();
		
		FileReader fr = null;
		BufferedReader br = null;
		String linea;
		ArrayList<String> strBffr = new ArrayList<String>();
		
		try {
			fr = new FileReader (netFile);
			br = new BufferedReader(fr);

			saltarLineas(br,3);
			
			linea = br.readLine();
			this.name = linea.split(":\\s+")[1];

			saltarLineas(br,23);
			
			boolean pesosLeidos = false;
			do {
				linea = br.readLine();
				
				String[] arr = linea.split("\\|"); 
				if(arr[5].trim().equals("i")) {
					inputCount++;
				}
				else if (arr[5].trim().equals("h")) {
					hiddenCount++;
					strBffr.add(linea);
				}
				else if (arr[5].trim().equals("o")) {
					pesosLeidos = true;
					strBffr.add(linea);
				}
				
			} while (!pesosLeidos);
			
			for (int i = 0; i < strBffr.size(); i++) {
				String str = strBffr.get(i);
				
				String[] arr = str.split("\\|"); 
				
				bias.add( Double.parseDouble(arr[4].trim().replace(',','.')) );
			}
			
			saltarLineas(br, 7);
			
			for (int i = 0; i < hiddenCount+1; i++) {
				linea = br.readLine();
				String[] arr = linea.split(",*\\s*[0-9]+:\\s*"); 
				
				pesos.add(new ArrayList<Double>());
				for (int j = arr.length-1; j > 0; j--) {
					String peso = arr[j].trim();
					pesos.get(i).add(Double.parseDouble(peso.trim().replace(',','.')));
				}
			}
			
			
		} catch (IOException e) {
			e.printStackTrace();
		} 
	}
	
	/**
	 * Ejecuta la red neuronal sobre los datos de un fichero.
	 * @param patternFile Archivo de datos sobre el que lanzar la red neuronal
	 */
	public void ejecutar(String patternFile) {
		String linea;
		FileReader fr = null;
		BufferedReader br = null;
		int numEjemplos, numInputs;
		double[] inputValues  = new double[inputCount];
		double[] hiddenValues = new double[hiddenCount];
		double   outputValue,
		         computedOutput,
		         error = 0.0;
		
		try {
			fr = new FileReader (patternFile);
			br = new BufferedReader(fr);

			saltarLineas(br,3);

			linea = br.readLine();
			numEjemplos = Integer.parseInt( linea.split("\\s+")[4] );
			
			linea = br.readLine();
			numInputs = Integer.parseInt( linea.split("\\s+")[5] );

			if (numInputs != this.inputCount) {
				System.err.println("Number of inputs in pattern file ("+numInputs+") does "
						+ "not match with number of inputs of the net ("+inputCount+")");
				System.exit(1);
			}
				
			saltarLineas(br,2);
			
			while( (linea=br.readLine()) != null )
			{
				String[] lineaSeparada = linea.split("\\t");
				computedOutput = 0.0;
				
				for (int i = 0; i < inputCount; i++) {
					inputValues[i] = Double.parseDouble(lineaSeparada[i]);

				}
				outputValue = Double.parseDouble(lineaSeparada[lineaSeparada.length-1]);

				for (int i = 0; i < hiddenCount; i++) {
					double tmpHiddenValue = 0.0;
					
					for (int j = 0; j < inputValues.length; j++) {
						tmpHiddenValue += inputValues[j] * pesos.get(i).get(j);
					}
					
					tmpHiddenValue =  tmpHiddenValue + bias.get(i);
					hiddenValues[i] = 1.0/(1+Math.exp(-tmpHiddenValue));
					
					computedOutput += hiddenValues[i] * pesos.get(pesos.size()-1).get(i);
				}
				computedOutput += bias.get(bias.size()-1);
				
				error += Math.pow(computedOutput-outputValue,2);
			}
			
			System.out.println("Error cuadrático medio: "+error/numEjemplos);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Salta un n&uacute;mero de l&iacute;neas de un {@link BufferedReader}
	 * @param br     Buffer del que se quiere saltar l&iacute;neas
	 * @param numero N&uacute;mero de lineas a saltar
	 */
	private void saltarLineas(BufferedReader br, int numero) {
		for (int i = 0; i < numero; i++) {
			try {
				br.readLine();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}
