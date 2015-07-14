package de.androloc.einkaufszettel;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.Enumeration;
import java.util.Hashtable;

public class EinkaufszettelDB extends SQLiteOpenHelper {

	private static Context appContext;
	private static SQLiteDatabase dbConnection;
	
	EinkaufszettelDB() {
		super(appContext,appContext.getResources().getString(R.string.db_name),null,Integer.parseInt(appContext.getResources().getString(R.string.db_version)));
	}

	EinkaufszettelDB(Context context) {
		super(context,context.getResources().getString(R.string.db_name),null,Integer.parseInt(context.getResources().getString(R.string.db_version)));
		appContext = context;
		dbConnection = this.getWritableDatabase();
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// Datenbank bei der ersten Initialisierung �ber sql-File erstellen 
		
        LineReader readLine = new LineReader("sql/einkaufszettel.sql");
        readLine.setContext(appContext);
        if (readLine.OpenFile()) {
	        String sql = null;
        	while ((sql= readLine.ReadLine()) != null) {
        		db.execSQL(sql);
        	}
	        readLine.Close();
        }
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		
		// Datenbank ugraden der sql-Dateiname lautet db_upgrade_versionsnummer.sql (versionsnummer = 1, 2,...)   
		int i;
		//�ber alle noch nicht durchgef�hrten Updates laufen
		for (i=oldVersion+1;i<=newVersion;i++) {
			LineReader readLine = new LineReader("sql/db_upgrade_"+Integer.toString(i)+".sql");
	        readLine.setContext(appContext);
	        if (readLine.OpenFile()) {
		        String sql = null;
	        	while ((sql= readLine.ReadLine()) != null) {
	        		db.execSQL(sql);
	        	}
		        readLine.Close();
	        }
		}
	}

	//Datenbankabfrage durchf�hren
	public Cursor query(String tableName, String queryString,String orderString) {
		//Cursor c = dbConnection.query(tableName, null, queryString, null, null, null, orderString);
		Cursor c = dbConnection.query(tableName, null, queryString, null, null, null, orderString);
		return c;
	}
	
	//Datenbankabfrage durchf�hren und ein Feld als Array zur�ckliefern
	public String[] getFieldArray(String tableName, String fieldName, String queryString,String orderString) {
		//Cursor c = dbConnection.query(tableName, null, queryString, null, null, null, orderString);
		String[] columns=new String[1];
		columns[0] = fieldName;
		Cursor c = dbConnection.query(tableName, columns, queryString, null, null, null, orderString);
		c.moveToFirst();
		if (c.getCount() > 0) {
			String[] array = new String[c.getCount()];
			int index = 0;
			do {
				array[index] = c.getString(c.getColumnIndex(fieldName));
				index++;
			} while (c.moveToNext());
			return array;
		} else {
			return null;
		}
	}

	//Datenbankabfrage durchf�hren und den Maximalwert einer Spalte zur�ckliefern
	public long getMax(String tableName, String maxField, long id) {
		//Cursor c = dbConnection.query(tableName, null, queryString, null, null, null, orderString);
		String sql = "SELECT MAX("+maxField+") AS "+maxField+" FROM " + tableName + " WHERE einkaufnr=" + Long.toString(id);
		Cursor c = dbConnection.rawQuery(sql, null);
		try {
			c.moveToFirst();
			long index = c.getLong(c.getColumnIndex(maxField));
			return index;
		} catch (Exception e) {
			// TODO: handle exception
			return 0;
		}
	}

	public void Update(String tableName, long id, Hashtable<String,String> data) {
		ContentValues values = new ContentValues();
		Enumeration<String> fields = data.keys();
		while (fields.hasMoreElements()) {
			String key = (String) fields.nextElement();
			values.put(key,data.get(key).toString());
		}
		dbConnection.update(tableName, values, appContext.getString(R.string._ID)+"=?",new String[] { Long.toString(id) });
	}
	public void Add(String tableName, Hashtable<String,String> data) {
		ContentValues values = new ContentValues();
		Enumeration<String> fields = data.keys();
		while (fields.hasMoreElements()) {
			String key = (String) fields.nextElement();
			values.put(key,data.get(key).toString());
		}
		dbConnection.insert(tableName, null, values);
	}

	public void Delete(String tableName, long id) {
		dbConnection.delete(tableName, appContext.getString(R.string._ID)+"=?",new String[] { Long.toString(id) });
		//Falls ein Shop gel�scht wird, dann m�ssen auch die entsprechenden Eink�ufe gel�scht werden
		if (tableName == appContext.getString(R.string.table_shops)) {
			tableName = appContext.getString(R.string.table_einkauf);
			dbConnection.delete(tableName, appContext.getString(R.string.field_einkauf_shop)+"=?",new String[] { Long.toString(id) });
		}
		if (tableName == appContext.getString(R.string.table_einkauf)) {
			tableName = appContext.getString(R.string.table_listen);
			dbConnection.delete(tableName, appContext.getString(R.string.field_listen_einkaufnr)+"=?",new String[] { Long.toString(id) });
		}
	}

	public void ExecuteSQL(String sql) {
		//Einen sql-Befehl ausl�sen
		try {
			dbConnection.execSQL(sql);
		} catch (Exception e) {
		}
	}
}
