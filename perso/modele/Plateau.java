package perso.modele;

import java.util.ArrayList;

import perso.algo.Evaluable;

public interface Plateau extends Cloneable, Evaluable {
	public abstract ArrayList<Coup> coupsPossibles(Joueur j);
    public abstract void joue(Joueur j, Coup c);
    public abstract boolean finDePartie();
    public abstract Object clone();
    public abstract boolean coupValide(Joueur j, Coup c);
}
