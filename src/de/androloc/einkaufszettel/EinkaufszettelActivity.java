package de.androloc.einkaufszettel;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ImageButton;
import android.view.View;
import android.view.View.OnClickListener;
import android.app.Dialog;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;


public class EinkaufszettelActivity extends Activity {
    /** Called when the activity is first created. */
    
	private static final int DIALOG_ALERT = 1;
	private EinkaufszettelDB db;
	private SQLiteDatabase dbConnection;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        //this.setTitle(getString(R.string.app_name));

        /*Hauptmenü-Punkte
        ------------------*/
    
        //On-Click für Einkaufslisten-Layout initialisieren
        //======================================================
        ((ImageButton)findViewById(R.id.imageListen)).setOnClickListener(listenListener);

        //On-Click für Artikel-Layout initialisieren
        //==========================================
        ((ImageButton)findViewById(R.id.imageArtikel)).setOnClickListener(artikelListener);

        //On-Click für Shop-Layout initialisieren
        //==========================================
        ((ImageButton)findViewById(R.id.imageShops)).setOnClickListener(shopsListener);

        //On-Click für Exit-Layout initialisieren
        //==========================================
        ((ImageButton)findViewById(R.id.imageExit)).setOnClickListener(exitListener);
        
        //Datenbank initilisieren
        db = new EinkaufszettelDB(this);

	}
	
    //On-Click-Event für Einkaufsliste
	//================================
	OnClickListener listenListener = new OnClickListener() {
	    public void onClick(View v) {
	        //Einkaufliste aufrufen
	    	//---------------------
	    	Intent inteinkauf = new Intent(EinkaufszettelActivity.this,de.androloc.einkaufszettel.EinkaeufeActivity.class);
	    	startActivity(inteinkauf);
	    }
	};

	//On-Click-Event für Artikel
	//==========================
	OnClickListener artikelListener = new OnClickListener() {
	    public void onClick(View v) {
	        //Artikel-Activity aufrufen
	    	//-------------------------
	    	Intent intartikel = new Intent(EinkaufszettelActivity.this,de.androloc.einkaufszettel.ArtikelActivity.class);
	    	startActivity(intartikel);
	    }
	};

	//On-Click-Event für Shops
	//========================
	OnClickListener shopsListener = new OnClickListener() {
	    public void onClick(View v) {
	        //Shop-Avtivity aufrufen
	    	//----------------------
	    	Intent intshops = new Intent(EinkaufszettelActivity.this,de.androloc.einkaufszettel.ShopsActivity.class);
	    	startActivity(intshops);
	    }
	};

	//On-Click-Event für Exit
	//========================
	OnClickListener exitListener = new OnClickListener() {
	    public void onClick(View v) {
	    	showDialog(DIALOG_ALERT);
	    }
	};

	//muss überschrieben werden wird beim erstellen einmalig aufgerufen
	@Override
	protected Dialog onCreateDialog(int id) {
		
		if (id == DIALOG_ALERT) {
			//AlertDialog-Builder referenzieren
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			//AlertDialog-Builder konfigurieren
			builder.setTitle(R.string.app_name);         										
			builder.setMessage(R.string.finish_question);		
			builder.setCancelable(false);											
			//OK/Ja Button
			builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() { 
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// Hier den Code einfügen der bei OK ausgeführt werden soll
					finish();
				}
			});
			//Abbrechen/nein Button
			builder.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {		
				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.cancel();
				}
			});
			//Dialog-Objekt zurückliefern
			return builder.create();
		}
		return super.onCreateDialog(id);
	}
	@Override
	protected void onDestroy() {
		super.onDestroy();
		try {
			dbConnection.close();
			db.close();
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
}