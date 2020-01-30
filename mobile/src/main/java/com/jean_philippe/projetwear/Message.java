package com.jean_philippe.projetwear;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

@SuppressLint("Registered")
public class Message extends AppCompatActivity implements View.OnClickListener, Callback {
    private final OkHttpClient client = new OkHttpClient();
    private String student_id;
    private double gps_lat;
    private double gps_long;
    private String student_message;
    private LinearLayout linearLayout;
    private Button requestButton;
    private TextView resultsTextView;
    private Snackbar snackbar;

    public Message(String student_id, double gps_lat, double gps_long, String student_message) {
        this.student_id = student_id;
        this.gps_lat = gps_lat;
        this.gps_long = gps_long;
        this.student_message = student_message;
    }

    private StringBuilder encode(int student_id, float gps_lat, float gps_long, String student_message) throws
            UnsupportedEncodingException {
        StringBuilder sbParams = new StringBuilder();

        sbParams.append("student_id").append("=")
                .append(URLEncoder.encode(String.valueOf(student_id), "UTF-8")).append('&');
        sbParams.append("gps_lat").append("=")
                .append(URLEncoder.encode(String.valueOf(gps_lat), "UTF-8")).append('&');
        sbParams.append("gps_long").append("=")
                .append(URLEncoder.encode(String.valueOf(gps_long), "UTF-8")).append('&');
        sbParams.append("student_message").append("=")
                .append(URLEncoder.encode(student_message, "UTF-8")).append('&');

        return sbParams;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        makeView();
        setContentView(linearLayout);
        snackbar = Snackbar.make(linearLayout, "Requête en cours d'exécution",
                Snackbar.LENGTH_INDEFINITE);
    }

    @SuppressLint("SetTextI18n")
    private void makeView() {
        requestButton = new Button(this);
        requestButton.setText("Lancer une requête");
        requestButton.setOnClickListener(this);

        resultsTextView = new TextView(this);

        linearLayout = new LinearLayout(this);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        linearLayout.addView(requestButton);
        linearLayout.addView(resultsTextView);
    }

    @Override
    public void onClick(View view) {
        if (!isConnected()) {
            Snackbar.make(view, "Aucune connexion à internet.", Snackbar.LENGTH_LONG).show();
            return;
        }
        snackbar.show();

        String urlString = "https://hmin309-embedded-systems.herokuapp.com/message-exchange/messages/";
        Request request = new Request.Builder().url(urlString).build();

        client.newCall(request).enqueue(this);
    }

    private boolean isConnected() {
        ConnectivityManager connectivityManager =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        assert connectivityManager != null;
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }

    @Override
    public void onFailure(Call call, IOException e) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                resultsTextView.setText("Erreur");
                snackbar.dismiss();
            }
        });
    }

    @Override
    public void onResponse(Call call, Response response) throws IOException {
        if (!response.isSuccessful()) {
            throw new IOException(response.toString());
        }

        assert response.body() != null;
        final String body = response.body().string();

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                resultsTextView.setText(body);
                snackbar.dismiss();
            }
        });
    }
}