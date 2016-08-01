package com.example.arush.customtrackertest.customtracker_communication;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by arush on 7/31/2016.
 */
public class SendHandler {
    private final String URL = ""; //add URL
    private static SendHandler handler;

    static {
        handler = new SendHandler();
    }

    private SendHandler() { }

    public void sendData(Context context, JSONObject jsonObject) {
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, URL, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("Response", response.toString());
            }
        },
        new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Error Response", error.getMessage());
            }
        });

        requestQueue.add(request);
    }

    public static SendHandler getInstance(){
        return handler;
    }
}
