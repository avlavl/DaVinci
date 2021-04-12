package com.aioros.investor;

import android.os.Environment;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;

/**
 * Created by aizhang on 2017/6/8.
 */

public class FileUtility {
    private String storageDirectory;
    public ArrayList<String> dateList1;
    public ArrayList<String> dateList2;
    public ArrayList<Double> closeList1;
    public ArrayList<Double> closeList2;
    public int rows1 = 0;
    public int rows2 = 0;
    public ArrayList<String> futuresDateList;

    public FileUtility() {
        storageDirectory = Environment.getExternalStorageDirectory() + "/";
    }

    public String getStorageDirectory() {
        return storageDirectory;
    }

    public int importDataFile1(String fileName) {
        dateList1 = new ArrayList<>();
        closeList1 = new ArrayList<>();
        try {
            File file = new File(storageDirectory + fileName);
            if (!file.exists()) {
                return -1;
            }
            InputStreamReader isr = new InputStreamReader(new FileInputStream(file), "gbk");
            BufferedReader br = new BufferedReader(isr);
            br.readLine();
            br.readLine();
            String line;
            while ((line = br.readLine()) != null) {
                String[] words = line.split("\t");
                dateList1.add(words[0]);
                closeList1.add(Double.parseDouble(words[4]));
            }
            rows1 = dateList1.size();
            br.close();
            isr.close();
            return 0;
        } catch (IOException | NumberFormatException e) {
            e.printStackTrace();
            return -1;
        }
    }

    public int importDataFile2(String fileName) {
        dateList2 = new ArrayList<>();
        closeList2 = new ArrayList<>();
        try {
            File file = new File(storageDirectory + fileName);
            if (!file.exists()) {
                return -1;
            }
            InputStreamReader isr = new InputStreamReader(new FileInputStream(file), "gbk");
            BufferedReader br = new BufferedReader(isr);
            br.readLine();
            br.readLine();
            String line;
            while ((line = br.readLine()) != null) {
                String[] words = line.split("\t");
                dateList2.add(words[0]);
                closeList2.add(Double.parseDouble(words[4]));
            }
            rows2 = closeList2.size();
            br.close();
            isr.close();
            return 0;
        } catch (IOException | NumberFormatException e) {
            e.printStackTrace();
            return -1;
        }
    }

    public int importFuturesDate(String fileName) {
        futuresDateList = new ArrayList<>();
        try {
            File file = new File(storageDirectory + fileName);
            if (!file.exists()) {
                return -1;
            }
            InputStreamReader isr = new InputStreamReader(new FileInputStream(file), "gbk");
            BufferedReader br = new BufferedReader(isr);
            String line;
            while ((line = br.readLine()) != null) {
                futuresDateList.add(line);
              }
            br.close();
            isr.close();
            return 0;
        } catch (IOException | NumberFormatException e) {
            e.printStackTrace();
            return -1;
        }
    }

    public File createSDFile(String fileName) throws IOException {
        File file = new File(storageDirectory + fileName);
        file.createNewFile();
        return file;
    }

    public File createSDDir(String dirName) {
        File dir = new File(storageDirectory + dirName);
        dir.mkdir();
        return dir;
    }

    public boolean isFileExist(String fileName) {
        File file = new File(storageDirectory + fileName);
        return file.exists();
    }

    public File write2SDFromInput(String path, String fileName, InputStream input) {
        File file = null;
        OutputStream output = null;
        try {
            createSDDir(path);
            file = createSDFile(path + fileName);
            output = new FileOutputStream(file);
            byte buffer[] = new byte[4 * 1024];
            while ((input.read(buffer)) != -1) {
                output.write(buffer);
            }
            output.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                output.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return file;
    }
}

