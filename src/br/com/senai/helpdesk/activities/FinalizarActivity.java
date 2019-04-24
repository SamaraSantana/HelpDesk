package br.com.senai.helpdesk.activities;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;
import br.com.senai.helpdesk.R;
import br.com.senai.helpdesk.adapter.HelpDeskAdapter;
import br.com.senai.helpdesk.model.Chamado;
import br.com.senai.helpdesk.util.WebService;

public class FinalizarActivity extends Activity implements
		android.content.DialogInterface.OnClickListener {
	private AlertDialog dialogFoto;
	private Uri uriFoto;
	// criando o requestCode
	private final int GALERIA = 1;
	private final int CAMERA = 0;
	private Bitmap bmpFoto;
	private ImageView imageFoto;
	private Chamado chamado;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		// thread
		// sem essas linhas nao funciona
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
				.permitAll().build();
		StrictMode.setThreadPolicy(policy);

		super.onCreate(savedInstanceState);

		setContentView(R.layout.finalizar_activity);

		imageFoto = (ImageView) findViewById(R.id.imageFoto);

		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(R.string.titulo_alert);

		String[] opcoes = new String[2];
		opcoes[0] = getString(R.string.camera);
		opcoes[1] = getString(R.string.galeria);
		builder.setItems(opcoes, this);

		dialogFoto = builder.create();

		chamado = (Chamado) getIntent().getSerializableExtra("chamado");
		if (chamado != null) {
			preencherCampos();

		}

	}

	private void preencherCampos() {
		if (chamado.getFoto() != null) {
			String data = new String(chamado.getFoto());
			byte[] base64 = Base64.decode(data, Base64.DEFAULT);
			bmpFoto = BitmapFactory.decodeByteArray(base64, 0, base64.length);

			imageFoto.setImageBitmap(bmpFoto);
		} else {
			imageFoto.setImageResource(R.drawable.camera);
		}
	}

	public void selecionarImagem(View v) {
		dialogFoto.show();
	}

	// Colocar a Foto escolhida no ImageView
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == Activity.RESULT_OK) {

			switch (requestCode) {
			case CAMERA:
				Intent intent = new Intent(
						Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
				intent.setData(uriFoto);
				sendBroadcast(intent);

				try {
					bmpFoto = MediaStore.Images.Media.getBitmap(
							getContentResolver(), uriFoto);
				} catch (Exception e) {
					Log.w("ERRO", e.getMessage());
				}
				imageFoto.setImageBitmap(bmpFoto);
				break;

			case GALERIA:
				String[] colunaCaminhoArquivo = { MediaStore.Images.Media.DATA };
				uriFoto = data.getData();

				Cursor cursor = getContentResolver().query(uriFoto,
						colunaCaminhoArquivo, null, null, null);
				cursor.moveToFirst();
				int columnIndex = cursor
						.getColumnIndex(colunaCaminhoArquivo[0]);
				String caminhoImagem = cursor.getString(columnIndex);
				cursor.close();

				uriFoto = Uri.fromFile(new File(caminhoImagem));

				try {
					bmpFoto = MediaStore.Images.Media.getBitmap(
							getContentResolver(), uriFoto);
				} catch (Exception e) {
					Log.w("ERRO", e.getMessage());
				}
				imageFoto.setImageBitmap(bmpFoto);
				break;

			default:
				break;
			}
		}
	}

	public void onClick(DialogInterface dialog, int which) {

		Intent intent;

		switch (which) {

		// Capturar via Câmera
		case 0:
			File diretorio = Environment
					.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
			String nomeImagem = diretorio.getPath() + "/"
					+ System.currentTimeMillis() + ".jpg";
			uriFoto = Uri.fromFile(new File(nomeImagem));
			intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
			intent.putExtra(MediaStore.EXTRA_OUTPUT, uriFoto);
			startActivityForResult(intent, CAMERA);
			break;
		// Selecionar da Galeria
		case 1:
			intent = new Intent(
					Intent.ACTION_PICK,
					android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
			startActivityForResult(intent, GALERIA);
			break;
		}
	}

	public void salvar(View v) {
		JSONObject json = new JSONObject();
		try {
			try {		
				ByteArrayOutputStream stream = new ByteArrayOutputStream();
				bmpFoto.compress(CompressFormat.PNG, 50, stream);
				byte[] bytearray = stream.toByteArray();
				
				String base64 = Base64.encodeToString(bytearray, Base64.DEFAULT);		
				json.put("bytearray", base64);
				
			} catch (Exception e) {
				Log.w("ERRO", e.getMessage());
			}	
			json.put("id", chamado.getId());
			
			String jsonString = json.toString();

			URL url = new URL(WebService.URL + "chamado/foto");
			HttpURLConnection conexao = null;
			conexao = (HttpURLConnection) url.openConnection();
			conexao.setRequestProperty("Content-Type", "application/json");
			conexao.setRequestMethod("POST");
			conexao.setDoInput(true);
			conexao.setDoOutput(true);

			conexao.connect();
			OutputStream out = conexao.getOutputStream();
			out.write(jsonString.getBytes("UTF-8"));
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
			
			finish();
			
		} catch (Exception e) {
			Log.w("ERRO", e.getMessage());
		}
	}
}
