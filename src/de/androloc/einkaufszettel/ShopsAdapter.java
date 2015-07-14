package de.androloc.einkaufszettel;

import android.content.Context;
import android.database.Cursor;
import android.view.View;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ShopsAdapter extends CursorAdapter {

	private LayoutInflater inflater;
	private int idxShopName, idxShopOrt;
	
	public ShopsAdapter(Context context, Cursor c, boolean autoRequery) {
		super(context, c, autoRequery);
		// TODO Auto-generated constructor stub
		inflater = LayoutInflater.from(context);
		idxShopName = c.getColumnIndex(context.getString(R.string.field_shops_shopname));
		idxShopOrt = c.getColumnIndex(context.getString(R.string.field_shops_ort));
	}

	@Override
	public void bindView(View view, Context context, Cursor cursor) {
		// TODO Auto-generated method stub
		ImageView shopImage = (ImageView) view.findViewById(R.id.imageShop); 
		shopImage.setImageResource(R.drawable.shop);
		TextView shopName = (TextView) view.findViewById(R.id.textShopname);
		shopName.setText(cursor.getString(idxShopName));
		TextView shopOrt = (TextView) view.findViewById(R.id.textOrt);
		shopOrt.setText(cursor.getString(idxShopOrt));
	}

	@Override
	public View newView(Context context, Cursor cursor, ViewGroup parent) {
		// TODO Auto-generated method stub
		return inflater.inflate(R.layout.shop_listitem, null);
	}
}
