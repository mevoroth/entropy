package entropy;
import java.net.*;
import java.io.*;
import java.util.*;

/**
 * Cette classe permet de charger dynamiquement une classe de joueur,
 * qui doit obligatoirement implanter l'interface IJoueur. Vous lui
 * donnez aussi en argument le nom de la machine distante (ou "localhost")
 * sur laquelle le serveur de jeu est lancé, ainsi que le port sur lequel
 * la machine écoute.
 *
 * Exemple: >java -cp . entropy/ClientJeu entropy.joueurProf localhost 1234
 * 
 * Le client s'occupe alors de tout en lançant les méthodes implantées de l'interface
 * IJoueur. Toute la gestion réseau est donc cachée.
 * 
 * @author L. Simon (Univ. Paris-Sud)- 2006-2012
 * @see IJoueur
 */
public class ClientJeu {
	
	static final int NOIR = 2; // Vous pouvez changer cela en interne si vous le souhaitez
	static final int BLANC = 1; // Mais pas lors de la conversation avec l'arbitre
	static final int VIDE = 0;
	
	/**
	 * @param args Dans l'ordre : NomClasseJoueur MachineServeur PortEcoute
	 */
	public static void main(String[] args) {
				
		if (args.length < 3) {
			System.err.println("ClientJeu Usage: NomClasseJoueur MachineServeur PortEcoute");
			System.exit(1);
		}
		
		// Le nom de la classe joueur � charger dynamiquement
		String classeJoueur = args[0];
		// Le nom de la machine serveur a �t� donn� en ligne de commande
		String serverMachine = args[1];
		// Le num�ro du port sur lequel on se connecte a aussi �t� donn�
		int portNum = Integer.parseInt(args[2]);
		
		System.out.println("Le client se connectera sur " + serverMachine + ":" + portNum);
		
		Socket clientSocket = null;
		IJoueur joueur; 
		String msg, firstToken;
		StringTokenizer msgTokenizer; // permet d'analyser les chaines de caractères lues
		int couleurAJouer; // C'est la couleur qui doit jouer le prochain coup
		int maCouleur; // C'est ma couleur (quand je joue)
		
		boolean jeuTermine = false;
		int departLigne, departColonne, arriveeLigne, arriveeColonne; // Un mouvement
		try {
			// initialise la socket
			clientSocket = new Socket(serverMachine, portNum);
			PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
			BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

			//*****************************************************
			System.out.print("Chargement de la classe joueur " + classeJoueur + "... ");
			Class cjoueur = Class.forName(classeJoueur);
			joueur = (IJoueur)cjoueur.newInstance();
			System.out.println("Ok");
			// ****************************************************
			
			// Envoie de l'identifiant de votre binome.
			out.println(joueur.binoName());
			System.out.println("Mon nom de quadrinome envoyé est " + joueur.binoName());
			
			// Récupère le message sous forme de chaine de caractères
			msg = in.readLine();
			System.out.println(msg);
			
			// Lit le contenu du message, toutes les infos du message
			msgTokenizer = new StringTokenizer(msg, " \n\0");
			if ((msgTokenizer.nextToken()).equals("Blanc")) {
				System.out.println("Je suis Blanc, je commence.");
				maCouleur = BLANC;
			} else { // doit etre égal à "Noir"
				System.out.println("Je suis Noir, c'est ennemi qui commence.");
				maCouleur = NOIR;
			}

			// permet d'initialiser votre joueur avec sa couleur
			joueur.initJoueur(maCouleur);
			
			do { // boucle générale de jeu
				
				msg = in.readLine(); // Read the msg from the server
				System.out.println(msg);
				msgTokenizer = new StringTokenizer(msg, " \n\0");
				firstToken = msgTokenizer.nextToken();
				if (firstToken.equals("FIN!")) {
					jeuTermine = true;
					String theWinnerIs = msgTokenizer.nextToken();
					if (theWinnerIs.equals("Blanc")) {
						couleurAJouer = BLANC;
					} else {
						if (theWinnerIs.equals("Noir"))
							couleurAJouer = NOIR;
						else
							couleurAJouer = VIDE;
					}
					
					if( couleurAJouer == maCouleur )
						System.out.println("J'ai gagné");
					
					joueur.declareLeVainqueur(couleurAJouer);
				}
				else if (firstToken.equals("JOUEUR")) {
					// On demande au joueur de jouer
					if ((msgTokenizer.nextToken()).equals("Blanc")) {
						couleurAJouer = BLANC;
					} else {
						couleurAJouer = NOIR;
					}
					if (couleurAJouer == maCouleur) {
						// On appelle la classe du joueur pour choisir un mouvement
						msg = joueur.choixMouvement();
						out.println(msg);
					}
				} else if (firstToken.equals("MOUVEMENT")) {
					// On lit ce que joue le joueur et on l'envoie � l'autre
					departLigne = Integer.parseInt(msgTokenizer.nextToken());
					departColonne = Integer.parseInt(msgTokenizer.nextToken());
					arriveeLigne = Integer.parseInt(msgTokenizer.nextToken());
					arriveeColonne = Integer.parseInt(msgTokenizer.nextToken());
					joueur.mouvementEnnemi(departLigne, departColonne, arriveeLigne, arriveeColonne);
				}
			}while(!jeuTermine);
		}
		catch (Exception e) {
			System.out.println(e);
		}
	}
}
