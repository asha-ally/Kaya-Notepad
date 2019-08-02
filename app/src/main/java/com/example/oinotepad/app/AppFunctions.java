package com.example.oinotepad.app;

import android.app.Activity;
import android.app.Application;
import android.app.TimePickerDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.View;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class AppFunctions extends Application {

    public static void func_showToast(Context context, CharSequence msg) {
        Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
    }

    public static boolean isInternetConnected(Context context) {
        boolean flag = false;
        ConnectivityManager connectivityManager =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();

        if(activeNetworkInfo != null && activeNetworkInfo.isConnected()){
            flag = true;
        }
        return flag;
    }


    public static String func_formatDate(int year, int month, int day) {

        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(0);
        cal.set(year, month, day);
        Date date = cal.getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(date);
    }


    public static String func_formatDateFromString(String value) {

        if(value.length() == 13){
            Long raw_time_long = Long.parseLong(value);
            java.util.Date new_time = new java.util.Date(raw_time_long);
            SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy"); //, HH:mm
            return sdf.format(new_time);
        } else {
            return value;
        }

        //Date date = null;
        /*if(value.length() > 6){
            SimpleDateFormat fromUser = new SimpleDateFormat("yyyy-mm-dd");
            SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy");

            try {
                String reformat = sdf.format(fromUser.parse(value));
                return reformat;
            }
            catch (Exception e) {
                Log.d("DateFromString", e.getLocalizedMessage());
            }
        }*/

    }


    public static String func_formatUnixDate(String raw_time) {

        Long raw_time_long = Long.parseLong(raw_time);
        if(raw_time.length() < 13){
            raw_time_long = raw_time_long * 1000;
        }
        java.util.Date new_time = new java.util.Date(raw_time_long);
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy, HH:mm");

        return sdf.format(new_time);
    }



    public static String func_formatDecimal(double val) {

        DecimalFormat form = new DecimalFormat("0.00");
        String value_out = String.valueOf(form.format(val));

        return value_out;
    }


    public static  String func_sanitizePhoneNumber(String phone) {

        if(phone.equals("")){
            return "";
        }

        else if (phone.length() < 11 & phone.startsWith("0")) {
            String p = phone.replaceFirst("^0", "254");
            return p;
        }

        else if(phone.length() == 13 && phone.startsWith("+")){
            //String p = phone.replaceFirst("^+", "");
            String p = phone.substring(1);
            return p;
        } else {
            return phone;
        }
    }


    public static int[] func_currentTime(){

        Calendar calendar = Calendar.getInstance();
        int currentDay = calendar.get(Calendar.DATE);
        int currentMonth = calendar.get(Calendar.MONTH);
        int currentYear = calendar.get(Calendar.YEAR);
        int currentHour = calendar.get(Calendar.HOUR_OF_DAY);
        int currentMinute = calendar.get(Calendar.MINUTE);

        String currDate = currentYear + "-" + currentMonth + "-" + currentDay;
        String currTime = currentHour + ":" + currentMinute;

        int [] dateVals;

        dateVals = new int[5];
        dateVals[0] = currentYear;
        dateVals[1] = currentMonth;
        dateVals[2] = currentDay;
        dateVals[3] = currentHour;
        dateVals[4] = currentMinute;

        return dateVals;

    }


    public static void appListenerTimeChange(final Activity context, final EditText date_field) {

        final int [] currDate = func_currentTime();
        date_field.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                TimePickerDialog timePickerDialog = new TimePickerDialog(context, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hourOfDay, int minutes) {
                        String min_sufuri = "", hr_sufuri = "";
                        if(hourOfDay < 10) { hr_sufuri = "0"; }
                        if(minutes < 10) { min_sufuri = "0"; }
                        date_field.setText(hr_sufuri + hourOfDay + ":" + min_sufuri + minutes);
                    }
                }, currDate[3], currDate[4], false);

                timePickerDialog.show();
            }
        });

    }

}
