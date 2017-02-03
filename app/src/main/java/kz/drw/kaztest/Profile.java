package kz.drw.kaztest;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.FirebaseApp;
import com.google.firebase.iid.FirebaseInstanceId;
import com.gregacucnik.EditTextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;


import kz.drw.kaztest.utils.AppController;
import kz.drw.kaztest.utils.CircleImageView;
import kz.drw.kaztest.utils.Constants;
import kz.drw.kaztest.utils.Crop;
import kz.drw.kaztest.utils.MyRequest;
import kz.drw.kaztest.utils.epay.EpayActivity;
import kz.drw.kaztest.utils.epay.MyActivity;

import static kz.drw.kaztest.Profile.dlg1;
import static kz.drw.kaztest.Profile.newPass;
import static kz.drw.kaztest.Profile.newPassRe;
import static kz.drw.kaztest.Profile.oldPass;


public class Profile extends Fragment {

    View view;
    Bitmap rotatedBMP=null;
    EditTextView tvName,tvPhone;
    String photo="";
    ImageLoader imageLoader = AppController.getInstance().getImageLoader();
    TextView tvBirth, tvCity, tvSchool;
    public  static Boolean isBacked = false;
    CircleImageView ava;
    String name="", lastname="", patron="", lastName="";
    public  static  String devic="";
    public  static  String oldPass="", newPass="", newPassRe="";
    Button btnPriced, btnChangePass, btnSave, btnWomen, btnMen;
    String [] fio, cityNames, schoolNames;
    String userID="", myBirth="", base64="", myCity="", mySchool="";
    Integer[] cityIds, schoolIds;
    Integer idCity=0, idSchool=0;
    TextView tvPrice;
    int isPol =0 , old=0;
    int myYear = 2000, myMonth=0, myDay = 1;
    int DIALOG_DATE = 1;
    private static  Boolean isMen=true, isCity=false, isSchool=false;
    LinearLayout layAvatar;
    MainActivity main;
    public static  Dialog1 dlg1;

    public Profile() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view =  inflater.inflate(R.layout.profile, container, false);
       // ((MainActivity) getActivity()).setActionBarTitle("Профиль");
                initResources();
                GetCities();
                GetProfile();



        btnChangePass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dlg1 = new Dialog1();
                dlg1.setStyle(DialogFragment.STYLE_NO_TITLE, 0);
                dlg1.show(getActivity().getSupportFragmentManager(), "dlg1");
            }
        });

        tvCity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GetCitiesArray();
            }
        });

        tvSchool.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(idCity==0) Toast.makeText(getActivity(), getResources().getString(R.string.setCity), Toast.LENGTH_SHORT).show();
                else GetSchools();
            }
        });
        btnMen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnMen.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                btnWomen.setBackgroundColor(getResources().getColor(R.color.colorWhite2));
                btnMen.setTextColor(getResources().getColor(R.color.colorWhite));
                btnWomen.setTextColor(getResources().getColor(R.color.colorBlack));
                isPol = 1;
            }
        });
        btnWomen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnMen.setBackgroundColor(getResources().getColor(R.color.colorWhite2));
                btnWomen.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                btnWomen.setTextColor(getResources().getColor(R.color.colorWhite));
                btnMen.setTextColor(getResources().getColor(R.color.colorBlack));
                isPol = 2;
            }
        });
        tvBirth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog tpd = new DatePickerDialog(getActivity(), myCallBack, myYear, myMonth, myDay);
                tpd.show();

            }
        });
        layAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Thread myThread2 = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Crop.pickImage(getActivity(), Profile.this);
                    }
                });
                myThread2.start();

                    }


        });

        btnPriced.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayAdapter<String> listAdapter;
                final Dialog dialog = new Dialog(getActivity());
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.priced);
                Button btnCont = (Button) dialog.findViewById(R.id.btnContinue);
                final EditText edit = (EditText) dialog.findViewById(R.id.editSum);
                final LinearLayout laySum = (LinearLayout) dialog.findViewById(R.id.laySum);
                final WebView web = (WebView) dialog.findViewById(R.id.web);
                dialog.show();
                btnCont.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if(!edit.getText().toString().equals("")) {
                            if(Double.parseDouble(edit.getText().toString())>=200){
                            GetBASE64(edit.getText().toString());
//                        startActivity(new Intent(getActivity().getApplicationContext(), Oplata.class).putExtra("amount",edit.getText().toString()));
                            dialog.cancel();
                            dialog.dismiss();}
                        else Toast.makeText(getActivity().getApplicationContext(), getActivity().getResources().getString(R.string.minBalanceError), Toast.LENGTH_SHORT).show();
                        } else Toast.makeText(getActivity().getApplicationContext(), getActivity().getResources().getString(R.string.fillEmpty), Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });
//           layAvatar.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                    if (getActivity().checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
//                            == PackageManager.PERMISSION_GRANTED) {
//                        Intent pickPhoto = new Intent(Intent.ACTION_PICK,
//                                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//
//                        startActivityForResult(pickPhoto, 2);
//                    }
//                    else  showNoStoragePermissionSnackbar();
//                }
//                else {Intent pickPhoto = new Intent(Intent.ACTION_PICK,
//                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//
//                    startActivityForResult(pickPhoto, 2);}
//            }
//        });


        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lastName="";

                if(!tvName.getText().toString().equals(""))
                {
                    String fio2 = tvName.getText().toString();
                    fio2 = fio2.replaceAll("  "," ");
                    fio = fio2.split(" ");

                    if(fio.length<2) Toast.makeText(getActivity(), getResources().getString(R.string.setNameSurname), Toast.LENGTH_SHORT).show();
                else {
                        if(isCity){
                            for(int i=0; i<cityNames.length; i++) {
                                if(cityNames[i].equals(myCity))
                                 idCity=cityIds[i];
                            }

                        }
                        if(fio.length>2) lastName = fio[2];
                        if(idCity!=0 || isCity) {
                            if(isPol!=0) {
                                if(isPol==1) isMen =true;
                                else  isMen = false;
                             setChanges();}
                           else Toast.makeText(getActivity(), getResources().getString(R.string.errorSelectSex), Toast.LENGTH_SHORT).show();
                        }
                        else Toast.makeText(getActivity(), getResources().getString(R.string.errorSelectCity), Toast.LENGTH_SHORT).show();
                     }
                }
                else Toast.makeText(getActivity(), getResources().getString(R.string.fillFIO), Toast.LENGTH_SHORT).show();

            }
        });
        
        return  view;
    }
    public void showNoStoragePermissionSnackbar() {
        Snackbar.make(getView(), "Нет разрешения к памяти" , Snackbar.LENGTH_LONG)
                .setAction("НАСТРОЙКИ", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        openApplicationSettings();

                        Toast.makeText(getActivity(),
                                "Откройте Разрешения: Дайте разрешения на Память!",
                                Toast.LENGTH_SHORT)
                                .show();
                    }
                })
                .show();
    }
    @Override
    public void onResume() {
        super.onResume();
        if(isBacked) {
            Log.e("ffffss","112323");
        GetProfile();
        isBacked   =false;}
    }
//    @Override
//    public void onPause() {
//        super.onPause();
//        if(isBacked) {
//        GetProfile();
//        isBacked   =false;}
//    }

    public void openApplicationSettings() {
        Intent appSettingsIntent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                Uri.parse("package:" + getActivity().getPackageName()));
        startActivityForResult(appSettingsIntent, 1);
    }
    private void showDialog(int dialog_date) {
        if (dialog_date == DIALOG_DATE) {

        }
    }


    DatePickerDialog.OnDateSetListener myCallBack = new DatePickerDialog.OnDateSetListener() {

        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            myYear = year;
            myMonth = monthOfYear+1;
            myDay = dayOfMonth;
            Calendar cal = Calendar.getInstance();
            TimeZone tz = cal.getTimeZone();

            myBirth=myDay+"-"+myMonth+"-"+myYear;
            String zero="", zero2="";
            if(myDay<10) zero="0";
            if(myMonth<10) zero2="0";

            tvBirth.setText(zero+myDay+"."+zero2+myMonth+"."+myYear);
            schoolColor();
            DIALOG_DATE=2;

        }
    };



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == Crop.REQUEST_PICK && resultCode == getActivity().RESULT_OK) {
            beginCrop(data.getData());

        } else if (requestCode == Crop.REQUEST_CROP) {
            handleCrop(resultCode, data);

        }

    }

    // Crop Image
    private void beginCrop(Uri source) {
        Uri destination = Uri.fromFile(new File(getActivity().getCacheDir(), "cropped"));
        Crop.of(source, destination).asSquare().start(getActivity(),Profile.this);

    }
    // Crop image to ImageView
    private void handleCrop(int resultCode, Intent result) {
        if (resultCode == -1) {
            Uri selectedImage = Crop.getOutput(result);

            InputStream image_stream = null;
            try {
                image_stream = getActivity().getContentResolver().openInputStream(selectedImage);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

            rotatedBMP= BitmapFactory.decodeStream(image_stream);
            ava.setImageURI(Crop.getOutput(result));
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            rotatedBMP.compress(Bitmap.CompressFormat.PNG, 70, byteArrayOutputStream);
            byte[] byteArray = byteArrayOutputStream .toByteArray();
            String encoded = Base64.encodeToString(byteArray, Base64.DEFAULT);
            base64 = encoded;
            Constants.Show_ProgressDialog(getActivity(), getResources().getString(R.string.wait));
             SetAva();





        } else if (resultCode == Crop.RESULT_ERROR) {
            Toast.makeText(getActivity().getApplicationContext(), Crop.getError(result).getMessage(), Toast.LENGTH_SHORT).show();

        }
    }
    private void GetBASE64(final String sum) {
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        StringRequest stringRequest = new StringRequest(Request.Method.GET, "http://kaztest.com/Mobileoplata/Sign?amount="+sum+"&userid="+MainActivity.userID,
                new Response.Listener<String>() {
                    @Override
                        public void onResponse(String response) {
                        Log.e("aaaa",response);
                        startActivity(new Intent(getActivity(), EpayActivity.class).putExtra("base64",response));

                        }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("", "Error: " + error.getMessage());
            }
        });
        queue.add(stringRequest);

    }
    private void SetAva() {
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("UserId", MainActivity.userID+"");
        params.put("photo", base64);

        JsonObjectRequest myRequest = new JsonObjectRequest(
                Request.Method.POST,
                Constants.AVATAR,
                new JSONObject(params),

                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("ffa22","qqq");
                        Toast.makeText(getActivity().getApplicationContext(), getResources().getString(R.string.success), Toast.LENGTH_SHORT).show();
//                        ((MainActivity) getActivity()).setAvatar("1");
                        Constants.isChangedImage=1;
                        Constants.Hide_ProgressDialog();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getActivity().getApplicationContext(), getResources().getString(R.string.success), Toast.LENGTH_SHORT).show();
                        Constants.Hide_ProgressDialog();
                    }
                }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
               // headers.put("Content-Type", "application/json");
                return headers;
            }
        };
        AppController.getInstance().addToRequestQueue(myRequest, "tag");
    }


    private void GetCitiesArray() {
        ArrayAdapter<String> listAdapter;
        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.listview);
        final ListView listv = (ListView) dialog.findViewById(R.id.list1);
        listAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, android.R.id.text1, cityNames);
        listv.setAdapter(listAdapter);
        dialog.show();

        listv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(position>=0){
                    String txt = ((TextView)view).getText().toString();
                    tvCity.setText(txt);
                    for(int i=0;i<cityNames.length;i++){
                        if(cityNames[i].equals(txt))
                        { idCity=cityIds[i];
                        myCity = cityNames[i];}
                    }
                    dialog.cancel();
                    dialog.dismiss(); }
            }
        });
    }

    private void GetCities() {
        JsonArrayRequest req = new JsonArrayRequest(Constants.LIST_CITIES,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {

                        try {
                            cityNames = new String[response.length()];
                            cityIds = new Integer[response.length()];
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject js = (JSONObject) response.get(i);
                                cityNames[i] = js.getString("city1");
                                cityIds[i] = js.getInt("id");

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        Constants.Hide_ProgressDialog();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Constants.Hide_ProgressDialog();
                Toast.makeText(getActivity(), getResources().getString(R.string.error), Toast.LENGTH_SHORT).show();
            }
        });
        AppController.getInstance().addToRequestQueue(req);
    }
    private void GetSchools() {
        Constants.Show_ProgressDialog(getActivity(),getResources().getString(R.string.wait));
        JsonArrayRequest req = new JsonArrayRequest(Constants.LIST_SCHOOLS+"id="+(idCity),
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            schoolNames = new String[response.length()];
                            schoolIds = new Integer[response.length()];
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject js = (JSONObject) response.get(i);
                                schoolNames[i] = js.getString("school1");
                                schoolIds[i] = js.getInt("id");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        Constants.Hide_ProgressDialog();
                        GetSchoolsArray();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Constants.Hide_ProgressDialog();
                Toast.makeText(getActivity(), getResources().getString(R.string.error), Toast.LENGTH_SHORT).show();
            }
        });
        AppController.getInstance().addToRequestQueue(req);
    }

    private void GetSchoolsArray() {
        ArrayAdapter<String> listAdapter;
        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.listview);
        final ListView listv = (ListView) dialog.findViewById(R.id.list1);
        listAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, android.R.id.text1, schoolNames);
        listv.setAdapter(listAdapter);
        dialog.show();

        listv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(position>=0){
                    String txt = ((TextView)view).getText().toString();
                    tvSchool.setText(txt);
                    for(int i=0;i<schoolNames.length;i++){
                        if(schoolNames[i].equals(txt))
                            idSchool=schoolIds[i];
                    }
                    dialog.cancel();
                    dialog.dismiss(); }
            }
        });
    }
    private void setChanges() {
        Constants.Show_ProgressDialog(getActivity(), getResources().getString(R.string.wait));
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.CHANGE_PROFILE,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Constants.Hide_ProgressDialog();
                        Toast.makeText(getActivity(), getResources().getString(R.string.success), Toast.LENGTH_SHORT).show();
                        ((MainActivity) getActivity()).setName(fio[0]+" "+fio[1]+" "+lastName);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Constants.Hide_ProgressDialog();
                        Toast.makeText(getActivity(), getResources().getString(R.string.error), Toast.LENGTH_SHORT).show();
                    }
                }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String>  params = new HashMap<String, String>();
//         params.put("Content-Type", "application/json");
                return params;
            }
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("UserId",MainActivity.userID);
                params.put("birtdate",myBirth);
                params.put("cityid",idCity+"");
                if(idSchool>0)
                params.put("schollid",idSchool+"");
                params.put("FirstName",fio[1]);
                params.put("MiddleName",lastName);
                params.put("LastName",fio[0]);
                params.put("Gender",isMen+"");
//               params.put("photo",base64+"");
                return params;
            }

        };
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);
    }


    private void GetProfile() {
        MyRequest jsonReq = new MyRequest(Request.Method.GET,
                Constants.PROFILE+MainActivity.userID, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                if (response != null) {
                    try {
                        if(!response.getString("UserName").equals("null"))
                            tvPhone.setText(response.getString("UserName"));
                        if(!response.getString("LastName").equals("null"))
                           lastname = response.getString("LastName");
                        if(!response.getString("FirstName").equals("null"))
                           name = response.getString("FirstName");
                        if(!response.getString("MiddleName").equals("null"))
                           patron = response.getString("MiddleName");
                        if(!response.getString("scholl").equals("null"))
                        {   isSchool=true;
                            mySchool = response.getString("scholl");
                            tvSchool.setText(response.getString("scholl")+"");

                        }
                        if(!response.getString("Balance").equals("null"))
                        {
                            tvPrice.setText("Баланс: "+response.getString("Balance")+" тг");
                        }
                        if(!response.getString("Gender").equals("null"))
                        {
                            if(response.getBoolean("Gender")) {
                            isPol = 1;
                                  btnMen.setTextColor(getResources().getColor(R.color.colorWhite));
                                    btnMen.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                                }
                            else  {
                                btnWomen.setTextColor(getResources().getColor(R.color.colorWhite));
                                    isPol = 2; btnWomen.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                            }
                         }
                        if(!response.getString("birtdate").equals("null"))
                        {
                            String bd[] = response.getString("birtdate").split("T");
                            String time[] = bd[0].split("-");
                            myBirth = time[2]+"-"+time[1]+"-"+time[0];
                            tvBirth.setText(time[2]+"."+time[1]+"."+time[0]);
                            myYear = Integer.parseInt(time[0]);
                            schoolColor();

                        }
                        if(!response.getString("photo").equals("null"))
                        {
                            String urlPhoto = response.getString("photo");
                            urlPhoto = urlPhoto.replace("~","");
                            ava.setBackgroundResource(0);
                            ava.setImageUrl("http://www.kaztest.com"+urlPhoto, imageLoader);

                        }
                        if(!response.getString("city").equals("null"))
                        {   isCity=true;
                            myCity = response.getString("city");
                            tvCity.setText(response.getString("city"));
                            idCity = response.getInt("cityid");

                        }
                        else {
                            isCity=false;  myCity="";
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    Constants.Hide_ProgressDialog();
                    tvName.setText(lastname + " "+name+" "+patron);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("", "Error: " + error.getMessage());
                Constants.Hide_ProgressDialog();
            }
        });
        AppController.getInstance().addToRequestQueue(jsonReq);
    }
//   onA
    private void schoolColor(){
        DateFormat dff = new SimpleDateFormat("dd-MM-yyyy");
        String date2 = dff.format(Calendar.getInstance().getTime());
        String[] dates = date2.split("-");
        int year = Integer.parseInt(dates[2]);
        old = year - myYear;
        if(old<=17) {
            tvSchool.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
            tvSchool.setFocusable(true);
            tvSchool.setEnabled(true);
        }
        else {
            tvSchool.setTextColor(getResources().getColor(R.color.colorWhiter));
            tvSchool.setFocusable(false);
            tvSchool.setText(getResources().getString(R.string.school));
            tvSchool.setEnabled(false);
        }
    }
    private void imageFromGallery(int resultCode, Intent data) {
        Uri selectedImage = data.getData(); String [] filePathColumn = {MediaStore.Images.Media.DATA};
        Cursor cursor = getActivity().getContentResolver().query(selectedImage, filePathColumn, null, null, null);
        cursor.moveToFirst();
        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
        String filePath = cursor.getString(columnIndex);

        rotatedBMP = BitmapFactory.decodeFile(filePath);
        ava.setImageBitmap(rotatedBMP);
        cursor.close();
        base64 =getEncoded64ImageStringFromBitmap(rotatedBMP);

//       eee
//        mImageView.setImageBitmap(bitmap);
    }




    public String getEncoded64ImageStringFromBitmap(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte[] byteFormat = stream.toByteArray();
        // get the base 64 string
        String imgString = Base64.encodeToString(byteFormat, Base64.NO_WRAP);

        return imgString;
    }
    private void initResources() {
        tvBirth = (TextView) view.findViewById(R.id.tvBirth);
        tvPrice = (TextView) view.findViewById(R.id.tvPrice);
        tvName = (EditTextView) view.findViewById(R.id.tvName);
        tvCity = (TextView) view.findViewById(R.id.tvCity);
        tvSchool = (TextView) view.findViewById(R.id.tvSchool);
        tvPhone = (EditTextView) view.findViewById(R.id.tvPhone);
        tvPhone.setLocked(true);
        btnChangePass = (Button) view.findViewById(R.id.btnChangePass);
        btnSave = (Button) view.findViewById(R.id.btnSave);
        btnWomen = (Button) view.findViewById(R.id.btnWomen);
        btnMen = (Button) view.findViewById(R.id.btnMen);
        btnPriced = (Button) view.findViewById(R.id.btnPriced);
        layAvatar = (LinearLayout) view.findViewById(R.id.layAvatar);
        ava = (CircleImageView) view.findViewById(R.id.profile_image);
    }
    @SuppressLint("ValidFragment")
    public static class Dialog1 extends DialogFragment {
        Button btnOK;
        EditText edit1, edit2, edit3;
        public Dialog1() {

        }
        final String LOG_TAG = "myLogs";

        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View v = inflater.inflate(R.layout.dialog1, null);
            btnOK = (Button) v.findViewById(R.id.BTNOK);
            edit1 = (EditText)  v.findViewById(R.id.edit);
            edit2 = (EditText)  v.findViewById(R.id.editNewPass);
            edit3 = (EditText)  v.findViewById(R.id.editNewPass2);

            btnOK.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    oldPass = edit1.getText().toString();
                    newPass = edit2.getText().toString();
                    newPassRe = edit3.getText().toString();
                    if(!oldPass.matches("") && !newPass.matches("")&& !newPassRe.matches("")) {
                        if(newPass.equals(newPassRe))
                            ChangePass();
                        else Toast.makeText(getActivity(), getResources().getString(R.string.errorCorrectPass), Toast.LENGTH_SHORT).show();
                    }
                    else Toast.makeText(getActivity(), getResources().getString(R.string.fillEmpty), Toast.LENGTH_SHORT).show();
                }
            });

            return v;
        }

        private void ChangePass() {
            final StringRequest stringRequest = new StringRequest(Request.Method.GET,  Constants.CHANGE_PASS+MainActivity.userID+"&oldp="+oldPass+"&newp="+newPass,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            if(response.equals("0"))
                            {  Toast.makeText(getActivity(), getResources().getString(R.string.success), Toast.LENGTH_SHORT).show();
                                dlg1.dismiss();
                                SharedPreferences sharedpreferences = getActivity().getSharedPreferences(Constants.MY_PREF, Context.MODE_PRIVATE);
                                devic = sharedpreferences.getString("devic","");
                                SharedPreferences.Editor editor = sharedpreferences.edit();
                                editor.clear().commit();
                                startActivity(new Intent(getActivity().getApplicationContext(), Login.class));
                                getActivity().finish();
                            }
                            else if(response.equals("1"))
                                Toast.makeText(getActivity(), getResources().getString(R.string.error)+" "+getResources().getString(R.string.wrongPass), Toast.LENGTH_SHORT).show();
                            SharedPreferences sharedpreferences = getActivity().getSharedPreferences(Constants.MY_PREF, Context.MODE_PRIVATE);
                            devic = sharedpreferences.getString("devic","");
                            SharedPreferences.Editor editor = sharedpreferences.edit();
                            editor.clear().commit();
                            startActivity(new Intent(getActivity().getApplicationContext(), Login.class));
                            getActivity().finish();

                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(getActivity(), getResources().getString(R.string.error), Toast.LENGTH_SHORT).show();
                        }
                    }){
                @Override
                protected Map<String,String> getParams(){
                    Map<String,String> params = new HashMap<String, String>();
                    return params;
                }

            };
            RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
            requestQueue.add(stringRequest);
        }

        public void onDismiss(DialogInterface dialog) {
            super.onDismiss(dialog);

        }

        public void onCancel(DialogInterface dialog) {
            super.onCancel(dialog);

        }


    }
    class TestAsync extends AsyncTask<Void, Integer, String>
    {


        protected void onPreExecute (){

        }

        protected String doInBackground(Void...arg0) {
            return null;

        }

        protected void onProgressUpdate(Integer...a){

        }

        protected void onPostExecute(String result) {

        }
    }
}

