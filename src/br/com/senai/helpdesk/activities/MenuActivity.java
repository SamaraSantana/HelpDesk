package br.com.senai.helpdesk.activities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import br.com.senai.helpdesk.R;

public class MenuActivity extends Activity implements OnItemClickListener {
	ListView listMenu;
	TextView tvBemvindo;

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_menu);
		listMenu = (ListView) findViewById(R.id.list_opcao);
		listMenu.setOnItemClickListener(this);
		
		construirMenu();
		
		tvBemvindo = (TextView) findViewById(R.id.tvBemvindo);
		String tv = getIntent().getExtras().getString("funcionario");
		tvBemvindo.setText("Bem vindo, " +tv);
	}

	// metodo para fazer o menu
	private void construirMenu() {
		String[] de = { "imagem", "texto" };
		int[] para = { R.id.imagemOpcao, R.id.textoOpcao };
		List<Map<String, Object>> opcoes = new ArrayList<Map<String, Object>>();

		Map<String, Object> item3 = new HashMap<String, Object>();
		item3.put("imagem", R.drawable.list);
		item3.put("texto", getString(R.string.lista_chamado));
		item3.put("opcao", "lista_chamado");
		opcoes.add(item3);

	

		SimpleAdapter adapter = new SimpleAdapter(this, opcoes,
				R.layout.item_opcao, de, para);
		listMenu.setAdapter(adapter);
	}

	// fazer os itens da lista funcionar
	@Override
	// parent elemento pai, de onde foi clicados
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		Map<String, Object> opcao = (Map<String, Object>) parent
				.getItemAtPosition(position);

		String op = opcao.get("opcao").toString();
		switch (op) {
		case "lista_chamado":
			Intent intent = new Intent(this, ListaChamadoActivity.class);
			String tv = getIntent().getExtras().getString("funcionario");
			intent.putExtra("funcionario", tv);
			startActivity(intent);
			break;
		}
	}
}
