package perso.monpackage;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Collection;
import perso.modele.Coup;
import perso.modele.Joueur;


public class MonJeu implements perso.modele.Plateau {
	public enum Cases
	{
		NEUTRE,
		BLANC,
		NOIR
	};
	public enum Joueurs
	{
		BLANC,
		NOIR
	};
	public static final int TAILLE = 5;
	private Cases[][] _c = {
		{Cases.NOIR,Cases.NOIR,Cases.NEUTRE,Cases.BLANC,Cases.BLANC},
		{Cases.NOIR,Cases.NEUTRE,Cases.NEUTRE,Cases.NEUTRE,Cases.BLANC},
		{Cases.NOIR,Cases.NEUTRE,Cases.NEUTRE,Cases.NEUTRE,Cases.BLANC},
		{Cases.NOIR,Cases.NEUTRE,Cases.NEUTRE,Cases.NEUTRE,Cases.BLANC},
		{Cases.NOIR,Cases.NOIR,Cases.NEUTRE,Cases.BLANC,Cases.BLANC}
	};
	
	public MonJeu(MonJeu o)
	{
		for (int j = 0; j < TAILLE; ++j)
		{
			for (int i = 0; i < TAILLE; ++i)
			{
				_c[i][j] = o._c[i][j];
			}
		}
	}
	
	public MonJeu(Cases[][] c)
	{
		_c = c;
	}
	
	public MonJeu() {}

	@Override
	public ArrayList<Coup> coupsPossibles(Joueur p) {
		ArrayList<Coup> cps = new ArrayList<Coup>();
		Cases c = ((Joueurs)(p.getId()) == Joueurs.BLANC ? Cases.BLANC
			: Cases.NOIR);
		ArrayList<Point> ps = new ArrayList<Point>();
		boolean non_connecte = false;
		for (int j = 0; j < TAILLE; ++j)
		{
			for (int i = 0; i < TAILLE; ++i)
			{
				if (_c[i][j] == c)
				{
					ps.add(new Point(i, j));
					if (!_estConnecte(i, j))
					{
						non_connecte = true;
					}
				}
			}
		}
		Point piece;
		if (non_connecte)
		{
			for (int i = 0, c1 = ps.size(); i < c1; ++i)
			{
				piece = ps.get(i);
				if (!_estConnecte(piece.x, piece.y))
				{
					cps.addAll(_getCoupsConnexion(piece.x, piece.y));
				}
			}
		}
		else
		{
			for (int i = 0, c1 = ps.size(); i < c1; ++i)
			{
				piece = ps.get(i);
				if (_estConnecteAmi(piece.x, piece.y))
				{
					cps.addAll(_getCoupsPossibles(piece.x, piece.y));
				}
			}
		}
		return cps;
	}

	private Collection<Coup> _getCoupsConnexion(int x, int y) {
		ArrayList<Coup> cs = new ArrayList<Coup>();
		cs.addAll(_getCoupsConnexionSub(x, y, x, y-1));
		cs.addAll(_getCoupsConnexionSub(x, y, x+1, y-1));
		cs.addAll(_getCoupsConnexionSub(x, y, x+1, y));
		cs.addAll(_getCoupsConnexionSub(x, y, x+1, y+1));
		cs.addAll(_getCoupsConnexionSub(x, y, x, y+1));
		cs.addAll(_getCoupsConnexionSub(x, y, x-1, y+1));
		cs.addAll(_getCoupsConnexionSub(x, y, x-1, y));
		cs.addAll(_getCoupsConnexionSub(x, y, x-1, y-1));
		return cs;
	}

	private Collection<Coup> _getCoupsConnexionSub(int x, int y, int x2, int y2) {
		if (x2 >= TAILLE
			|| x2 < 0
			|| y2 >= TAILLE
			|| y2 < 0)
		{
			return new ArrayList<Coup>();
		}
		ArrayList<Coup> cs = new ArrayList<Coup>();
		byte fin = 0x0;
		
		int[] dx = new int[] {0, 1, 1, 1, 0, -1, -1, -1};
		int[] dy = new int[] {-1, -1, 0, 1, 1, 1, 0, -1};
		int real_x,
			real_y;
		for (int i = 0, odx = x-x2, ody = y-y2; i < 8; ++i)
		{
			if (dx[i] == odx && dy[i] == ody)
			{
				fin |= (0x1<<i);
			}
		}
		
		for (int i = 1; (fin&0xFF) != 0xFF; ++i)
		{
			for (int j = 0; j < 8; ++j)
			{
				if ((fin&(0x1 << j)) == 0)
				{
					real_x = x2+(dx[j]*i);
					real_y = y2+(dy[j]*i);
					if (real_x >= TAILLE
						|| real_x < 0
						|| real_y >= TAILLE
						|| real_y < 0
						|| (_c[real_x][real_y] != _c[x][y]
							&& _c[real_x][real_y] != Cases.NEUTRE))
					{
						fin |= (0x1<<j);
					}
					else if (_c[real_x][real_y] == _c[x][y])
					{
						if (_estConnecteAmi(real_x, real_y))
						{
							cs.add(new MonCoup(real_x, real_y, x2, y2));
						}
						fin |= (0x1<<j);
					}
				}
			}
		}
		return cs;
	}

	private boolean _estConnecte(int x, int y) {
		Comparable<Cases> comp = new Comparable<MonJeu.Cases>() {
			@Override
			public int compareTo(Cases o) {
				return o != Cases.NEUTRE ? 1 : 0;
			}
		};
		return (_estConnecteA(x, y, x, y-1, comp)
			+ _estConnecteA(x, y, x+1, y-1, comp)
			+ _estConnecteA(x, y, x+1, y, comp)
			+ _estConnecteA(x, y, x+1, y+1, comp)
			+ _estConnecteA(x, y, x, y+1, comp)
			+ _estConnecteA(x, y, x-1, y+1, comp)
			+ _estConnecteA(x, y, x-1, y, comp)
			+ _estConnecteA(x, y, x-1, y-1, comp) > 0);
	}

	private boolean _estConnecteAmi(int x, int y) {
		Comparable<Cases> comp = (new Comparable<MonJeu.Cases>() {
			private Cases _c;
			public Comparable<Cases> setCase(Cases c)
			{
				_c = c;
				return this;
			}
			@Override
			public int compareTo(Cases o) {
				return o == _c ? 1 : 0;
			}
		}).setCase(_c[x][y]);
		return (_estConnecteA(x, y, x, y-1, comp)
			+ _estConnecteA(x, y, x+1, y-1, comp)
			+ _estConnecteA(x, y, x+1, y, comp)
			+ _estConnecteA(x, y, x+1, y+1, comp)
			+ _estConnecteA(x, y, x, y+1, comp)
			+ _estConnecteA(x, y, x-1, y+1, comp)
			+ _estConnecteA(x, y, x-1, y, comp)
			+ _estConnecteA(x, y, x-1, y-1, comp) > 0);
	}

	private int _estConnecteA(int x1, int y1, int x2, int y2, Comparable<Cases> comp) {
		return (x2 >= 0 && x2 < TAILLE
			&& y2 >= 0 && y2 < TAILLE ?
			comp.compareTo(_c[x2][y2]) : 0);
	}

	private Collection<Coup> _getCoupsPossibles(int x, int y) {
		ArrayList<Coup> cs = new ArrayList<Coup>();
		byte fin = 0x0;
		
		int[] dx = new int[] {0, 1, 1, 1, 0, -1, -1, -1};
		int[] dy = new int[] {-1, -1, 0, 1, 1, 1, 0, -1};
		int real_x,
			real_y;
		
		for (int i = 1; (fin&0xFF) != 0xFF; ++i)
		{
			for (int j = 0; j < 8; ++j)
			{
				if ((fin&(0x1 << j)) == 0)
				{
					real_x = x+(dx[j]*i);
					real_y = y+(dy[j]*i);
					if (real_x < TAILLE
						&& real_x >= 0
						&& real_y < TAILLE
						&& real_y >= 0
						&& _c[real_x][real_y] == Cases.NEUTRE)
					{
						cs.add(new MonCoup(x, y, real_x, real_y));
					}
					else
					{
						fin |= (0x1<<j);	
					}
				}
			}
		}
		return cs;
	}

	@Override
	public void joue(Joueur j, Coup c) {
		if (!coupValide(j, c))
		{
			return;
		}
		_c[((MonCoup)c).getTo().x][((MonCoup)c).getTo().y] =
			_c[((MonCoup)c).getFrom().x][((MonCoup)c).getFrom().y];
		_c[((MonCoup)c).getFrom().x][((MonCoup)c).getFrom().y] = Cases.NEUTRE;
	}

	private Cases _getColor(Point from) {
		return _c[from.x][from.y];
	}

	@Override
	public boolean finDePartie() {
		boolean blanc = true;
		boolean noir = true;
		for (int j = 0; j < TAILLE; ++j)
		{
			for (int i = 0; i < TAILLE; ++i)
			{
				if (_c[i][j] != Cases.NEUTRE && _estConnecteAmi(i, j))
				{
					if (_c[i][j] == Cases.BLANC)
					{
						blanc = false;
					}
					else
					{
						noir = false;
					}
				}
			}
		}
		return blanc || noir;
	}

	@Override
	public boolean coupValide(Joueur j, Coup c) {
		// TODO Non valide
		MonCoup mc = (MonCoup)c;
		return mc.getFrom().x >= 0 && mc.getFrom().x < TAILLE
			&& mc.getFrom().y >= 0 && mc.getFrom().y < TAILLE
			&& mc.getTo().x >= 0 && mc.getTo().x < TAILLE
			&& mc.getTo().y >= 0 && mc.getTo().y < TAILLE
			&& ((((Joueurs)j.getId()) == Joueurs.BLANC
				&& _getColor(mc.getFrom()) == Cases.BLANC)
			|| ((Joueurs)j.getId()) == Joueurs.NOIR
				&& _getColor(mc.getFrom()) == Cases.NOIR)
			&& _getColor(mc.getTo()) == Cases.NEUTRE;
	}
	
	public Object clone()
	{
		return new MonJeu(this);
	}
	
	public String toString()
	{
		String buffer = "";
		for (int j = 0; j < TAILLE; ++j)
		{
			for (int i = 0; i < TAILLE; ++i)
			{
				buffer += (_c[i][j] == Cases.BLANC ? "[B]" :
					(_c[i][j] == Cases.NOIR ? "[N]" : "[-]"));
			}
			buffer += '\n';
		}
		return buffer;
	}
	
	@Override
	public int eval(Joueur j, String s) {
		if (s.equals("open"))
		{
			return _opening(j);
		}
		else if (s.equals("positionCle"))
		{
			return _positionCle(j);
		}
		else if (s.equals("isolement"))
		{
			return _isolement(j);
		}
		else if (s.equals("counterOpening"))
		{
			return _counterOpening(j);
		}
		return 0;
	}

	private int _counterOpening(Joueur j) {
		Cases ma_case = (j.getId() == Joueurs.BLANC ? Cases.BLANC :
			Cases.NOIR);
		return (_c[2][0] == ma_case ^ _c[2][4] == ma_case)
			&& (_c[0][2] == ma_case || _c[4][2] == ma_case)
			? 1000000000 : 0;
	}

	@Override
	public int meilleurEval(String string) {
		if (string.equals("open"))
		{
			return 1; // Jamais utilisé en principe
		}
		else if (string.equals("positionCle"))
		{
			return 49;
		}
		else if (string.equals("isolement"))
		{
			return 10;
		}
		else if (string.equals("counterOpening"))
		{
			return 1; // Jamais utilisé
		}
		return 0;
	}
	
	private int _isolement(Joueur j)
	{
		Cases ma_case = (j.getId() == Joueurs.BLANC ? Cases.BLANC :
			Cases.NOIR);
		int val = 0;
		
		for (int y = 0; y < TAILLE; ++y)
		{
			for (int x = 0; x < TAILLE; ++x)
			{
				if (_c[x][y] == ma_case)
				{
					int tmp = _estConnecteEnnemi(x, y);
					switch (tmp)
					{
					case 1: tmp = 10; break;
					case 2: tmp = 5; break;
					case 3: tmp = 4; break;
					case 4: tmp = 3; break;
					case 5: tmp = 2; break;
					case 6: tmp = 1; break;
					case 7: tmp = 1; break;
					case 0: break;
					}
					val += tmp;
				}
			}
		}
		return val;
	}
	
	private int _positionCle(Joueur j) {
		Cases ma_case = (j.getId() == Joueurs.BLANC ? Cases.BLANC :
			Cases.NOIR);
		int val = 0;
		
		for (int y = 0; y < TAILLE; ++y)
		{
			for (int x = 0; x < TAILLE; ++x)
			{
				if (_c[x][y] == ma_case)
				{
					val += _positionCle(x, y);
				}
			}
		}
		return val;
	}

	private int _positionCle(int x, int y) {
		return (((x+1)*(y+1))%2)==1 ? 7 : 1;
	}

	private int _estConnecteEnnemi(int x, int y) {
		Comparable<Cases> comp = (new Comparable<MonJeu.Cases>() {
			private Cases _c;
			public Comparable<Cases> setCase(Cases c)
			{
				_c = (c == Cases.BLANC ? Cases.NOIR : Cases.BLANC);
				return this;
			}
			@Override
			public int compareTo(Cases o) {
				return o == _c ? 1 : -1;
			}
		}).setCase(_c[x][y]);
		return (_estConnecteA(x, y, x, y-1, comp)
			+ _estConnecteA(x, y, x+1, y-1, comp)
			+ _estConnecteA(x, y, x+1, y, comp)
			+ _estConnecteA(x, y, x+1, y+1, comp)
			+ _estConnecteA(x, y, x, y+1, comp)
			+ _estConnecteA(x, y, x-1, y+1, comp)
			+ _estConnecteA(x, y, x-1, y, comp)
			+ _estConnecteA(x, y, x-1, y-1, comp));
	}

	private int _opening(Joueur j) {
		Cases ma_case = (j.getId() == Joueurs.BLANC ? Cases.BLANC :
			Cases.NOIR);
		return ((_c[0][2] == ma_case &&
					(_c[0][3] == Cases.NEUTRE || _c[0][1] == Cases.NEUTRE))
				|| (_c[4][2] == ma_case &&
					(_c[4][3] == Cases.NEUTRE || _c[4][1] == Cases.NEUTRE))
			? 1000000000*(int)(Math.random()*2) : Integer.MIN_VALUE);
	}

}
