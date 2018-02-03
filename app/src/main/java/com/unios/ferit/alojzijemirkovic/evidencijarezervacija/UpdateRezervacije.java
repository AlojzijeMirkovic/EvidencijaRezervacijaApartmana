package com.unios.ferit.alojzijemirkovic.evidencijarezervacija;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

public class UpdateRezervacije extends AppCompatActivity {

    BazaPodataka bazaPodataka;

    public EditText editApartman, editRezervacija, editBrojOsoba, editImeIprezime, editNapomena, editKontakt;
    public TextView datumDolaska, datumOdlaska, datumDolaskaP, datumOdlaskaP;


    private DatePickerDialog.OnDateSetListener pDatumListener;
    private DatePickerDialog.OnDateSetListener zDatumListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_rezervacije);

        bazaPodataka = new BazaPodataka(this);

        datumOdlaska = findViewById(R.id.datumOdlaska);
        datumDolaska = findViewById(R.id.datumDolaska);
        datumDolaskaP = findViewById(R.id.datumDolaskaP);
        datumOdlaskaP = findViewById(R.id.datumOdlaskaP);

        editApartman = findViewById(R.id.edit_apartman);
        editRezervacija = findViewById(R.id.idRezervacije);
        editImeIprezime = findViewById(R.id.et_imeiprezime);
        editKontakt = findViewById(R.id.et_kontakt);
        editBrojOsoba = findViewById(R.id.et_brojosoba);
        editNapomena = findViewById(R.id.et_napomena);

        datumDolaska.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar kalendar = Calendar.getInstance();
                int godina = kalendar.get(Calendar.YEAR);
                int mjesec = kalendar.get(Calendar.MONTH);
                int dan = kalendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(UpdateRezervacije.this, android.R.style.Theme_Holo_Light_Dialog_MinWidth, pDatumListener, godina, mjesec, dan);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        datumOdlaska.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar kalendar = Calendar.getInstance();
                int godina = kalendar.get(Calendar.YEAR);
                int mjesec = kalendar.get(Calendar.MONTH);
                int dan = kalendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(UpdateRezervacije.this, android.R.style.Theme_Holo_Light_Dialog_MinWidth,zDatumListener, godina, mjesec, dan);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        pDatumListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int godina, int mjesec, int dan) {
                mjesec = mjesec + 1;
                String dDolaska = dan + "." + mjesec + "." + godina + ".";
                datumDolaskaP.setText(dDolaska);
            }
        };

        zDatumListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int godina, int mjesec, int dan) {
                mjesec = mjesec + 1;
                String dOdlaska = dan + "." + mjesec + "." + godina + ".";
                datumOdlaskaP.setText(dOdlaska);
            }
        };

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater mMenuInflater =  getMenuInflater();
        mMenuInflater.inflate(R.menu.menu_update_rezervacije, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        if (item.getItemId() == R.id.edit){
            if ((editRezervacija.getText().length() != 0) && (editApartman.getText().length() != 0) && (editImeIprezime.getText().length() != 0) && (editKontakt.getText().length() != 0) && (editBrojOsoba.getText().length() != 0) && (datumDolaskaP.getText().length() != 0) && (datumOdlaskaP.getText().length() != 0)){
                boolean updated = bazaPodataka.updatePodataka(editRezervacija.getText().toString(), editApartman.getText().toString(), datumDolaskaP.getText().toString(), datumOdlaskaP.getText().toString(), editImeIprezime.getText().toString(), editKontakt.getText().toString(), editBrojOsoba.getText().toString(), editNapomena.getText().toString());
                if (updated == true) {
                    Toast.makeText(UpdateRezervacije.this, "Rezervacija ažurirana.", Toast.LENGTH_LONG).show();
                    editRezervacija.setText("");
                    editApartman.setText("");
                    datumDolaskaP.setText("");
                    datumOdlaskaP.setText("");
                    editImeIprezime.setText("");
                    editKontakt.setText("");
                    editBrojOsoba.setText("");
                    editNapomena.setText("");
                }else
                    Toast.makeText(UpdateRezervacije.this, "Ažuriranje nije uspjelo, ispunite polja i pokušajte ponvno.", Toast.LENGTH_LONG).show();
            }
        }else if (item.getItemId() == R.id.print){
            Cursor ispis = bazaPodataka.ispisPodataka();
            if (ispis.getCount() == 0)
                poruka("Rezervacije:", "Nema rezervacija.");
            else {
                StringBuffer buffer = new StringBuffer();
                while (ispis.moveToNext()) {
                    buffer.append("Rezervacija: " + ispis.getString(0) + "\n");
                    buffer.append("Apartman: " + ispis.getString(1) + "\n");
                    buffer.append("Datum dolaska: " + ispis.getString(2) + "\n");
                    buffer.append("Datum odlaska: " + ispis.getString(3) + "\n");
                    buffer.append("Ime i prezime: " + ispis.getString(4) + "\n");
                    buffer.append("Kontakt: " + ispis.getString(5) + "\n");
                    buffer.append("Broj osoba: " + ispis.getString(6) + "\n");
                    buffer.append("Napomena: " + ispis.getString(7) + "\n\n");
                }
                poruka("Rezervacije:", buffer.toString());
            }
        }else if (item.getItemId() == R.id.info){
            final AlertDialog.Builder builder = new AlertDialog.Builder(UpdateRezervacije.this);
            builder.setTitle("Pomoć");
            builder.setMessage("Nakon pritiska na znak \"+\" otvara se početni izbornik." + "\n\n" +
                    "Naginjanjem uređaja nazad pokreće se aktivnost za unos nove rezervacije, " +
                    "naginjanjem uređaja desno pokreće se aktivnost za uređivanje rezervacije, " +
                    "naginjanjem uređaja naprijed pokreće se aktivnost za brisanje rezervacije, " +
                    "a pomicanjem uređaja u lijevo ispisuju se rezervacije." + "\n\n" +
                    "Za brisanje rezervacije potrebno je upisati ID rezervacije koju želite obrisati." + "\n\n" +
                    "Za uređivanje rezervacije potrebno je upisati ID rezervacije koju želite urediti, te ispuniti ostala polja s informacijama o rezervaciji.");
            builder.setCancelable(true);
            builder.setPositiveButton("U redu", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.cancel();
                }
            });
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        }
        return super.onOptionsItemSelected(item);
    }

    public void poruka(String naslov, String poruka){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(naslov);
        builder.setMessage(poruka);
        builder.show();
    }

    @Override
    public void onBackPressed(){
        final AlertDialog.Builder builder = new AlertDialog.Builder(UpdateRezervacije.this);
        builder.setMessage("Jeste li sigurni da želite otići na početni izbornik?");
        builder.setCancelable(true);
        builder.setNegativeButton("Ne", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });
        builder.setPositiveButton("Da", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                finish();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}