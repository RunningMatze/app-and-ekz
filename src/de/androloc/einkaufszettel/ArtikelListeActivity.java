package de.androloc.einkaufszettel;

import java.util.Hashtable;
import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.AutoCompleteTextView;
import android.content.DialogInterface;
import android.database.Cursor;

public class ArtikelListeActivity extends Activity {

	private EinkaufszettelDB db;
	private Cursor dbCursorA;
	private Cursor dbCursorL;
	private Cursor dbCursorNew;
	private ArrayAdapter<String> artikelArray;
	private Spinner artikelAuswahl;
	private ArtikelAdapter artikelAuswahlAdapter;
	private ListView artikelListe;
	private ArtikelListenAdapter artikelListenAdapter;
	private long einkaufID;
	private Button addArtikel; 
	private Button addTextArtikel; 
	private AutoCompleteTextView textArtikel;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.artikel_einkauf);
	
	    //ID des Einkaufs in der DB
	    einkaufID = this.getIntent().getLongExtra(getString(R.string._ID), 0);
	    
	    //Button + zum Hinzufügen aus dem Spinner
	    addArtikel = (Button) findViewById(R.id.buttonAddArtikel);

	    //Button + zum Hinzufügen aus dem AutoText
	    addTextArtikel = (Button) findViewById(R.id.buttonAddText);

	    //Datenbank initialisieren
        db = new EinkaufszettelDB();

        // ** Spinner mit Artikeln **
	    artikelAuswahl=(Spinner) findViewById(R.id.spinnerArtikel);
        //SQL-Abfrage durchführen Cursor holen
        //====================================
        dbCursorA = db.query(getString(R.string.table_artikel), null, getString(R.string.field_artikel_bezeichnung)+" ASC");
        startManagingCursor(dbCursorA);
        //CursorAdapter zum befüllen des Spinners
        //=======================================
        artikelAuswahlAdapter = new ArtikelAdapter(artikelAuswahl.getContext(),dbCursorA,true);
        artikelAuswahl.setAdapter(artikelAuswahlAdapter);
        if (artikelAuswahl.getCount() == 0){
        	addArtikel.setEnabled(false);
        }
        
        // ** Datenbank-Cursor für Autocomplete Textfeld **
        //=================================================
	    textArtikel = (AutoCompleteTextView) findViewById(R.id.autoTextArtikel);
        //SQL-Abfrage durchführen und ArrayAdapter erstellen
        //==================================================
        String[] artikel = db.getFieldArray(getString(R.string.table_artikel), getString(R.string.field_artikel_bezeichnung), null, getString(R.string.field_artikel_bezeichnung)+" ASC");
        if (artikel != null) {
        	artikelArray = new ArrayAdapter<String>(this,android.R.layout.simple_dropdown_item_1line,artikel);
        	textArtikel.setAdapter(artikelArray);
        }
        
        
        // ** Artikel-Liste des Einkaufs **
        //====================================
	    artikelListe=(ListView) findViewById(R.id.listeArtikel);
        //SQL-Abfrage durchführen Cursor holen
        //====================================
        dbCursorL = db.query(getString(R.string.table_listen),getString(R.string.field_listen_einkaufnr)+"="+einkaufID , getString(R.string.field_listen_sortindex)+" ASC");
        startManagingCursor(dbCursorL);
        //CursorAdapter zum befüllen des Spinners
        //=======================================
        artikelListenAdapter = new ArtikelListenAdapter(artikelListe.getContext(),dbCursorL,true);
        artikelListe.setAdapter(artikelListenAdapter);

        //Button zum hinzufügen eines Artikels aus dem Spinner wurde geklickt
        addArtikel.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				//Artikel aus Spinner in Datenbank einstellen
				long artID = artikelAuswahl.getSelectedItemId();
				if (artID > 0) {
					try {
						Cursor curArt = db.query(getString(R.string.table_artikel), getString(R.string._ID)+"="+artID, null);
						curArt.moveToFirst();
						String artText = curArt.getString(curArt.getColumnIndex(getString(R.string.field_artikel_bezeichnung)));
						addArtikelToList(artText);
					} catch (Exception e) {
					}
				}
			}
		});
        artikelListe.requestFocus();
        
        //Button zum hinzufügen eines Artikels aus dem Autotext wurde geklickt
        addTextArtikel.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String artText = textArtikel.getText().toString();
				addArtikelToList(artText);
				textArtikel.setText("");
				addToArtikelliste(artText);
			}
		});

        //Text im Textfeld für Artikel wird geändert
        textArtikel.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				if (textArtikel.getText().toString().equals("")) {
					addTextArtikel.setEnabled(false);
				} else {
					addTextArtikel.setEnabled(true);
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

	//Hinzufügen eines Artikels zur Liste
	//===================================
	private void addArtikelToList(String artText) {
		try {
			Hashtable<String,String> fields = new Hashtable<String,String>();
			fields.put(getString(R.string.field_listen_einkaufnr), Long.toString(einkaufID));
			fields.put(getString(R.string.field_listen_anzahl), "1");
			fields.put(getString(R.string.field_listen_artikel), artText);
			fields.put(getString(R.string.field_listen_erledigt), "0");
			//Nächst höheren Sortierindex ermitteln
			long sortIndex = db.getMax(getString(R.string.table_listen), getString(R.string.field_listen_sortindex), einkaufID);
			sortIndex+=1;
			fields.put(getString(R.string.field_listen_sortindex), Long.toString(sortIndex));
			db.Add(getString(R.string.table_listen),fields);
			dbCursorL.requery();
			artikelListenAdapter.notifyDataSetChanged();
			artikelListe.setSelection(artikelListe.getCount());
		} catch (Exception e) {
		}
	}

	//Hinzufügen eines neuen Artikels zur Artikeldatenbank
	//falls dieser noch nicht in der Artikeldatenbank existiert
	//=========================================================
	private void addToArtikelliste(String artText) {
        final String artikelText = artText;
		dbCursorNew=db.query(getString(R.string.table_artikel), getString(R.string.field_artikel_bezeichnung) + " = '" + artText + "'", null);
        startManagingCursor(dbCursorNew);
        if (dbCursorNew.getCount() == 0) {
        	//der eingegebene Artikel existiert noch nicht in der Artikeldatenbank
        	//Dialog mit Abfrage zum automatischen hinzufügen
        	//====================================================================
			//AlertDialog-Builder referenzieren
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			//AlertDialog-Builder konfigurieren
			builder.setTitle(R.string.app_name);         										
			builder.setMessage(R.string.artikel_autoadd);		
			builder.setCancelable(true);											
			//OK/Ja Button
			builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() { 
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// Hier den Code einfügen der bei OK ausgeführt werden soll
					//Artikel in Datenbank einstellen
					try {
						Hashtable<String,String> fields = new Hashtable<String,String>();
						fields.put(getString(R.string.field_artikel_bezeichnung), artikelText);
						db.Add(getString(R.string.table_artikel),fields);
					} catch (Exception e) {
						// TODO: handle exception
					}
					dialog.cancel();
				}
			});
			//Abbrechen/nein Button
			builder.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {		
				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.cancel();
				}
			});
			builder.show();
        }
	    try {
	        dbCursorNew.close();
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
}
