package com.example.consumindows;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.Buffer;

public class NetworkUtils {
    private static final int TEMPO_PROCESSAMENTO = 15000;

    public static enum VerbosHTTP {
        GET {
            public String toString() {
                return "GET";
            }
        },
        POST {
            public String toString() {
                return "POST";
            }
        },
        PUT {
            public String toString() {
                return "PUT";
            }
        },
        DELETE {
            public String toString() {
                return "DELETE";
            }
        }
    }

    public static String getJSONFromAPI(String url) {
        String retorno = "";
        try {
            URL apiEnd = new URL(url);
            int codigoResposta;
            HttpURLConnection conexao;
            InputStream is;

            conexao = (HttpURLConnection) apiEnd.openConnection();
            conexao.setRequestMethod(VerbosHTTP.GET.toString());
            conexao.setReadTimeout(TEMPO_PROCESSAMENTO);
            conexao.setConnectTimeout(TEMPO_PROCESSAMENTO);
            conexao.connect();

            codigoResposta = conexao.getResponseCode();
            if (codigoResposta < HttpURLConnection.HTTP_BAD_REQUEST) {
                is = conexao.getInputStream();
            } else {
                is = conexao.getErrorStream();
            }

            retorno = converterInputStreamToString(is);
            is.close();
            conexao.disconnect();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return retorno;
    }

    private static String converterInputStreamToString(InputStream is) {
        StringBuffer buffer = new StringBuffer();
        try {

            BufferedReader br;
            String linha;

            br = new BufferedReader(new InputStreamReader(is));

            while (null != (linha = br.readLine())) {
                buffer.append(linha);
            }

            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return buffer.toString();
    }
}
