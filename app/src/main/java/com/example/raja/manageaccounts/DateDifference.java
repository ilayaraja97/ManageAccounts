package com.example.raja.manageaccounts;

import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by raja on 07/07/17.
 */

public class DateDifference {
    /*//test fragment
	public static void main(String args[]){
		Date n=new Date(1436105600300l);
		System.out.println(n+"  "+relativeDate(n));
	}
	*/
    public static String relativeDate(Date date) {
        // takes UTC time
        // function to compare date to present and print period elapsed
        Calendar d = Calendar.getInstance();
        Log.d("ilaya_",date.toString());
        d.setTime(date);
        //d.setTimeZone(TimeZone.getTimeZone("GMT"));
        Calendar now= Calendar.getInstance();
        long difference=now.getTimeInMillis()-d.getTimeInMillis();
        Log.d("ilaya_",""+d.getTime()+now.getTime());
        Log.d("ilaya_time"," "+difference);
        if(difference<1)
        {
            return new SimpleDateFormat("HH:mm:ss MM/dd/yyyy", Locale.getDefault()).format(date);
        }
        final int divSet[]={1000,60,60,24,7,4,12,Integer.MAX_VALUE};
        final String timeSet[]={"millisecond","second","minute","hour","day","week","month","year"};
        int info=0,at=0;
        for(int i=0;i<divSet.length;i++)
        {
            if(difference%divSet[i]!=0)
            {
                info=(int) (difference%divSet[i]);
                at=i;
            }
            difference/=divSet[i];
        }
        if(info!=1)
        {
            if(at==timeSet.length-1)
                return (SimpleDateFormat.getDateInstance()).format(date);
            if(at>4)
            {
                return (SimpleDateFormat.getDateInstance()).format(date);
            }
            return (info+" "+timeSet[at]+"s ago");
        }
        else
        {
            switch(at)
            {
                case 0:
                case 1:
                case 2:
                case 3:
                    return ("a "+timeSet[at]+" ago");
                case 4:
                    return ("yesterday");
                case 5:
                case 6:
                case 7:
                    return (SimpleDateFormat.getDateInstance()).format(date);
                    //return ("last "+timeSet[at]);
            }
        }
        return (SimpleDateFormat.getDateInstance()).format(date);

    }
}
