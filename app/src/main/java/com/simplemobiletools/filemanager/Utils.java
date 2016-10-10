package com.simplemobiletools.filemanager;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;

import com.simplemobiletools.filemanager.models.FileDirItem;

import java.text.DecimalFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utils {
    public static String getFilename(final String path) {
        return path.substring(path.lastIndexOf("/") + 1);
    }

    public static String getFileExtension(String fileName) {
        return fileName.substring(fileName.lastIndexOf(".") + 1, fileName.length()).toLowerCase();
    }

    public static void showToast(Context context, int resId) {
        Toast.makeText(context, context.getResources().getString(resId), Toast.LENGTH_SHORT).show();
    }

    public static boolean hasStoragePermission(Context cxt) {
        return ContextCompat.checkSelfPermission(cxt, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
    }

    public static boolean isNameValid(String name) {
        final Pattern pattern = Pattern.compile("^[-_.A-Za-z0-9 ]+$");
        final Matcher matcher = pattern.matcher(name);
        return matcher.matches();
    }

    public static String getFormattedSize(FileDirItem item) {
        final long size = item.getSize();
        return formatSize(size);
    }

    public static String formatSize(long size) {
        if (size <= 0)
            return "0 B";

        final String[] units = {"B", "kB", "MB", "GB", "TB"};
        final int digitGroups = (int) (Math.log10(size) / Math.log10(1024));
        return new DecimalFormat("#,##0.#").format(size / Math.pow(1024, digitGroups)) + " " + units[digitGroups];
    }
}
