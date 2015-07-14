package de.androloc.einkaufszettel;

import android.content.Context;
import android.database.Cursor;
import android.view.View;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class EinkaeufeAdapter extends CursorAdapter {

	private LayoutInflater inflater;
	private int idxDatum, idxShop;
	private EinkaufszettelDB db;
	private Cursor dbCursor;
	private static Context appContext;

	public EinkaeufeAdapter(Context context, Cursor c, boolean autoRequery) {
		super(context, c, autoRequery);
		// TODO Auto-generated constructor stub
		inflater = LayoutInflater.from(context);
		idxDatum = c.getColumnIndex(context.getString(R.string.field_einkauf_datum));
		idxShop = c.getColumnIndex(context.getString(R.string.field_einkauf_shop));
		appContext = context;
		db = new EinkaufszettelDB();
	}

	@Override
	public void bindView(View view, Context context, Cursor cursor) {
		// TODO Auto-generated method stub
		ImageView einkaufImage = (ImageView) view.findViewById(R.id.imageEinkauf); 
		einkaufImage.setImageResource(R.drawable.einkaufslisten);
		TextView einkaufDatum = (TextView) view.findViewById(R.id.textDatum);
		einkaufDatum.setText(cursor.getString(idxDatum));
		TextView shopOrt = (TextView) view.findViewById(R.id.textShopOrt);
		//TODO über die id Shop und Ort aus Tabelle shops auslesen
		String id = cursor.getString(idxShop);
		String shopUndOrt = "";
		dbCursor = db.query(appContext.getString(R.string.table_shops), "_id="+id, null);
		try {
			dbCursor.moveToFirst();
			shopUndOrt = dbCursor.getString(dbCursor.getColumnIndex(appContext.getString(R.string.field_shops_shopname)));
			String ort = dbCursor.getString(dbCursor.getColumnIndex(appContext.getString(R.string.field_shops_ort)));
			if (shopUndOrt.length()>0){
				if (ort.length()>0) {
					shopUndOrt += ", ";
				}
			}
			shopUndOrt += ort;
		} catch (Exception e) {
			// TODO: handle exception
		}
		shopOrt.setText(shopUndOrt);
		dbCursor.close();
	}

	@Override
	public View newView(Context context, Cursor cursor, ViewGroup parent) {
		// TODO Auto-generated method stub
		return inflater.inflate(R.layout.einkauf_listitem, null);
	}
}
