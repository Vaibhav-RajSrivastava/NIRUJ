package com.androidchatapp;


import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

public class PharmacistUser   extends AppCompatActivity {
    ListView usersList;
    TextView noUsersText;
    ArrayList<String> a1 = new ArrayList<>();
    int totalUsers = 0;
    ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_pharmacist_user );

        usersList = (ListView) findViewById( R.id.PharUsersList );
        noUsersText = (TextView) findViewById( R.id.noPharUsersText );

        pd = new ProgressDialog( PharmacistUser.this );
        pd.setMessage( "Loading..." );
        pd.show();

        String url = "https://medical123-b2b39.firebaseio.com/User.json";

        StringRequest request = new StringRequest( Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                doOnSuccess( s );
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                System.out.println( "" + volleyError );
            }
        } );

        RequestQueue rQueue = Volley.newRequestQueue( PharmacistUser.this );
        rQueue.add( request );

        usersList.setOnItemClickListener( new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                UserDetails.chatWith = a1.get( position );
                startActivity( new Intent( PharmacistUser.this, Chat.class ) );
            }
        } );
    }

    public void doOnSuccess(String s) {
        try {
            JSONObject obj = new JSONObject( s );

            Iterator i = obj.keys();
            String key = "";

            while (i.hasNext()) {
                key = i.next().toString();
                char first = key.charAt( 0 );
                char sec =key.charAt(1);
                if(first=='C'||first=='D'||sec=='a') {

                    if (!key.equals( UserDetails.username )) {
                        a1.add( key );
                    }
                }
                totalUsers++;
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (totalUsers <= 1) {
            noUsersText.setVisibility( View.VISIBLE );
            usersList.setVisibility( View.GONE );
        } else {
            noUsersText.setVisibility( View.GONE );
            usersList.setVisibility( View.VISIBLE );
            usersList.setAdapter( new ArrayAdapter<String>( this, android.R.layout.simple_list_item_1, a1 ) );
        }

        pd.dismiss();
    }
}





