package de.androloc.einkaufszettel;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.DatePicker;
import android.widget.EditText;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Hashtable;

public class EinkaufEditActivity extends Activity {

	private Spinner shopListe;
	private DatePicker einkaufDatum;
	private Button buttonOK;
	private Button buttonCancel;
	private Button buttonDelete;
	private EditText textBemerkung;
	private EinkaufszettelDB db;
	private Cursor dbCursorS;
	private Cursor dbCursorE;
	private ShopsAdapter shopAdapter;
	private long einkaufID;
	private long shopID;
	private static final int DIALOG_DELETE_EINKAUF = 1;

	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.einkauf_edit);

	    
	    //Index des gewählten Einkaufs in der Datenbank
        einkaufID = this.getIntent().getLongExtra(getString(R.string._ID),0);  //Index des Records

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
        //Löschen-Button initialisieren
        buttonDelete = (Button) findViewById(R.id.buttonDelete);

        //Datenbank initialisieren
        db = new EinkaufszettelDB();

        //SQL-Abfrage durchführen Cursor holen
        //und Spinner mit allen Shops befüllen
        //====================================
        dbCursorS = db.query(getString(R.string.table_shops), null, getString(R.string.field_shops_shopname)+" ASC");
        startManagingCursor(dbCursorS);
        //CursorAdapter zum befüllen der Liste initialisieren und an Liste übergeben
        //==========================================================================
        shopAdapter = new ShopsAdapter(shopListe.getContext(),dbCursorS,true);
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

        //Datum auf aktuellen Tag setzen (Home-Button)
        ((ImageButton)findViewById(R.id.button_date_now)).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				final Calendar cal = Calendar.getInstance();
				einkaufDatum.updateDate(cal.get(Calendar.YEAR),cal.get(Calendar.MONTH),cal.get(Calendar.DATE));
			}
		});
        
        //Einkaufsdaten auslesen und Werte an widgets übergeben
        //-----------------------------------------------------
        try {
            dbCursorE = db.query(getString(R.string.table_einkauf), getString(R.string._ID) + "=" + einkaufID, null);
            dbCursorE.moveToFirst();
            //Shop über die entsprechende ID ermitteln und setzen
            shopID = dbCursorE.getLong(dbCursorE.getColumnIndex(getString(R.string.field_einkauf_shop)));
            for (int pos=0; pos < shopAdapter.getCount(); pos++){
            	if (shopAdapter.getItemId(pos) == shopID){
            		shopListe.setSelection(pos);
            		break;
            	}
            }
            String strDatum=dbCursorE.getString(dbCursorE.getColumnIndex(getString(R.string.field_einkauf_datum)));
            String day = strDatum.substring(0, 2);
            String month = strDatum.substring(3, 5);
            String year = strDatum.substring(6, 10);
            einkaufDatum.updateDate(Integer.parseInt(year),Integer.parseInt(month)-1, Integer.parseInt(day));
            textBemerkung.setText(dbCursorE.getString(dbCursorE.getColumnIndex(getString(R.string.field_einkauf_bemerkung))));
        } catch (Exception e) {
			// TODO: handle exception
		}
	
        //Abbrechen - Button
        buttonCancel.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				setResult(RESULT_CANCELED);
				ExitAct();
			}
		});
        
        //Übernehmen - Button
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
				db.Update(getString(R.string.table_einkauf),einkaufID,fields);
				setResult(RESULT_OK);
				ExitAct();
			}
		});
	
        //Löschen-Button
        buttonDelete.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//Sicherheitsabfrage
				showDialog(DIALOG_DELETE_EINKAUF);
			}
		});
	}

	//Zurück zum Parent
	private void ExitAct() {
		try {
			dbCursorS.close();
			dbCursorE.close();
			db.close();
			finish();
		} catch (Exception e) {
		}
    }

	//muss überschrieben werden wird beim erstellen einmalig aufgerufen
	@Override
	protected Dialog onCreateDialog(int id) {
		
		if (id == DIALOG_DELETE_EINKAUF) {
			//AlertDialog-Builder referenzieren
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			//AlertDialog-Builder konfigurieren
			//Hier besser eine String-ID für den Titel verwenden, Hinweis kommt ja öfters vor
			builder.setTitle(getString(R.string.delete_einkauf));         										
			//Hier besser eine String-ID für die Nachricht verwenden.
			builder.setMessage(getString(R.string.einkauf_delete_question1)+"\n"+getString(R.string.einkauf_delete_question2));		
			//Dialog kann nicht mit der Back-Taste geschlossen werden.
			builder.setCancelable(false);
			//OK/Ja Button
			builder.setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() { 
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// Hier den Code einfügen der bei OK ausgeführt werden soll
					db.Delete(getString(R.string.table_einkauf),einkaufID);
					setResult(RESULT_FIRST_USER);
					ExitAct();
				}
			});
			//Abbrechen/nein Button
			builder.setNegativeButton(getString(R.string.no), new DialogInterface.OnClickListener() {		
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// Hier den Code einfügen der bei Cancel ausgeführt werden soll
					dialog.cancel();
				}
			});
			//Dialog-Objekt zurückliefern
			return builder.create();
		}
		return super.onCreateDialog(id);
	}
}
