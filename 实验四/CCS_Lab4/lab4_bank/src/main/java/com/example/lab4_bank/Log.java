package com.example.lab4_bank;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class Log {

    public static FileOutputStream output;

    public Log() {
        try {
            File file = new File("log.txt");
            FileOutputStream output = new FileOutputStream(file, true);
            Log.output = output;
        } catch (IOException e) {
            System.out.println("open log.txt failed");
        }
    }
}
