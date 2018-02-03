package com.unios.ferit.alojzijemirkovic.evidencijarezervacija;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

public class BrisanjeRezervacije extends AppCompatActivity {

    BazaPodataka bazaPodataka;
    public EditText editRezervacija;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_brisanje_rezervacije);

        bazaPodataka = new BazaPodataka(this);
        editRezervacija = findViewById(R.id.idRezervacije);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater mMenuInflater =  getMenuInflater();
        mMenuInflater.inflate(R.menu.menu_brisanje_rezervacije, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        if (item.getItemId() == R.id.delete){

            Integer obrisiRed = bazaPodataka.brisanjePodataka(editRezervacija.getText().toString());
            if (obrisiRed > 0) {
                Toast.makeText(BrisanjeRezervacije.this, "Rezervacija obrisana.", Toast.LENGTH_LONG).show();
                editRezervacija.setText("");
            }else
                Toast.makeText(BrisanjeRezervacije.this, "Rezervacija nije obrisana.", Toast.LENGTH_LONG).show();

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
            final AlertDialog.Builder builder = new AlertDialog.Builder(BrisanjeRezervacije.this);
            builder.setMessage("Nakon pritiska na znak \"+\" otvara se početni izbornik." + "\n\n" +
                    "Naginjanjem uređaja nazad pokreče se aktivnost za unos nove rezervacije, " +
                    "naginjanjem uređaja desno pokreće se aktivnost za uređivanje rezervacije, " +
                    "naginjanjem uređaja naprijed pokreće se aktivnost za brisanje rezervacije, " +
                    "a naginjanjem uređaja lijevo ispisuju se rezervacije." + "\n\n" +
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
        final AlertDialog.Builder builder = new AlertDialog.Builder(BrisanjeRezervacije.this);
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