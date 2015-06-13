package com.epsi.sportkinator.sportkinator.activity;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.epsi.sportkinator.sportkinator.R;
import com.epsi.sportkinator.sportkinator.entities.Question;
import com.epsi.sportkinator.sportkinator.entities.Sport;
import com.epsi.sportkinator.sportkinator.parser.SportXmlParser;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class FormSport extends ActionBarActivity {
    private InputStream inputStream;
    private String sport;
    private String selectedImagePath;
    EditText nameSport;
    EditText nameQuestion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_sport);

        nameSport = (EditText) findViewById(R.id.nameSport);
        nameQuestion = (EditText) findViewById(R.id.nameQuestion);

        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if(extras == null) {
                sport= null;
            } else {
                sport= extras.getString("sport");
            }
        } else {
            sport= (String) savedInstanceState.getSerializable("sport");
        }
        Button buttonLoadImage = (Button)findViewById(R.id.addImageButton);
        buttonLoadImage.setOnClickListener(new Button.OnClickListener(){

            public void onClick(View arg0) {

                // in onCreate or any event where your want the user to
                // select a file
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,
                        "Select Picture"), 1);
            }
        });
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        //super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == 1) {
                Uri selectedImageUri = data.getData();
                selectedImagePath = getPath(selectedImageUri);
                Button buttonLoadImage = (Button)findViewById(R.id.addImageButton);
                buttonLoadImage.setClickable(false);
                buttonLoadImage.setBackgroundColor(Color.GREEN);
            }
        }
    }

    public String getPath(Uri uri) {
        // just some safety built in
        if( uri == null ) {
            // TODO perform some logging or show user feedback
            return null;
        }
        // try to retrieve the image from the media store first
        // this will only work for images selected from gallery
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        if( cursor != null ){
            int column_index = cursor
                    .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        }
        // this is our fallback here
        return uri.getPath();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_form_sport, menu);
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

    public void buttonAddQuestion(View view){
        InputStream inputStream = null;

        File mFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/bdConnaissance.xml");

        try {
            inputStream = new FileInputStream(mFile);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        SportXmlParser sportXmlParser = new SportXmlParser(inputStream);


        if(!sportXmlParser.sportAllReadyExists(nameSport.getText().toString())){

            Question newQuestion = new Question(nameQuestion.getText().toString(),"oui","id");
            Sport newSport= new Sport(nameSport.getText().toString(),selectedImagePath);
            try {
                sportXmlParser.addQuestion(this,sport,newQuestion,newSport);
            } catch (Exception e) {
                Toast.makeText(getApplicationContext(), "Une erreur est survenue", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
            Toast.makeText(getApplicationContext(), "Le sport a bien été ajouté", Toast.LENGTH_SHORT).show();
            Intent mainActivity = new Intent(FormSport.this, Main.class);
            startActivity(mainActivity);
        }
        else{
            Toast.makeText(getApplicationContext(), "Le sport existe deja !", Toast.LENGTH_SHORT).show();
        }

    }
}
