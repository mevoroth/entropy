package perso.algo;

import perso.modele.Joueur;

public interface Heuristique {
	public int eval(Evaluable e, Joueur j);
}
