package com.example.raja.manageaccounts;

import android.content.Context;
import android.content.pm.PackageManager;
import android.media.MediaScannerConnection;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.util.*;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by raja on 06/09/17.
 */

public class ExportImport {
    private static Cipher cipher;
    public static String encrypt(String plainText, SecretKey secretKey)
            throws Exception {
        byte[] plainTextByte = plainText.getBytes();
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        byte[] encryptedByte = cipher.doFinal(plainTextByte);

        String encryptedText = Base64.encodeToString(encryptedByte,Base64.DEFAULT);
        return encryptedText;
    }

    public static String decrypt(String encryptedText, SecretKey secretKey)
            throws Exception {
        byte[] encryptedTextByte = Base64.decode(encryptedText,Base64.DEFAULT);
        cipher.init(Cipher.DECRYPT_MODE, secretKey);
        byte[] decryptedByte = cipher.doFinal(encryptedTextByte);
        String decryptedText = new String(decryptedByte);
        return decryptedText;
    }

    static void export(Context context) {
        String filename="managecounts.backup";
        DbHandler db=new DbHandler(context);
        String content="";
        try {
            content = db.exportDatabase().toString();
        }catch (Exception e){
            e.printStackTrace();
        }
        try {
            SecretKey secretKey = new SecretKeySpec("asdf5/*-asdf5/*-".getBytes(), "AES");
            cipher = Cipher.getInstance("AES");
            //char[] inputBytes = new char[content.length()];
            content = encrypt(content,secretKey);
        } catch (Exception e) {
            e.printStackTrace();
        }
        File file;
        FileOutputStream outputStream;
        try {
            file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), filename);
            outputStream = new FileOutputStream(file);
            outputStream.write(content.getBytes("UTF-8"));
            outputStream.close();
            MediaScannerConnection.scanFile(
                    context,
                    new String[]{file.getAbsolutePath()},
                    null,
                    null);
        } catch (IOException e) {
            e.printStackTrace();
        }
        db.close();
    }
    static void importDb(Context context) {
        String filename="managecounts.backup";
        DbHandler db=new DbHandler(context);
        String content="";
        File file;
        FileInputStream inputStream;
        try {
            file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), filename);
            inputStream = new FileInputStream(file);
            int i=0;
            while((i=inputStream.read())!=-1)
            {
                content+=(char)i;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.d("ilaya_import",content);
        try {
            //Log.d("ilaya_import",content);
            SecretKey secretKey = new SecretKeySpec("asdf5/*-asdf5/*-".getBytes(), "AES");
            cipher = Cipher.getInstance("AES");
            content = decrypt(content,secretKey);
            Log.d("ilaya_import",content);
            JSONObject json = new JSONObject(content);
            //Log.d("ilaya_import",json.getString("purpose"));
            db.importDatabase(json);
        } catch (Exception e) {
            e.printStackTrace();
        }
        db.close();
    }
}
