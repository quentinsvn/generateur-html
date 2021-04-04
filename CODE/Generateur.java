import java.util.Scanner;
import java.io.FileInputStream;

import java.io.PrintWriter;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;

public class Generateur 
{
	private String prefix;
	private String repSource;
	private String repRacine;
	private String dossier;

	private String enTete;
	private String piedDePage;

	private String[] listeFichier;
	private int      nbFichier;

	private int positionLigne;

	private int cti;
	private int ct1;
	private int ct2;

	private int idh2;
	private int idh3;

/*------------------------------------------------*/
/*             inisialise le generateur           */
	public Generateur()
	{
		this.repRacine    = "../REP_RACINE/";
		this.repSource    = "../REP_SOURCE/";
		this.dossier      = "table_ordonancement.data";

		this.listeFichier  = new String[99];

		this.nbFichier     = 0;
		this.positionLigne = 1;

		this.cti = 1;
		this.ct2 = 1;
		this.ct1 = 1;

		this.idh2 = 0;
		this.idh3 = 0;

		this.lectureTable();                             //appelle la metode lectureTable

		this.creationChapitre();                         // appelle la metode creationChapitre

	}

/*                                                */
/*------------------------------------------------*/


/*------------------------------------------------*/
/*            lecture de la table ordonancement   */

	public void lectureTable()
	{
		int cpt = 0;

		try
		{
			final Scanner sc = new Scanner (new FileInputStream ( this.repSource + this.dossier ), "UTF8");

			for( cpt = 0 ; sc.hasNextLine(); cpt++)                               // recupere les ligne de la table d'ordonancement 
				this.listeFichier[cpt] = sc.nextLine();                           // stock chaque ligne de la table d'ordonancement

			this.nbFichier = cpt;                       

			sc.close();
			
		}catch (final Exception e) { e.printStackTrace(); }

		this.creationPageGarde();                   // apelle la création de la page de garde
	}
/*                                                */
/*------------------------------------------------*/

/*------------------------------------------------*/
/*         creation de la page de garde           */

	public void creationPageGarde()
	{
		this.dossier = this.listeFichier[0];
		String ligne = "";

		int cptLigne = 1;

		try
		{
			final Scanner sc = new Scanner (new FileInputStream ( this.repSource + this.dossier ), "UTF8");

			while( sc.hasNextLine() )                                                       // boucle j'usqu'a la fin du fichiers
			{
				while ( cptLigne < this.positionLigne && sc.hasNextLine())                  // boucle jusqu'a la ligne actuelle
				{
					ligne = sc.nextLine();                                                  
					cptLigne++;
				}

				ligne = sc.nextLine();                                                      // stock la ligne dans une variable
				ligne = Outils.transformationContenu(ligne);                                // transforme les balise de la ligne contenu

				if ( ligne.length() > 3 )                                                // si il y a plus de 3 caractere sur la ligne
				{
					switch( ligne.substring(0, 3) )
					{
						case "FGA" -> {this.prefix = ligne.substring(4); }
						case "TGA" -> {Outils.debutHTML  ( ligne.substring(4), this.repRacine, this.prefix+".html");
						               this.ecritureTitre( ligne.substring(4) );}
						case "PGA" -> this.ecriturePGA();
						case "IGA" -> this.ecritureIGA(ligne.substring(4));
						case "TEN" -> this.enTete = ligne.substring(4);
						case "TPI" -> this.piedDePage = ligne.substring(4);
					}
				}

				cptLigne ++;
				this.positionLigne++;
			}

			sc.close();
		}catch (final Exception e) { e.printStackTrace(); }

		Outils.finHTML(this.repRacine, this.prefix + ".html");                // rajoute la fin html
	}

/*                                                */
/*------------------------------------------------*/

/*------------------------------------------------*/
/*      écriture de la balise corspondant a IGA   */

	public void ecritureIGA(String ligne)
	{
		String texteAlt = "";
		String image    = "";

		int cpt;

		for ( cpt = 0; cpt < ligne.length(); cpt++)                       // si la le caractere se trouvant a la position de cpt est egale a ':'
			if ( ligne.charAt(cpt) == ':' )
			{
				image = ligne.substring(0, cpt);                       // recupere le nom de l'image se trouvant avant le ':'
				texteAlt = ligne.substring(cpt + 1);                   // recupere le texte alternatif se trouvant apres le ':'
			}


		String copie = Outils.copieTexte(this.repRacine + this.prefix + ".html");  // copie le fichier


		try
		{
			PrintWriter pw = new PrintWriter(new OutputStreamWriter(new FileOutputStream( this.repRacine + this.prefix + ".html" ), "UTF8" ));

			pw.println ( copie );                                  // colle le fichier

			// ecrit une balise img qui affiche l'image recuperé et qui met le texte alternatif recuperé
			pw.println ("<a href=\"encyclo_lutin_00.html\" ><img src=\"" + this.repRacine+ "/images/" + image + "\" alt=\"" + texteAlt +"\" class = \"iga\" title=\"Aller vers le sommaire\" /></a>");

			pw.close();
		}
		catch (Exception e){ e.printStackTrace(); }
		

	}

/*                                                */
/*------------------------------------------------*/


/*------------------------------------------------*/
/*             écriture du titre                  */

	public void ecritureTitre(String titre)
	{
		String copie = Outils.copieTexte(this.repRacine + this.prefix + ".html");         // copie le fichier

		try
		{
			PrintWriter pw = new PrintWriter(new OutputStreamWriter(new FileOutputStream( this.repRacine + this.prefix + ".html" ), "UTF8" ));

			pw.println ( copie );                                 // colle le fichier

			// met l'icon pour aller au sommaire
			pw.println ("<a title=\"Aller vers le sommaire\" id=\"pg_icon\" href=\"encyclo_lutin_00.html\"> -> </a><h1 class=\"tga\">" +  titre  + "</h1>");

			pw.close();
		}
		catch (Exception e){ e.printStackTrace(); }
	}

/*                                                */
/*------------------------------------------------*/

/*------------------------------------------------*/
/*     écriture de la balise corspondant a PGA    */

	public void ecriturePGA()
	{	
		String fichier    = "";
		String ligne      = "";
		String paragraphe = "<p class=\"pga\">\n";

		int cptLigne = 1;
		

		try 
		{
			final Scanner sc = new Scanner (new FileInputStream ( this.repSource + this.dossier ), "UTF8");

			while ( cptLigne <= this.positionLigne && sc.hasNextLine())  // boucle jusqu'a la ligne actuelle
			{
				ligne = sc.nextLine();
				cptLigne++;
			}


			while ( ligne.length() > 3 && ligne.substring(0,3).equals("PGA") && sc.hasNextLine() ) // écrit toute les ligne PGA qui sont a la suite
			{
					ligne       = Outils.transformationContenu(ligne);   // transforme les balise de la ligne contenu
					paragraphe += ligne.substring(4) + " \n";            // crée le paragraphe en ajoutant les ligne parcourus
					ligne       = sc.nextLine();

					cptLigne++;
					this.positionLigne++;
			}


			if ( !sc.hasNextLine() )  paragraphe += ligne.substring(4) + " \n";

			sc.close();

		} catch (final Exception e) { e.printStackTrace(); }


		paragraphe += "</p>\n";                   // ajoute la fin de balise p au paragraphe
		fichier     = Outils.copieTexte( this.repRacine + this.prefix + ".html"); // copie le fichier


		try
		{
			PrintWriter pw = new PrintWriter(new OutputStreamWriter(new FileOutputStream( this.repRacine + this.prefix + ".html"), "UTF8" ));

			pw.println ( fichier +  "\n"+ paragraphe );  // colle le fichier et le paragraphe

			pw.close();
		}

		catch (Exception e){ e.printStackTrace(); }

	}

/*                                                */
/*------------------------------------------------*/


/*------------------------------------------------*/
/*           creation des page chapitre           */
	public void creationChapitre()
	{
		String chapitre = "";

		try
		{

			for ( int cpt = 0; cpt < this.nbFichier; cpt++)
			{

				chapitre = this.prefix + "_" + String.format("%02d", cpt) + ".html" ;      // crée le fichier html au chapitre corspondant

				PrintWriter pw = new PrintWriter(new OutputStreamWriter(new FileOutputStream( this.repRacine + chapitre ), "UTF8" ));

				if ( cpt > 0) new Page( chapitre, this.listeFichier[cpt], this.enTete, this.piedDePage); // appelle la classe page pour chaque fichier html crée
				else          this.genSommaire(chapitre );                                    // appelle la creation du sommaire

				pw.close();

			}
		}
		catch (Exception e){ e.printStackTrace(); }
	}
/*                                                */
/*------------------------------------------------*/

/*------------------------------------------------*/
/*           creation de la page du sommaire      */

	public void genSommaire(String chapitre)
	{
		String ligne   = "";
		String copie   = "";
		String fichier = "";

		int nbChapitre = 0;
		
		Outils.debutHTML("Sommaire", this.repRacine, chapitre); // appelle le sous programme debutHTML de Outils

		copie = Outils.copieTexte(this.repRacine + chapitre); // copie le texte du fichier

		try
		{
			PrintWriter pw = new PrintWriter(new OutputStreamWriter(new FileOutputStream( this.repRacine + chapitre), "UTF8" ));

			pw.println(copie);          // colle le fichier

			// met l'icon de retour a la page de garde
			pw.println("\t<a title=\"Aller vers la page de garde\" id=\"sm_icon\" href=\"encyclo_lutin.html\"> <- </a><h1> Sommaire </h1>\n");
			pw.println("\t<div class=\"sm\" >\n");  // ouvre une balise div qui prend la class "sm"

			pw.close();
		} catch (Exception e){ e.printStackTrace(); }


		try
		{
			Scanner sc = new Scanner ( new FileInputStream ( this.repSource + "table_ordonancement.data" ) );

			while ( sc.hasNextLine() )                     // parcours la boucle le temps qu'il existe une ligne suivante
			{
				fichier = sc.nextLine();

				Scanner sc2 = new Scanner ( new FileInputStream ( this.repSource + fichier ), "UTF8" ); 
				
				while ( sc2.hasNextLine() )               // parcours la boucle le temps qu'il existe une ligne suivante 
				{
					ligne = sc2.nextLine();

					if ( ligne.length() > 3 && sc2.hasNextLine() )
					{
						switch( ligne.substring(0, 3) )
						{
							case "CTI" -> this.ecritureCTI ( ligne.substring(4) , nbChapitre , chapitre );
							case "CT1" -> this.ecritureCT1 ( ligne.substring(4) , nbChapitre , chapitre );
							case "CT2" -> this.ecritureCT2 ( ligne.substring(4) , nbChapitre , chapitre );
						}
					}
				}

				sc2.close();
				nbChapitre++;
			}

			sc.close();
		}
		catch (Exception e){ e.printStackTrace(); }

		copie = Outils.copieTexte(this.repRacine + chapitre);    // copie toute les ligne du fichier
			
		try
		{
			PrintWriter pw = new PrintWriter(new OutputStreamWriter(new FileOutputStream( this.repRacine + chapitre), "UTF8" ));

			pw.println(copie);                                   // colle le fichier
			pw.println("\t</div>");                              // ferme la balise div

			pw.close();
		} catch (Exception e){ e.printStackTrace(); }

		Outils.finHTML(this.repRacine, chapitre);                // apelle le sous programme finHTML du programme Outils

	}


/*                                                */
/*------------------------------------------------*/

/*------------------------------------------------*/
/*   creation de la balise CTI dans le sommaire   */


	public void ecritureCTI(String nomChapitre, int nbChapitre ,String chapitre)
	{
		String fichier = Outils.copieTexte(this.repRacine + chapitre);          // copie toute les ligne du fichier
				
		try
		{
			PrintWriter pw = new PrintWriter(new OutputStreamWriter(new FileOutputStream( this.repRacine + chapitre), "UTF8" ));
			
			pw.println( fichier );                 // colle le fichier

			// met dans un lien la direction a la page du fichier CTI et le numerote en chiffre romain
			pw.println ( "\t<a href=\"" + this.prefix +"_"+ String.format("%02d", nbChapitre) + ".html\" >" + Outils.nombreRomain(this.cti) + ". " + nomChapitre + "</a> <br />" );

			pw.close();
		} catch (Exception e){ e.printStackTrace(); }
		
		this.cti++;
		this.ct1 = 0;
		this.idh2 = 0;
		this.idh3 = 0;
	}

/*                                                */
/*------------------------------------------------*/

/*------------------------------------------------*/
/*   creation de la balise CT2 dans le sommaire   */

	public void ecritureCT2(String nomChapitre, int nbChapitre ,String chapitre)
	{
		String fichier = Outils.copieTexte(this.repRacine + chapitre);   // copie toute les ligne du fichier
				
		try
		{
			PrintWriter pw = new PrintWriter(new OutputStreamWriter(new FileOutputStream( this.repRacine + chapitre), "UTF8" ));
			
			pw.println ( fichier );                                       // colle le fichier

			// met dans un lien la direction a la page du fichier CT2 et a l'encre et le numerote en chiffre arabe
			pw.print   ( "\t\t&emsp; &emsp; <a href=\"" +  this.prefix + "_" + String.format("%02d", nbChapitre) + ".html#h3_" + this.idh3 + "\" >");
			pw.print   (this.ct1 + "."+ this.ct2 +". " + nomChapitre + "</a> <br />"  );

			pw.close();
		} catch (Exception e){ e.printStackTrace(); }
		
		this.idh3++;
		this.ct2++;
	}

/*                                                */
/*------------------------------------------------*/

/*------------------------------------------------*/
/*   creation de la balise CT1 dans le sommaire   */

	public void ecritureCT1(String nomChapitre, int nbChapitre ,String chapitre)
	{

		this.ct1++;
		
		String fichier = Outils.copieTexte(this.repRacine + chapitre);    // copie toute les ligne du fichier
				
		try
		{
			PrintWriter pw = new PrintWriter(new OutputStreamWriter(new FileOutputStream( this.repRacine + chapitre), "UTF8" ));
			
			pw.println ( fichier );                                // colle le fichier

			// met dans un lien la direction a la page du fichier CT1 et a l'encre et le numerote en chiffre arabe
			pw.print ( "\t&emsp; <a href=\"" +  this.prefix +"_"+ String.format("%02d", nbChapitre) + ".html#h2_" + this.idh2 + "\" >" );
			pw.print ( this.ct1 + ". " + nomChapitre + "</a> <br />");

			pw.close();
			
		} catch (Exception e){ e.printStackTrace(); }
		
		this.idh2++;
		this.ct2 = 1;
	}

/*                                                */
/*------------------------------------------------*/


	public static void main(String[] a)
	{
		System.out.println("Génération en cours...");
		new Generateur();
		System.out.println("Fin de la génération !");
	}
}