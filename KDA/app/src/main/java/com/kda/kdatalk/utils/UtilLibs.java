package com.kda.kdatalk.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.kda.kdatalk.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class UtilLibs {

    public static boolean isNetworkConnect(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm != null) {
            NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
            return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
        }
        return false;
    }

    public static String addErrTextHtml(String source, String str_err) {
        //

        String convert_err = "<span style=\"color:red\"><strike>" + str_err + " " + "</strike> </span>";
        source += convert_err;

//        String htmt_str_err = "<p>Day la vi du ve the <span style=\"color:red\"><strike>the strike trong HTML</strike></span></p>";
        //String htmt_str_corr = "<p>Day la vi du ve the <span style=\"color:#30FF00\">the strike trong HTML</span></p>";
        return source;
    }

    public static String addCorrectTextHtml(String source, String str_correct) {
        //

        String convert_correct = "<span style=\"color:#30FF00\">" + str_correct + " " + "</span>";
        source += convert_correct;

//        String htmt_str_err = "<p>Day la vi du ve the <span style=\"color:red\"><strike>the strike trong HTML</strike></span></p>";
//        String htmt_str_corr = "<p>Day la vi du ve the <span style=\"color:#30FF00\">the strike trong HTML</span></p>";
        return source;
    }

    public static ArrayList<String> compare2String(String source, String fix_str) {

        ArrayList<String> result = new ArrayList<>();

        String source_result = "<span>"; //need "</p>
        String fix_result = "<span>";


        String[] source_split = source.split("\\s+");
        String[] fix_str_split = fix_str.split("\\s+");

        if (source_split.length < fix_str_split.length) {

            for (int i = 0; i < source_split.length; i++) {
                if (!source_split[i].equals(fix_str_split[i])) {
                    source_result = addErrTextHtml(source_result, source_split[i]) + "<span> </span>";
                    fix_result = addCorrectTextHtml(fix_result, fix_str_split[i]);

                } else {
                    source_result += " " + source_split[i] + " ";
                    fix_result += " " + fix_str_split[i] + " ";
                }
            }

            for (int i = source_split.length; i < fix_str_split.length; i++) {
                fix_result = addCorrectTextHtml(fix_result, fix_str_split[i]);
            }

        } else {
            for (int i = 0; i < fix_str_split.length; i++) {
                if (!source_split[i].equals(fix_str_split[i])) {
                    source_result = addErrTextHtml(source_result, source_split[i]);
                    fix_result = addCorrectTextHtml(fix_result, fix_str_split[i]);

                } else {
                    source_result += " " + source_split[i] + " ";
                    fix_result += " " + fix_str_split[i] + " ";
                }
            }

            for (int i = fix_str_split.length; i < source_split.length; i++) {
                source_result = addErrTextHtml(source_result, source_split[i]);
            }

        }


        source_result += "</span>";
        fix_result += "</span>";

        result.add(source_result);
        result.add(fix_result);

        // err -> correct


        return result;
    }

    public interface ListenerAlert {
        void cancel();
        void agree();
    }

    public static void showAlert(final Context context, String message, final ListenerAlert listenerAlert) {
        new AlertDialog.Builder(context)
                .setMessage(message)
                .setPositiveButton(R.string.lbl_agree, (dialog, which) -> {
                    dialog.cancel();
                    listenerAlert.agree();
                })
                .setNegativeButton(R.string.lbl_cancel, (dialog, which) -> {
                    dialog.cancel();
                    listenerAlert.cancel();
                })
                .show();
    }

    public static void showAlert(Context context, String message) {
        new AlertDialog.Builder(context)
                .setTitle("Thông báo!")
                .setMessage(message)

                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Continue with delete operation
                    }
                })
                // A null listener allows the button to dismiss the dialog and take no further action.
                .show();
    }

    public static String getDate(long milliSeconds, String dateFormat) {
        // Create a DateFormatter object for displaying date in specified format.
        SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);

        // Create a calendar object that will convert the date and time value in milliseconds to date.
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliSeconds);
        return formatter.format(calendar.getTime());
    }

}
