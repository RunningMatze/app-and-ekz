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

public class ShopEditActivity extends Activity {

	private Intent actIntent;
	private EditText textShopname;
	private EditText textOrt;
	private Button buttonOK;
	private Button buttonCancel;
	private Button buttonDelete;
	private long shopID = 0;
	private EinkaufszettelDB db;
	private Cursor dbCursor;
	private static final int DIALOG_DELETE_SHOP = 1;

	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
        setContentView(R.layout.shop_edit);
	
        actIntent = this.getIntent();
        
        //Editfeld Shopname
        textShopname = (EditText) findViewById(R.id.textShopname);
        //Editfeld Ort
        textOrt = (EditText) findViewById(R.id.textOrt);

        //Fertig-Button
        buttonOK = (Button) findViewById(R.id.buttonOK);
        //Abbrechen-Button
        buttonCancel = (Button) findViewById(R.id.buttonCancel);
        //L�schen-Button
        buttonDelete = (Button) findViewById(R.id.buttonDelete);

        //Aktuellen Shop aus der Datenbank auslesen
        shopID = actIntent.getLongExtra(getString(R.string._ID),0);  //Index des Records
        if (shopID != 0) {
            db = new EinkaufszettelDB();
            dbCursor=db.query(getString(R.string.table_shops), getString(R.string._ID) + " = " + shopID, null);
            dbCursor.moveToFirst();
            textShopname.setText(dbCursor.getString(dbCursor.getColumnIndex(getString(R.string.field_shops_shopname))));
            textOrt.setText(dbCursor.getString(dbCursor.getColumnIndex(getString(R.string.field_shops_ort))));
        }
        
        //Text�nderung bei Shopname, �bernehmen-Button freigeben
        textShopname.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				buttonOK.setEnabled(true);
			}
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,	int after) {
			}
			
			@Override
			public void afterTextChanged(Editable s) {
			}
		});
        
        //Text�nderung bei Ort, �bernehmen-Button freigeben
        textOrt.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				buttonOK.setEnabled(true);
			}
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,	int after) {
			}
			
			@Override
			public void afterTextChanged(Editable s) {
			}
		});
	
        //�bernehmen-Schaltfl�che wird geklickt
        buttonOK.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Datenbank updaten
				Hashtable<String,String> fields = new Hashtable<String,String>();
				fields.put(getString(R.string.field_shops_shopname), textShopname.getText().toString());
				fields.put(getString(R.string.field_shops_ort), textOrt.getText().toString());
				db.Update(getString(R.string.table_shops),shopID,fields);
				setResult(RESULT_OK);
				ExitAct();
			}
		});
        
        //Abbrechen-Button wird bet�tigt
        buttonCancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				setResult(RESULT_CANCELED);
				ExitAct();
			}
		});

        //L�schen-Schaltfl�che wird geklickt
        buttonDelete.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				//Sicherheitsabfrage
				showDialog(DIALOG_DELETE_SHOP);
			}
		});
	}

	//Zur�ck zum Parent
	private void ExitAct() {
		try {
			dbCursor.close();
			db.close();
			finish();
		} catch (Exception e) {
		}
    }

	//muss �berschrieben werden wird beim erstellen einmalig aufgerufen
	@Override
	protected Dialog onCreateDialog(int id) {
		
		if (id == DIALOG_DELETE_SHOP) {
			//AlertDialog-Builder referenzieren
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			//AlertDialog-Builder konfigurieren
			//Hier besser eine String-ID f�r den Titel verwenden, Hinweis kommt ja �fters vor
			builder.setTitle(getString(R.string.delete_shop));         										
			//Hier besser eine String-ID f�r die Nachricht verwenden.
			builder.setMessage(getString(R.string.shop_delete_question1)+"\n"+getString(R.string.shop_delete_question2));		
			//Dialog kann nicht mit der Back-Taste geschlossen werden.
			builder.setCancelable(false);
			//OK/Ja Button
			builder.setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() { 
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// Hier den Code einf�gen der bei OK ausgef�hrt werden soll
					db.Delete(getString(R.string.table_shops),shopID);
					setResult(RESULT_OK);
					ExitAct();
				}
			});
			//Abbrechen/nein Button
			builder.setNegativeButton(getString(R.string.no), new DialogInterface.OnClickListener() {		
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// Hier den Code einf�gen der bei Cancel ausgef�hrt werden soll
					dialog.cancel();
				}
			});
			//Dialog-Objekt zur�ckliefern
			return builder.create();
		}
		return super.onCreateDialog(id);
	}
}



