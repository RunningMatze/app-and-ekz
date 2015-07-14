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

public class EinkaeufeActivity extends Activity {

	//ListView-Object
	private ListView einkaufListe;
	private EinkaufszettelDB db;
	private Cursor dbCursor;
	private EinkaeufeAdapter einkaeufeAdapter;
	private static final int ACT_EDIT = 1;
	private Button buttonAdd;
	private Button buttonBack;

	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.einkaeufe);

        //Hinzufügen Button
        buttonAdd = (Button)findViewById(R.id.buttonAddEinkauf);
        //Zurück Button
        buttonBack = (Button)findViewById(R.id.buttonBack);
        
        
        //ListView initialisieren
        einkaufListe=(ListView) findViewById(R.id.einkaufListe);
        
        //Datenbank initialisieren
        db = new EinkaufszettelDB();

        //SQL-Abfrage durchführen Cursor holen
        //====================================
        dbCursor = db.query(getString(R.string.table_einkauf), null, getString(R.string.field_einkauf_datum_sort)+" DESC");
        startManagingCursor(dbCursor);
    
        //CursorAdapter zum befüllen der Liste initialisieren und an Liste übergeben
        //==========================================================================
        einkaeufeAdapter = new EinkaeufeAdapter(einkaufListe.getContext(),dbCursor,true);
        einkaufListe.setAdapter(einkaeufeAdapter);
        
        //Click auf Listeneintrag
        einkaufListe.setOnItemClickListener(new OnItemClickListener() {
        	@Override
	        public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long id) {
        		// Tab-Activity für Einkauf und Artikelliste anzeigen
	        	Intent intTabEinkauf = new Intent(EinkaeufeActivity.this,de.androloc.einkaufszettel.EinkaufActivity.class);
	        	intTabEinkauf.putExtra(getString(R.string._ID), id);
	    		startActivityForResult(intTabEinkauf, ACT_EDIT);
        	}
		});
        
    
        //Neuer Einkauf wird hinzugefügt
        buttonAdd.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
		    	Intent inteinkauf = new Intent(EinkaeufeActivity.this,de.androloc.einkaufszettel.EinkaufAddActivity.class);
		    	startActivityForResult(inteinkauf, ACT_EDIT);
			}
		});

        //Zurück
        buttonBack.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				ExitAct();
			}
		});
	}

	//Detail-Acttivity oder Add-Activity für Einkäufe wurde beendet
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == ACT_EDIT) {
			if (resultCode == RESULT_OK) {
				//Es wurde eine Änderung durchgeführt, Datenbank aktualisieren
				dbCursor.requery();
				einkaeufeAdapter.notifyDataSetChanged();
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
