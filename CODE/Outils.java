import java.util.Scanner;
import java.io.FileInputStream;

import java.io.PrintWriter;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;

public class Outils
{
    /*------------------------------------------*/
	/*         Affichage DebutHTML              */
	public static void debutHTML(final String nom , String chemin , String fichier)
	{
		String dhtml = "";
		
		dhtml = "<!DOCTYPE html>\n"
		      + "<html lang=\"fr-FR\">\n"
		      + "<head>\n"
			  + "\t<meta charset=\"UTF-8\" />\n"
			  + "\t<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\" />\n"
			  + "\t<link href=\"../REP_RACINE/css/style.css\" rel=\"stylesheet\" type=\"text/css\" />\n" 
			  + "\t<title>" + nom + "</title>\n"
			  + "</head>\n"
			  + "<body>";

		try
		{
			PrintWriter pw = new PrintWriter(new OutputStreamWriter(new FileOutputStream( chemin + fichier ), "UTF8" ));

			pw.println ( dhtml );
			pw.close();
		}
		catch (Exception e){ e.printStackTrace(); }
	}

	/*------------------------------------------*/
	/*     Affichage DebutHTML avec en-tete     */
	public static void debutHTML(final String nom , String chemin , String fichier, String enTete)
	{
		String dhtml = "";
		
		dhtml = "<!DOCTYPE html>\n"
		      + "<html lang=\"fr-FR\">\n"
		      + "<head>\n"
			  + "\t<meta charset=\"UTF-8\" />\n"
			  + "\t<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\" />\n"
			  + "\t<link href=\"../REP_RACINE/css/style.css\" rel=\"stylesheet\" type=\"text/css\" />\n" 
			  + "\t<title>" + nom + "</title>\n"
			  + "</head>\n"
			  + "<body>\n";

		if ( enTete != null )
			dhtml += "\t<header>" + enTete + "</header>\n";

		try
		{
			PrintWriter pw = new PrintWriter(new OutputStreamWriter(new FileOutputStream( chemin + fichier ), "UTF8" ));

			pw.println ( dhtml );
			pw.close();
		}
		catch (Exception e){ e.printStackTrace(); }
	}

    /*------------------------------------------*/
	/*          Affichage FinHTML               */
	public static void finHTML(String chemin , String fichier)
	{
		String fhtml = "</body>\n"
		             + "</html>";

		String copie = Outils.copieTexte( chemin + fichier);
		
		try
		{
			PrintWriter pw = new PrintWriter(new OutputStreamWriter(new FileOutputStream(chemin + fichier), "UTF8" ));

			pw.println(copie);
			pw.println ( fhtml );
			pw.close();
		}
		catch (Exception e){ e.printStackTrace(); }
	}

	/*------------------------------------------*/
	/*     Affichage FinHTML avec pied de page  */
	public static void finHTML(String chemin , String fichier, String piedDePage)
	{
		String fhtml = "";

		if ( piedDePage != null )
			fhtml = "\t <footer>" + piedDePage + "</footer>\n";

		fhtml += "</body>\n"
		      +  "</html>";

		String copie = Outils.copieTexte( chemin + fichier);
		
		try
		{
			PrintWriter pw = new PrintWriter(new OutputStreamWriter(new FileOutputStream(chemin + fichier), "UTF8" ));

			pw.println(copie);
			pw.println ( fhtml );
			pw.close();
		}
		catch (Exception e){ e.printStackTrace(); }
	}
	/*------------------------------------------*/
	/*   copie du texte contenu dans le fichier */
	public static String copieTexte(String fichier)
	{
		String texte = "";
		try
		{
			final Scanner sc = new Scanner (new FileInputStream ( "../REP_RACINE/" + fichier ), "UTF8");

			while (sc.hasNextLine()) 
			{
				texte += sc.nextLine() + "\n";
			}
			sc.close();
		}catch (final Exception e) { e.printStackTrace(); }


		return texte;
	}

    /*------------------------------------------*/
	/*    Transformation en nombre romain       */
    public static String nombreRomain(final int nbr) 
    {
		
		String[][] nbRomain;
		String sChapitre = "";

		nbRomain = new String[10][10];

		for (int cptCol = 0; cptCol < 10; cptCol++)
			for (int cptLig = 0; cptLig < 10; cptLig++) {
				switch (conversionNombreString(cptCol * 10 + cptLig).charAt(0)) {
					case '0' -> sChapitre = "";
					case '1' -> sChapitre = "X";
					case '2' -> sChapitre = "XX";
					case '3' -> sChapitre = "XXX";
					case '4' -> sChapitre = "XL";
					case '5' -> sChapitre = "L";
					case '6' -> sChapitre = "LX";
					case '7' -> sChapitre = "LXX";
					case '8' -> sChapitre = "LXXX";
					case '9' -> sChapitre = "XC";
				}

				switch (conversionNombreString(cptCol * 10 + cptLig).charAt(1)) {
					case '0' -> sChapitre += "";
					case '1' -> sChapitre += "I";
					case '2' -> sChapitre += "II";
					case '3' -> sChapitre += "III";
					case '4' -> sChapitre += "IV";
					case '5' -> sChapitre += "V";
					case '6' -> sChapitre += "VI";
					case '7' -> sChapitre += "VII";
					case '8' -> sChapitre += "VIII";
					case '9' -> sChapitre += "IX";
				}

				nbRomain[cptLig][cptCol] = sChapitre;
			}

		return nbRomain[nbr % 10][nbr / 10];
	}

    /*------------------------------------------*/
	/* Conversion d'un nombre entier ent string */
	public static String conversionNombreString(final int nombre) 
	{

		final char d = (char) ((int) '0' + (nombre / 10));
		final char u = (char) ((int) '0' + (nombre % 10));
		final String retour = d + "" + u;

		return retour;
	}

	/*------------------------------------------*/
	/* Conversion d'un nombre String en entier  */
	public static int conversionNombreInt(final String nombre) 
	{
		int retour = 0;
		int d = nombre.charAt(0) - '0';
		int u = 0;

		if (nombre.length() == 2 )
		{
			u = nombre.charAt(1) - '0';
			retour = d * 10 + u;
		}
		else retour = d;

		return retour;
	}

	/*------------------------------------------*/
	/*     Remplacement en balise               */
	public static String transformationContenu(String ligne)
	{
		ligne = ligne.replaceAll( "#CRLF#", "<br />");
		ligne = ligne.replaceAll( "#/g#"  , "<b>"   );
		ligne = ligne.replace( "#\\g#" , "</b>"  );
		ligne = ligne.replaceAll( "#/i#"  , "<i>"   );
		ligne = ligne.replace( "#\\i#" , "</i>"  );

		return ligne;
	}

}