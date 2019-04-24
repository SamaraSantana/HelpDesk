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
	 * Método para realizar a leitura de um InputStream
	 * e convertê-lo em String
	 * 
	 * @param in
	 * InputSteam com os dados para serem lidos
	 *  
	 * @return
	 * conteúdo convertido para String
	 */
	public static String readStream(InputStream in) {
		// Cria uma referência para BufferedReader
		BufferedReader reader = null;
		
		// Cria um StringBuilder
		StringBuilder builder = new StringBuilder();
		try {
			// Cria o BufferedReader passando o InputStream recebido
			// como parâmetro
			reader = new BufferedReader(new InputStreamReader(in));
			
			// Cria uma linha nula
			String line = "null";
			
			// Lê as linhas e acrescenta ao StringBuilder
			// até que não haja mais conteúdo nas linhas
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
	 * Método para ler os dados de um endereço HTTP
	 * 
	 * @param urlAddres
	 * no formato http://
	 * 
	 * @return
	 * contéudo lido
	 */
	public static String makeRequest(String urlAddres) {
		// Cria uma referência para se conectar com uma URL Http
		HttpURLConnection con = null;
		
		// Cria uma referência para uma URL
		java.net.URL url = null;
		
		// String de retorno
		String resposta = null;
		try {
			// Instancia a url através do endereço recebido no método
			url = new java.net.URL(urlAddres);
			
			// Abre a conexão e guarda na variável con
			con = (HttpURLConnection) url.openConnection();
			
			// Invocar o método readStream para ler o conteúdo da página
			resposta = readStream(con.getInputStream());
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// Fecha a conexão
			con.disconnect();
		}
		// Retorna o texto lido
		return resposta;
	}
}
