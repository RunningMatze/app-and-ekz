package de.androloc.einkaufszettel;

import android.preference.PreferenceActivity;
import android.os.Bundle;

public class ZettelPreferenceActivity extends PreferenceActivity {

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    addPreferencesFromResource(R.xml.pref_zettel);

	    // TODO Auto-generated method stub
	}

}
