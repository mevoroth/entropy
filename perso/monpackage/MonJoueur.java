package perso.monpackage;

import perso.modele.Joueur;
import perso.monpackage.MonJeu.Joueurs;

public class MonJoueur implements Joueur {
	private Joueurs _j;

	@Override
	public Object getId() {
		return _j;
	}

	@Override
	public void setId(Object id) {
		_j = (Joueurs) id;
	}
}
