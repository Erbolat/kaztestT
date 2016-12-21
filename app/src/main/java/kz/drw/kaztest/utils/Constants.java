package kz.drw.kaztest.utils;

import android.app.Activity;
import android.app.ProgressDialog;
import android.widget.EditText;

import java.io.IOException;
import java.io.InputStream;
import java.util.regex.Pattern;

/**
 * Created by Nurik on 11.07.2016.
 */
public class Constants {
    public static final String DOMAIN = "http://www.api.kaztest.com/";
    public static final String LOGIN = DOMAIN+"api/user/login";
    public static final String REGISTER = DOMAIN+"api/Register/Register";
    public static final String LIST_LAWS = DOMAIN+"api/Trenerovka/GetZakons";
    public static final String LIST_SCHOOLS = DOMAIN+"api/User/GetSchools?";
    public static final String LIST_CITIES = DOMAIN+"api/Default1/GetCity";
    public static final String REPAIR_PASS = DOMAIN+"api/User/SMS?loginlog=";
    public static final String AVATAR = DOMAIN+"api/User/Photo";

    public static final String TREN_SELECT_LAW = DOMAIN+"api/Trenerovka/Getgostest?";
    public static final String GET_LEVEL_TREN = DOMAIN+"api/Trenerovka/Trenerovka?";
    public static final String GET_LIST_TREN = DOMAIN+"api/Trenerovka/GetZakons";
    public static final String FINISH_LEVEL= DOMAIN+"api/Trenerovka/Addlevel?";
    public static final String GET_INFO= DOMAIN+"api/User/Getinfo";
    public static final String CORPUSA_PROGRAMMA1=DOMAIN+ "api/CorpusA/Programma1?";
    public static final String CORPUSA_PROGRAMMA2=DOMAIN+ "api/CorpusA/Programma2?";
    public static final String CORPUSB_PROGRAMMA1=DOMAIN+ "api/CorpusB/Programma1?";
    public static final String CORPUSB_PROGRAMMA2= DOMAIN+"api/CorpusB/Programma2?";
    public static final String CORPUSB_PROGRAMMA3=DOMAIN+ "api/CorpusB/Programma3?";
    public static final String PROFILE=DOMAIN+ "api/User/Userinfo/?userid=";
    public static final String CHANGE_PROFILE=DOMAIN+ "api/User/EditUser";
    public static final String CHANGE_PASS=DOMAIN+ "api/User/Changepass?userID=";
    public static final String GET_RATING_GOS=DOMAIN+ "api/User/AllRating?";
    public static final String GET_HISTORY=DOMAIN+ "api/User/Myhistory?userid=";
    public static final String SET_RESULT=DOMAIN+ "api/User/Result";
    public static final String ADD_PUSH=DOMAIN+ "api/User/Adddevice";
    public static final String DELETE_PUSH=DOMAIN+ "api/User/LogOut?userid=";
    public static final String MY_PREF = "KazTest" ;
    public static final String SET_RESULT_RATING = DOMAIN+"api/User/Rating" ;
    public static String Corpus = "";
    public static String Program = "1" ;
    public static Boolean kaztestLang=false;
    public static boolean isResult=false;
    public static boolean isTest=false;
    public static String language="ru";
    public static boolean isRating=false;
    public static boolean canpas = false;
    public static boolean isCORPUSB=false;


    // CHECK EDITTEXT IS EMPTY   TODO:  Constants.isEmpty(editText);
    public static boolean isEmpty(EditText myeditText) {
        return myeditText.getText().toString().trim().length() == 0;
    }

    public static String replaceStrNull(String object){
        if(object!=null){
            if(object.equals("nulll"))
                return  "";
            else
                return object;
        }
        else
            return "";
    }

    public static final Pattern EMAIL_ADDRESS_PATTERN = Pattern.compile(
            "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
                    "\\@" +
                    "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
                    "(" +
                    "\\." +
                    "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
                    ")+"
    );
    public static boolean checkEmail(String editText) {

        return EMAIL_ADDRESS_PATTERN.matcher(editText).matches();
    }


    // CHECK PASSWORD IS EQUALS   TODO:  Constants.checkPassword(editText1,editText2);
    public static boolean checkPassword(EditText editText, EditText editText2) {
        String text1 = editText.getText().toString();
        String text2 = editText2.getText().toString();
        return text1.equals(text2);
    }


    // PROGRESSDIALOG SHOW   TODO:  Constants.Show_ProgressDialog(this,"download ...");
    public static ProgressDialog progDialog;
    public static void Show_ProgressDialog(Activity activity, String message) {
        progDialog = new ProgressDialog(activity);
        progDialog.setMessage(message);
        progDialog.setCanceledOnTouchOutside(false);
        progDialog.show();
    }
    // PROGRESSDIALOG HISE   TODO:  Constants.Hide_ProgressDialog
    public static void Hide_ProgressDialog() {
        progDialog.dismiss();
    }
    public static String loadJSONFromAsset(Activity activity) {
        String json = null;
        try {

            InputStream is = activity.getAssets().open("services.json");

            int size = is.available();

            byte[] buffer = new byte[size];

            is.read(buffer);

            is.close();
            json = new String(buffer, "UTF-8");


        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;

    }


}
