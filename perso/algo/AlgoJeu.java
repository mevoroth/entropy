package perso.algo;

import perso.modele.Coup;
import perso.modele.Plateau;

/**
 * @author Daniel Quach
 */
public interface AlgoJeu {
    /** Renvoie le meilleur
     * @param p
     * @return
     */
	public Coup meilleurCoup(Plateau p);
}
