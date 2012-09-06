package perso.monpackage;

import java.awt.Point;

import perso.modele.Coup;


public class MonCoup implements Coup {

	private Point _from;
	private Point _to;
	
	public MonCoup(int xfrom, int yfrom, int xto, int yto) {
		_from = new Point(xfrom, yfrom);
		_to = new Point(xto, yto);
	}

	public Point getFrom() {
		return _from;
	}

	public Point getTo() {
		return _to;
	}
	
	public String toString()
	{
		return (_from.x+1) + " " + (_from.y+1) + " " + (_to.x+1) + " " + (_to.y+1) + '\0';
	}

}
