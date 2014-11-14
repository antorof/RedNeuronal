package redes;

public class Main {

	public static void main(String[] args) {

//		RedNeuronal redEstandar = new RedNeuronal("ABALONE_STD.NET", false);
//		RedNeuronal.Salida out1 = redEstandar.ejecutar("abalone.pat");
//		System.out.println(out1);
//
//		RedNeuronal redNormalizada = new RedNeuronal("ABALONE_NORM.NET", true);
//		RedNeuronal.Salida out2 = redNormalizada.ejecutar("abalone-norm.pat");
//		System.out.println(out2);
//
//		RedNeuronal redNormalizada2 = new RedNeuronal("ABALONE_NORMALIZADA.NET",true);
//		RedNeuronal.Salida out3 = redNormalizada2.ejecutar("abalone-norm.pat");
//		System.out.println(out3);
//		System.out.println(out3.desnormalizar(1.0, 29.0));
		System.out.println(">>>>");
		RedNeuronal redStdLB = new RedNeuronal("red_abalone.net", false);
//		redStdLB.printPesos();
//		redStdLB.printBias();
		RedNeuronal.Salida outStdLB = redStdLB.ejecutar("abalone.pat");
		System.out.println(outStdLB);
	}

}
