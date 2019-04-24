package br.com.senai.helpdesk.activities;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import br.com.senai.helpdesk.R;
import br.com.senai.helpdesk.model.Funcionario;
import br.com.senai.helpdesk.util.WebService;

import com.google.gson.Gson;

public class MainActivity extends Activity {

	EditText editLogin, editSenha;

	String login, senha;
	Funcionario funcionario;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
				.permitAll().build();
		StrictMode.setThreadPolicy(policy);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		editLogin = (EditText) findViewById(R.id.editLogin);
		editSenha = (EditText) findViewById(R.id.editSenha);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	public void logar(View v) {
		Funcionario f = new Funcionario();
		f.setLogin(editLogin.getEditableText().toString());
		f.setSenha(editSenha.getEditableText().toString());

		Gson gson = new Gson();
		String json = gson.toJson(f);

		try {
			URL url = new URL(WebService.URL + "chamado/login");
			HttpURLConnection conexao = null;
			conexao = (HttpURLConnection) url.openConnection();
			conexao.setRequestProperty("Content-Type", "application/json");
			conexao.setRequestMethod("POST");
			conexao.setDoInput(true);
			conexao.setDoOutput(true);

			conexao.connect();
			OutputStream out = conexao.getOutputStream();
			out.write(json.getBytes("UTF-8"));
			out.flush();
			out.close();
			InputStream in = null;
			int status = conexao.getResponseCode();
			if (status >= HttpURLConnection.HTTP_BAD_REQUEST) {
				Log.d("ERRO", "Error code: " + status);
				in = conexao.getErrorStream();
			} else {
				in = conexao.getInputStream();
			}
			String resposta = WebService.readStream(in);
			Log.w("STRING", resposta);
			in.close();
			Toast.makeText(this, resposta, Toast.LENGTH_SHORT).show();

			if (editLogin.getEditableText().toString().equals("") || editSenha.getEditableText().toString().equals("") || resposta.toString().contains("Erro")) {
				Toast.makeText(this, "Funcionário Inválido", Toast.LENGTH_SHORT).show();			
			} else {
				Intent intent = new Intent(this, MenuActivity.class);
				intent.putExtra("funcionario", f.getLogin());
				startActivity(intent);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
