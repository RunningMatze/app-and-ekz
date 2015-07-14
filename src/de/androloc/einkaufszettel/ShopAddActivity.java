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

public class ShopAddActivity extends Activity {

	private Button buttonOK;
	private Button buttonCancel;
	private EditText textShopname;
	private EditText textOrt;
	private EinkaufszettelDB db;

	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
        setContentView(R.layout.shop_add);
	
        //Editfeld Shopname
        textShopname = (EditText) findViewById(R.id.textShopname);
        //Editfeld Ort
        textOrt = (EditText) findViewById(R.id.textOrt);

        //Fertig-Button
        buttonOK = (Button) findViewById(R.id.buttonOK);
        //Abbrechen-Button
        buttonCancel = (Button) findViewById(R.id.buttonCancel);

        //Abbrechen-Event
        buttonCancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				setResult(RESULT_CANCELED);
				ExitAct();
			}
		});
	
        //Textänderung im Feld Shopname
        textShopname.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// Prüfen ob Text eingegeben wurde
				CheckShopData();
			}
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,	int after) {
			}
			@Override
			public void afterTextChanged(Editable s) {
			}
		});
        textOrt.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// Prüfen ob Text eingegeben wurde
				CheckShopData();
			}
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}
			@Override
			public void afterTextChanged(Editable s) {
			}
		});
	
        //Übernehmen-Event
        buttonOK.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				db = new EinkaufszettelDB();
				// TODO Datenbank updaten
				Hashtable<String,String> fields = new Hashtable<String,String>();
				fields.put(getString(R.string.field_shops_shopname), textShopname.getText().toString());
				fields.put(getString(R.string.field_shops_ort), textOrt.getText().toString());
				db.Add(getString(R.string.table_shops),fields);
				setResult(RESULT_OK);
				ExitAct();
			}
		});
	}

	//Auf Text prüfen
	private void CheckShopData() {
		boolean hasText = false;
		if (textShopname.getText().toString().trim().length() > 0) {
			if (textOrt.getText().toString().trim().length() > 0) {
				hasText=true;
			}
		}
		buttonOK.setEnabled(hasText);
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
