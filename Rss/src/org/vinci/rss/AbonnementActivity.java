package org.vinci.rss;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import org.vinci.rss.model.Article;
import org.vinci.rss.model.ContainerData;
import org.xmlpull.v1.XmlPullParser;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class AbonnementActivity extends ListActivity{
	
	ArrayList<String> elements;
	ArrayList<Article> articles;
	ProgressDialog mProgressDialog;
	private AbonnementActivity mContext;
	public static final int MSG_ERR = 0;
	public static final int MSG_CNF = 1;
	public static final int MSG_IND = 2;


	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		elements = new ArrayList<String>();
		lectureInfoSite();
		lectureRSSSite();		
		
	}
	
	public boolean onCreateOptionsMenu(Menu m) {
		SubMenu sm1 = m.addSubMenu(0,1,0,elements.get(0));
		SubMenu sm2 = m.addSubMenu(0,2,0,"Quitter");
		sm1.setIcon(android.R.drawable.ic_dialog_info);
		sm2.setIcon(android.R.drawable.ic_menu_close_clear_cancel);
		
		return super.onCreateOptionsMenu(m);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch(item.getItemId()) {
			case 1:
				Toast.makeText(this,elements.get(1),Toast.LENGTH_LONG).show();
				break;
			case 2:
				Toast.makeText(this,"menu Quitter",Toast.LENGTH_LONG).show();
				finish();
				break;
		}

		return super.onOptionsItemSelected(item);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		Log.i( "Retour", ""+resultCode );
		this.finish();
		android.os.Process.killProcess(android.os.Process.myPid());
		System.exit(0);
		getParent().finish();

	}
	
	private void lectureInfoSite() {
		try {
			XmlPullParser xpp = getResources().getXml(R.xml.flux);
			while (xpp.getEventType() != XmlPullParser.END_DOCUMENT) {
				if (xpp.getEventType()==XmlPullParser.TEXT) {
					elements.add(xpp.getText());
				}
				xpp.next();
			}			
			Log.i(" item1 ", ""+elements.get(0));
			Log.i(" item2 ", ""+elements.get(1));
			} catch (Throwable t) {
				Toast.makeText(this,
				"Echec de la demande"+t.toString(),Toast.LENGTH_LONG).show();
		}
	}
	
	private void lectureRSSSite() {
		mContext = this;
		mProgressDialog = ProgressDialog.show(this, "Veuillez patienter",
		"Initialisation...", true);

		new Thread((new Runnable() {
			@Override
			public void run() {
				Message msg = null;
				String progressBarData = "Récupération des articles...";
				// Positionnement du message
				msg = mHandler.obtainMessage(MSG_IND, (Object) progressBarData);
				// Envoi du message au handler
				mHandler.sendMessage(msg);
				// On défini l'url du fichier XML
				URL url = null;
				try {
					url = new URL(elements.get(1));
				} catch (MalformedURLException e1) {
					e1.printStackTrace();
				}
				articles = ContainerData.getArticles(url);
				msg = mHandler.obtainMessage(MSG_CNF,"Récupération terminée !");
				mHandler.sendMessage(msg);
				}
			})).start();
	}
		final Handler mHandler = new Handler() {
			public void handleMessage(Message msg) {
				String text = null;
				switch (msg.what) {
					case MSG_IND:
						if (mProgressDialog.isShowing()) {
							mProgressDialog.setMessage(((String) msg.obj));
						}
						break;
					case MSG_ERR:
						text = (String) msg.obj;
						Toast.makeText(mContext, "Erreur: " + text,Toast.LENGTH_LONG).show();
						if (mProgressDialog.isShowing()) {
							mProgressDialog.dismiss();
						}
						break;
					case MSG_CNF:
						text = (String) msg.obj;
						Toast.makeText(mContext, "" + text,Toast.LENGTH_LONG).show();
						if (mProgressDialog.isShowing()) {
							mProgressDialog.dismiss();
						}
					// Positionnement de la vue liste des articles car tout est ok
					setListAdapter(new ArrayAdapter<Article>(mContext,R.layout.list_item,articles));
					ListView lv = getListView();
					lv.setTextFilterEnabled(true);
					mProgressDialog.dismiss();
					break;
				}
			}
		};
	


		

}
