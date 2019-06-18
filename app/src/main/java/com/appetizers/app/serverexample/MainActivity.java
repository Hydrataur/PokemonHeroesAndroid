package com.appetizers.app.serverexample;

import android.content.DialogInterface;
import android.content.res.Resources;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.PrintWriter;

/**
 * חשוב!!!!! עליכם להוסיף לmanifest.xml את השורה
 * <uses-permission android:name="android.permission.INTERNET"/>
 */
public class MainActivity extends AppCompatActivity {

    LinearLayout layout;
    LinearLayout container;

    /**
     * Both of these are for the server connection process
     */
    EditText input;
    String ip;

    private TextView nameText;
    private TextView HPText;
    private TextView attackText;
    private TextView defenseText;
    private TextView damageText;
    private TextView movementText;
    private TextView rangedText;
    private TextView flyingText;

    private ImageView pokeImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Set all Views to correct id in order to change the correct element.
        nameText = findViewById(R.id.nameText);
        HPText = findViewById(R.id.HPText);
        attackText = findViewById(R.id.attackText);
        defenseText = findViewById(R.id.defenseText);
        damageText = findViewById(R.id.damageText);
        movementText = findViewById(R.id.movementText);
        rangedText = findViewById(R.id.rangedText);
        flyingText = findViewById(R.id.flyingText);
        pokeImage = findViewById(R.id.pokeImage);

        //Set layouts in order to change background and visibility later
        layout = findViewById(R.id.layout);
        container = findViewById(R.id.containerLayout);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Pokemon Heroes");
        builder.setIcon(R.drawable.hpicon);
        builder.setMessage("Please enter the IP address of the server");

        input = new EditText(this);
        builder.setView(input);

        //Set Positive Button
        builder.setPositiveButton("Continue", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ip = input.getText().toString();
                makeServerTask();
            }
        });

        //Set Negative Button
        builder.setNegativeButton("Exit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                System.exit(1);
            }
        });

        AlertDialog ad = builder.create();
        ad.show();

    }

    public void makeServerTask(){
        Log.d("Debug", "About to make servertask");
        ServerTask task = new ServerTask(ip, 12345, this); //Create the ServerTask
        Log.d("Debug", "Made servertask");
        task.execute(); //Start the ServerTask
        Log.d("Debug", "Executed servertask");
    }

    public void useData(String line) {
        //Set UI to Pokemon display
        layout.setBackgroundResource(R.drawable.bg);
        container.setVisibility(View.VISIBLE);

        //Make sure we don't try to work with wrong type of server message, resulting in a crash
        if(line.startsWith("Android")){
            String[] strs = line.split("&&"); //Split the message into smaller chunks

            //Start with 1 because 0 should be "Android". Sets the correct text values for each TextView
            nameText.setText(strs[1].substring(0, 1).toUpperCase() + strs[1].substring(1));
            HPText.setText(strs[2] + "/" + strs[3]);
            attackText.setText(strs[4]);
            defenseText.setText(strs[5]);
            damageText.setText(strs[6] + "-" + strs[7]);
            movementText.setText(strs[8]);
            rangedText.setText(strs[9]);
            flyingText.setText(strs[10]);

            //Set the image for ImageView according to Pokemon name
            Resources resources = getResources();
            final int resourceId = resources.getIdentifier(strs[1], "drawable", getPackageName());

            pokeImage.setImageResource(resourceId);
        }
    }

}
