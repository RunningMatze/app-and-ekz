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

public class ShopsActivity extends Activity {

	//ListView-Object
	private ListView shopListe;
	private EinkaufszettelDB db;
	private Cursor dbCursor;
	private ShopsAdapter shopAdapter;
	private static final int ACT_EDIT = 1;
	private Button buttonAdd;
	private Button buttonBack;
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
        setContentView(R.layout.shops);
        
        //Hinzufügen Button
        buttonAdd = (Button)findViewById(R.id.buttonAddShop);
        //Zurück Button
        buttonBack = (Button)findViewById(R.id.buttonBack);
        
        //ListView initialisieren
        shopListe=(ListView) findViewById(R.id.shopList);
        
        //Datenbank initialisieren
        db = new EinkaufszettelDB();

        //SQL-Abfrage durchführen Cursor holen
        //====================================
        dbCursor = db.query(getString(R.string.table_shops), null, getString(R.string.field_shops_shopname)+" ASC");
        startManagingCursor(dbCursor);

        //CursorAdapter zum befüllen der Liste initialisieren und an Liste übergeben
        //==========================================================================
        shopAdapter = new ShopsAdapter(shopListe.getContext(),dbCursor,true);
        shopListe.setAdapter(shopAdapter);

        shopListe.setOnItemClickListener(new OnItemClickListener() {
        	@Override
	        public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long id) {
        		// in dieser Activity können Shops geändert oder gelöscht werden
	        	Intent intDetail = new Intent(ShopsActivity.this,de.androloc.einkaufszettel.ShopEditActivity.class);
	        	intDetail.putExtra(getString(R.string._ID), id);
	    		startActivityForResult(intDetail, ACT_EDIT);
        	}
		});
        
        //Neuer Shop wird hinzugefügt
        buttonAdd.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
	        	Intent intAdd = new Intent(ShopsActivity.this,de.androloc.einkaufszettel.ShopAddActivity.class);
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

	//Detail-Acttivity oder Add-Activity für Shops wurde beendet
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == ACT_EDIT) {
			if (resultCode == RESULT_OK) {
				//Es wurde eine Änderung durchgeführt, Datenbank aktualisieren
				dbCursor.requery();
				shopAdapter.notifyDataSetChanged();
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