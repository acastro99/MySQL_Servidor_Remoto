package com.example.proyect;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONArray;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class registro_coroado extends AppCompatActivity {
    private ListView lvdatosa;
    private AsyncHttpClient clientea = new AsyncHttpClient();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro_coroado);

        lvdatosa = findViewById(R.id.lvDatosRCADO);

        clientea = new AsyncHttpClient();

        obtenerCoros();

    }

    private void obtenerCoros(){
        String url = "https://appmovilgamez.000webhostapp.com/obtenerCoroA.php";
        clientea.post(url, new AsyncHttpResponseHandler() {
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
            lvdatosa.setAdapter(a);

            lvdatosa.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                    CorosAdo a = listar.get(position);
                    String url = "https://appmovilgamez.000webhostapp.com/eliminarca.php?id_ca="+a.getId();

                    clientea.post(url, new AsyncHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                            if (statusCode == 200){
                                Toast.makeText(registro_coroado.this, "Coro liminado Correctamente", Toast.LENGTH_SHORT).show();
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


            lvdatosa.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    CorosAdo a = listar.get(position);
                    StringBuffer b = new StringBuffer();
                    b.append("ID: " + a.getId() + "\n");
                    b.append("TITULO: " + a.getTitulo() + "\n");
                    b.append("AUTOR: " + a.getTitulo() + "\n");
                    b.append("LETRA: " + a.getLetra() + "\n");

                    AlertDialog.Builder al = new AlertDialog.Builder(registro_coroado.this);
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
}
