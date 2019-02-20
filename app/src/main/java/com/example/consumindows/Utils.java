package com.example.consumindows;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Utils {
    //    Exemplo de registro JSON
//    {
//        "results": [
//        {
//            "user": {
//            "gender": "female",
//                    "name": {
//                "title": "miss",
//                        "first": "concepcion",
//                        "last": "moreno"
//            },
//            "location": {
//                "street": "2853 avenida de burgos",
//                        "city": "ferrol",
//                        "state": "navarra",
//                        "zip": 96648
//            },
//            "email": "concepcion.moreno@example.com",
//                    "username": "yellowgoose845",
//                    "password": "respect",
//                    "salt": "FNcsQF4d",
//                    "md5": "1640b9fc26b3127a9f4ad641abfd6ad3",
//                    "sha1": "0388df4b233bc58113c46ed38343c89cf243dcca",
//                    "sha256": "b543fc3a3d79ae3f1c807b43e97e7e4d604200844467658855b74835d4bca546",
//                    "registered": 1094537427,
//                    "dob": 945019819,
//                    "phone": "959-588-725",
//                    "cell": "601-773-707",
//                    "DNI": "76069509-I",
//                    "picture": {
//                "large": "https://randomuser.me/api/portraits/women/7.jpg",
//                        "medium": "https://randomuser.me/api/portraits/med/women/7.jpg",
//                        "thumbnail": "https://randomuser.me/api/portraits/thumb/women/7.jpg"
//            }
//        }
//        }
//    ],
//        "nationality": "ES",
//            "seed": "c71d88ec4186eddf01",
//            "version": "0.7"
//    }

    public PessoaObj getInformacao(String end) {
        String json;
        PessoaObj retorno;
        json = NetworkUtils.getJSONFromAPI(end);
        Log.i("Resultado", json);
        retorno = parseJson(json);
        return retorno;
    }

    private PessoaObj parseJson(String json) {

        try {
            PessoaObj pessoa = new PessoaObj();

            JSONObject jsonObj = new JSONObject(json);
            JSONArray array = jsonObj.getJSONArray("results");

            SimpleDateFormat sdt = new SimpleDateFormat("dd/MM/yyyy");
            Date data;

            JSONObject objArray = array.getJSONObject(0);

            JSONObject obj = objArray.getJSONObject("user");

            pessoa.setEmail(obj.getString("email"));
            pessoa.setUsername(obj.getString("username"));
            pessoa.setSenha(obj.getString("password"));
            pessoa.setTelefone(obj.getString("phone"));
            data = new Date(obj.getLong("dob") * 1000);
            pessoa.setNascimento(sdt.format(data));

            JSONObject nome = obj.getJSONObject("name");
            pessoa.setNome(nome.getString("first"));
            pessoa.setSobrenome(nome.getString("last"));

            JSONObject endereco = obj.getJSONObject("location");
            pessoa.setEndereco(endereco.getString("street"));
            pessoa.setEstado(endereco.getString("state"));
            pessoa.setCidade(endereco.getString("city"));

            JSONObject foto = obj.getJSONObject("picture");
            pessoa.setFoto(baixarImagem(foto.getString("large")));

            return pessoa;


        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    private Bitmap baixarImagem(String url) {
        try {
            URL endereco;
            InputStream inputStream;
            Bitmap imagem;
            endereco = new URL(url);
            inputStream = endereco.openStream();
            imagem = BitmapFactory.decodeStream(inputStream);
            inputStream.close();
            return imagem;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
