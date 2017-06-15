package com.aioros.investor;

import android.os.Environment;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by aizhang on 2017/6/8.
 */

public class HttpDownloader {
    private URL url = null;

    public String download(String urlStr) {
        StringBuffer sb = new StringBuffer();
        BufferedReader br = null;
        String line = null;

        try {
            url = new URL(urlStr);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            br = new BufferedReader(new InputStreamReader(urlConnection.getInputStream(), "GB2312"));
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
            br.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

    public int download(String urlStr, String path, String name) {
        ArrayList<String> stringList = new ArrayList<>();
        BufferedReader br = null;
        String line = null;

        try {
            url = new URL(urlStr);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            br = new BufferedReader(new InputStreamReader(urlConnection.getInputStream(), "GB2312"));
            while ((line = br.readLine()) != null) {
                stringList.add(line);
            }
            br.close();

            String storageDir = Environment.getExternalStorageDirectory().toString();
            String filePath = storageDir + "/" + path + "/" + name;
            File file = new File(filePath);
            if (file.exists()) {
                file.delete();
            }
            new File(storageDir + "/" + path).mkdir();
            file.createNewFile();
            FileWriter fw = new FileWriter(file);
            PrintWriter pw = new PrintWriter(fw);
            int size = stringList.size();
            for (int i = 0; i < size - 1; i++) {
                pw.println(stringList.get(size - 1 - i));
            }
            fw.flush();
            pw.close();
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
        return 0;
    }

    /**
     * 下载文件并写SD卡
     *
     * @param urlStr
     * @param path
     * @param fileName
     * @return 0-success,-1-fail,1-existed
     */
    public int downFile(String urlStr, String path, String fileName) {
        InputStream inputStream = null;
        try {
            FileUtility fileUtil = new FileUtility();
            if (fileUtil.isFileExist(path + fileName))
                return 1;
            else {
                inputStream = getInputStreamFromUrl(urlStr);
                File resultFile = fileUtil.write2SDFromInput(path, fileName, inputStream);
                if (resultFile == null)
                    return -1;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                inputStream.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return 0;
    }

    public InputStream getInputStreamFromUrl(String urlStr) throws MalformedURLException, IOException {
        url = new URL(urlStr);
        HttpURLConnection urlCon = (HttpURLConnection) url.openConnection();
        InputStream inputStream = urlCon.getInputStream();
        return inputStream;
    }
}