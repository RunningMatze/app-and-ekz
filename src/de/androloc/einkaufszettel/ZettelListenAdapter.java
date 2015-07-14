package de.androloc.einkaufszettel;


import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import android.widget.CursorAdapter;
import android.widget.TextView;

public class ZettelListenAdapter extends CursorAdapter {

	private LayoutInflater inflater;
	private int idxAnzahl;
	private int idxArtikel;
	private int idxErledigt;
	
	public ZettelListenAdapter(Context context, Cursor c, boolean autoRequery) {
		super(context, c, autoRequery);
		// TODO Auto-generated constructor stub
		inflater = LayoutInflater.from(context);
		idxAnzahl = c.getColumnIndex(context.getString(R.string.field_listen_anzahl));
		idxArtikel = c.getColumnIndex(context.getString(R.string.field_listen_artikel));
		idxErledigt = c.getColumnIndex(context.getString(R.string.field_listen_erledigt));
	}

	@Override
	public void bindView(View view, Context context, Cursor cursor) {
		// TODO Auto-generated method stub
		TextView artikelAnzahl = (TextView) view.findViewById(R.id.textAnzahl);
		String anzAnzeige = cursor.getString(idxAnzahl)+"x";
		artikelAnzahl.setText(anzAnzeige);
		TextView artikelName = (TextView) view.findViewById(R.id.textArtikel);
		artikelName.setText(cursor.getString(idxArtikel));
		String erledigt =cursor.getString(idxErledigt);
		if (erledigt.equals("1")) {
			artikelName.setPaintFlags(artikelName.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
			artikelName.setPaintFlags(artikelName.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
			artikelName.setTextColor(Color.parseColor("#808080"));
			artikelAnzahl.setPaintFlags(artikelName.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
			artikelAnzahl.setPaintFlags(artikelName.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
			artikelAnzahl.setTextColor(Color.parseColor("#808080"));
		} else {
			artikelName.setPaintFlags(artikelName.getPaintFlags() & (~ Paint.STRIKE_THRU_TEXT_FLAG));
			artikelName.setTextColor(Color.parseColor("#000080"));
			artikelAnzahl.setPaintFlags(artikelName.getPaintFlags() & (~ Paint.STRIKE_THRU_TEXT_FLAG));
			artikelAnzahl.setTextColor(Color.parseColor("#000080"));
		}
	}

	@Override
	public View newView(Context context, Cursor cursor, ViewGroup parent) {
		// TODO Auto-generated method stub
		return inflater.inflate(R.layout.liste_listitem, null);
	}
}
