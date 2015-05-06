package com.epsi.sportkinator.sportkinator.activity;

import android.os.Environment;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.epsi.sportkinator.sportkinator.R;
import com.epsi.sportkinator.sportkinator.entities.Question;
import com.epsi.sportkinator.sportkinator.entities.Sport;
import com.epsi.sportkinator.sportkinator.entities.Tag;
import com.epsi.sportkinator.sportkinator.parser.SportXmlParser;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.nio.channels.FileChannel;


public class Main extends ActionBarActivity {

    private Question question;
    private Tag tagXML;
    private TextView textViewToChange;
    private Button buttonReplay;
    private Button buttonAddQuestion;
    private Button buttonShowAddQuestion;
    private LinearLayout formSport;
    private LinearLayout formQuestion;
    private LinearLayout playsButton;
    private EditText editTextSport;
    private EditText editTextQuestion;
    private String response;
    private boolean dontKnow=false;
    private Question questiondontKnow;
    private LinearLayout linearLayoutFindResponse;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textViewToChange = (TextView) findViewById(R.id.questionText);
        buttonReplay =(Button) findViewById(R.id.buttonReplay);
        buttonAddQuestion =(Button) findViewById(R.id.buttonAddQuestion);
        buttonShowAddQuestion =(Button) findViewById(R.id.buttonShowAddQuestion);

        formSport =(LinearLayout) findViewById(R.id.formSport);
        formQuestion =(LinearLayout) findViewById(R.id.formQuestion);
        playsButton =(LinearLayout) findViewById(R.id.playsButton);

        linearLayoutFindResponse =(LinearLayout) findViewById(R.id.linearLayoutFindResponse);

        editTextSport = (EditText) findViewById (R.id.editTextSport);
        editTextQuestion = (EditText) findViewById (R.id.editTextQuestion);


        buttonReplay.setVisibility(View.GONE);
        buttonAddQuestion.setVisibility(View.GONE);
        formSport.setVisibility(View.GONE);
        formQuestion.setVisibility(View.GONE);
        buttonShowAddQuestion.setVisibility(View.GONE);
        linearLayoutFindResponse.setVisibility(View.GONE);

        try {
            File mFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/bdConnaissance.xml");
            if(!mFile.exists()) {
                copy("bdConnaissance.xml");
                Toast.makeText(getApplicationContext(), "Initialisation de la base de connaissance terminée.", Toast.LENGTH_SHORT).show();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        readTag(null);
    }

    private void readTag(String response) {
        SportXmlParser sportXmlParser = new SportXmlParser();

        InputStream inputStream = null;
      //  inputStream = getResources().openRawResource(R.raw.connaissance);


        File mFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/bdConnaissance.xml");

        try {
            inputStream = new FileInputStream(mFile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        //inputStream = getResources().getXml(R.xml.connaissance);

        question = sportXmlParser.readQuestion(inputStream, question, response);

        if (question.getName()==null)
        {

            textViewToChange.setText(
                    "Le sport auquel vous pensez est : " + question.getResponse() + " !!");

            buttonReplay.setVisibility(View.VISIBLE);
            buttonShowAddQuestion.setVisibility(View.VISIBLE);
            linearLayoutFindResponse.setVisibility(View.VISIBLE);
            playsButton.setVisibility(View.GONE);




        }
        else{
            textViewToChange.setText(
                    question.getName());
        }


        try {
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
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
        buttonShowAddQuestion.setVisibility(View.GONE);
        playsButton.setVisibility(View.VISIBLE);
        buttonReplay.setVisibility(View.GONE);
        buttonAddQuestion.setVisibility(View.GONE);
        formSport.setVisibility(View.GONE);
        formQuestion.setVisibility(View.GONE);
        buttonShowAddQuestion.setVisibility(View.GONE);
        linearLayoutFindResponse.setVisibility(View.GONE);
    }

    public void buttonReplay(View view){
        playsButton.setVisibility(View.VISIBLE);
        buttonReplay.setVisibility(View.GONE);
        buttonAddQuestion.setVisibility(View.GONE);
        formSport.setVisibility(View.GONE);
        formQuestion.setVisibility(View.GONE);
        buttonShowAddQuestion.setVisibility(View.GONE);
        linearLayoutFindResponse.setVisibility(View.GONE);
        question=null;
        readTag(null);
    }

    public void buttonShowAddQuestion(View view){
        formSport.setVisibility(View.VISIBLE);
        formQuestion.setVisibility(View.VISIBLE);
        buttonAddQuestion.setVisibility(View.VISIBLE);
    }

    public void buttonAddQuestion(View view){
        SportXmlParser sportXmlParser = new SportXmlParser();
        InputStream inputStream = null;
       // inputStream = getResources().openRawResource(R.raw.connaissance);

        File mFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/bdConnaissance.xml");

        try {
            inputStream = new FileInputStream(mFile);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        if(!sportXmlParser.sportAllReadyExists(inputStream,editTextSport.getText().toString())){
            //inputStream = getResources().getXml(R.xml.connaissance);
            Question newQuestion = new Question(editTextQuestion.getText().toString(),response,"id");
            Sport newSport= new Sport(editTextSport.getText().toString());
            try {
                sportXmlParser.addQuestion(this,question.getResponse(),newQuestion,newSport);
            } catch (Exception e) {
                Toast.makeText(getApplicationContext(), "Une erreur est survenue", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
            Toast.makeText(getApplicationContext(), "Le sport a bien été ajouté", Toast.LENGTH_SHORT).show();
        }
        else{
            Toast.makeText(getApplicationContext(), "Le sport existe deja !", Toast.LENGTH_SHORT).show();
        }

        playsButton.setVisibility(View.VISIBLE);
        buttonReplay.setVisibility(View.GONE);
        buttonAddQuestion.setVisibility(View.GONE);
        formSport.setVisibility(View.GONE);
        formQuestion.setVisibility(View.GONE);
        buttonShowAddQuestion.setVisibility(View.GONE);
        question=null;
        readTag(null);
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