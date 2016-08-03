package com.shawon.yousuf.zipmanager.util;

/**
 * Created by Yousuf on 8/3/2016.
 */

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;


public class Compress {
    private static final int BUFFER = 2048;

    private String[] _files;
    private String _zipFile;

    ProgressDialog pDialog;

    private String TAG = getClass().getSimpleName();

    public Compress(Context context, String[]files, String zipFile) {
        _files = files;
        _zipFile = zipFile;

        pDialog = new ProgressDialog(context);
    }

    public void zip() {

        if( _files != null && _zipFile != null ){
            new CreateZipTask().execute();
        }

    }



    private class CreateZipTask extends AsyncTask<Void, Void, Boolean>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            if (pDialog != null) {
                pDialog.show();
                pDialog.setMessage("Processing ....");
            }

        }

        @Override
        protected Boolean doInBackground(Void... voids) {


            try  {
                BufferedInputStream origin = null;
                FileOutputStream dest = new FileOutputStream(_zipFile);

                ZipOutputStream out = new ZipOutputStream(new BufferedOutputStream(dest));

                byte data[] = new byte[BUFFER];

                for(int i=0; i < _files.length; i++) {
                    Log.v("Compress", "Adding: " + _files[i]);
                    FileInputStream fi = new FileInputStream(_files[i]);
                    origin = new BufferedInputStream(fi, BUFFER);
                    ZipEntry entry = new ZipEntry(_files[i].substring(_files[i].lastIndexOf("/") + 1));
                    out.putNextEntry(entry);
                    int count;
                    while ((count = origin.read(data, 0, BUFFER)) != -1) {
                        out.write(data, 0, count);
                    }
                    origin.close();
                }

                out.close();
                Log.d(TAG, "Finish zipping");
                return true;
            } catch(Exception e) {
                e.printStackTrace();

                return false;
            }

        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);

            if (pDialog != null) {
                pDialog.hide();
                pDialog.dismiss();
            }



        }
    }

}
