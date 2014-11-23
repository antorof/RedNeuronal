package redes;

public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
//		RedNeuronal redEstandar0 = new RedNeuronal("ABALONE_STD.NET", true, false);
//		RedNeuronal.Salida out1_1 = redEstandar0.ejecutar("abalone.pat");
//		System.out.println(out1_1);

//		RedNeuronal redEstandar = new RedNeuronal("ABALONE_ESTANDAR_1.NET", true, false);
//		RedNeuronal.Salida out1 = redEstandar.ejecutar("abalone.pat");
//		System.out.println(out1);
//
//		RedNeuronal redNormalizada = new RedNeuronal("ABALONE_NORM.NET", true);
//		RedNeuronal.Salida out2 = redNormalizada.ejecutar("abalone-norm.pat");
//		System.out.println(out2);

		RedNeuronal redNormalizada2 = new RedNeuronal("ABALONE_NORMALIZADA_1.NET", false, true,1.0,29.0);
		RedNeuronal.Salida out3 = redNormalizada2.ejecutar("abalone-norm.pat");
		System.out.println(out3);
//		System.out.println(out3.desnormalizar(1.0, 29.0));
//
//		RedNeuronal redStdLB = new RedNeuronal("red_abalone_LB_inv.net", true, false);
//		RedNeuronal.Salida outStdLB = redStdLB.ejecutar("abalone.pat");
//		System.out.println(outStdLB);

		redNormalizada2 = new RedNeuronal("redes/ABALONE_NORM_4.NET", false, true,1.0,29.0);
		out3 = redNormalizada2.ejecutar("abalone-norm.pat");
		System.out.println(out3);
		redNormalizada2 = new RedNeuronal("redes/ABALONE_NORM_5.NET", false, true,1.0,29.0);
		out3 = redNormalizada2.ejecutar("abalone-norm.pat");
		System.out.println(out3);
		redNormalizada2 = new RedNeuronal("redes/ABALONE_NORM_6.NET", false, true,1.0,29.0);
		out3 = redNormalizada2.ejecutar("abalone-norm.pat");
		System.out.println(out3);
		redNormalizada2 = new RedNeuronal("redes/ABALONE_NORM_7.NET", false, true,1.0,29.0);
		out3 = redNormalizada2.ejecutar("abalone-norm.pat");
		System.out.println(out3);
		redNormalizada2 = new RedNeuronal("redes/ABALONE_NORM_8.NET", false, true,1.0,29.0);
		out3 = redNormalizada2.ejecutar("abalone-norm.pat");
		System.out.println(out3);
		

		redNormalizada2 = new RedNeuronal("redes/ABALONE_NORM_5.NET", false, false);
		out3 = redNormalizada2.ejecutar("abalone.pat");
		System.out.println(out3);
	}

}
