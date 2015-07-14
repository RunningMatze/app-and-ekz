package de.androloc.einkaufszettel;

import java.util.Hashtable;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;


public class ArtikelListenAdapter extends CursorAdapter {

	private Context listContext;
	private LayoutInflater inflater;
	private int idxID;
	private int idxAnzahl;
	private int idxArtikel;
	private int idxSortIndex;
	private long swapID;
	private EinkaufszettelDB db;
	
	public ArtikelListenAdapter(Context context, Cursor c, boolean autoRequery) {
		super(context, c, autoRequery);
		// TODO Auto-generated constructor stub
		inflater = LayoutInflater.from(context);
		idxID = c.getColumnIndex(context.getString(R.string._ID));
		idxAnzahl = c.getColumnIndex(context.getString(R.string.field_listen_anzahl));
		idxArtikel = c.getColumnIndex(context.getString(R.string.field_listen_artikel));
		idxSortIndex = c.getColumnIndex(context.getString(R.string.field_listen_sortindex));
	}

	@Override
	public void bindView(View view, Context context, Cursor cursor) {
		// TODO Auto-generated method stub
		TextView artikelName = (TextView) view.findViewById(R.id.textArtikel);
		artikelName.setText(cursor.getString(idxArtikel));
		Button artikelAnzahl = (Button) view.findViewById(R.id.buttonCount);
		ImageButton buttonDelete = (ImageButton) view.findViewById(R.id.buttonDelete);
		ImageButton buttonUp = (ImageButton) view.findViewById(R.id.buttonUp);
		ImageButton buttonDown = (ImageButton) view.findViewById(R.id.buttonDown);
		final Cursor listCursor=cursor;
		final int position = listCursor.getPosition();
		final long listID = cursor.getLong(idxID);
		final long curSortIndex = cursor.getLong(idxSortIndex);
		listContext = context;
		
		
		//Up und Down-Buttons ggf. ausblenden
		if (cursor.isFirst()) {
			buttonUp.setEnabled(false);
		} else {
			buttonUp.setEnabled(true);
		}
		if (cursor.isLast()) {
			buttonDown.setEnabled(false);
		} else {
			buttonDown.setEnabled(true);
		}
		
		//Anzahl auf Button anzeigen
		//--------------------------
		int selectedAnzahl= cursor.getInt(idxAnzahl);
		artikelAnzahl.setText(Integer.toString(selectedAnzahl)+"x");
			
		artikelAnzahl.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Button thisButton = (Button) v.findViewById(R.id.buttonCount);
				final Dialog dialog = new Dialog(listContext);
				dialog.setContentView(R.layout.dialog_anzahl);
				dialog.setTitle("Artikelanzahl");
				dialog.setCancelable(true);
				final Spinner spinnerAnzahl = (Spinner) dialog.findViewById(R.id.spinnerAnzahl);
				//Spinner mit items befüllen und Wert übergeben
				//--------------------------
				String[] spinData = new String[99];
				String spinItem; 
				for (int i=0; i<99; i++) {
					spinItem = Integer.toString(i+1) + "x";
					spinData[i] = spinItem;
				}
				ArrayAdapter<String> spinAdapter = new ArrayAdapter<String>(listContext,android.R.layout.simple_spinner_item,spinData);
				spinnerAnzahl.setAdapter(spinAdapter);
				spinnerAnzahl.setSelection(spinAdapter.getPosition(thisButton.getText().toString()));
				Button buttonOK = (Button) dialog.findViewById(R.id.buttonOK);
				buttonOK.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						// Datenbank aktualisieren und Dialog schließen
						db = new EinkaufszettelDB();
						// TODO Datenbank updaten
						int newAnzahl = spinnerAnzahl.getSelectedItemPosition()+1;
						Hashtable<String,String> fields = new Hashtable<String,String>();
						fields.put(listContext.getString(R.string.field_listen_anzahl), Integer.toString(newAnzahl));
						db.Update(listContext.getString(R.string.table_listen),listID,fields);
						db.close();
						dialog.cancel();
					}
				});
				dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
					
					@Override
					public void onCancel(DialogInterface dialog) {
						// TODO Auto-generated method stub
						listCursor.requery();
						ArtikelListenAdapter.this.notifyDataSetChanged();
					}
				});
				dialog.show();
			}	
		});
		
		//Spinner-Item für Anzahl wurde ausgewählt
		/*
		artikelAnzahl.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> liste, View view,int pos, long id) {
				// TODO Auto-generated method stub
				try {
					db = new EinkaufszettelDB();
					// TODO Datenbank updaten
					int newAnzahl = Integer.parseInt(liste.getItemAtPosition(pos).toString().replace("x", ""));
					Hashtable<String,String> fields = new Hashtable<String,String>();
					fields.put(listContext.getString(R.string.field_listen_anzahl), Integer.toString(newAnzahl));
					db.Update(listContext.getString(R.string.table_listen),listID,fields);
					db.close();
				} catch (Exception e) {
					// TODO: handle exception
				}
			}
			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
			}
		});
		*/
		
		
		buttonDelete.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				//AlertDialog-Builder referenzieren
				AlertDialog.Builder builder = new AlertDialog.Builder(listContext);
				//AlertDialog-Builder konfigurieren
				builder.setTitle(R.string.app_name);         										
				builder.setMessage("Artikel löschen?");		
				builder.setCancelable(false);											
				//OK/Ja Button
				builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() { 
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// Artikel löschen
						try {
							db = new EinkaufszettelDB();
							db.Delete(listContext.getString(R.string.table_listen), listID);
							db.close();
							listCursor.requery();
							ArtikelListenAdapter.this.notifyDataSetChanged();
						} catch (Exception e) {
							// TODO: handle exception
						}
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
		});

		buttonUp.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String fromIndex = Long.toString(curSortIndex);
				String toIndex = Long.toString(curSortIndex-1);
				try {
					db = new EinkaufszettelDB();
					//Zuerst den Swap-Artikel anpassen
					listCursor.moveToPosition(position-1);
					swapID = listCursor.getLong(listCursor.getColumnIndex(listContext.getString(R.string._ID)));
					Hashtable<String,String> fields = new Hashtable<String,String>();
					fields.put(listContext.getString(R.string.field_listen_sortindex), fromIndex);
					db.Update(listContext.getString(R.string.table_listen),swapID,fields);
					//Dann Sortindex des angewählten Artikels ändern
					fields.clear();
					fields.put(listContext.getString(R.string.field_listen_sortindex), toIndex);
					db.Update(listContext.getString(R.string.table_listen),listID,fields);
					db.close();
					listCursor.requery();
					ArtikelListenAdapter.this.notifyDataSetChanged();
				} catch (Exception e) {
					// TODO: handle exception
				}
			}
		});

		buttonDown.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String fromIndex = Long.toString(curSortIndex);
				String toIndex = Long.toString(curSortIndex+1);
				try {
					db = new EinkaufszettelDB();
					//Zuerst den Swap-Artikel anpassen
					listCursor.moveToPosition(position+1);
					swapID = listCursor.getLong(listCursor.getColumnIndex(listContext.getString(R.string._ID)));
					Hashtable<String,String> fields = new Hashtable<String,String>();
					fields.put(listContext.getString(R.string.field_listen_sortindex), fromIndex);
					db.Update(listContext.getString(R.string.table_listen),swapID,fields);
					//Dann Sortindex des angewählten Artikels ändern
					fields.clear();
					fields.put(listContext.getString(R.string.field_listen_sortindex), toIndex);
					db.Update(listContext.getString(R.string.table_listen),listID,fields);
					db.close();
					listCursor.requery();
					ArtikelListenAdapter.this.notifyDataSetChanged();
				} catch (Exception e) {
					// TODO: handle exception
				}
			}
		});

		buttonUp.setOnLongClickListener(new OnLongClickListener() {
			
			@Override
			public boolean onLongClick(View v) {
				try {
					db = new EinkaufszettelDB();
					//Zuerst den neuen Sort-Index ermitteln (niedrigster Index - 1)
					listCursor.moveToFirst();
					int indexSort = listCursor.getColumnIndex(listContext.getString(R.string.field_listen_sortindex));
					String toIndex = Long.toString(listCursor.getLong(indexSort) - 1);
					//Dann Sortindex des angewählten Artikels ändern
					Hashtable<String,String> fields = new Hashtable<String,String>();
					fields.put(listContext.getString(R.string.field_listen_sortindex), toIndex);
					db.Update(listContext.getString(R.string.table_listen),listID,fields);
					db.close();
					listCursor.requery();
					ArtikelListenAdapter.this.notifyDataSetChanged();
					return true;
				} catch (Exception e) {
					// TODO: handle exception
					return false;
				}
			}
		});

		buttonDown.setOnLongClickListener(new OnLongClickListener() {
			
			@Override
			public boolean onLongClick(View v) {
				// TODO Auto-generated method stub
				try {
					db = new EinkaufszettelDB();
					//Zuerst den neuen Sort-Index ermitteln (höchster Index + 1)
					listCursor.moveToLast();
					int indexSort = listCursor.getColumnIndex(listContext.getString(R.string.field_listen_sortindex));
					String toIndex = Long.toString(listCursor.getLong(indexSort) + 1);
					//Dann Sortindex des angewählten Artikels ändern
					Hashtable<String,String> fields = new Hashtable<String,String>();
					fields.put(listContext.getString(R.string.field_listen_sortindex), toIndex);
					db.Update(listContext.getString(R.string.table_listen),listID,fields);
					db.close();
					listCursor.requery();
					ArtikelListenAdapter.this.notifyDataSetChanged();
					return true;
				} catch (Exception e) {
					// TODO: handle exception
					return false;
				}
			}
		});
	}

	@Override
	public View newView(Context context, Cursor cursor, ViewGroup parent) {
		// TODO Auto-generated method stub
		return inflater.inflate(R.layout.einkauf_artikel_listitem, null);
	}
}
