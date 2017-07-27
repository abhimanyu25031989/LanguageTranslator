package example.com.abhi.languagetranslator;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    private EditText source_language, target_language, text_to_translate;
    private TextView translated_text;
    private Button translate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        source_language = (EditText) findViewById(R.id.source_language);
        target_language = (EditText) findViewById(R.id.target_language);
        text_to_translate = (EditText) findViewById(R.id.text_to_translate);

        translated_text = (TextView) findViewById(R.id.tranlated_text);

        translate = (Button) findViewById(R.id.translate);

        translate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                translate();
            }
        });
    }


    private void translate() {

        final String text_to_translate_string, source_language_string, target_language_string;

        text_to_translate_string = text_to_translate.getText().toString().trim();
        source_language_string = source_language.getText().toString().trim();
        target_language_string = target_language.getText().toString().trim();

        StringRequest stringRequest = new StringRequest(Request.Method.GET, "http://deu.hablaa.com/hs/translation/" + text_to_translate_string + "/" + source_language_string + "-" + target_language_string + "/",

                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {

                            JSONArray jsonArray = new JSONArray(response);
                            JSONObject jsonObject = jsonArray.getJSONObject(0);
                            translated_text.setText(jsonObject.getString("text"));

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        error.printStackTrace();

                        NetworkResponse response = error.networkResponse;

                        if (response != null && response.data != null) {
                            switch (response.statusCode) {

                                case 404:

                                    Log.i("TEST", " its 404");
                                    break;

                                case 204:

                                    Log.i("TEST", " its 204");
                                    break;

                            }
                        }
                    }

                }

        );

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

}
