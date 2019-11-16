package com.example.proyect;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.loopj.android.http.AsyncHttpClient;

public class CorosAleActivity extends AppCompatActivity {
    private EditText ettituloca, etautorca, etletraca;
    private Button btnRegistrarca;
    private ListView lvdatosca;
    private AsyncHttpClient clienteca = new AsyncHttpClient();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coros_ale);
    }
}
