package perso.algo;

import perso.modele.Joueur;

public interface Evaluable {
	public int eval(Joueur j, String string);
	public int meilleurEval(String string);
}
