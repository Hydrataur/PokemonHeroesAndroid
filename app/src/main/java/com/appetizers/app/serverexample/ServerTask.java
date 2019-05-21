package com.appetizers.app.serverexample;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

//Class for server interaction
public class ServerTask extends AsyncTask<Void,String,Void> {
    private String ip;
    private int port;
    private MainActivity activity; //Save activity as var in order to update it later
    private Socket socket;

    public ServerTask(String ip, int port, MainActivity activity) {
        Log.i("Task", "Made ServerTask");
        this.ip = ip;
        this.port = port;
        this.activity = activity;
    }

    //Pre-thread
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        Toast.makeText(activity, "Starting", Toast.LENGTH_SHORT).show(); //Toast for debug
    }

    //Post-thread
    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        Toast.makeText(activity, "Ended", Toast.LENGTH_SHORT).show(); //Toast for debug
        System.exit(1);
    }

    @Override
    protected Void doInBackground(Void... voids) {
        try {
            Log.i("Task", "In bg task");
            socket = new Socket(ip,port); //Create the socket
            Log.i("Task", "Made socket");
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));//Reads messages from server through socket
            String line = reader.readLine(); //Get the first message from the server
            while(line!=null && !line.equals("Bye")){ //While the server hasn't shut down or sent a Bye message
                publishProgress(line); //Update activity in order to change it
                line = reader.readLine(); //Get the next line from the server
            }
            socket.close(); //Close the socket once the process is done
        } catch (IOException e) {
            e.printStackTrace();
            Log.i("Task", e.getMessage());
        }
        return null;
    }

    /**
     *Way to interact with main thread and server simultaneously
     * Update activity with info from server
     * @param values
     */
    //values[0] == line
    @Override
    protected void onProgressUpdate(String... values) {
        super.onProgressUpdate(values);
        if(!values[0].equals(""))
            activity.useData(values[0]); //Send data to activity if it's not empty

    }
}
