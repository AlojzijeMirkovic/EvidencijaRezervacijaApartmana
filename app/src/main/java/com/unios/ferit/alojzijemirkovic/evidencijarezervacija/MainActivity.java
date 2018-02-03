package com.unios.ferit.alojzijemirkovic.evidencijarezervacija;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

public class MainActivity extends AppCompatActivity implements SensorEventListener{

    BazaPodataka bazaPodataka;

    private Sensor gyro;
    private SensorManager sensorManager;

    FloatingActionButton fab, aFab, dFab, eFab, pFab;
    Animation fabOpen, fabClose, rotateForward, rotateBackward;
    boolean isOpen = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bazaPodataka = new BazaPodataka(this);

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        gyro = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        sensorManager.registerListener(MainActivity.this, gyro, SensorManager.SENSOR_DELAY_NORMAL);

        fab = findViewById(R.id.fab);
        aFab = findViewById(R.id.fab_add);
        dFab = findViewById(R.id.fab_delete);
        eFab = findViewById(R.id.fab_edit);
        pFab = findViewById(R.id.fab_print);

        fabOpen = AnimationUtils.loadAnimation(this,R.anim.fab_open);
        fabClose = AnimationUtils.loadAnimation(this,R.anim.fab_close);
        rotateForward = AnimationUtils.loadAnimation(this, R.anim.rotate_forward);
        rotateBackward = AnimationUtils.loadAnimation(this, R.anim.rotate_backward);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                animateFab();
            }
        });
    }

    private void animateFab(){
        if (isOpen){
            fab.startAnimation(rotateBackward);
            aFab.startAnimation(fabClose);
            dFab.startAnimation(fabClose);
            eFab.startAnimation(fabClose);
            pFab.startAnimation(fabClose);
            aFab.setClickable(false);
            dFab.setClickable(false);
            eFab.setClickable(false);
            pFab.setClickable(false);
            isOpen=false;
        }else{
            fab.startAnimation(rotateForward);
            aFab.startAnimation(fabOpen);
            dFab.startAnimation(fabOpen);
            eFab.startAnimation(fabOpen);
            pFab.startAnimation(fabOpen);
            aFab.setClickable(true);
            dFab.setClickable(true);
            eFab.setClickable(true);
            pFab.setClickable(true);
            isOpen=true;
        }
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        if(isOpen == true){
            if(sensorEvent.values[1] < -2f){
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
                animateFab();
            }else if (sensorEvent.values[1] > 2f){
                startActivity(new Intent(MainActivity.this, UpdateRezervacije.class));
                animateFab();
            }else if (sensorEvent.values[0] < -1f){
                startActivity(new Intent(MainActivity.this, DodavanjeRezervacije.class));
                animateFab();
            }else if (sensorEvent.values[0] > 1f){
                startActivity(new Intent(MainActivity.this, BrisanjeRezervacije.class));
                animateFab();
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    public void poruka(String naslov, String poruka){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(naslov);
        builder.setMessage(poruka);
        builder.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater mMenuInflater = getMenuInflater();
        mMenuInflater.inflate(R.menu.menu_mainactivity, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        if(item.getItemId() == R.id.info){
            final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setTitle("Pomoć");
            builder.setMessage("Nakon pritiska na znak \"+\" otvara se početni izbornik." + "\n\n" +
                    "Naginjanjem uređaja nazad pokreće se aktivnost za unos nove rezervacije, " +
                    "naginjanjem uređaja desno pokreće se aktivnost za uređivanje rezervacije, " +
                    "naginjanjem uređaja naprijed pokreće se aktivnost za brisanje rezervacije, " +
                    "a naginjanjem uređaja lijevo ispisuju se rezervacije." + "\n\n" +
                    "Za brisanje rezervacije potrebno je upisati ID rezervacije koju želite obrisati." + "\n\n" +
                    "Za uređivanje rezrvacije potrebno je upisati ID rezervacije koju želite urediti, te ispuniti ostala polja s informacijama o rezervaciji.");
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

    @Override
    public void onBackPressed(){
        final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setMessage("Jeste li sigurni da želite izaći iz aplikacije?");
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