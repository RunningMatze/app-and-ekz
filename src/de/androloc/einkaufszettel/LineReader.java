package de.androloc.einkaufszettel;

import java.io.*;
import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;

//Datei Zeile für Zeile auslesen
public class LineReader {

	private final static String TAG = LineReader.class.getSimpleName();
	
	private InputStreamReader streamReader = null;
	private BufferedReader buffReader = null;
	private AssetManager assManager;
	
	//Context der Activity
	//====================
	private Context context;
	public void setContext(Context context) {
		this.context = context;
	}

	//Property-Filename
	//=================
	private String fileName = null;
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	//Konstruktor mit Dateiname
	//=========================
	public LineReader(String FileName){
		this.fileName=FileName;
	}

	//Datei öffnen
	//============
	public Boolean OpenFile() {
		if (this.fileName == null || this.context == null) {
			return false;
		} else {		
		
			try {
				assManager = context.getResources().getAssets();
				streamReader = new InputStreamReader(assManager.open(fileName, AssetManager.ACCESS_BUFFER));
				buffReader = new BufferedReader(streamReader);
				return true;
			} catch (Throwable t) {
				Log.e(TAG,context.getString(R.string.fileopenerror,this.fileName));
				return false;
			}
		}
	}

	//Nächste Zeile auslesen
	//======================
	public String ReadLine() {
		
		String nextLine = null;
		try {
			if ((nextLine = buffReader.readLine()) != null) {
				nextLine = nextLine.toString();
			} else {
				nextLine = null;
			}
		} catch (Throwable t) {
			Log.e(TAG,context.getString(R.string.filereaderror,this.fileName));
		}
		
		return nextLine;
	}
	
	//Dateiobjekte schließen
	//======================
	public void Close() {
		if (buffReader != null) {
			try {
				buffReader.close();
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
		if (streamReader != null) {
			try {
				
				streamReader.close();
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
	}
}
