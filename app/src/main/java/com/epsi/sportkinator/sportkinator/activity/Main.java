package com.epsi.sportkinator.sportkinator.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.epsi.sportkinator.sportkinator.R;
import com.epsi.sportkinator.sportkinator.custom.CustomTextView;
import com.epsi.sportkinator.sportkinator.entities.Question;
import com.epsi.sportkinator.sportkinator.entities.Sport;
import com.epsi.sportkinator.sportkinator.entities.Tag;
import com.epsi.sportkinator.sportkinator.parser.SportXmlParser;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;


public class Main extends ActionBarActivity {

    private Question question;
    private Sport sport;
    private Tag tagXML;
    private TextView textViewToChange;
    private ImageView imageSport;
    private LinearLayout layoutFindResponse;
    private LinearLayout layoutResponseButtons;
    private LinearLayout layoutResponse;
    private String response;
    private boolean dontKnow=false;
    private Question questiondontKnow;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //display question or sport
        textViewToChange = (TextView) findViewById(R.id.questionText);
        Typeface fontTron = Typeface.createFromAsset(getAssets(), "fifa.ttf");
        textViewToChange.setTypeface(fontTron);

        imageSport = (ImageView) findViewById(R.id.itemImage);

        layoutFindResponse =(LinearLayout) findViewById(R.id.linearLayoutFindResponse);
        layoutResponseButtons =(LinearLayout) findViewById(R.id.linearLayoutResponseButtons);
        layoutResponse =(LinearLayout) findViewById(R.id.layoutResponse);


        layoutFindResponse.setVisibility(View.GONE);
        layoutResponse.setVisibility(View.GONE);


        try {
            File mFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/bdConnaissance.xml");
            if(!mFile.exists()) {
                copy("bdConnaissance.xml");
                Toast.makeText(getApplicationContext(), "Initialisation de la base de connaissance terminée.", Toast.LENGTH_SHORT).show();
            }
        } catch (IOException e) {
            Toast.makeText(getApplicationContext(), "Une erreur est survenue lors de l'initialisation de la base de connaissance.", Toast.LENGTH_SHORT).show();
            Log.e("Copy file ", e.getMessage());
            e.printStackTrace();
        }


        readTag(null);
    }

    private void readTag(String response) {
        File mFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/bdConnaissance.xml");
        InputStream inputStream = getInputStream(mFile);


        SportXmlParser sportXmlParser = new SportXmlParser(inputStream);
        question = sportXmlParser.readQuestion(question, response);

        if (question.getName()==null)
        {
            sport = sportXmlParser.getSport(question.getResponse());
            if (sport != null)
            {
                textViewToChange.setText(
                        "Le sport auquel vous pensez est : " + sport.getName() + " !!");
                if (sport.getImage() != null)
                {
                    if (sport.getImage().contains("/"))
                    {
                        imageSport.setImageBitmap(BitmapFactory.decodeFile(sport.getImage()));
                    }
                    else
                    {
                        int resourceNumber = getResources().getIdentifier(sport.getName(),"mipmap", getPackageName());
                        imageSport.setImageResource(resourceNumber);
                    }
                }
                else
                {
                    imageSport.setImageResource(android.R.color.transparent);
                }


                layoutFindResponse.setVisibility(View.VISIBLE);
                layoutResponseButtons.setVisibility(View.GONE);
                layoutResponse.setVisibility(View.VISIBLE);

            }


        }
        else
        {
            textViewToChange.setText(question.getName());
        }

    }

    private InputStream getInputStream(File mFile) {
        try {
            return new FileInputStream(mFile);
        } catch (FileNotFoundException e)
        {
            e.printStackTrace();
            return null;
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    private void changeQuestion(String response)
    {
        readTag(response);
    }

    public void buttonYes(View view) {
        response= "oui";
        changeQuestion(response);
    }

    public void buttonNo(View view) {
        response= "non";
        changeQuestion(response);
    }
    public void buttonDontKnow(View view) {
        if(!dontKnow) {
            dontKnow = true;
            questiondontKnow = question;
        }
        response= "non";
        changeQuestion(response);
    }

    public void buttonContinue(View view){

        if(dontKnow) {
            question = questiondontKnow;
            response = "oui";
            changeQuestion(response);
            dontKnow=false;
        }
        else{
            response = "oui";
            question=null;
            changeQuestion(response);
        }

    }

    public void buttonReplay(View view){
        layoutResponseButtons.setVisibility(View.VISIBLE);
        layoutResponse.setVisibility(View.GONE);
        layoutFindResponse.setVisibility(View.GONE);
        question=null;
        readTag(null);
    }

    public void buttonShowAddQuestion(View view){
        new AlertDialog.Builder(this)
                .setTitle("Continuer")
                .setMessage("Voulez vous continuer ?")
                .setPositiveButton("Oui", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        layoutResponseButtons.setVisibility(View.VISIBLE);
                        layoutFindResponse.setVisibility(View.GONE);
                        layoutResponse.setVisibility(View.GONE);
                        response = "oui";
                        if(dontKnow) {
                            question = questiondontKnow;
                            changeQuestion(response);
                            dontKnow=false;
                        }
                        else{
                            question=null;
                            changeQuestion(response);
                        }
                    }
                })
                .setNegativeButton("Non", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Intent formSportActivity = new Intent(Main.this, FormSport.class);
                        formSportActivity.putExtra("sport", question.getResponse());
                        startActivity(formSportActivity);
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
        layoutFindResponse.setVisibility(View.VISIBLE);
        layoutResponse.setVisibility(View.GONE);
    }

   private void copy(String name) throws IOException {
        try {

            InputStream myInput = getResources().openRawResource(R.raw.connaissance);
            String outFileName = Environment.getExternalStorageDirectory() + File.separator + name;
            OutputStream myOutput = new FileOutputStream(outFileName);

            // transfer bytes from the inputfile to the outputfile
            byte[] buffer = new byte[1024];
            int length;
            while ((length = myInput.read(buffer)) > 0) {
                myOutput.write(buffer, 0, length);
            }
            // Close the streams
            myOutput.flush();
            myOutput.close();
            myInput.close();
            } catch (Exception e) {
                e.printStackTrace(new PrintWriter("Erreur"));
            }
        }
    }