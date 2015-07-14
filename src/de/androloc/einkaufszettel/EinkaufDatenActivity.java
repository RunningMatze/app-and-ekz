package de.androloc.einkaufszettel;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.content.Intent;
import android.database.Cursor;

public class EinkaufDatenActivity extends Activity {

	private long einkaufID;
	private String shopID;
	private EinkaufszettelDB db;
	private Cursor dbCursorE;
	private Cursor dbCursorS;
	private TextView textDatum;
	private TextView textShop;
	private TextView textOrt;
	private TextView textBemerkungen;
	private Button buttonChange;
	private Button buttonBack;
	private Button buttonArtikel;
	private static final int ACT_EDIT = 1;
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.einkauf_daten);
	
	    //TextView für Einkaufsdatum
	    textDatum = (TextView) findViewById(R.id.textDatum);
	    //TextView für Shop
	    textShop = (TextView) findViewById(R.id.textShop);
	    //TextView für Ort des Shops
	    textOrt = (TextView) findViewById(R.id.textOrt);
	    //TextView für Bemerkungen
	    textBemerkungen = (TextView) findViewById(R.id.textBemerkungen);
	    //Button für Datenänderung
	    buttonChange = (Button) findViewById(R.id.buttonChange);
	    //Button für Zurück
	    buttonBack = (Button) findViewById(R.id.buttonBack);
	    //Button für Artikel hinzufügen
	    buttonArtikel = (Button) findViewById(R.id.buttonAddArtikel);

	    
	    //Index des gewählten Einkaufs in der Datenbank
        einkaufID = this.getIntent().getLongExtra(getString(R.string._ID),0);  //Index des Records
	    //Datenbank initialisieren
        db = new EinkaufszettelDB();
        refresh();
        
	    //Button für Artikelliste bearbeiten
	    buttonArtikel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intArtikelListe = new Intent().setClass(EinkaufDatenActivity.this, ArtikelListeActivity.class);
				intArtikelListe.putExtra(getString(R.string._ID), einkaufID);
				startActivity(intArtikelListe);
			}
		});

        buttonBack.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				ExitAct();
			}
		});

        //Einkaufsdaten ändern
        buttonChange.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intEdit = new Intent().setClass(EinkaufDatenActivity.this, EinkaufEditActivity.class);
				intEdit.putExtra(getString(R.string._ID), einkaufID);
				startActivityForResult(intEdit, ACT_EDIT);
			}
		});
	}
	
	//Edit-Activity für Einkauf wurde beendet
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == ACT_EDIT) {
			if (resultCode == RESULT_OK) {
				//Es wurde eine Änderung durchgeführt, Datenbank aktualisieren
				refresh();
			}
			if (resultCode == RESULT_FIRST_USER) {
				//Einkauf wurde gelöscht
				ExitAct();
			}
		}
	}

	protected void refresh() {

		dbCursorE=db.query(getString(R.string.table_einkauf), "_id=" + einkaufID, null);
        if (dbCursorE.moveToFirst()) {
        	textDatum.setText(dbCursorE.getString(dbCursorE.getColumnIndex(getString(R.string.field_einkauf_datum))));
        	textBemerkungen.setText(dbCursorE.getString(dbCursorE.getColumnIndex(getString(R.string.field_einkauf_bemerkung))));
        	shopID=dbCursorE.getString(dbCursorE.getColumnIndex(getString(R.string.field_einkauf_shop)));
            dbCursorS=db.query(getString(R.string.table_shops), "_id=" + shopID, null);
            if (dbCursorS.moveToFirst()) {
            	textShop.setText(dbCursorS.getString(dbCursorS.getColumnIndex(getString(R.string.field_shops_shopname))));
            	textOrt.setText(dbCursorS.getString(dbCursorS.getColumnIndex(getString(R.string.field_shops_ort))));
            }
        }
        try {
        	dbCursorS.close();
        	dbCursorE.close();
        	db.close();
		} catch (Exception e) {
			// TODO: handle exception
		}

	}
	
	//Zurück zum Parent
	private void ExitAct() {
		try {
			db.close();
			finish();
		} catch (Exception e) {
		}
    }
}
