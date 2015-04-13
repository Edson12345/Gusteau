package com.edsonchambi.gusteau;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class lista_item extends ActionBarActivity {
    private ProgressDialog pDialog;

    JSONParser jParser = new JSONParser();

    ArrayList<HashMap<String, String>> lista_items;

    private static String url_all_items = "http://10.0.2.2/rsabroso/config/lista_items.php";

    private static final String TAG_SUCCESS = "success";
    private static final String TAG_ITEMS = "lista_items";
    private static final String TAG_ID_ITEM = "id_item";
    private static final String TAG_NOMBRE_ITEM = "nombre_item";
    private static final String TAG_CATEGORIA = "categoria";
    private static final String TAG_COSTO = "costo";

    JSONArray items = null;

    ListView lista;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_item);

        lista_items = new ArrayList<HashMap<String, String>>();

        new LoadAllProducts().execute();
        lista = (ListView) findViewById(R.id.listAllProducts);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

    }

    class LoadAllProducts extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(lista_item.this);
            pDialog.setMessage("Cargando Items. Por favor espere...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        protected String doInBackground(String... args) {

            List params = new ArrayList();
            JSONObject json = jParser.makeHttpRequest(url_all_items, "GET", params);
            Log.d("All Product: ", json.toString());

            try {
                int success = json.getInt(TAG_SUCCESS);
                if (success == 1) {
                    items = json.getJSONArray(TAG_ITEMS);
                    for (int i = 0; i < items.length(); i++) {

                        JSONObject c = items.getJSONObject(i);

                        String id = c.getString(TAG_ID_ITEM);
                        String nombre = c.getString(TAG_NOMBRE_ITEM);
                        String cate = c.getString(TAG_CATEGORIA);
                        String costo = c.getString(TAG_COSTO);

                        HashMap map = new HashMap();

                        map.put(TAG_ID_ITEM, id);
                        map.put(TAG_NOMBRE_ITEM, nombre);
                        map.put(TAG_CATEGORIA, cate);
                        map.put(TAG_COSTO, costo);

                        lista_items.add(map);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(String file_url) {
            pDialog.dismiss();
            runOnUiThread(new Runnable() {
                public void run() {
                    ListAdapter adapter = new SimpleAdapter(
                            lista_item.this,
                            lista_items,
                            R.layout.result_list_view,
                            new String[]{
                                    TAG_ID_ITEM,
                                    TAG_NOMBRE_ITEM,
                                    TAG_CATEGORIA,
                                    TAG_COSTO,
                            },
                            new int[]{
                                    R.id.single_post_id,
                                    R.id.single_post_nombre,
                                    R.id.single_post_categ,
                                    R.id.single_post_costo,
                            });
                    lista.setAdapter(adapter);
                }
            });
        }
    }


        @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_lista_item, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
