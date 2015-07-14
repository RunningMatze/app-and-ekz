package de.androloc.einkaufszettel;

import android.content.Context;
import android.database.Cursor;
import android.view.View;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ArtikelAdapter extends CursorAdapter {

	private LayoutInflater inflater;
	private int idxArtikel;
	
	public ArtikelAdapter(Context context, Cursor c, boolean autoRequery) {
		super(context, c, autoRequery);
		// TODO Auto-generated constructor stub
		inflater = LayoutInflater.from(context);
		idxArtikel = c.getColumnIndex(context.getString(R.string.field_artikel_bezeichnung));
	}

	@Override
	public void bindView(View view, Context context, Cursor cursor) {
		// TODO Auto-generated method stub
		ImageView artikelImage = (ImageView) view.findViewById(R.id.imageArtikel); 
		artikelImage.setImageResource(R.drawable.artikel);
		TextView artikelName = (TextView) view.findViewById(R.id.textArtikel);
		artikelName.setText(cursor.getString(idxArtikel));
	}

	@Override
	public View newView(Context context, Cursor cursor, ViewGroup parent) {
		// TODO Auto-generated method stub
		return inflater.inflate(R.layout.artikel_listitem, null);
	}
}
