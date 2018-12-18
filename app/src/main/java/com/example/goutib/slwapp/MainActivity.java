package com.example.goutib.slwapp;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.beardedhen.androidbootstrap.BootstrapEditText;
import com.example.goutib.slwapp.R;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;


public class MainActivity extends AppCompatActivity  {

    final Context context = this;
    private BootstrapEditText ipAddress;
    private BootstrapButton opaque,transparent,winopen,winclose,aon,aoff;
    private String ledStatus;
   // private Switch automatic;
    private String serverAdress,serverAdress2,serverAdress3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ipAddress = (BootstrapEditText) findViewById(R.id.ip);
       // automatic = (Switch) findViewById(R.id.auto);
        opaque = (BootstrapButton) findViewById(R.id.opaque);
        transparent = (BootstrapButton) findViewById(R.id.transparent);
        winopen = (BootstrapButton) findViewById(R.id.open);
        winclose = (BootstrapButton) findViewById(R.id.close);
        aon=findViewById(R.id.aon);
        aoff=findViewById(R.id.aoff);

transparent.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {

        ledStatus = "3";
        //Connect to default port number. Ex: http://IpAddress:80
        serverAdress2 = ipAddress.getText().toString() + ":" + "80";
        HttpRequestTask requestTask = new HttpRequestTask(serverAdress2);
        requestTask.execute(ledStatus);
        transparent.setEnabled(false);
        opaque.setEnabled(true);
    }
});

winopen.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {

        ledStatus = "4";
        //Connect to default port number. Ex: http://IpAddress:80
        serverAdress2 = ipAddress.getText().toString() + ":" + "80";
        HttpRequestTask requestTask = new HttpRequestTask(serverAdress2);
        requestTask.execute(ledStatus);
        winopen.setEnabled(false);
        winclose.setEnabled(true);
    }
});

winclose.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        ledStatus = "5";
        //Connect to default port number. Ex: http://IpAddress:80
        serverAdress2 = ipAddress.getText().toString() + ":" + "80";
        HttpRequestTask requestTask = new HttpRequestTask(serverAdress2);
        requestTask.execute(ledStatus);
        winclose.setEnabled(false);
        winopen.setEnabled(true);
    }
});
        aon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (ipAddress.getText().toString().equals(""))
                    Toast.makeText(MainActivity.this, "Please enter the ip address...", Toast.LENGTH_SHORT).show();
                else{


                    ledStatus = "10";
                    //Connect to default port number. Ex: http://IpAddress:80
                    serverAdress2 = ipAddress.getText().toString() + ":" + "80";
                    HttpRequestTask requestTask = new HttpRequestTask(serverAdress2);
                    requestTask.execute(ledStatus);
                    opaque.setEnabled(false);
                    transparent.setEnabled(false);
                    winopen.setEnabled(false);
                    winclose.setEnabled(false);
                    aon.setEnabled(false);
                    aoff.setEnabled(true);
                }
            }
        });

        aoff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (ipAddress.getText().toString().equals(""))
                    Toast.makeText(MainActivity.this, "Please enter the ip address...", Toast.LENGTH_SHORT).show();
                else{



                    ledStatus = "11";
                    //Connect to default port number. Ex: http://IpAddress:80
                    serverAdress2 = ipAddress.getText().toString() + ":" + "80";
                    HttpRequestTask requestTask = new HttpRequestTask(serverAdress2);
                    requestTask.execute(ledStatus);

                    opaque.setEnabled(true);
                    transparent.setEnabled(true);
                    winopen.setEnabled(true);
                    winclose.setEnabled(true);
                    aoff.setEnabled(false);
                    aon.setEnabled(true);
                }
            }
        });

        opaque.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ledStatus = "2";
                //Connect to default port number. Ex: http://IpAddress:80
                serverAdress2 = ipAddress.getText().toString() + ":" + "80";
                HttpRequestTask requestTask = new HttpRequestTask(serverAdress2);
                requestTask.execute(ledStatus);
                opaque.setEnabled(false);
                transparent.setEnabled(true);
            }
        });





    }


    public class HttpRequestTask extends AsyncTask<String, Void, String> {

        private String serverAdress;
        private String serverResponse = "";
        private AlertDialog dialog;

        private HttpRequestTask(String serverAdress) {
            this.serverAdress = serverAdress;

            dialog = new AlertDialog.Builder(context)
                    .setTitle("HTTP Response from Ip Address:")
                    .setCancelable(true)
                    .create();
        }

        @Override
        protected String doInBackground(String... params) {
            dialog.setMessage("Data sent , waiting response from server...");

            if (!dialog.isShowing())
                dialog.show();

            String val = params[0];
            final String url = "http://" + serverAdress + "/led/" + val;

            try {
                HttpClient client = new DefaultHttpClient();
                HttpGet getRequest = new HttpGet();
                getRequest.setURI(new URI(url));
                HttpResponse response = client.execute(getRequest);

                InputStream inputStream = null;
                inputStream = response.getEntity().getContent();
                BufferedReader bufferedReader =
                        new BufferedReader(new InputStreamReader(inputStream));

                serverResponse = bufferedReader.readLine();
                inputStream.close();

            } catch (URISyntaxException e) {
                e.printStackTrace();
                serverResponse = e.getMessage();
            } catch (ClientProtocolException e) {
                e.printStackTrace();
                serverResponse = e.getMessage();
            } catch (IOException e) {
                e.printStackTrace();
                serverResponse = e.getMessage();
            }

            return serverResponse;
        }

        @Override
        protected void onPostExecute(String s) {
            dialog.setMessage(serverResponse);

            if (!dialog.isShowing())
                dialog.show();
        }

        @Override
        protected void onPreExecute() {
            dialog.setMessage("Sending data to server, please wait...");

            if (!dialog.isShowing())
                dialog.show();
        }
    }
}
