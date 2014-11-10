package redes;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;


public class RedNeuronal 
{
	double x1,x2,x3,x4,x5,x6,x7,x8;
	double z1,z2,z3,z4,z5,z6,z7,z8;
	double f_z1,f_z2,f_z3,f_z4,f_z5,f_z6,f_z7,f_z8;
	double y;
	
	public String nombre;
	public ArrayList<ArrayList<Double>> pesos;
	public ArrayList<Double> bias;
	
	String netFile;
	public int inputCount  = 0,
	           hiddenCount = 0;
	
	double y_original = 0.0;
	double error = 0.0;
	
	int numEjemplos;
	
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
			this.nombre = linea.split(":\\s+")[1];

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

	/**
	 * MAIN
	 * @param args Command line args
	 */
	public static void main(String[] args) {

		RedNeuronal redEstandar = new RedNeuronal("ABALONE_STD.NET");
		redEstandar.ejecutar("abalone.pat");

		RedNeuronal redNormalizada = new RedNeuronal("ABALONE_NORM.NET");
		redNormalizada.ejecutar("abalone-norm.pat");
	}
}
