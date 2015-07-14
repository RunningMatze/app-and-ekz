package de.androloc.einkaufszettel;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.DatePicker;
import android.widget.EditText;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Hashtable;

public class EinkaufAddActivity extends Activity {

	private Spinner shopListe;
	private DatePicker einkaufDatum;
	private Button buttonOK;
	private Button buttonCancel;
	private EditText textBemerkung;
	private EinkaufszettelDB db;
	private Cursor dbCursor;
	private ShopsAdapter shopAdapter;
	private long shopID = 0;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.einkauf_add);

	    //ListView initialisieren
        shopListe=(Spinner) findViewById(R.id.spinnerShops);
        //Datumsauswahl initialisieren
        einkaufDatum = (DatePicker) findViewById(R.id.dateEinkauf);
        //OK-Button initialisieren
        buttonOK = (Button) findViewById(R.id.buttonOK);
        //Cancel-Button initialisieren
        buttonCancel = (Button) findViewById(R.id.buttonCancel);
        //Textfeld Bemerkungen
        textBemerkung = (EditText) findViewById(R.id.editTextBemerkung);

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

        shopListe.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
        	@Override
        	public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long id) {
        		// TODO Auto-generated method stub
        		shopID = id;
        		buttonOK.setEnabled(true);
        	}
        	@Override
        	public void onNothingSelected(AdapterView<?> arg0) {
        		shopID = 0;
        		buttonOK.setEnabled(false);
        	}
		});
	
        //Abbrechen-Button wurde geklickt
        buttonCancel.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				setResult(RESULT_CANCELED);
				ExitAct();
			}
		});
        //Übernehmen Button - Einkauf in DB speichern
        buttonOK.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				String bemerkung=textBemerkung.getText().toString();
				Date datum=new Date(einkaufDatum.getYear() - 1900, einkaufDatum.getMonth(), einkaufDatum.getDayOfMonth());
				DateFormat formatter = new SimpleDateFormat("dd.MM.yyyy");
				String datumStr= formatter.format(datum);
				formatter = new SimpleDateFormat("yyyy-MM-dd");
				String datumSortStr= formatter.format(datum);
				Hashtable<String,String> fields = new Hashtable<String,String>();
				fields.put(getString(R.string.field_einkauf_shop), Long.toString(shopID));
				fields.put(getString(R.string.field_einkauf_datum), datumStr);
				fields.put(getString(R.string.field_einkauf_datum_sort), datumSortStr);
				fields.put(getString(R.string.field_einkauf_bemerkung), bemerkung);
				db.Add(getString(R.string.table_einkauf),fields);
				setResult(RESULT_OK);
				ExitAct();
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
