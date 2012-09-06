/**
 * 
 */
package perso.entropy;

import perso.algo.Alphabeta;
import perso.algo.Heuristique;
import perso.entropy.algo.Heuristiques;
import perso.modele.Coup;
import perso.monpackage.MonCoup;
import perso.monpackage.MonJeu;
import entropy.IJoueur;

/**
 * @author Daniel Quach
 */
public class MonJoueur implements IJoueur {
	private static final int BLANC = 1;
	private static final int NOIR = 2;
	private int _color;
	private MonJeu _jeu;
	
	private int _nbcoups = 0;
	
	private perso.monpackage.MonJoueur _e = new perso.monpackage.MonJoueur();
	private perso.monpackage.MonJoueur _a = new perso.monpackage.MonJoueur();
	/**
	 * @see entropy.IJoueur#initJoueur(int)
	 */
	@Override
	public void initJoueur(int mycolour) {
		_color = mycolour;
		_jeu = new MonJeu();
		_e.setId(
			(_color == BLANC ? MonJeu.Joueurs.NOIR : MonJeu.Joueurs.BLANC)
		);
		_a.setId(
			(_color == BLANC ? MonJeu.Joueurs.BLANC : MonJeu.Joueurs.NOIR)
		);
	}

	/**
	 * @see entropy.IJoueur#getNumJoueur()
	 */
	@Override
	public int getNumJoueur() {
		return _color;
	}

	/**
	 * @see entropy.IJoueur#choixMouvement()
	 */
	@Override
	public String choixMouvement() {
		if (_jeu.finDePartie())
		{
			return "x x x x";
		}
		else
		{
			++_nbcoups;
			Alphabeta ab = new Alphabeta(_getHeuristique(), _a, _e, _getDeepness());
			Coup c = ab.meilleurCoup(_jeu);
			if (c != null)
			{
				_jeu.joue(_a, c);
				return c.toString();
			}
			else
			{
				return "0 0 0 0";
			}
		}
	}

	private int _getDeepness() {
		if (_nbcoups == 1)
		{
			return 1;
		}
		else if (_nbcoups == 2)
		{
			return 1;
		}
		return 7;
	}

	private Heuristique _getHeuristique() {
		if (_nbcoups == 1)
		{
			return Heuristiques.opening;
		}
		else if (_nbcoups == 2)
		{
			return Heuristiques.counterOpening;
		}
		return Heuristiques.repartition;
	}

	/**
	 * @see entropy.IJoueur#declareLeVainqueur(int)
	 */
	@Override
	public void declareLeVainqueur(int color) {
		System.out.println("Je suis le " + (color == _color ? "gagnant"
			: "perdant") + " !");
	}

	/**
	 * @see entropy.IJoueur#mouvementEnnemi(int, int, int, int)
	 */
	@Override
	public void mouvementEnnemi(int startCol, int startRow, int finishCol,
		int finishRow) {
		++_nbcoups;
		_jeu.joue(
			_e,
			new MonCoup(startCol-1, startRow-1, finishCol-1, finishRow-1)
		);
	}
	
	/**
	 * @see entropy.IJoueur#binoName()
	 */
	@Override
	public String binoName() {
		return "Daniel Quach";
	}

}
