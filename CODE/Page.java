import java.util.Scanner;
import java.io.FileInputStream;

import java.io.PrintWriter;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;

public class Page 
{
	private int positionLigne;
	private int tabulation;

	private String source;
	private	String repSource;
	private String repRacine;
	private String destination;

	private int idh2;
	private int idh3;

	private String enTete;
	private String piedDePage;

	public Page(String destination, String source, String enTete, String piedDePage)
	{
		this.enTete     = enTete;
		this.piedDePage = piedDePage;

		this.positionLigne = 1;
		this.tabulation    = 1;

		this.source      = source;
		this.destination = destination;
		
		this.repSource = "../REP_SOURCE/";
		this.repRacine = "../REP_RACINE/";

		this.idh2 = 0;
		this.idh3 = 0;

		this.generationPage();
	}

/*------------------------------------------------*/
/*   creation du fichier chapitre                 */

	public void generationPage()
	{
		int cptLigne = 1;
		String ligne = "";

		try
		{
			final Scanner sc = new Scanner (new FileInputStream ( this.repSource + this.source ), "UTF8");

			while (sc.hasNextLine())                                         // parcours la boucle le temps qu'il existe une ligne suivante
			{
				
				while ( cptLigne < this.positionLigne && sc.hasNextLine())   // boucle jusqu'a la ligne actuelle
				{
					ligne = sc.nextLine();
					cptLigne++;
				}

				if(sc.hasNextLine() ) ligne = sc.nextLine();  // vas a la ligne suivante

				if ( ligne.length() > 3 )               // si la ligne a plus de 3 caractere
				{
					switch( ligne.substring(0, 3) )
					{
						case "CTI" -> {Outils.debutHTML( ligne.substring(4) , this.repRacine, this.destination, this.enTete );
							         this.ecritureCTI( ligne.substring(4) ); }
						case "CT1" -> this.ecritureCT1( ligne.substring(4) );
						case "CT2" -> this.ecritureCT2( ligne.substring(4) );
						case "CPS" -> this.ecritureCPS();
						case "CPC" -> this.ecritureCPC();
						case "C/C" -> this.ecritureDebutCC();
						case "C\\C"-> this.ecritureFinCC();
						case "CIM" -> this.ecritureCIM(ligne.substring(4));
						case "///" -> this.ecritureCommentaire(ligne.substring(3) );
						case "CLO" -> this.ecritureCLO();
						case "CCO" -> this.ecritureCCO();
						case "CAN" -> this.ecritureCAN(ligne.substring(4));
						
					}
				}

				cptLigne ++;
				this.positionLigne ++;
			}

			Outils.finHTML(this.repRacine , this.destination, this.piedDePage); // ajoute la fin HTML 

			sc.close();
		}catch (final Exception e) { e.printStackTrace(); }

	}

/*                                                */
/*------------------------------------------------*/

/*------------------------------------------------*/
/*   écriture de la balise CIM dans le chapitre   */

	public void ecritureCIM(String ligne)
	{
		String texteAlt = "";
		String image    = "";
		String copie    = "";


		for (int cpt = 0; cpt < ligne.length(); cpt++)    //parcours tout la ligne
		{
			if ( ligne.charAt(cpt) == ':' )      // si la le caractere se trouvant a la position de cpt est egale a ':'
			{
				image    = ligne.substring(0, cpt);     // recupere le nom de l'image se trouvant avant le ':'
				texteAlt = ligne.substring(cpt + 1);    // recupere le texte alternatif se trouvant apres le ':'
			}
		}
			
		copie = Outils.copieTexte(this.repRacine + this.destination);   //copie le fichier

		try
		{
			PrintWriter pw = new PrintWriter(new OutputStreamWriter(new FileOutputStream( this.repRacine + this.destination ), "UTF8" ));

			pw.println ( copie );       // colle le fichier

			// ecrit une balise img qui affiche l'image recuperé et qui met le texte alternatif recuperé
			pw.println ( this.getTab() + "<img src=\"" + this.repRacine + "/images/" + image + "\" alt=\"" + texteAlt +"\" class = \"cim\" />");

			pw.close();
		}
		catch (Exception e){ e.printStackTrace(); }
		

	}
/*                                                */
/*------------------------------------------------*/

/*------------------------------------------------*/
/*écriture de l'animation d'image dans le chapitre*/

	public void ecritureCAN(String ligne)
	{

		String fichier     = "";
		String prefixImage = "";

		String sRet   = "";
		String script = "";

		int nbimage    = 1;
		int nbImageMax = 0;
		

		for ( int cpt = 0; cpt < ligne.length(); cpt++)    //parcours tout la ligne 
		{
			if ( ligne.charAt(cpt) == ':' )               // si la le caractere se trouvant a la position de cpt est egale a ':'
			{
				prefixImage = ligne.substring(0, cpt);    // recupere le prefix de l'image se trouvant avant le ':'
				nbImageMax  = Outils.conversionNombreInt ( ligne.substring(cpt + 1) );   // recupere le nombre d'images maximun de l'animation et le convertie en entier
			}
		}
			
               // crée la partie html du script
			sRet = "<!-- Images animées --> \n"
			     + this.getTab() +"<div class=\"CAN\"> <br /> \n"
			     + this.getTab() +"\t<img id=\"img\" src=\"animations/" + prefixImage.substring(0, prefixImage.length() - 4) + Outils.conversionNombreString(nbimage) + prefixImage.substring(prefixImage.length()-4) + "\"/> <br/> \n"
			     + this.getTab() +"\t<button type=\"button\" onclick=\"imagePrecedente()\"> <- </button> \n"
			     + this.getTab() +"\t<button type=\"button\" onclick=\"imageSuivante()\"> -> </button> \n"
			     + this.getTab() +"</div>\n";


              // crée la partie js du script
			script = "<!-- Script js pour animés les images --> \n"
			       + "<script type=\"text/javascript\"> \n"
			       + this.getTab() +"function imageSuivante()  \n  "+ this.getTab() + "{  \n"
			       + this.getTab() +"\tx = (x === images.length - 1) ? 0 : x + 1; \n"
			       + this.getTab() +"\tdocument.getElementById(\"img\").src = images[x]; \n"
			       + this.getTab() +"}  \n"
			       + this.getTab() +"function imagePrecedente()  \n "+ this.getTab() + "{ \n"
			       + this.getTab() +"\tx = (x <= 0) ? images.length - 1 : x - 1;  \n"
			       + this.getTab() +"\tdocument.getElementById(\"img\").src = images[x];  \n"
			       + this.getTab() +"}  \n"
			       + this.getTab() +"var images = [], x = -1;";


		fichier = Outils.copieTexte(this.repRacine + this.destination); // copie le fichier

		try
		{
			PrintWriter pw = new PrintWriter(new OutputStreamWriter(new FileOutputStream( this.repRacine + this.destination), "UTF8" ));

			pw.println ( fichier );   // colle le fichier
			pw.println ( sRet    );   // colle la partie html du script
			pw.println ( script  );   // colle la partie js du script


			for (int cpt = 0; cpt < nbImageMax; cpt++)      //ajoute une nouvelle image dans le tableau jsqu'au nombre maximumd'image défini par l'utilisateur
				pw.println( this.getTab() + "images[" + cpt + "] = \"animations/" +prefixImage.substring(0, prefixImage.length() - 4) + Outils.conversionNombreString(cpt + 1) + prefixImage.substring(prefixImage.length()-4) + "\"");


			pw.println("</script>");    // ferme la balise script

			pw.close();

		}catch (Exception e){ e.printStackTrace(); }
		
		
	}
/*                                                */
/*------------------------------------------------*/

/*------------------------------------------------*/
/*            écriture des commentaire            */

	public void ecritureCommentaire(String ligne)
	{
		String copie = Outils.copieTexte(this.repRacine + this.destination);  // copie le fichier

		try
		{
			PrintWriter pw = new PrintWriter(new OutputStreamWriter(new FileOutputStream( this.repRacine + this.destination ), "UTF8" ));

			pw.println ( copie );    // colle le fichier
			pw.println ( this.getTab() + "<!--" + ligne  + "-->" ); // met en commentaire html la ligne

			pw.close();
		}
		catch (Exception e){ e.printStackTrace(); }
	}
/*                                                */
/*------------------------------------------------*/

/*------------------------------------------------*/
/*   écriture de la balise CPS dans le chapitre   */

	public void ecritureCPS()
	{	
		String fichier    = "";
		String ligne      = "";
		String paragraphe = this.getTab() + "<p>\n";

		int cptLigne = 1;

		this.tabulation++;
		
		try 
		{
			final Scanner sc = new Scanner (new FileInputStream ( this.repSource + this.source ), "UTF8");

			while ( cptLigne <= this.positionLigne && sc.hasNextLine())  // parcours le fichier jusqu'a atteindre la ligne actruelle
			{
				ligne = sc.nextLine();
				cptLigne++;
			}


			while ( ligne.length() > 3 && ligne.substring(0,3).equals("CPS") && sc.hasNextLine() )  // écrit toute les ligne CPS qui sont a la suite
			{
					ligne       = Outils.transformationContenu(ligne);          // transforme les balise de la ligne contenu
					paragraphe += this.getTab() + ligne.substring(4) + " \n";   // crée le paragraphe en ajoutant les ligne parcourus
					ligne       = sc.nextLine();

					cptLigne++;
					this.positionLigne++;
			}


			if ( !sc.hasNextLine() ) paragraphe += this.getTab() + ligne.substring(4) + " \n";

			sc.close();
		} catch (final Exception e) { e.printStackTrace(); }

		this.tabulation--;

		paragraphe += this.getTab() + "</p>\n"; // ajoute une fin de balise p au paragraphe
		fichier     = Outils.copieTexte( this.repRacine + this.destination);     //copie le fichier

		try
		{
			PrintWriter pw = new PrintWriter(new OutputStreamWriter(new FileOutputStream( this.repRacine + this.destination), "UTF8" ));

			pw.println ( fichier + "\n" + paragraphe );            // colle le fichier ainsi que le paragraphe

			pw.close();
		}
		catch (Exception e){ e.printStackTrace(); }
		
	}

/*                                                */
/*------------------------------------------------*/

/*------------------------------------------------*/
/*   écriture de la balise CPC dans le chapitre   */

	public void ecritureCPC()
	{
		String ligne      = "";
		String paragraphe = "";
		String fichier    = "";

		int cptLigne = 1;

		try 
		{
			final Scanner sc = new Scanner (new FileInputStream ( this.repSource + this.source ), "UTF8");

			paragraphe = this.getTab() + "<p class=\"cpc\">\n"; // ajoute au pragraphe l'ouveture de la balise paragrahe avec la classe  "cpc"

			this.tabulation++;


			while ( cptLigne <= this.positionLigne && sc.hasNextLine())   // parcours le fichier jusqu'a atteindre la ligne actruelle
			{
				ligne = sc.nextLine();
				cptLigne++;
			}


			while ( ligne.length() > 3 && ligne.substring(0,3).equals("CPC") && sc.hasNextLine() ) // parcours le fichier jusqu'a pus avoir de CPC a la suite ou ne puis avoir de ligne 
			{
					ligne       = Outils.transformationContenu(ligne);
					paragraphe += this.getTab() + ligne.substring(4) + " \n";       // crée le paragraphe en mettant chaque ligne parcourus
					ligne       = sc.nextLine();

					cptLigne++;
					this.positionLigne++;
			}


			if ( ligne.length() > 3 && !sc.hasNextLine() ) paragraphe += this.getTab() + ligne.substring(4) + " \n";

			sc.close();

		} catch (final Exception e) { e.printStackTrace(); }


		this.tabulation--;

		paragraphe += this.getTab() + "</p>\n"; // ajoute une fin de balise p au paragraphe
		fichier     = Outils.copieTexte(this.repRacine + this.destination);  // copie le fichier


		try
		{
			PrintWriter pw = new PrintWriter(new OutputStreamWriter(new FileOutputStream( this.repRacine + this.destination), "UTF8" ));

			pw.println ( fichier +  "\n" + paragraphe ); // colle le fichier et le paragraphe

			pw.close();
		}catch (Exception e){ e.printStackTrace(); }
	}

/*                                                */
/*------------------------------------------------*/

/*------------------------------------------------*/
/*   écriture de la balise CLO dans le chapitre   */

	public void ecritureCLO()
	{	
		String ligne      = "";
		String fichier    = "";
		String paragraphe = this.getTab() + "<ul>\n";


		int cptLigne = 1;
		
		this.tabulation++;
		

		try 
		{
			final Scanner sc = new Scanner (new FileInputStream ( this.repSource + this.source ), "UTF8");


			while ( cptLigne <= this.positionLigne && sc.hasNextLine()) // parcours le fichier jusqu'a atteindre la ligne actruelle
			{
				ligne = sc.nextLine();
				cptLigne++;
			}


			while ( ligne.length() > 3 && ligne.substring(0,3).equals("CLO") && sc.hasNextLine() ) // parcours le fichier jusqu'a pus avoir de CLO a la suite ou ne puis avoir de ligne 
			{
					ligne       = Outils.transformationContenu(ligne);
					paragraphe += this.getTab() + "<li> "+ ligne.substring(4) + " </li>\n";    // crée le paragraphe en mettant chaque ligne entre balise de liste 
					ligne       = sc.nextLine();

					cptLigne++;
					this.positionLigne++;
			}


			if ( !sc.hasNextLine() ) paragraphe += this.getTab() + ligne.substring(4) + " \n";

			sc.close();
		} catch (final Exception e) { e.printStackTrace(); }


		this.tabulation--;


		paragraphe += this.getTab() + "</ul>\n"; // ajoute la balise de fin de liste au paragraphe
		fichier     = Outils.copieTexte( this.repRacine + this.destination);   // copie le fichier


		try
		{
			PrintWriter pw = new PrintWriter(new OutputStreamWriter(new FileOutputStream( this.repRacine + this.destination), "UTF8" ));

			pw.println ( fichier + "\n" + paragraphe );    // colle le fichier et le paragraphe

			pw.close();

		}
		catch (Exception e){ e.printStackTrace(); }
		
	}
/*                                                */
/*------------------------------------------------*/

/*------------------------------------------------*/
/*   écriture de la balise CCO dans le chapitre   */

	public void ecritureCCO()
	{	
		String ligne      = "";
		String paragraphe = this.getTab() + "<pre>\n";
		String fichier    = "";

		int cptLigne = 1;
		
		this.tabulation++;


		try 
		{
			final Scanner sc = new Scanner (new FileInputStream ( this.repSource + this.source ), "UTF8");


			while ( cptLigne <= this.positionLigne && sc.hasNextLine())  // parcours le fichier jusqu'a atteindre la ligne actruelle
			{
				ligne = sc.nextLine();
				cptLigne++;
			}


			while ( ligne.length() > 3 && ligne.substring(0,3).equals("CCO") && sc.hasNextLine() ) // parcours le fichier jusqu'a pus avoir de CCO a la suite ou ne puis avoir de ligne 
			{
					ligne       = Outils.transformationContenu(ligne);      // transforme les balise de la ligne contenu
					paragraphe += this.getTab() + ligne.substring(4) + " \n";  // crée le paragraphe en ajoutant chaque ligne 
					ligne       = sc.nextLine();

					cptLigne++;
					this.positionLigne++;
			}


			if ( !sc.hasNextLine() ) paragraphe += this.getTab() + ligne.substring(4) + " \n";


			sc.close();
		} catch (final Exception e) { e.printStackTrace(); }


		this.tabulation--;
		
		paragraphe += this.getTab() + "</pre>\n";      // ajoute la balise au paragraphe
		fichier     = Outils.copieTexte( this.repRacine + this.destination);  // copie le fichier


		try
		{
			PrintWriter pw = new PrintWriter(new OutputStreamWriter(new FileOutputStream( this.repRacine + this.destination), "UTF8" ));

			pw.println ( fichier +  "\n"+ paragraphe ); // colle le fichier et le paragraphe

			pw.close();
		}
		catch (Exception e){ e.printStackTrace(); }
		
	}

/*                                                */
/*------------------------------------------------*/

/*------------------------------------------------*/
/*   écriture de la balise CTI dans le chapitre   */

	public void ecritureCTI(String nomChapitre)
	{
		String fichier  = "";
		this.tabulation = 1;

		fichier = Outils.copieTexte(this.repRacine + this.destination); // copie le fichier

		try
		{
			PrintWriter pw = new PrintWriter(new OutputStreamWriter(new FileOutputStream( this.repRacine + this.destination), "UTF8" ));

			pw.println ( fichier ); // colle le fichier

			// met l'icon de retour au sommaire
			pw.println (  this.getTab() + "<a href=\"encyclo_lutin_00.html\" id=\"sm_icon\" title=\"Revenir vers le sommaire\"> <- </a><h1> " + nomChapitre + "</h1>" );

			pw.close();
		}catch (Exception e){ e.printStackTrace(); }
	}

/*                                                */
/*------------------------------------------------*/

/*------------------------------------------------*/
/*   écriture de la balise CT1 dans le chapitre   */

	public void ecritureCT1(String titreNiveau1)
	{
		String fichier = Outils.copieTexte(this.repRacine + this.destination); // copie le fichier
		
		try
		{
			PrintWriter pw = new PrintWriter(new OutputStreamWriter(new FileOutputStream( this.repRacine + this.destination), "UTF8" ));

			pw.println ( fichier );    // colle le fichier
			pw.println (  this.getTab() + "<h2 id=\"h2_" + this.idh2 + "\" > " + titreNiveau1 + "</h2>" ); // met entre balise h2 la ligne

			pw.close();
		}catch (Exception e){ e.printStackTrace(); }

		this.idh2++;
	}

/*                                                */
/*------------------------------------------------*/

/*------------------------------------------------*/
/*   écriture de la balise CT2 dans le chapitre   */

	public void ecritureCT2(String titreNiveau2)
	{
		String fichier = Outils.copieTexte(this.repRacine + this.destination); // copie le fichier

		try
		{
			PrintWriter pw = new PrintWriter(new OutputStreamWriter(new FileOutputStream( this.repRacine + this.destination), "UTF8" ));

			pw.println ( fichier );  // colle le fichier
			pw.println (  this.getTab() + "<h3 id=\"h3_" + this.idh3 + "\" > " + titreNiveau2 + "</h3>" ); // met entre balise h3 la ligne

			pw.close();
		}catch (Exception e){ e.printStackTrace(); }

		this.idh3++;
	}

/*                                                */
/*------------------------------------------------*/

/*-----------------------------------------------------*/
/* écriture du debut de la balise CCO dans le chapitre */

	public void ecritureDebutCC()
	{
		String fichier = Outils.copieTexte(this.repRacine + this.destination); // copie le fichier

		try
		{
			PrintWriter pw = new PrintWriter(new OutputStreamWriter(new FileOutputStream( this.repRacine + this.destination), "UTF8" ));

			pw.println ( fichier );  // colle le fichier
			pw.println (  this.getTab() + "<div>" );  // ouvre la balise div

			pw.close();
		}catch (Exception e){ e.printStackTrace(); }

		this.tabulation++;
	}

/*                                                */
/*------------------------------------------------*/

/*------------------------------------------------------*/
/* écriture de la fin de la balise CCO dans le chapitre */

	public void ecritureFinCC()
	{
		String fichier = Outils.copieTexte(this.repRacine + this.destination);  // copie le fichier

		this.tabulation--;

		try
		{
			PrintWriter pw = new PrintWriter(new OutputStreamWriter(new FileOutputStream( this.repRacine + this.destination), "UTF8" ));

			pw.println( fichier );  // colle le fichier
			pw.println (  this.getTab() + "</div>" );    // ferme la balise div

			pw.close();
		}catch (Exception e){ e.printStackTrace(); }
	}

/*                                                */
/*------------------------------------------------*/

/*--------------------------------------*/
/*      écriture de la tabulation       */

	public String getTab()
	{
		String s ="";

		for ( int cpt = 1; cpt <= this.tabulation ; cpt++ )
			s += "\t";

		return s;
	}
/*                                                */
/*------------------------------------------------*/
}
