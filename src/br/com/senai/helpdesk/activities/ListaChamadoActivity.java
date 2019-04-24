package br.com.senai.helpdesk.activities;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONStringer;

import com.google.gson.JsonParser;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import br.com.senai.helpdesk.R;
import br.com.senai.helpdesk.adapter.HelpDeskAdapter;
import br.com.senai.helpdesk.model.Chamado;
import br.com.senai.helpdesk.util.WebService;

public class ListaChamadoActivity extends Activity implements
		android.content.DialogInterface.OnClickListener {
	ListView listView;
	AlertDialog dialog;
	Chamado c;
	List<Chamado> lista;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// thread
		// sem essas linhas nao funciona
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
				.permitAll().build();
		StrictMode.setThreadPolicy(policy);

		super.onCreate(savedInstanceState);
		setContentView(R.layout.lista_chamado_activity);

		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		String[] items = { "Finalizar Chamado" };
		builder.setItems(items, this);
		dialog = builder.create();

		listView = (ListView) findViewById(R.id.listChamado);
		// ArrayAdapter<Chamado> adapter = new ArrayAdapter<Chamado>(this,
		// android.R.layout.simple_list_item_1, getChamadoWS());
		listView.setAdapter(new HelpDeskAdapter(getChamadoWS()));

		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				c = lista.get(position);
				dialog.show();
			}
		});
	}

	public List<Chamado> getChamadoWS() {
		// lista de contatos para adicionar os usuarios listados
		lista = new ArrayList<Chamado>();
		// URL de acesso
		String tv = getIntent().getExtras().getString("funcionario");
		String url = WebService.URL + "chamado/listarChamados/" + tv;
		// resposra através do makeResquest
		String resposta = WebService.makeRequest(url);
		try {
			// converte a resposta para JSONArray
			JSONArray array = new JSONArray(resposta);
			for (int i = 0; i < array.length(); i++) {
				JSONObject json = array.getJSONObject(i);
				c = new Chamado();

				c.setId(json.getInt("id"));
				c.setStatus(json.getString("status"));
				c.setProblema(json.getString("problema"));

				lista.add(c);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return lista;
	}

	@Override
	public void onClick(DialogInterface dialog, int which) {
		switch (which) {
		case 0:
			Intent intent = new Intent(this, FinalizarActivity.class);
			intent.putExtra("chamado", c);
			startActivity(intent);
			break;
		}
	}
}
