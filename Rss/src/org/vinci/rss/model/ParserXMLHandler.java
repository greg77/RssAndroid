package org.vinci.rss.model;

import java.util.ArrayList;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;


public class ParserXMLHandler extends DefaultHandler {

	// nom des tags XML
	private final String ITEM = "item";
	private final String TITLE = "title";
	private final String LINK = "link";
	private final String PUBDATE = "pubDate";
	private final String CREATOR = "creator";
	private final String DESCRIPTION = "description";
	
	// Array list d'articles	
	private ArrayList<Article> articles;
	
	// Boolean permettant de savoir si nous sommes � l'int�rieur d'un item
	private boolean inItem;
	
	// Feed courant
	private Article currentArticle;
	
	// Buffer permettant de contenir les donn�es d'un tag XML
	private StringBuffer buffer;
	
	@Override
	public void processingInstruction(String target, String data) throws SAXException {		
		super.processingInstruction(target, data);
	}

	public ParserXMLHandler() {
		super();		
	}
	
	
	// * Cette m�thode est appel�e par le parser une et une seule  
	// * fois au d�marrage de l'analyse de votre flux xml. 
	// * Elle est appel�e avant toutes les autres m�thodes de l'interface,  
	// * � l'exception unique, �videmment, de la m�thode setDocumentLocator. 
	// * Cet �v�nement devrait vous permettre d'initialiser tout ce qui doit 
	// * l'�tre avant led�but du parcours du document.
	 
	@Override
	public void startDocument() throws SAXException {
		super.startDocument();
		articles = new ArrayList<Article>();
		
	}

	/* 
	 * Fonction �tant d�clench�e lorsque le parser trouve un tag XML
	 * C'est cette m�thode que nous allons utiliser pour instancier un nouveau feed
 	*/
	@Override
	public void startElement(String uri, String localName, String name,	Attributes attributes) throws SAXException {
		// Nous r�initialisons le buffer a chaque fois qu'il rencontre un item
		buffer = new StringBuffer();		
		
		// Ci dessous, localName contient le nom du tag rencontr�
		
		// Nous avons rencontr� un tag ITEM, il faut donc instancier un nouveau feed
		if (localName.equalsIgnoreCase(ITEM)){			
			this.currentArticle = new Article();
			inItem = true;
		}
		
		// Vous pouvez d�finir des actions � effectuer pour chaque item rencontr�
		if (localName.equalsIgnoreCase(TITLE)){
			// Nothing to do	
		}
		if (localName.equalsIgnoreCase(LINK)){
			// Nothing to do	
		}
		if (localName.equalsIgnoreCase(PUBDATE)){	
			// Nothing to do	
		}
		if (localName.equalsIgnoreCase(CREATOR)){
			// Nothing to do
		}
		if(localName.equalsIgnoreCase(DESCRIPTION)){
			// Nothing to do	
		}
	}
	
	 
	// * Fonction �tant d�clench�e lorsque le parser � pars� 	
	// * l'int�rieur de la balise XML La m�thode characters  
	// * a donc fait son ouvrage et tous les caract�re inclus 
	// * dans la balise en cours sont copi�s dans le buffer 
	// * On peut donc tranquillement les r�cup�rer pour compl�ter
	// * notre objet currentFeed
	
	@Override
	public void endElement(String uri, String localName, String name) throws SAXException {		
		
		if (localName.equalsIgnoreCase(TITLE)){
			if(inItem){				
				// Les caract�res sont dans l'objet buffer
				this.currentArticle.setTitle(buffer.toString());				
				buffer = null;
			}
		}
		if (localName.equalsIgnoreCase(LINK)){
			if(inItem){				
				this.currentArticle.setLink(buffer.toString());				
				buffer = null;
			}
		}
		if (localName.equalsIgnoreCase(PUBDATE)){	
			if(inItem){				
				this.currentArticle.setPubDate(buffer.toString());				
				buffer = null;
			}
		}
		if (localName.equalsIgnoreCase(CREATOR)){
			if(inItem){				
				this.currentArticle.setCreator(buffer.toString());				
				buffer = null;	
			}
		}
		if(localName.equalsIgnoreCase(DESCRIPTION)){
			if(inItem){				
				this.currentArticle.setDescription(buffer.toString());				
				buffer = null;
			}
		}
		if (localName.equalsIgnoreCase(ITEM)){		
			articles.add(currentArticle);
			inItem = false;
		}
	}

	// * Tout ce qui est dans l'arborescence mais n'est pas partie  
	// * int�grante d'un tag, d�clenche la lev�e de cet �v�nement.  
	// * En g�n�ral, cet �v�nement est donc lev� tout simplement 
	// * par la pr�sence de texte entre la balise d'ouverture et 
	// * la balise de fermeture

	public void characters(char[] ch,int start, int length)	throws SAXException{		
		String lecture = new String(ch,start,length);
		if(buffer != null) buffer.append(lecture);      		
	}
	
	
	// cette m�thode nous permettra de r�cup�rer les donn�es
	public ArrayList<Article> getData() {
		return this.articles;
	}
	
	
}
