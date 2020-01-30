package com.jean_philippe.projetwear;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.material.snackbar.Snackbar;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Ferdousur Rahman Sarker on 3/17/2018.
 */

public class AddTask extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, AdapterView.OnItemSelectedListener {
    public static final String PROFILE_KEY = "profile";
    public static final String CODE = "code";
    public static final String CATEGORY_KEY = "category";
    public static final String REQEST_CODE_FILE = "requestfile";
    private static final String PERMS = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final int RC_GPS_PERMS = 100;
    private static final String TAG = "MapActivity";
    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String COURSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1234;
    static String oldNameFinal = "";
    static String oldName;
    TaskDBHelper mydb;
    DatePickerDialog dpd;
    TimePickerDialog tpd;
    int startYear = 0, startMonth = 0, startDay = 0;
    String categoryFinal;
    String nameFinal;
    String descriptionFinal;
    Intent intent;
    Boolean isUpdate;
    String id;
    EditText time;
    private Boolean mLocationPermissionsGranted = false;
    private RelativeLayout containerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.task_add_new);

        Spinner spinner = findViewById(R.id.spinnerCategory);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.Categorys, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

        mydb = new TaskDBHelper(getApplicationContext());
        intent = getIntent();
        isUpdate = intent.getBooleanExtra("isUpdate", false);
    }

    @SuppressLint("SetTextI18n")
    public void init_update() {
        id = intent.getStringExtra("id");
        TextView toolbar_task_add_title = (TextView) findViewById(R.id.toolbar_task_add_title);
        Spinner task_category = (Spinner) findViewById(R.id.spinnerCategory);
        EditText task_description = (EditText) findViewById(R.id.task_description);
        String itemText = (String) task_category.getSelectedItem();

        toolbar_task_add_title.setText("Modifié");
        Cursor task = mydb.getDataSpecific(id);
        if (task != null) {
            task.moveToFirst();
            int index = 1;

            for (int i = 0; i < task_category.getCount(); i++) {
                if (task_category.getItemAtPosition(i).toString().equalsIgnoreCase(task.getString(1))) {
                    index = i;
                }
            }
            task_category.setSelection(index);
            oldNameFinal = task.getString(2);
            task_description.setText(task.getString(4));
        }
    }

    public void closeAddTask(View v) {
        finish();
    }

    public void doneAddTask(final View v) {
        int errorStep = 0;
        final EditText student_id = (EditText) findViewById(R.id.student_id);
        Spinner task_category = (Spinner) findViewById(R.id.spinnerCategory);
        final EditText task_message = (EditText) findViewById(R.id.task_description);

        categoryFinal = task_category.getSelectedItem().toString();
        descriptionFinal = task_message.getText().toString();

        /* Checking */
        if (errorStep == 0) {
            if (isUpdate) {
                deleteAlarm(oldNameFinal);
                onSetAlarm(nameFinal, categoryFinal);
                mydb.updateContact(id, categoryFinal, nameFinal, descriptionFinal);
                Toast.makeText(getApplicationContext(), "Tâche modifiée.", Toast.LENGTH_SHORT).show();
            } else {
                mydb.insertContact(categoryFinal, nameFinal, descriptionFinal);
                onSetAlarm(nameFinal, categoryFinal);
                Toast.makeText(getApplicationContext(), "Tâche ajoutée.", Toast.LENGTH_SHORT).show();

                LocationManager locationManager = null;
                String fournisseur = "";

                if (locationManager == null) {
                    locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                    Criteria criteres = new Criteria();

                    // la précision  : (ACCURACY_FINE pour une haute précision ou ACCURACY_COARSE pour une moins bonne précision)
                    criteres.setAccuracy(Criteria.ACCURACY_FINE);

                    // l'altitude
                    criteres.setAltitudeRequired(true);

                    // la direction
                    criteres.setBearingRequired(true);

                    // la vitesse
                    criteres.setSpeedRequired(true);

                    // la consommation d'énergie demandée
                    criteres.setCostAllowed(true);
                    criteres.setPowerRequirement(Criteria.POWER_HIGH);

                    fournisseur = locationManager.getBestProvider(criteres, true);
                    Log.d("GPS", "fournisseur : " + fournisseur);
                }

                if (fournisseur != null) {
                    // dernière position connue
                        /*if (!EasyPermissions.hasPermissions(this, PERMS)) {
                            EasyPermissions.requestPermissions(this, getString(R.string.popup_title_permission_files_access), RC_GPS_PERMS, PERMS);
                            return;
                        }*/

                    /*if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        if (shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION)) {
                            explain();
                        } else {
                            askForPermission();
                        }
                    }*/
                    if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        getLocationPermission();
                    }
                    Location localisation = locationManager.getLastKnownLocation(fournisseur);
                    Log.d("GPS", "localisation : " + localisation.toString());
                    String coordonnees = String.format("Latitude : %f - Longitude : %f\n", localisation.getLatitude(), localisation.getLongitude());
                    Log.d("GPS", "coordonnees : " + coordonnees);
                    String autres = String.format("Vitesse : %f - Altitude : %f - Cap : %f\n", localisation.getSpeed(), localisation.getAltitude(), localisation.getBearing());
                    Log.d("GPS", autres);
                    //String timestamp = String.format("Timestamp : %d\n", localisation.getTime());
                    //Log.d("GPS", "timestamp : " + timestamp);
                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                    Date date = new Date(localisation.getTime());
                    Log.d("GPS", sdf.format(date));
                }

                final String finalFournisseur = fournisseur;
                LocationListener ecouteurGPS = new LocationListener() {
                    @Override
                    public void onLocationChanged(Location localisation) {
                        Toast.makeText(getApplicationContext(), finalFournisseur + " localisation", Toast.LENGTH_SHORT).show();

                        Log.d("GPS", "localisation : " + localisation.toString());
                        String coordonnees = String.format("Latitude : %f - Longitude : %f\n", localisation.getLatitude(), localisation.getLongitude());
                        Log.d("GPS", coordonnees);
                        String autres = String.format("Vitesse : %f - Altitude : %f - Cap : %f\n", localisation.getSpeed(), localisation.getAltitude(), localisation.getBearing());
                        Log.d("GPS", autres);
                        //String timestamp = String.format("Timestamp : %d\n", localisation.getTime());
                        //Log.d("GPS", "timestamp : " + timestamp);
                        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                        Date date = new Date(localisation.getTime());
                        Log.d("GPS", sdf.format(date));

                        new Message(student_id.toString(), localisation.getLatitude(), localisation.getLongitude(), task_message.toString()).onClick(v);
                    }

                    @Override
                    public void onProviderDisabled(String fournisseur) {
                        Toast.makeText(getApplicationContext(), fournisseur + " désactivé !", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onProviderEnabled(String fournisseur) {
                        Toast.makeText(getApplicationContext(), fournisseur + " activé !", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onStatusChanged(String fournisseur, int status, Bundle extras) {
                        switch (status) {
                            case LocationProvider.AVAILABLE:
                                Toast.makeText(getApplicationContext(), fournisseur + " état disponible", Toast.LENGTH_SHORT).show();
                                break;
                            case LocationProvider.OUT_OF_SERVICE:
                                Toast.makeText(getApplicationContext(), fournisseur + " état indisponible", Toast.LENGTH_SHORT).show();
                                break;
                            case LocationProvider.TEMPORARILY_UNAVAILABLE:
                                Toast.makeText(getApplicationContext(), fournisseur + " état temporairement indisponible", Toast.LENGTH_SHORT).show();
                                break;
                            default:
                                Toast.makeText(getApplicationContext(), fournisseur + " état : " + status, Toast.LENGTH_SHORT).show();
                        }
                    }
                };
            }
            finish();
        } else {
            Toast.makeText(getApplicationContext(), "Réessayer s'il vous plait", Toast.LENGTH_SHORT).show();
        }
    }

   /* private void displayOptions() {
        Snackbar.make(containerView, "Vous avez désactivé la permission", Snackbar.LENGTH_LONG).setAction("Paramètres", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                final Uri uri = Uri.fromParts("package", getActivity().getPackageName(), null);
                intent.setData(uri);
                startActivity(intent);
            }
        }).show();
    }*/

    private void askForPermission() {
        requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 2);
    }

    private void getLocationPermission() {
        Log.d(TAG, "getLocationPermission: getting location permissions");
        String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION};

        if (ContextCompat.checkSelfPermission(this.getApplicationContext(), FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            if (ContextCompat.checkSelfPermission(this.getApplicationContext(), COURSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                mLocationPermissionsGranted = true;
                // initMap();
            } else {
                ActivityCompat.requestPermissions(this, permissions, LOCATION_PERMISSION_REQUEST_CODE);
            }
        } else {
            ActivityCompat.requestPermissions(this, permissions, LOCATION_PERMISSION_REQUEST_CODE);
        }
    }

    private void explain() {
        Snackbar.make(containerView, "Cette permission est nécessaire pour les cordonné GPS", Snackbar.LENGTH_LONG).setAction("Activer", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                askForPermission();
            }
        }).show();
    }

    @Override
    public void onResume() {
        super.onResume();
        dpd = (DatePickerDialog) getFragmentManager().findFragmentByTag("startDatepickerdialog");
        if (dpd != null) dpd.setOnDateSetListener(this);
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        startYear = year;
        startMonth = monthOfYear;
        startDay = dayOfMonth;
        int monthAddOne = startMonth + 1;
        String date = (startDay < 10 ? "0" + startDay : "" + startDay) + "/" +
                (monthAddOne < 10 ? "0" + monthAddOne : "" + monthAddOne) + "/" +
                startYear;
    }

    public void showStartDatePicker(View v) {
        dpd = DatePickerDialog.newInstance(AddTask.this, startYear, startMonth, startDay);
        dpd.setOnDateSetListener(this);
        dpd.show(getFragmentManager(), "startDatepickerdialog");
    }

    public void onSetAlarm(String nameFinal, String categoryFinal) {
        boolean sportCategory = false;
        if (categoryFinal.toLowerCase().contains("des sports"))
            sportCategory = true;

        Intent i = new Intent(this, RecepteurAlarme.class);
        i.putExtra(PROFILE_KEY, nameFinal);
        i.putExtra(CATEGORY_KEY, sportCategory);
        PendingIntent pi = PendingIntent.getBroadcast(this, saveInteger(nameFinal), i, 0);
    }

    public int saveInteger(String nameFinal) {
        SharedPreferences pref = this.getSharedPreferences(REQEST_CODE_FILE, Context.MODE_PRIVATE);
        int num = pref.getInt(CODE, 0);

        if (num > 1147483647)
            num = 0;
        num++;

        SharedPreferences.Editor editor = pref.edit();
        editor.putInt(CODE, num);
        editor.putInt(nameFinal, num);
        editor.apply();

        return num;
    }

    public void deleteAlarm(String oldNameFinal) {
        Intent i = new Intent(this, RecepteurAlarme.class);
        i.putExtra(PROFILE_KEY, oldNameFinal);
        PendingIntent pi = PendingIntent.getBroadcast(this, getIntegerName(oldNameFinal), i, 0);
        AlarmManager alarmManager = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
        assert alarmManager != null;
        alarmManager.cancel(pi);
    }

    public int getIntegerName(String oldNameFinal) {
        SharedPreferences pref = this.getSharedPreferences(REQEST_CODE_FILE, Context.MODE_PRIVATE);

        return pref.getInt(oldNameFinal, 0);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }
}