package perso.algo;

import java.util.ArrayList;

import perso.modele.Coup;
import perso.modele.Joueur;
import perso.modele.Plateau;


public class Alphabeta implements AlgoJeu {
	private int _prof;
	private Joueur _jMin;
	private Joueur _jMax;
	private Heuristique _h;
	
	public Alphabeta(Heuristique h, Joueur joueurMax, Joueur joueurMin,
		int prof) {
		_h = h;
		_jMax = joueurMax;
		_jMin = joueurMin;
		_prof = prof;
	}
	@Override
	public Coup meilleurCoup(Plateau p) {
		Coup coup = null;
		int alpha = Integer.MIN_VALUE;
		ArrayList<Coup> cs = p.coupsPossibles(_jMax);
		Plateau tmp_p = null;
		Coup alpha_move = null;
		int minmax;
		for (int i = 0, c = cs.size(); i < c; ++i)
		{
			alpha_move = cs.get(i);
			tmp_p = (Plateau) p.clone();
			tmp_p.joue(_jMax, alpha_move);
			minmax = _minMax(tmp_p, alpha_move, _prof-1, alpha,
				Integer.MAX_VALUE);
			if (minmax > alpha)
			{
				alpha = minmax;
				coup = alpha_move;
			}
		}
		return coup;
	}
	
	private int _minMax(Plateau p, Coup c, int prof, int alpha, int beta) {
		if (prof == 0)
		{
			return _h.eval(p, _jMax);
		}
		else if (p.finDePartie())
		{
			return Integer.MAX_VALUE-1;
		}
		
		Coup coup = null;
		ArrayList<Coup> cs = p.coupsPossibles(_jMin);
		Plateau tmp_p = null;
		int minmax;
		for (int i = 0, count = cs.size(); i < count; ++i)
		{
			coup = cs.get(i);
			tmp_p = (Plateau) p.clone();
			tmp_p.joue(_jMin, coup);
			minmax = _maxMin(tmp_p, coup, prof-1, alpha, beta);
			if (minmax < beta)
			{
				beta = minmax;
				if (alpha >= beta)
				{
					return alpha;
				}
			}
		}
		
		return beta;
	}
	
	private int _maxMin(Plateau p, Coup c, int prof, int alpha, int beta) {
		if (prof == 0)
		{
			return _h.eval(p, _jMin);
		}
		else if (p.finDePartie())
		{
			return Integer.MIN_VALUE+1;
		}
		
		Coup coup = null;
		ArrayList<Coup> cs = p.coupsPossibles(_jMax);
		Plateau tmp_p = null;
		int minmax;
		for (int i = 0, count = cs.size(); i < count; ++i)
		{
			coup = cs.get(i);
			tmp_p = (Plateau) p.clone();
			tmp_p.joue(_jMax, coup);
			minmax = _minMax(tmp_p, coup, prof-1, alpha, beta);
			if (minmax > alpha)
			{
				alpha = minmax;
				if (alpha >= beta)
				{
					return beta;
				}
			}
		}
		
		return alpha;
	}
}
