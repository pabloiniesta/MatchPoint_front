package com.example.loginlayout;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class fragment_events_future extends Fragment implements Interfaz{
    View view;
    private RecyclerView recyclerView;
    private List<holder_event_card> listEventsFuture;

    public fragment_events_future(){

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_events_future, container, false);
        recyclerView = view.findViewById(R.id.recyclerId);
        events_recycleview_adapter recyclerAdapter = new events_recycleview_adapter(getContext(), listEventsFuture);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(recyclerAdapter);
        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        listEventsFuture = new ArrayList<>();

        Connection con = new Connection(this);
        con.execute("http://10.4.41.144:3000/participant/coming/username/" + UsuariSingleton.getInstance().getId(), "GET", null);
        /*

        listEventsFuture.add(new holder_event_card("Futbol", "John", "25/05/1999"));
        listEventsFuture.add(new holder_event_card("Baloncesto", "Johnas", "25/05/1989"));
        listEventsFuture.add(new holder_event_card("Futbol", "John", "25/05/1999"));
        listEventsFuture.add(new holder_event_card("Baloncesto", "Johnas", "25/05/1989"));
         */
    }

    @Override
    public void Respuesta(JSONObject datos) {
        try {
            if (datos.getInt("codigo") == 200) {
                System.out.println("HE ENTRADO 200");
                if (datos.getJSONArray("array") == null) {
                    return;
                }
                System.out.println("TAAAAAA :" + datos);
                JSONArray response = datos.getJSONArray("array");
                System.out.println("TAMAÑO DE LA RESPUESTA2: " + response.length());
                for(int i = 0; i < response.length(); i++) {
                    JSONObject preevent = response.getJSONObject(i);
                    JSONObject event = preevent.getJSONObject("event");
                    System.out.println(event);
                    String titulo = event.getString("sport");

                    String calendario = event.getString("date");
                    calendario = convertMongoDate(calendario);

                    String creador = preevent.getString("username");
                    //parseJSON(creador);
                    //System.out.println("IM OUTTT: " + usuarioCreador);
                    //creador = usuarioCreador;

                    listEventsFuture.add(new holder_event_card(titulo, creador, calendario));
                }
                events_recycleview_adapter recyclerAdapter = new events_recycleview_adapter(getContext(), listEventsFuture);
                recyclerView.setAdapter(recyclerAdapter);
                //OperacionAuxiliar();
                System.out.println("HE SALIDO2");
            }
            else System.out.println(datos.getInt("codigo"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private static String convertMongoDate(String val){
        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        SimpleDateFormat outputFormat= new SimpleDateFormat("dd/MM/yyy  (HH:mm)");
        try {
            String finalStr = outputFormat.format(inputFormat.parse(val));
            System.out.println(finalStr);
            return finalStr;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "";
    }
}
