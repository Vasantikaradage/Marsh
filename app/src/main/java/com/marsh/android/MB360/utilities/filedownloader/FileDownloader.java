package com.marsh.android.MB360.utilities.filedownloader;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;

import androidx.core.content.ContextCompat;

import com.marsh.android.MB360.utilities.AppLocalConstant;
import com.marsh.android.MB360.utilities.AppServerConstants;
import com.marsh.android.MB360.utilities.LogMyBenefits;
import com.marsh.android.MB360.utilities.LogTags;
import com.marsh.android.MB360.utilities.UtilMethods;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class FileDownloader {

    private final Executor executor = Executors.newSingleThreadExecutor();

    public void downloadFile(final Activity activity, final Context context, final String fileUrl, String fileNameWithExtension, final DownloadCallback callback) {
        if (hasReadWritePermission(context)) {
            executor.execute(() -> {
                try {
                    LogMyBenefits.d(LogTags.DOWNLOAD_ACTIVITY, "File Download Started");
                    LogMyBenefits.d(LogTags.DOWNLOAD_ACTIVITY, "File URL :" + fileUrl);

                    URL url = new URL(fileUrl);
                    HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                    urlConnection.setRequestMethod("GET");
                    urlConnection.connect();

                    InputStream inputStream = new BufferedInputStream(urlConnection.getInputStream());
                    String destinationPath = getDestinationPath(context, fileNameWithExtension);
                    LogMyBenefits.d(LogTags.DOWNLOAD_ACTIVITY, "File Download Directory :" + destinationPath);
                    FileOutputStream outputStream = new FileOutputStream(destinationPath);

                    byte[] buffer = new byte[1024];
                    int bytesRead;
                    while ((bytesRead = inputStream.read(buffer)) != -1) {
                        outputStream.write(buffer, 0, bytesRead);
                    }

                    outputStream.flush();
                    outputStream.close();
                    inputStream.close();

                    urlConnection.disconnect();

                    // Notify UI that the file download is completed
                    callback.onDownloadComplete(destinationPath);
                    LogMyBenefits.d(LogTags.DOWNLOAD_ACTIVITY, "File Downloaded @ " + destinationPath);
                } catch (Exception e) {
                    // Notify UI about any error occurred during download
                    if (e instanceof FileNotFoundException) {
                        callback.onDownloadFailed("File not found!");
                    } else {
                        callback.onDownloadFailed(e.getMessage());
                    }

                }
            });
        } else {
            // Request permission from the user if not granted
            // This should trigger the system permission dialog
            callback.onDownloadFailed("Read/Write permissions not granted");
            AppLocalConstant.verifyStoragePermissions(activity);
        }
    }

    private String getDestinationPath(Context context, String fileNameWithExtension) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            // For Android 10 (API level 29) and above, use MediaStore
            // Scoped Storage, save the file in app-specific directory
            return new File(context.getFilesDir(), fileNameWithExtension).getAbsolutePath();
        } else {
            // For earlier versions, save the file in public directory
            // ExternalStorageDirectory is deprecated in API level 29
            return new File(context.getFilesDir(), fileNameWithExtension).getAbsolutePath();
        }
    }

    private boolean hasReadWritePermission(Context context) {
        if (Build.VERSION.SDK_INT < 32) {
            return ContextCompat.checkSelfPermission(context,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                    && ContextCompat.checkSelfPermission(context,
                    Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
        } else {
            return true;
        }
    }

    public interface DownloadCallback {
        void onDownloadComplete(String filePath);

        void onDownloadFailed(String errorMessage);
    }
}
