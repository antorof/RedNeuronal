package redes;

public class Main {

	public static void main(String[] args) {

		RedNeuronal redEstandar = new RedNeuronal("ABALONE_STD.NET");
		redEstandar.ejecutar("abalone.pat");

		RedNeuronal redNormalizada = new RedNeuronal("ABALONE_NORM.NET");
		redNormalizada.ejecutar("abalone-norm.pat");
		
		RedNeuronal redNormalizada2 = new RedNeuronal("ABALONE_NORMALIZADA.NET");
		redNormalizada2.ejecutar("abalone-norm.pat");

	}

}
