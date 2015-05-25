package com.teentitans.cakeproject.utils;


import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class ConnectionUtil {

    public static void loadURL(String link, String parameters) throws IOException {

        HttpURLConnection connection;
        OutputStreamWriter request;

        URL url = new URL(link);
        connection = (HttpURLConnection) url.openConnection();
        connection.setDoOutput(true);
        connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        connection.setRequestMethod("POST");

        request = new OutputStreamWriter(connection.getOutputStream());
        request.write(parameters);
        request.flush();
        request.close();
        connection.disconnect();
    }

    public static String getResponseFromURL(String link) throws IOException {

        HttpURLConnection connection;

        URL url = new URL(link);
        connection = (HttpURLConnection) url.openConnection();
        connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

        InputStream isr = new BufferedInputStream(connection.getInputStream());
        byte[] bytes = new byte[10000];
        ByteArrayOutputStream baos = new ByteArrayOutputStream(10000);
        while (true) {
            int numRead = isr.read(bytes);
            if (numRead == -1)
                break;
            baos.write(bytes, 0, numRead);
        }

        String response = new String(baos.toByteArray(), "UTF-8");
        isr.close();
        connection.disconnect();

        return response;
    }

    public static String getResponseFromURL(String link, String parameters) throws IOException {

        HttpURLConnection connection;
        OutputStreamWriter request;

        URL url = new URL(link);
        connection = (HttpURLConnection) url.openConnection();
        connection.setDoOutput(true);
        connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        connection.setRequestMethod("POST");

        request = new OutputStreamWriter(connection.getOutputStream());
        request.write(parameters);
        request.flush();
        request.close();

        InputStream isr = new BufferedInputStream(connection.getInputStream());
        byte[] bytes = new byte[10000];
        ByteArrayOutputStream baos = new ByteArrayOutputStream(10000);
        while (true) {
            int numRead = isr.read(bytes);
            if (numRead == -1)
                break;
            baos.write(bytes, 0, numRead);
        }

        String response = new String(baos.toByteArray(), "UTF-8");
        isr.close();
        connection.disconnect();

        return response;
    }
}
