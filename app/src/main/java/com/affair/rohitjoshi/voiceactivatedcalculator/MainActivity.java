package com.affair.rohitjoshi.voiceactivatedcalculator;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.speech.*;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Locale;


public class MainActivity extends AppCompatActivity {

    private ListView listoutput;
    private TextView txtSpeechInput;    //OUTPUT STRING DISPLAY
    private TextToSpeech voiceout;      //VOICE OUTPUT
    private ImageButton btnSpeak;       //MICROPHONE BUTTON
    private final int REQ_CODE_SPEECH_INPUT = 100;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        /* Code for Text to Speech Engine initialization */
        voiceout = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status == voiceout.SUCCESS)
                {
                    int result =   voiceout.setLanguage(Locale.ENGLISH);                                //SETTING LANGUAGE TO ENGLISH
                    if(result == voiceout.LANG_MISSING_DATA || result == voiceout.LANG_NOT_SUPPORTED)
                    {
                        Log.e( "VOICE OUTPUT",  "Language not Supported");
                    }
                    else
                    {

                    }

                }

                else
                {
                    Log.e( "VOICE OUTPUT",  "Initialization failure");
                }

            }
        });

        //listoutput = (ListView) findViewById(R.id.list_output);
        txtSpeechInput = (TextView) findViewById(R.id.txtSpeechInput);
        btnSpeak = (ImageButton) findViewById(R.id.btnSpeak);

        // hide the action bar
        // getActionBar().hide();

        btnSpeak.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                promptSpeechInput();
            }
        });

        final Intent mSpeechRecognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        mSpeechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);

    }

    /**
     * Showing google speech input dialog
     * */
    private void promptSpeechInput() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT,
                getString(R.string.speech_prompt));
        try {
            startActivityForResult(intent, REQ_CODE_SPEECH_INPUT);
        } catch (ActivityNotFoundException a) {
            Toast.makeText(getApplicationContext(),
                    getString(R.string.speech_not_supported),
                    Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Receiving speech input
     * */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQ_CODE_SPEECH_INPUT: {
                if (resultCode == RESULT_OK && null != data) {

                    ArrayList<String> result = data
                            .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);

                    String string=result.get(0);

                    char c = 'a';

                    for(int i=0; i < string.length() ; i++)
                    {
                        char check = string.charAt(i);

                        if( check == '+' )
                        {
                            c='+';
                            break;
                        }

                        else if ( check == '-')
                        {
                            c = '-';
                            break;
                        }


                        else if ( check == 'x' || check == '*' )
                        {
                            c = 'x';
                            break;
                        }


                        else if ( check == '/')
                        {
                            c = '/';
                            break;
                        }

                    }


                    if(c =='+') {
                        String[] expression = string.split("\\+");
                        Log.d("exp", expression[0]);
                        Log.d("exp", expression[1]);
                        int x = 0, y = 0, z = 0;
                        x = Integer.parseInt(expression[0].trim());
                        y = Integer.parseInt(expression[1].trim());
                        z = x + y;
                        string = string + " = " + String.valueOf(z);

                        txtSpeechInput.setText(string);
                        speak();
                    }

                    if(c =='-') {
                        String[] expression = string.split("\\-");
                        Log.d("exp", expression[0]);
                        Log.d("exp", expression[1]);
                        int x = 0, y = 0, z = 0;
                        x = Integer.parseInt(expression[0].trim());
                        y = Integer.parseInt(expression[1].trim());
                        z = x - y;
                        string = string + " = " + String.valueOf(z);

                        txtSpeechInput.setText(string);
                        speak();
                    }

                    if(c =='x') {
                        String[] expression = string.split("x");
                        Log.d("exp", expression[0]);
                        Log.d("exp", expression[1]);
                        int x = 0, y = 0, z = 0;
                        x = Integer.parseInt(expression[0].trim());
                        y = Integer.parseInt(expression[1].trim());
                        z = x * y;
                        string = string + " = " + String.valueOf(z);

                        txtSpeechInput.setText(string);
                        speak();
                    }

                    if(c =='/') {
                        String[] expression = string.split("\\/");
                        Log.d("exp", expression[0]);
                        Log.d("exp", expression[1]);
                        float x = 0, y = 0, z = 0;
                        int a;
                        x = Integer.parseInt(expression[0].trim());
                        y = Integer.parseInt(expression[1].trim());
                        try {

                            z = x / y;
                            string = string + " = " + String.valueOf(z);
                        }

                        catch (ArithmeticException dbz)
                        {
                            string = "The denominator can't be zero.";
                        }
                        txtSpeechInput.setText(string);
                        speak();

                    }

                    if (c=='a')
                    {
                        string = "Please try again";

                        txtSpeechInput.setText(string);
                        speak();

                    }
                }
                break;
            }

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
       // getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    private void speak()
    {
        String text = txtSpeechInput.getText().toString();
        voiceout.setPitch(1/10);
        voiceout.setSpeechRate(1/2);

        voiceout.speak(text,TextToSpeech.QUEUE_FLUSH, null);
    }

    @Override
    protected void onDestroy()
    {
        if(voiceout != null)
        {
            voiceout.stop();
            voiceout.shutdown();
        }
        super.onDestroy();
    }
}