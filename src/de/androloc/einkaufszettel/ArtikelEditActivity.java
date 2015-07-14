package de.androloc.einkaufszettel;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.Button;
import android.content.Intent;
import android.database.Cursor;
import java.util.Hashtable;
import android.app.Dialog;
import android.app.AlertDialog;
import android.content.DialogInterface;


public class ArtikelEditActivity extends Activity {

	private Intent actIntent;
	private EditText textArtikel;
	private Button buttonOK;
	private Button buttonCancel;
	private Button buttonDelete;
	private long artikelID = 0;
	private EinkaufszettelDB db;
	private Cursor dbCursor;
	private static final int DIALOG_DELETE_ARTIKEL = 1;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
        setContentView(R.layout.artikel_edit);
    	
        actIntent = this.getIntent();
        
        //Editfeld Shopname
        textArtikel = (EditText) findViewById(R.id.textArtikel);

        //Fertig-Button
        buttonOK = (Button) findViewById(R.id.buttonOK);
        //Abbrechen-Button
        buttonCancel = (Button) findViewById(R.id.buttonCancel);
        //Löschen-Button
        buttonDelete = (Button) findViewById(R.id.buttonDelete);

        //Aktuellen Artikel aus der Datenbank auslesen
        artikelID = actIntent.getLongExtra(getString(R.string._ID),0);  //Index des Records
        if (artikelID != 0) {
            db = new EinkaufszettelDB();
            dbCursor=db.query(getString(R.string.table_artikel), getString(R.string._ID) + " = " + artikelID, null);
            dbCursor.moveToFirst();
            textArtikel.setText(dbCursor.getString(dbCursor.getColumnIndex(getString(R.string.field_artikel_bezeichnung))));
        }
        //Textänderung bei Artikel wenn leer dann Übernehmen sperren
        textArtikel.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				if (textArtikel.getText().toString().trim().length() == 0) {
					buttonOK.setEnabled(false);
				} else {
					buttonOK.setEnabled(true);
				}
			}
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,	int after) {
			}
			@Override
			public void afterTextChanged(Editable s) {
			}
		});
	
        //Übernehmen Button wird geklickt
        buttonOK.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				//Datenbank updaten
				Hashtable<String,String> fields = new Hashtable<String,String>();
				fields.put(getString(R.string.field_artikel_bezeichnung), textArtikel.getText().toString());
				db.Update(getString(R.string.table_artikel),artikelID,fields);
				setResult(RESULT_OK);
				ExitAct();
			}
		});
        
        //Abbrechen-Button wird betätigt
        buttonCancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				setResult(RESULT_CANCELED);
				ExitAct();
			}
		});

        //Löschen-Schaltfläche wird geklickt
        buttonDelete.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				//Sicherheitsabfrage
				showDialog(DIALOG_DELETE_ARTIKEL);
			}
		});
	}

	//Zurück zum Parent
	private void ExitAct() {
		try {
			dbCursor.close();
			db.close();
			finish();
		} catch (Exception e) {
		}
    }

	//muss überschrieben werden wird beim erstellen einmalig aufgerufen
	@Override
	protected Dialog onCreateDialog(int id) {
		
		if (id == DIALOG_DELETE_ARTIKEL) {
			//AlertDialog-Builder referenzieren
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			//AlertDialog-Builder konfigurieren
			//Hier besser eine String-ID für den Titel verwenden, Hinweis kommt ja öfters vor
			builder.setTitle(getString(R.string.delete_artikel));         										
			//Hier besser eine String-ID für die Nachricht verwenden.
			builder.setMessage(getString(R.string.artikel_delete_question1)+"\n"+getString(R.string.artikel_delete_question2));		
			//Dialog kann nicht mit der Back-Taste geschlossen werden.
			builder.setCancelable(false);
			//OK/Ja Button
			builder.setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() { 
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// Hier den Code einfügen der bei OK ausgeführt werden soll
					db.Delete(getString(R.string.table_artikel),artikelID);
					setResult(RESULT_OK);
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
