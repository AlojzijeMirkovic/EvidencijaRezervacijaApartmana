package com.unios.ferit.alojzijemirkovic.evidencijarezervacija;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class BazaPodataka extends SQLiteOpenHelper{
    public static String DATABASE_NAME = "evidencijarezervacije.db";
    public static String TABLE_REZERVACIJE = "rezervacije";
    public static String COLUMN_REZERVACIJA = "rezervacija";
    public static String COLUMN_APARTMAN = "apartman";
    public static String COLUMN_DATUMDOLASKA = "datumdolaska";
    public static String COLUMN_DATUMODLASKA = "datumodlaska";
    public static String COLUMN_IMEIPREZIME = "imeiprezime";
    public static String COLUMN_KONTAKT = "kontakt";
    public static String COLUMN_BROJOSOBA = "brojosoba";
    public static String COLUMN_NAPOMENA = "napomena";

    public BazaPodataka(Context context){
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE " + TABLE_REZERVACIJE + " (REZERVACIJA INTEGER PRIMARY KEY AUTOINCREMENT, APARTMAN TEXT, DATUMDOLASKA TEXT, DATUMODLASKA TEXT, IMEIPREZIME TEXT, KONTAKT TEXT, BROJOSOBA TEXT, NAPOMENA TEXT) ");

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_REZERVACIJE);
        onCreate(sqLiteDatabase);
    }

    public boolean dodajRezervaciju(String apartman, String datumdolaska, String datumodlaska, String imeiprezime, String kontakt, String brojosoba, String napomena){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_APARTMAN, apartman);
        values.put(COLUMN_DATUMDOLASKA, datumdolaska);
        values.put(COLUMN_DATUMODLASKA, datumodlaska);
        values.put(COLUMN_IMEIPREZIME, imeiprezime);
        values.put(COLUMN_KONTAKT, kontakt);
        values.put(COLUMN_BROJOSOBA, brojosoba);
        values.put(COLUMN_NAPOMENA, napomena);
        long dodaj = sqLiteDatabase.insert(TABLE_REZERVACIJE, null, values);
        if (dodaj == 1)
            return true;
        else
            return true;
    }

    public Cursor ispisPodataka(){
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        Cursor ispis = sqLiteDatabase.rawQuery("SELECT * FROM " + TABLE_REZERVACIJE, null);
        return ispis;
    }

    public boolean updatePodataka(String rezervacija, String apartman, String datumdolaska, String datumodlaska, String imeiprezime, String kontakt, String brojosoba, String napomena){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_REZERVACIJA, rezervacija);
        values.put(COLUMN_APARTMAN, apartman);
        values.put(COLUMN_DATUMDOLASKA, datumdolaska);
        values.put(COLUMN_DATUMODLASKA, datumodlaska);
        values.put(COLUMN_IMEIPREZIME, imeiprezime);
        values.put(COLUMN_KONTAKT, kontakt);
        values.put(COLUMN_BROJOSOBA, brojosoba);
        values.put(COLUMN_NAPOMENA, napomena);
        sqLiteDatabase.update(TABLE_REZERVACIJE,values, "REZERVACIJA = ?", new String[]{rezervacija});
        return true;
    }

    public Integer brisanjePodataka(String rezervacija){
        SQLiteDatabase sqLiteDatabase =  this.getWritableDatabase();
        return sqLiteDatabase.delete(TABLE_REZERVACIJE, "REZERVACIJA = ?", new String[]{rezervacija});
    }

}