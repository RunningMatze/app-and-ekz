package de.androloc.einkaufszettel;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.database.Cursor;
import android.content.Intent;

public class ArtikelActivity extends Activity {

	//ListView-Object
	private ListView artikelListe;
	private EinkaufszettelDB db;
	private Cursor dbCursor;
	private ArtikelAdapter artikelAdapter;
	private static final int ACT_EDIT = 1;
	private Button buttonAdd;
	private Button buttonBack;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.artikel);

	    //Hinzufügen Button
        buttonAdd = (Button)findViewById(R.id.buttonAddArtikel);
	    //Zurück Button
        buttonBack = (Button)findViewById(R.id.buttonBack);
        
        //ListView initialisieren
        artikelListe=(ListView) findViewById(R.id.artikelList);
        
        //Datenbank initialisieren
        db = new EinkaufszettelDB();

        //SQL-Abfrage durchführen Cursor holen
        //====================================
        dbCursor = db.query(getString(R.string.table_artikel), null, getString(R.string.field_artikel_bezeichnung)+" ASC");
        startManagingCursor(dbCursor);

        //CursorAdapter zum befüllen der Liste initialisieren und an Liste übergeben
        //==========================================================================
        artikelAdapter = new ArtikelAdapter(artikelListe.getContext(),dbCursor,true);
        artikelListe.setAdapter(artikelAdapter);

        artikelListe.setOnItemClickListener(new OnItemClickListener() {
        	@Override
	        public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long id) {
	        	Intent intDetail = new Intent(ArtikelActivity.this,de.androloc.einkaufszettel.ArtikelEditActivity.class);
	        	intDetail.putExtra(getString(R.string._ID), id);
	    		startActivityForResult(intDetail, ACT_EDIT);
        	}
		});
	
        //Hinzufügen-Button 
        //Neuer Artikel wird hinzugefügt
        buttonAdd.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
	        	Intent intAdd = new Intent(ArtikelActivity.this,de.androloc.einkaufszettel.ArtikelAddActivity.class);
	    		startActivityForResult(intAdd, ACT_EDIT);
			}
		});

        //Zurück-Button
        buttonBack.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				ExitAct();
			}
		});
	}

	//Detail-Activity bzw, Add-Activity für Artikel wurde beendet
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == ACT_EDIT) {
			if (resultCode == RESULT_OK) {
				//Es wurde eine Änderung durchgeführt, Datenbank aktualisieren
				dbCursor.requery();
				artikelAdapter.notifyDataSetChanged();
			}
		}
	}

	//Zurück zum Parent
	private void ExitAct() {
		try {
			dbCursor.close();
			db.close();
		} catch (Exception e) {
			// TODO: handle exception
		}
		finish();
    }
}
