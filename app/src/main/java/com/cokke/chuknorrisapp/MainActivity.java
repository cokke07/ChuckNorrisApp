package com.cokke.chuknorrisapp;

import android.os.AsyncTask;
import android.os.Bundle;
import android.text.style.TtsSpan;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.cokke.chuknorrisapp.models.Chiste;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

public class MainActivity extends AppCompatActivity {

    Chiste chiste;
    private TextView txtChistes;
    //Translate translate = TranslateOptions.getDefaultInstance().getService();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Button updateButton = findViewById(R.id.button);
        TextView textView = findViewById(R.id.textView);
        txtChistes = findViewById(R.id.textView);



        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new HttpGetRequest().execute();
                // Aquí actualizamos el texto del TextView
                textView.setText(txtChistes.getText());
            }
        });

    }

    public class HttpGetRequest extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... voids) {
            String urlAPI = "https://api.chucknorris.io/jokes/random";

            try {
                // Conexión
                URL url = new URL(urlAPI);
                HttpsURLConnection conexion = (HttpsURLConnection) url.openConnection();
                conexion.setRequestMethod("GET");

                // Leer la respuesta de API
                InputStream inputStream = conexion.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                StringBuilder resultado = new StringBuilder();
                String linea;
                linea = reader.readLine();
                resultado.append(linea);


                // Cerrar conexiones
                reader.close();
                inputStream.close();
                conexion.disconnect();

                return resultado.toString();

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String resultado)  {
            super.onPostExecute(resultado);

            TypeToken<Chiste> typeToken = new TypeToken<Chiste>() {};
            chiste = new Gson().fromJson(resultado, typeToken.getType());
            //Chiste chisteResponse = new Chiste();

            String text = chiste.value;

            //Translation translation = translate.translate(text, Translate.TranslateOption.targetLanguage("es"));
            //String textTranslated = translation.getTranslatedText();

            txtChistes.setText(text);

            txtChistes.setVisibility(View.VISIBLE);
        }
    }

}