package br.com.senai.helpdesk.util;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.widget.Toast;

public class Notifica {
	
	public static void notificaCurto(Context contexto, String texto) {
		Toast.makeText(contexto, texto, Toast.LENGTH_SHORT).show();
	}
	
	public static void notificacao(Context contexto, String titulo, String texto) {
		Notification.Builder builder = new Notification.Builder(contexto);
		builder.setContentTitle(titulo);
		builder.setContentText(texto);
		builder.setSmallIcon(android.R.drawable.ic_menu_save);
		
		long[] vibracao = {200L, 200L};
		builder.setVibrate(vibracao);
		NotificationManager manager = (NotificationManager) contexto.getSystemService(Activity.NOTIFICATION_SERVICE);
		manager.notify(1, builder.build());
		
		Uri som = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
		Ringtone ringtone = RingtoneManager.getRingtone(contexto, som);
		ringtone.play();
	}
}
