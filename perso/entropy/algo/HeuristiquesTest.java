package perso.entropy.algo;

import static org.junit.Assert.*;

import org.junit.Test;

import perso.modele.Plateau;
import perso.monpackage.MonJeu;
import perso.monpackage.MonJoueur;
import perso.monpackage.MonJeu.Cases;
import perso.monpackage.MonJeu.Joueurs;

public class HeuristiquesTest {

	@Test
	public void testHeuristiqueCorrecte() {
		Plateau p = new MonJeu(new Cases[][] {
			{Cases.NOIR,Cases.BLANC,Cases.BLANC,Cases.BLANC,Cases.NEUTRE},
			{Cases.NOIR,Cases.BLANC,Cases.NEUTRE,Cases.NEUTRE,Cases.NEUTRE},
			{Cases.NOIR,Cases.NOIR,Cases.NEUTRE,Cases.NEUTRE,Cases.NEUTRE},
			{Cases.NOIR,Cases.NEUTRE,Cases.NEUTRE,Cases.NEUTRE,Cases.NEUTRE},
			{Cases.BLANC,Cases.BLANC,Cases.NOIR,Cases.BLANC,Cases.NOIR}
		});
		MonJoueur j = new MonJoueur();
		j.setId(Joueurs.BLANC);
		int val = Heuristiques.repartition.eval(p, j);
		assertTrue(val > 0);
	}

}
