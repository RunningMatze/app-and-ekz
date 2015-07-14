package de.androloc.einkaufszettel;

import android.app.TabActivity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.widget.TabHost;

public class EinkaufActivity extends TabActivity {

	private long einkaufID;
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.einkauf_tab);
	    
	    Resources res = getResources(); 	// Resources-Objekt für die images
	    TabHost tabHost = getTabHost();  	// Tab-Host
	    TabHost.TabSpec spec;  				// Tab-Spezifikationen für die einzelnen Tabs
	    Intent intent;  					// Intents der einzelnen Tabs
	
        //Aktuellen Artikel aus der Datenbank auslesen
        einkaufID = this.getIntent().getLongExtra(getString(R.string._ID),0);  //Index des Records

	    //Tab für Einkaufs-Daten
	    intent = new Intent().setClass(this, EinkaufDatenActivity.class);
	    intent.putExtra(getString(R.string._ID), einkaufID);
	    spec = tabHost.newTabSpec("daten");
	    spec.setIndicator(getString(R.string.einkaufsdaten),res.getDrawable(R.drawable.meineinkauf));
	    spec.setContent(intent);
	    tabHost.addTab(spec);

	    //Tab für Einkaufszettel
	    intent = new Intent().setClass(this, EinkaufArtikelActivity.class);
	    intent.putExtra(getString(R.string._ID), einkaufID);
	    spec = tabHost.newTabSpec("artikel");
	    spec.setIndicator(getString(R.string.aktzettel),res.getDrawable(R.drawable.artikelliste));
	    spec.setContent(intent);
	    tabHost.addTab(spec);
	}

}
