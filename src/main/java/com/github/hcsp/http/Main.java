package com.github.hcsp.http;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class Main {
    public static void main(String[] args) throws IOException {
        URL url = new URL ("https://sina.cn/");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection ();
        connection.setRequestMethod ("GET");
        connection.setRequestProperty ("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/65.0.3325.181 Safari/537.36");
        connection.connect ();
        int code = connection.getResponseCode ();
        InputStream inputStream = connection.getInputStream ();
        StringWriter writer = new StringWriter ();
        IOUtils.copy (inputStream, writer, "UTF-8");

        String html = writer.toString ();
        System.out.println (html);
    }
}
