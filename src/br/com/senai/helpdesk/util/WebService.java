package br.com.senai.helpdesk.util;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;

import android.util.Log;

public class WebService {
	
	public static final String URL = "http://10.84.146.12:8080/ChamadoHelpDeskWS/services/";
	public static final String URLPROF = "http://10.84.146.2:8080/SpringWS/services/";
	
	/** 
	 * M�todo para realizar a leitura de um InputStream
	 * e convert�-lo em String
	 * 
	 * @param in
	 * InputSteam com os dados para serem lidos
	 *  
	 * @return
	 * conte�do convertido para String
	 */
	public static String readStream(InputStream in) {
		// Cria uma refer�ncia para BufferedReader
		BufferedReader reader = null;
		
		// Cria um StringBuilder
		StringBuilder builder = new StringBuilder();
		try {
			// Cria o BufferedReader passando o InputStream recebido
			// como par�metro
			reader = new BufferedReader(new InputStreamReader(in));
			
			// Cria uma linha nula
			String line = "null";
			
			// L� as linhas e acrescenta ao StringBuilder
			// at� que n�o haja mais conte�do nas linhas
			while((line = reader.readLine()) != null) {
				builder.append(line+"\n");
			}
		} catch (Exception e) {
			Log.w("ERRO", e.getMessage());
		} finally {
			if (reader != null) {
				try {
					// Tenta fechar o reader
					reader.close();
				} catch (Exception e2) {
					Log.w("ERRO", e2.getMessage());
				}
			}
		}
		// Retorna a String lida
		return builder.toString();
	}
	
	/**
	 * M�todo para ler os dados de um endere�o HTTP
	 * 
	 * @param urlAddres
	 * no formato http://
	 * 
	 * @return
	 * cont�udo lido
	 */
	public static String makeRequest(String urlAddres) {
		// Cria uma refer�ncia para se conectar com uma URL Http
		HttpURLConnection con = null;
		
		// Cria uma refer�ncia para uma URL
		java.net.URL url = null;
		
		// String de retorno
		String resposta = null;
		try {
			// Instancia a url atrav�s do endere�o recebido no m�todo
			url = new java.net.URL(urlAddres);
			
			// Abre a conex�o e guarda na vari�vel con
			con = (HttpURLConnection) url.openConnection();
			
			// Invocar o m�todo readStream para ler o conte�do da p�gina
			resposta = readStream(con.getInputStream());
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// Fecha a conex�o
			con.disconnect();
		}
		// Retorna o texto lido
		return resposta;
	}
}
