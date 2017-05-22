package com.nehasharma.security;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

public class MainActivity extends AppCompatActivity {

    EditText pwd;
    Button submit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        submit = (Button) findViewById(R.id.button);
        pwd = (EditText) findViewById(R.id.pwd);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {
                    String salt =  generateKey();
                    String password = salt + pwd.getText().toString();
                    Toast.makeText(getApplicationContext(), password, Toast.LENGTH_SHORT).show();

                    //salting complete
                    MessageDigest md = MessageDigest.getInstance("SHA256");
                    md.reset();
                    md.update(password.getBytes("UTF-8"));

                    //now lets view it
                    byte hashCode[] = md.digest();
                    StringBuffer generatedOutput = new StringBuffer();
                    for (int i = 0; i < hashCode.length; i++) {
                        String hex = Integer.toHexString(0xFF & hashCode[i]);
                        if (hex.length() == 1) {
                            generatedOutput.append('0');
                        }
                        generatedOutput.append(hex);

                    }

                    Toast.makeText(getApplicationContext(), generatedOutput.toString(), Toast.LENGTH_SHORT).show();
                } catch (Exception ei) {
                    Toast.makeText(getApplicationContext(), ei.toString(), Toast.LENGTH_SHORT).show();

                }
            }
        });
    }
    public static String generateKey() throws NoSuchAlgorithmException {
        // Generate a 256-bit key
        final int outputKeyLength = 256;

        SecureRandom secureRandom = new SecureRandom();
        // Do *not* seed secureRandom! instead Automatically seed from system entropy.
        KeyGenerator keyGen = KeyGenerator.getInstance("AES");
        keyGen.init(outputKeyLength, secureRandom);
        SecretKey secretKey = keyGen.generateKey();
        byte encoded[] = secretKey.getEncoded();

        String key = Base64.encodeToString(encoded,0);

        return key;
    }
}