package perso.entropy.algo;

import perso.algo.Heuristique;
import perso.algo.Evaluable;
import perso.modele.Joueur;

public class Heuristiques {
	/**
	 * Heuristique générale
	 */
	public static Heuristique repartition = new Heuristique() {
		@Override
		public int eval(Evaluable e, Joueur j) {
			return (int)((
				((float)e.eval(j, "positionCle")/(float)e.meilleurEval("positionCle"))//*0.95
				//+((float)e.eval(j, "isolement")/(float)e.meilleurEval("isolement"))*0.05
			)*100);
		}
	};
	/**
	 * Heuristique d'opening
	 */
	public static Heuristique opening = new Heuristique() {
		@Override
		public int eval(Evaluable e, Joueur j) {
			return e.eval(j, "opening");
		}
	};
	/**
	 * Heuristique de contre-opening
	 */
	public static Heuristique counterOpening = new Heuristique() {
		public int eval(Evaluable e, Joueur j) {
			return e.eval(j, "counterOpening");
		}
	};
}
