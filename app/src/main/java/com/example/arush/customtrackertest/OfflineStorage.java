package com.example.arush.customtrackertest;

import android.content.Context;

import com.google.firebase.database.FirebaseDatabase;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Created by arush on 7/16/2016.
 */
public class OfflineStorage {
    private static int Internal_Storage_Count = 0;
    private static File path;
    private static File file;
    private static FileWriter fileWriter;

    public OfflineStorage(Context context) {
        path = context.getFilesDir();
        file = new File(path, "offline_data_storage.txt");
        try {
            fileWriter = new FileWriter(path, true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public int getNumberofEntries() {
        return Internal_Storage_Count;
    }

    public void addData(String time, double latitude, double longitude) {
        String newline = System.getProperty("line.separator");
        PrintWriter writer = new PrintWriter(fileWriter);
        String data = String.format("%s, %d, %d %s", time, latitude, longitude, newline);
        writer.write(data);

        Internal_Storage_Count++;
    }

    public void optimizeStorage(int time_interval) {

    }

    public void sendToServer() throws IOException {
        //FirebaseDastabase database = new FirebaseDatabase();
        resetStorage();
    }

    private void resetStorage() throws IOException {
        FileWriter fw_temp = new FileWriter(path);
        PrintWriter pw_temp = new PrintWriter(fw_temp);
        pw_temp.write("");
        pw_temp.flush();
        pw_temp.close();
    }
}
