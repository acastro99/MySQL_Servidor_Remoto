package com.example.proyect;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONArray;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class CorosAleActivity extends AppCompatActivity {
    private EditText ettitulocal, etautorcal, etletracal;
    private Button btnRegistrarcal;
    private ListView lvdatoscal;
    private AsyncHttpClient clientecal = new AsyncHttpClient();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coros_ale);

        ettitulocal = findViewById(R.id.ettituloal);
        etautorcal = findViewById(R.id.etautoral);
        etletracal = findViewById(R.id.etletraal);

        btnRegistrarcal = findViewById(R.id.btnRegistraral);

        lvdatoscal = findViewById(R.id.lvDatosal);

        clientecal = new AsyncHttpClient();

        almacenarCoros();

        obtenerCoros();
    }

    private void almacenarCoros() {
        btnRegistrarcal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ettitulocal.getText().toString().length()== 0 )  {
                    ettitulocal.setError("Campo Obligatorio");
                }else if (etautorcal.getText().toString().length()== 0){
                    etautorcal.setError("Campo Obligatorio");
                }else  if (etletracal.getText().toString().length()== 0){
                    etletracal.setError("Campo Obligatorio");
                }else{
                    CorosAle a = new CorosAle();
                    a.setTitulo(ettitulocal.getText().toString().replaceAll(" ", "%20"));
                    a.setAutor(etautorcal.getText().toString().replaceAll(" ", "%20"));
                    a.setLetra(etletracal.getText().toString().replaceAll(" ", "%20"));

                    agregarCoros(a);
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    obtenerCoros();
                }
            }
        });
    }

    private  void agregarCoros(CorosAle a){
        String url = "https://appmovilgamez.000webhostapp.com/agregarale.php?";
        String parametros = "titulo="+a.getTitulo()+"&autor="+a.getAutor()+"&letra="+a.getLetra();
        clientecal.post(url + parametros, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                if (statusCode == 200){
                    Toast.makeText(CorosAleActivity.this, "Coro agregada correctamente", Toast.LENGTH_SHORT).show();
                    ettitulocal.setText("");
                    etautorcal.setText("");
                    etletracal.setText("");
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }
        });
    }



    private void obtenerCoros(){
        String url = "https://appmovilgamez.000webhostapp.com/obtenerCoroAle.php";
        clientecal.post(url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                if (statusCode == 200){
                    listarCoros(new String(responseBody));
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }
        });
    }

    private  void listarCoros(String respuesta){
        final ArrayList<CorosAle> listar = new ArrayList<CorosAle>();
        try{
            JSONArray jsonArreglo = new JSONArray(respuesta);
            for (int i=0; i<jsonArreglo.length(); i++){
                CorosAle a = new CorosAle();
                a.setId(jsonArreglo.getJSONObject(i).getInt("id_cale"));
                a.setTitulo(jsonArreglo.getJSONObject(i).getString("titulo"));
                a.setAutor(jsonArreglo.getJSONObject(i).getString("autor"));
                a.setLetra(jsonArreglo.getJSONObject(i).getString("letra"));

                listar.add(a);

            }

            ArrayAdapter<CorosAle> a = new ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item, listar);
            lvdatoscal.setAdapter(a);

            lvdatoscal.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                    CorosAle a = listar.get(position);
                    String url = "https://appmovilgamez.000webhostapp.com/eliminarale.php?id_cale="+a.getId();

                    clientecal.post(url, new AsyncHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                            if (statusCode == 200){
                                Toast.makeText(CorosAleActivity.this, "Coro liminado Correctamente", Toast.LENGTH_SHORT).show();
                                try {
                                    Thread.sleep(2000);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                obtenerCoros();
                            }
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

                        }
                    });

                    return true;
                }
            });


            lvdatoscal.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    CorosAle a = listar.get(position);
                    StringBuffer b = new StringBuffer();
                    b.append("ID: " + a.getId() + "\n");
                    b.append("TITULO: " + a.getTitulo() + "\n");
                    b.append("AUTOR: " + a.getTitulo() + "\n");
                    b.append("LETRA: " + a.getLetra() + "\n");

                    AlertDialog.Builder al = new AlertDialog.Builder(CorosAleActivity.this);
                    al.setCancelable(true);
                    al.setTitle("Detalle");
                    al.setMessage(a.tostring());
                    al.setIcon(R.drawable.xx);
                    al.show();
                }
            });
        }catch(Exception el){
            el.printStackTrace();
        }


    }

    public void coroale(View view) {
        Intent intent = new Intent(this, CorosAleActivity.class);
        startActivity(intent);
    }

}
