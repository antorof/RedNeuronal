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
	
	public void Constructor(String netFile) {
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
//				System.out.println(linea);
				String[] arr = linea.split(",*\\s*[0-9]+:\\s*"); 
				
				pesos.add(new ArrayList<Double>());
				for (int j = arr.length-1; j > 0; j--) {
					String peso = arr[j].trim();
//					peso = peso.charAt(peso.length()-1) == ',' ? peso.substring(0, peso.length()-1) : peso;
//					System.out.println(peso+" <--- "+arr[j].charAt(arr[j].length()-1));
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
	
	public RedNeuronal(String nombre, String archivo, ArrayList<ArrayList<Double>> pesos, ArrayList<Double> bias)
	{
		this.nombre = nombre;
		this.pesos = pesos;
		this.bias = bias;
		
		FileReader fr = null;
		BufferedReader br = null;

		String linea;
		
		try {
			fr = new FileReader (archivo);
			br = new BufferedReader(fr);

			saltarLineas(br,3);
			
			linea = br.readLine();
			numEjemplos = Integer.parseInt( linea.split("\\s+")[4] );

			saltarLineas(br,3);
			
		} catch (IOException e) {
			e.printStackTrace();
		} 
		
		try {
			while( (linea=br.readLine()) != null )
			{
				String[] lineaSeparada = linea.split("\\t");
				x1 = Double.parseDouble(lineaSeparada[0]);
				x2 = Double.parseDouble(lineaSeparada[1]);
				x3 = Double.parseDouble(lineaSeparada[2]);
				x4 = Double.parseDouble(lineaSeparada[3]);
				x5 = Double.parseDouble(lineaSeparada[4]);
				x6 = Double.parseDouble(lineaSeparada[5]);
				x7 = Double.parseDouble(lineaSeparada[6]);
				x8 = Double.parseDouble(lineaSeparada[7]);
				y_original = Double.parseDouble(lineaSeparada[8]);
				
				z1 = (x1 * pesos.get(0).get(0)) + (x2 * pesos.get(0).get(1)) + (x3 * pesos.get(0).get(2)) + (x4 * pesos.get(0).get(3)) + (x5 * pesos.get(0).get(4)) + (x6 * pesos.get(0).get(5)) + (x7 * pesos.get(0).get(6)) + (x8 * pesos.get(0).get(7)) + bias.get(0);
				z2 = (x1 * pesos.get(1).get(0)) + (x2 * pesos.get(1).get(1)) + (x3 * pesos.get(1).get(2)) + (x4 * pesos.get(1).get(3)) + (x5 * pesos.get(1).get(4)) + (x6 * pesos.get(1).get(5)) + (x7 * pesos.get(1).get(6)) + (x8 * pesos.get(1).get(7)) + bias.get(1);
				z3 = (x1 * pesos.get(2).get(0)) + (x2 * pesos.get(2).get(1)) + (x3 * pesos.get(2).get(2)) + (x4 * pesos.get(2).get(3)) + (x5 * pesos.get(2).get(4)) + (x6 * pesos.get(2).get(5)) + (x7 * pesos.get(2).get(6)) + (x8 * pesos.get(2).get(7)) + bias.get(2);
				z4 = (x1 * pesos.get(3).get(0)) + (x2 * pesos.get(3).get(1)) + (x3 * pesos.get(3).get(2)) + (x4 * pesos.get(3).get(3)) + (x5 * pesos.get(3).get(4)) + (x6 * pesos.get(3).get(5)) + (x7 * pesos.get(3).get(6)) + (x8 * pesos.get(3).get(7)) + bias.get(3);
				z5 = (x1 * pesos.get(4).get(0)) + (x2 * pesos.get(4).get(1)) + (x3 * pesos.get(4).get(2)) + (x4 * pesos.get(4).get(3)) + (x5 * pesos.get(4).get(4)) + (x6 * pesos.get(4).get(5)) + (x7 * pesos.get(4).get(6)) + (x8 * pesos.get(4).get(7)) + bias.get(4);
				z6 = (x1 * pesos.get(5).get(0)) + (x2 * pesos.get(5).get(1)) + (x3 * pesos.get(5).get(2)) + (x4 * pesos.get(5).get(3)) + (x5 * pesos.get(5).get(4)) + (x6 * pesos.get(5).get(5)) + (x7 * pesos.get(5).get(6)) + (x8 * pesos.get(5).get(7)) + bias.get(5);
				z7 = (x1 * pesos.get(6).get(0)) + (x2 * pesos.get(6).get(1)) + (x3 * pesos.get(6).get(2)) + (x4 * pesos.get(6).get(3)) + (x5 * pesos.get(6).get(4)) + (x6 * pesos.get(6).get(5)) + (x7 * pesos.get(6).get(6)) + (x8 * pesos.get(6).get(7)) + bias.get(6);
				z8 = (x1 * pesos.get(7).get(0)) + (x2 * pesos.get(7).get(1)) + (x3 * pesos.get(7).get(2)) + (x4 * pesos.get(7).get(3)) + (x5 * pesos.get(7).get(4)) + (x6 * pesos.get(7).get(5)) + (x7 * pesos.get(7).get(6)) + (x8 * pesos.get(7).get(7)) + bias.get(7);
				
				f_z1 = 1.0/(1+Math.exp(-z1));
				f_z2 = 1.0/(1+Math.exp(-z2));
				f_z3 = 1.0/(1+Math.exp(-z3));
				f_z4 = 1.0/(1+Math.exp(-z4));
				f_z5 = 1.0/(1+Math.exp(-z5));
				f_z6 = 1.0/(1+Math.exp(-z6));
				f_z7 = 1.0/(1+Math.exp(-z7));
				f_z8 = 1.0/(1+Math.exp(-z8));
				
				y = (f_z1 * pesos.get(8).get(0)) + (f_z2 * pesos.get(8).get(1)) + (f_z3 * pesos.get(8).get(2)) + (f_z4 * pesos.get(8).get(3)) + (f_z5 * pesos.get(8).get(4)) + (f_z6 * pesos.get(8).get(5)) + (f_z7 * pesos.get(8).get(6)) + (f_z8 * pesos.get(8).get(7)) + bias.get(8);
//				System.out.println(y);
				error += Math.pow(y-y_original,2);
				
				//System.out.println("Error parcial: "+ error);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		System.out.println(nombre+"> Error cuadratico medio: "+ error/numEjemplos);
		

		try{                    
			if( null != fr )  
				fr.close();               
		} catch (Exception e2){ 
			e2.printStackTrace();
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
		ArrayList<ArrayList<Double>> pesos = new ArrayList<ArrayList<Double>>();
		for (int i = 0; i < 9; i++) {
			pesos.add(new ArrayList<Double>());
		}
		ArrayList<Double> bias = new ArrayList<Double>();

		// Red normalizada
		pesos.get(0).add(-0.95271);
		pesos.get(0).add(-0.52956);
		pesos.get(0).add(-0.66791);
		pesos.get(0).add(0.77604);
		pesos.get(0).add(0.68592);
		pesos.get(0).add(-0.24722);
		pesos.get(0).add(-0.98658);
		pesos.get(0).add(0.26148);
		bias.add(-0.82863);

		pesos.get(1).add(-0.90706);
		pesos.get(1).add(0.80010);
		pesos.get(1).add(0.36500);
		pesos.get(1).add(0.64696);
		pesos.get(1).add(0.37117);
		pesos.get(1).add(0.88839);
		pesos.get(1).add(-0.23854);
		pesos.get(1).add(0.70254);
		bias.add(0.12752);

		pesos.get(2).add(-0.94440);
		pesos.get(2).add(-0.36316);
		pesos.get(2).add(-0.16251);
		pesos.get(2).add(-0.10637);
		pesos.get(2).add(0.80856);
		pesos.get(2).add(0.93893);
		pesos.get(2).add(0.03954);
		pesos.get(2).add(-0.28970);
		bias.add(0.25000);

		pesos.get(3).add(-0.37011);
		pesos.get(3).add(0.51194);
		pesos.get(3).add(-0.78827);
		pesos.get(3).add(-0.09390);
		pesos.get(3).add(-0.74159);
		pesos.get(3).add(-0.08617);
		pesos.get(3).add(-0.50951);
		pesos.get(3).add(0.03127);
		bias.add(-0.48446);

		pesos.get(4).add(0.24314);
		pesos.get(4).add(-0.75219);
		pesos.get(4).add(-0.36792);
		pesos.get(4).add(-0.24340);
		pesos.get(4).add(0.75248);
		pesos.get(4).add(0.00763);
		pesos.get(4).add(0.76067);
		pesos.get(4).add(0.40559);
		bias.add(-0.83949);
		
		pesos.get(5).add(-0.25000);
		pesos.get(5).add(-0.48445);
		pesos.get(5).add(-0.36578);
		pesos.get(5).add(-0.22229);
		pesos.get(5).add(-0.01945);
		pesos.get(5).add(-0.97507);
		pesos.get(5).add(-0.18594);
		pesos.get(5).add(-0.43407);
		bias.add(-0.74822);

		pesos.get(6).add(0.32973);
		pesos.get(6).add(-0.34833);
		pesos.get(6).add(0.83572);
		pesos.get(6).add(0.75775);
		pesos.get(6).add(0.27115);
		pesos.get(6).add(-0.43591);
		pesos.get(6).add(0.81247);
		pesos.get(6).add(0.01666);
		bias.add(0.74313);

		pesos.get(7).add(-1.01488);
		pesos.get(7).add(-0.80154);
		pesos.get(7).add(-0.87523);
		pesos.get(7).add(0.36764);
		pesos.get(7).add(0.69550);
		pesos.get(7).add(0.35758);
		pesos.get(7).add(0.29815);
		pesos.get(7).add(0.64496);
		bias.add(0.10723);

		pesos.get(8).add(0.16835);
		pesos.get(8).add(0.19487);
		pesos.get(8).add(0.39914);
		pesos.get(8).add(0.07573);
		pesos.get(8).add(0.31524);
		pesos.get(8).add(-0.48334);
		pesos.get(8).add(-0.08921);
		pesos.get(8).add(-0.54755);
		bias.add(1.03605);
		
		new RedNeuronal("Red normalizada","abalone-norm.pat",
				pesos, bias);
		
		// Red No normalizada
		
		pesos = new ArrayList<ArrayList<Double>>();
		for (int i = 0; i < 9; i++) {
			pesos.add(new ArrayList<Double>());
		}
		bias = new ArrayList<Double>();
		

		pesos.get(0).add(1.44192);
		pesos.get(0).add(-0.67264);
		pesos.get(0).add(0.28301);
		pesos.get(0).add(0.95225);
		pesos.get(0).add(-0.23870);
		pesos.get(0).add(-0.47484);
		pesos.get(0).add(0.03796);
		pesos.get(0).add(1.89164);
		bias.add(0.82818);

		pesos.get(1).add(-1.71697);
		pesos.get(1).add(0.43541);
		pesos.get(1).add(-0.71488);
		pesos.get(1).add(0.09971);
		pesos.get(1).add(1.69982);
		pesos.get(1).add(-3.23568);
		pesos.get(1).add(0.64018);
		pesos.get(1).add(0.91018);
		bias.add(-1.91846);

		pesos.get(2).add(1.70180);
		pesos.get(2).add(0.52775);
		pesos.get(2).add(0.68993);
		pesos.get(2).add(-0.16126);
		pesos.get(2).add(1.87386);
		pesos.get(2).add(0.64154);
		pesos.get(2).add(0.97577);
		pesos.get(2).add(0.27677);
		bias.add(1.03994);

		pesos.get(3).add(0.35071);
		pesos.get(3).add(0.38184);
		pesos.get(3).add(0.34350);
		pesos.get(3).add(-0.63501);
		pesos.get(3).add(0.50773);
		pesos.get(3).add(0.09662);
		pesos.get(3).add(-0.82621);
		pesos.get(3).add(2.25973);
		bias.add(-2.07313);

		pesos.get(4).add(1.50620);
		pesos.get(4).add(0.35027);
		pesos.get(4).add(-0.03809);
		pesos.get(4).add(-0.70343);
		pesos.get(4).add(1.93270);
		pesos.get(4).add(0.53395);
		pesos.get(4).add(-0.22273);
		pesos.get(4).add(0.15752);
		bias.add(0.76734);
		
		pesos.get(5).add(0.61588);
		pesos.get(5).add(-0.16970);
		pesos.get(5).add(0.24753);
		pesos.get(5).add( 0.41722);
		pesos.get(5).add(-1.06033);
		pesos.get(5).add(0.41414);
		pesos.get(5).add(-0.60858);
		pesos.get(5).add(0.01152);
		bias.add(-0.66420);

		pesos.get(6).add(-0.68749);
		pesos.get(6).add(-0.93696);
		pesos.get(6).add(-0.24731);
		pesos.get(6).add(-0.86707);
		pesos.get(6).add(0.94966);
		pesos.get(6).add(0.67711);
		pesos.get(6).add(-0.70988);
		pesos.get(6).add(-0.11485);
		bias.add(0.69834);

		pesos.get(7).add(-0.21166);
		pesos.get(7).add(0.37756);
		pesos.get(7).add(0.98636);
		pesos.get(7).add(-0.93795);
		pesos.get(7).add(0.53354);
		pesos.get(7).add(-0.61136);
		pesos.get(7).add(0.73489);
		pesos.get(7).add(0.47075);
		bias.add(0.33735);

		pesos.get(8).add(3.17004);
		pesos.get(8).add(3.87038);
		pesos.get(8).add(2.78502);
		pesos.get(8).add(3.06058);
		pesos.get(8).add(2.87675);
		pesos.get(8).add(-0.39011);
		pesos.get(8).add(1.70506);
		pesos.get(8).add(1.20518);
		bias.add(2.62773);

		RedNeuronal rn = new RedNeuronal("Red no normalizada","abalone.pat",
				pesos, bias);
		
		rn.Constructor("ABALONE_NORM.net");
		System.out.println("BIAS");
		for (Double db : rn.bias) {
			System.out.println(db);
		}
		System.out.println("PESOS");
		for (ArrayList<Double> al : pesos) {
			for (Double db : al) {
				System.out.print(db + " | ");
			}
			System.out.println("");
		}
		System.out.println("Inputs: "+rn.inputCount);
		System.out.println("Hidden: "+rn.hiddenCount);
		
		rn.ejecutar("abalone-norm.pat");
	}
}
