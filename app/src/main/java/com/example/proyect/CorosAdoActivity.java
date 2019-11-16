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

public class CorosAdoActivity extends AppCompatActivity {

    private EditText ettituloca, etautorca, etletraca;
    private Button btnRegistrarca;
    private ListView lvdatosca;
    private AsyncHttpClient clienteca = new AsyncHttpClient();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coros_ado);

        ettituloca = findViewById(R.id.ettituloca);
        etautorca = findViewById(R.id.etautorca);
        etletraca = findViewById(R.id.etletraca);

        btnRegistrarca = findViewById(R.id.btnRegistrarca);

        lvdatosca = findViewById(R.id.lvDatosca);

        clienteca = new AsyncHttpClient();

        almacenarCoros();

        obtenerCoros();
    }

    private void almacenarCoros() {
        btnRegistrarca.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ettituloca.getText().toString().length()== 0 )  {
                    ettituloca.setError("Campo Obligatorio");
                }else if (etautorca.getText().toString().length()== 0){
                    etautorca.setError("Campo Obligatorio");
                }else  if (etletraca.getText().toString().length()== 0){
                    etletraca.setError("Campo Obligatorio");
                }else{
                    CorosAdo a = new CorosAdo();
                    a.setTitulo(ettituloca.getText().toString().replaceAll(" ", "%20"));
                    a.setAutor(etautorca.getText().toString().replaceAll(" ", "%20"));
                    a.setLetra(etletraca.getText().toString().replaceAll(" ", "%20"));

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

    private  void agregarCoros(CorosAdo a){
        String url = "https://appmovilgamez.000webhostapp.com/agregarca.php?";
        String parametros = "titulo="+a.getTitulo()+"&autor="+a.getAutor()+"&letra="+a.getLetra();
        clienteca.post(url + parametros, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                if (statusCode == 200){
                    Toast.makeText(CorosAdoActivity.this, "Coro agregada correctamente", Toast.LENGTH_SHORT).show();
                    ettituloca.setText("");
                    etautorca.setText("");
                    etletraca.setText("");
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }
        });
    }



    private void obtenerCoros(){
        String url = "https://appmovilgamez.000webhostapp.com/obtenerCoroA.php";
        clienteca.post(url, new AsyncHttpResponseHandler() {
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
        final ArrayList<CorosAdo> listar = new ArrayList<CorosAdo>();
        try{
            JSONArray jsonArreglo = new JSONArray(respuesta);
            for (int i=0; i<jsonArreglo.length(); i++){
                CorosAdo a = new CorosAdo();
                a.setId(jsonArreglo.getJSONObject(i).getInt("id_ca"));
                a.setTitulo(jsonArreglo.getJSONObject(i).getString("titulo"));
                a.setAutor(jsonArreglo.getJSONObject(i).getString("autor"));
                a.setLetra(jsonArreglo.getJSONObject(i).getString("letra"));

                listar.add(a);

            }

            ArrayAdapter<CorosAdo> a = new ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item, listar);
            lvdatosca.setAdapter(a);

            lvdatosca.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                    CorosAdo a = listar.get(position);
                    String url = "https://appmovilgamez.000webhostapp.com/eliminarca.php?id_ca="+a.getId();

                    clienteca.post(url, new AsyncHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                            if (statusCode == 200){
                                Toast.makeText(CorosAdoActivity.this, "Coro liminado Correctamente", Toast.LENGTH_SHORT).show();
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


            lvdatosca.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    CorosAdo a = listar.get(position);
                    StringBuffer b = new StringBuffer();
                    b.append("ID: " + a.getId() + "\n");
                    b.append("TITULO: " + a.getTitulo() + "\n");
                    b.append("AUTOR: " + a.getTitulo() + "\n");
                    b.append("LETRA: " + a.getLetra() + "\n");

                    AlertDialog.Builder al = new AlertDialog.Builder(CorosAdoActivity.this);
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
