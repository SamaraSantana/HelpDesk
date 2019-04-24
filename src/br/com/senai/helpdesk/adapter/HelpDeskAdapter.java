package br.com.senai.helpdesk.adapter;

import java.io.File;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import br.com.senai.helpdesk.R;
import br.com.senai.helpdesk.model.Chamado;
import br.com.senai.helpdesk.util.Notifica;

public class HelpDeskAdapter extends BaseAdapter {

	private List<Chamado> chamados;

	public HelpDeskAdapter(List<Chamado> chamados) {
		this.chamados = chamados;
	}

	@Override
	public int getCount() {
		return chamados.size();
	}

	@Override
	public Object getItem(int position) {
		return chamados.get(position);
	}

	@Override
	public long getItemId(int position) {
		return chamados.get(position).getId();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Chamado chamado = chamados.get(position);
		LinearLayout line;

		if (convertView != null) {
			line = (LinearLayout) convertView;
		} else {
			line = new LinearLayout(parent.getContext());
			LayoutInflater inflater = (LayoutInflater) parent.getContext()
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			inflater.inflate(R.layout.lista_chamado_item, line, true);
		}

		chamado.getId();

		// Id
		TextView textid = (TextView) line.findViewById(R.id.text_id);
		textid.setText(chamado.getId() +"");

		// Status
		TextView textstatus = (TextView) line.findViewById(R.id.text_status);
		textstatus.setText("Status: " + chamado.getStatus());

		// Problema
		TextView textproblema = (TextView) line
				.findViewById(R.id.text_problema);
		textproblema.setText("Problema: " + chamado.getProblema());

		return line;
	}
}
