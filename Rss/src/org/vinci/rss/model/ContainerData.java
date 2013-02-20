package org.vinci.rss.model;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import android.content.Context;

public class ContainerData {	
	
	static public Context context;
	
	public ContainerData() {

	}

	public static ArrayList<Article> getArticles(URL lien){
		// On passe par une classe factory pour obtenir une instance de sax
		SAXParserFactory fabrique = SAXParserFactory.newInstance();
		SAXParser parseur = null;
		ArrayList<Article> articles = null;
		
		try {
			// On "fabrique" une instance de SAXParser
			parseur = fabrique.newSAXParser();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		}
		
		/* 
		 * Le handler sera gestionnaire du fichier XML c'est � dire que c'est lui qui sera charg�
		 * des op�rations de parsing. On vera cette classe en d�tails ci apr�s.
		*/
		DefaultHandler handler = new ParserXMLHandler();
		
		try {
			// On parse le fichier XML
			parseur.parse(lien.openConnection().getInputStream(), handler);
			
			// On r�cup�re directement la liste des feeds
			articles = ((ParserXMLHandler) handler).getData();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		// On la retourne l'array list
		return articles;
	}

}
