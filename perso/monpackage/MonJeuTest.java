package perso.monpackage;

import static org.junit.Assert.*;

import org.junit.Test;

import perso.modele.Plateau;
import perso.monpackage.MonJeu.Cases;
import perso.monpackage.MonJeu.Joueurs;

public class MonJeuTest {

	@Test
	public void testFinDePartie() {
		Plateau p = new MonJeu(new Cases[][] {
			{Cases.NOIR,Cases.BLANC,Cases.NOIR,Cases.BLANC,Cases.NOIR},
			{Cases.NEUTRE,Cases.NEUTRE,Cases.BLANC,Cases.NEUTRE,Cases.BLANC},
			{Cases.BLANC,Cases.NOIR,Cases.BLANC,Cases.NEUTRE,Cases.NOIR},
			{Cases.NEUTRE,Cases.NEUTRE,Cases.NEUTRE,Cases.NEUTRE,Cases.NEUTRE},
			{Cases.BLANC,Cases.NOIR,Cases.NEUTRE,Cases.NEUTRE,Cases.NOIR}
		});
		assertTrue(p.finDePartie());
	}
	@Test
	public void testCoupsPossibles()
	{
		Plateau p = new MonJeu();
		MonJoueur j = new MonJoueur();
		j.setId(Joueurs.BLANC);
		int cps = p.coupsPossibles(j).size();
		assertTrue(cps == 35);
	}
	@Test
	public void testCoupsPossibles2()
	{
		Plateau p = new MonJeu(new Cases[][]{
			{Cases.NOIR,Cases.NOIR,Cases.NEUTRE,Cases.BLANC,Cases.BLANC},
			{Cases.NOIR,Cases.NEUTRE,Cases.NEUTRE,Cases.NEUTRE,Cases.BLANC},
			{Cases.NOIR,Cases.NEUTRE,Cases.BLANC,Cases.NEUTRE,Cases.NEUTRE},
			{Cases.NOIR,Cases.NEUTRE,Cases.NEUTRE,Cases.NEUTRE,Cases.BLANC},
			{Cases.NOIR,Cases.NOIR,Cases.NEUTRE,Cases.BLANC,Cases.BLANC}
		});
		MonJoueur j = new MonJoueur();
		j.setId(Joueurs.BLANC);
		int cps = p.coupsPossibles(j).size();
		assertTrue(cps == 11);
	}
}
