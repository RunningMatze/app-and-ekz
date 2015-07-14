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
        
        //Hinzuf�gen Button
        buttonAdd = (Button)findViewById(R.id.buttonAddShop);
        //Zur�ck Button
        buttonBack = (Button)findViewById(R.id.buttonBack);
        
        //ListView initialisieren
        shopListe=(ListView) findViewById(R.id.shopList);
        
        //Datenbank initialisieren
        db = new EinkaufszettelDB();

        //SQL-Abfrage durchf�hren Cursor holen
        //====================================
        dbCursor = db.query(getString(R.string.table_shops), null, getString(R.string.field_shops_shopname)+" ASC");
        startManagingCursor(dbCursor);

        //CursorAdapter zum bef�llen der Liste initialisieren und an Liste �bergeben
        //==========================================================================
        shopAdapter = new ShopsAdapter(shopListe.getContext(),dbCursor,true);
        shopListe.setAdapter(shopAdapter);

        shopListe.setOnItemClickListener(new OnItemClickListener() {
        	@Override
	        public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long id) {
        		// in dieser Activity k�nnen Shops ge�ndert oder gel�scht werden
	        	Intent intDetail = new Intent(ShopsActivity.this,de.androloc.einkaufszettel.ShopEditActivity.class);
	        	intDetail.putExtra(getString(R.string._ID), id);
	    		startActivityForResult(intDetail, ACT_EDIT);
        	}
		});
        
        //Neuer Shop wird hinzugef�gt
        buttonAdd.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
	        	Intent intAdd = new Intent(ShopsActivity.this,de.androloc.einkaufszettel.ShopAddActivity.class);
	    		startActivityForResult(intAdd, ACT_EDIT);
			}
		});

        //Zur�ck-Button
        buttonBack.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				ExitAct();
			}
		});

	}

	//Detail-Acttivity oder Add-Activity f�r Shops wurde beendet
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == ACT_EDIT) {
			if (resultCode == RESULT_OK) {
				//Es wurde eine �nderung durchgef�hrt, Datenbank aktualisieren
				dbCursor.requery();
				shopAdapter.notifyDataSetChanged();
			}
		}
	}

	//Zur�ck zum Parent
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