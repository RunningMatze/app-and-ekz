package de.androloc.einkaufszettel;

import java.util.Hashtable;

import android.app.Activity;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;
import android.widget.Toast;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.database.Cursor;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;


public class EinkaufArtikelActivity extends Activity {

	private long einkaufID;
	private ListView listeArtikel;
	private EinkaufszettelDB db;
	private Cursor dbCursor;
	private Cursor dbCursorA;
	private ZettelListenAdapter zettelListenAdapter;
	private boolean show_hint;
	private boolean sort_strike;
	private boolean dont_turn;
	SharedPreferences zettel_Preferences;				//Einkaufszettel Einstellungen

	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.einkauf_artikel);
	    
	    zettel_Preferences = PreferenceManager.getDefaultSharedPreferences(this);

	    /*
	 	sort_strike = zettel_Preferences.getBoolean("sort_strike", false);
	 	dont_turn = zettel_Preferences.getBoolean("fixed_orientation", true);
	    
	 	//ggf. immer vertikal ausrichten
	    if (dont_turn == true) {
		 	setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
	    }

	    //ID des Einkaufs in der DB
	    einkaufID = this.getIntent().getLongExtra(getString(R.string._ID), 0);

	    //Datenbank initialisieren
	    db = new EinkaufszettelDB(); 

        // ** Artikel-Liste des Einkaufszettels **
	    listeArtikel=(ListView) findViewById(R.id.listeArtikel);
        //SQL-Abfrage durchführen Cursor holen
        //====================================
	    String sorting = getString(R.string.field_listen_sortindex)+" ASC";
	    if (sort_strike){
		    sorting = getString(R.string.field_listen_erledigt)+" ASC, " + getString(R.string.field_listen_sortindex)+" ASC";
	    }
	    dbCursor = db.query(getString(R.string.table_listen),getString(R.string.field_listen_einkaufnr)+"="+einkaufID , sorting);
	    startManagingCursor(dbCursor);
        //CursorAdapter zum befüllen des Spinners
        //=======================================
        zettelListenAdapter = new ZettelListenAdapter(listeArtikel.getContext(),dbCursor,true);
        listeArtikel.setAdapter(zettelListenAdapter);
	
        listeArtikel.setOnItemLongClickListener(new OnItemLongClickListener() {
        	@Override
        	public boolean onItemLongClick(AdapterView<?> arg0, View view, int arg2, long id) {
        		// TODO Auto-generated method stub
        		//Erledigt Flag updaten
                try {
            		dbCursorA = db.query(getString(R.string.table_listen),getString(R.string._ID)+"="+id ,null);
            		dbCursorA.moveToFirst();
            		int erledigt = dbCursorA.getInt(dbCursorA.getColumnIndex(getString(R.string.field_listen_erledigt)));
            		if (erledigt == 1) {
            			erledigt = 0;
            		} else {
            			erledigt = 1;
            		}
    				// TODO Datenbank updaten
    				Hashtable<String,String> fields = new Hashtable<String,String>();
    				fields.put(getString(R.string.field_listen_erledigt), Integer.toString(erledigt));
    				db.Update(getString(R.string.table_listen),id,fields);
    				dbCursor.requery();
    				zettelListenAdapter.notifyDataSetChanged();
                } catch (Exception e) {
					// TODO: handle exception
				}
        		return true;
        	}
        });
        */
	}
	

	/*
	@Override
	protected void onStart() {
		super.onStart();

		show_hint = zettel_Preferences.getBoolean("show_hint", true);
		if (show_hint) {
			Toast.makeText(this, getString(R.string.hint_strikeout), Toast.LENGTH_LONG).show();
		}
	 	//ggf. immer vertikal ausrichten
	 	dont_turn = zettel_Preferences.getBoolean("fixed_orientation", true);
	    if (dont_turn == true) {
		 	setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
	    }
	}
	*/
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();

		//ggf. immer vertikal ausrichten

	    if (dont_turn == true) {
		 	setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
	    }


	    zettel_Preferences = PreferenceManager.getDefaultSharedPreferences(this);
	 	sort_strike = zettel_Preferences.getBoolean("sort_strike", false);
	 	dont_turn = zettel_Preferences.getBoolean("fixed_orientation", true);
	    
	 	//ggf. immer vertikal ausrichten
	    if (dont_turn == true) {
		 	setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
	    }

	    //ID des Einkaufs in der DB
	    einkaufID = this.getIntent().getLongExtra(getString(R.string._ID), 0);

	    //Datenbank initialisieren
	    db = new EinkaufszettelDB(); 

        // ** Artikel-Liste des Einkaufszettels **
	    listeArtikel=(ListView) findViewById(R.id.listeArtikel);
        //SQL-Abfrage durchführen Cursor holen
        //====================================
	    String sorting = getString(R.string.field_listen_sortindex)+" ASC";
	    if (sort_strike){
		    sorting = getString(R.string.field_listen_erledigt)+" ASC, " + getString(R.string.field_listen_sortindex)+" ASC";
	    }
	    dbCursor = db.query(getString(R.string.table_listen),getString(R.string.field_listen_einkaufnr)+"="+einkaufID , sorting);
	    startManagingCursor(dbCursor);
        //CursorAdapter zum befüllen des Spinners
        //=======================================
        zettelListenAdapter = new ZettelListenAdapter(listeArtikel.getContext(),dbCursor,true);
        listeArtikel.setAdapter(zettelListenAdapter);
	
        listeArtikel.setOnItemLongClickListener(new OnItemLongClickListener() {
        	@Override
        	public boolean onItemLongClick(AdapterView<?> arg0, View view, int arg2, long id) {
        		// TODO Auto-generated method stub
        		//Erledigt Flag updaten
                try {
            		dbCursorA = db.query(getString(R.string.table_listen),getString(R.string._ID)+"="+id ,null);
            		dbCursorA.moveToFirst();
            		int erledigt = dbCursorA.getInt(dbCursorA.getColumnIndex(getString(R.string.field_listen_erledigt)));
            		if (erledigt == 1) {
            			erledigt = 0;
            		} else {
            			erledigt = 1;
            		}
    				// TODO Datenbank updaten
    				Hashtable<String,String> fields = new Hashtable<String,String>();
    				fields.put(getString(R.string.field_listen_erledigt), Integer.toString(erledigt));
    				db.Update(getString(R.string.table_listen),id,fields);
    				dbCursor.requery();
    				zettelListenAdapter.notifyDataSetChanged();
                } catch (Exception e) {
					// TODO: handle exception
				}
        		return true;
        	}
        });

	    
	    
	    show_hint = zettel_Preferences.getBoolean("show_hint", true);
		if (show_hint) {
			Toast.makeText(this, getString(R.string.hint_strikeout), Toast.LENGTH_LONG).show();
		}

	}

	@Override
	protected void onPause() {
		super.onPause();
	    stopManagingCursor(dbCursor);
		dbCursor.close();
		db.close();
	};
	
	//Methode muss zum Erstellen des Menüs überschrieben werden
    @Override  
    public boolean onCreateOptionsMenu(Menu menu) {   
    	MenuInflater inflater = getMenuInflater();   
        inflater.inflate(R.menu.menu_zettel, menu);   
        return super.onCreateOptionsMenu(menu);   
    }   

    //Methode muss zum reagieren auf das Anklicken der Menü-Items überschrieben werden
    @Override  
    public boolean onOptionsItemSelected(MenuItem item) {
       	switch (item.getItemId()) {
        case R.id.menu_zettel_clear:
        	//Alle Streichungen zurücksetzen
        	resetStrikeOut();
        	return true;
        case R.id.menu_zettel_settings:
			// Preferences-Activity aufrufen
	 		Intent i = new Intent(EinkaufArtikelActivity.this, ZettelPreferenceActivity.class);
			startActivity(i);
        	return true;
        default:
            return super.onOptionsItemSelected(item);
        }
    }

    //Alle Streichungen zurücksetzen
    private void resetStrikeOut(){
    	if (dbCursor.moveToFirst()) {
    		do {
    			int id = dbCursor.getInt(dbCursor.getColumnIndex(getString(R.string._ID)));
				Hashtable<String,String> fields = new Hashtable<String,String>();
				fields.put(getString(R.string.field_listen_erledigt), "0");
				db.Update(getString(R.string.table_listen),id,fields);
    		} while(dbCursor.moveToNext());
			dbCursor.requery();
			zettelListenAdapter.notifyDataSetChanged();
    	}
    }
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
    	// TODO Auto-generated method stub
    	super.onConfigurationChanged(newConfig);
    	if (dont_turn == true) {
        	if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
        	    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            }
    	}
    }
}
