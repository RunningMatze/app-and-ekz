package de.androloc.einkaufszettel;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import java.util.Hashtable;


public class ArtikelAddActivity extends Activity {

	private Button buttonOK;
	private Button buttonCancel;
	private EditText textArtikel;
	private EinkaufszettelDB db;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.artikel_add);

        //Editfeld Artikelbezeichnung
        textArtikel = (EditText) findViewById(R.id.textArtikel);
        //Fertig-Button
        buttonOK = (Button) findViewById(R.id.buttonOK);
        //Abbrechen-Button
        buttonCancel = (Button) findViewById(R.id.buttonCancel);

        //Abbrechen-Button wurde geklickt
        buttonCancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				setResult(RESULT_CANCELED);
				ExitAct();
			}
		});

        //Übernehmen-Event
        buttonOK.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				db = new EinkaufszettelDB();
				//Artikel in Datenbank einstellen
				Hashtable<String,String> fields = new Hashtable<String,String>();
				fields.put(getString(R.string.field_artikel_bezeichnung), textArtikel.getText().toString());
				db.Add(getString(R.string.table_artikel),fields);
				setResult(RESULT_OK);
				ExitAct();
			}
		});

		//Textänderung im Feld Shopname
        textArtikel.addTextChangedListener(new TextWatcher() {
    		@Override
    		public void onTextChanged(CharSequence s, int start, int before, int count) {
    			// Prüfen ob Text eingegeben wurde
    			if (textArtikel.getText().toString().trim().length() > 0) {
    				buttonOK.setEnabled(true);
    			} else {
    				buttonOK.setEnabled(false);
    			}
    		}
    		@Override
    		public void beforeTextChanged(CharSequence s, int start, int count,	int after) {
    		}
    		@Override
    		public void afterTextChanged(Editable s) {
    		}
    	});


	}
	
	//Zurück zum Parent
	private void ExitAct() {
		try {
			db.close();
		} catch (Exception e) {
			// TODO: handle exception
		}
		finish();
    }
}
