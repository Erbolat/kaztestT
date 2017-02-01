package kz.drw.kaztest.utils.epay;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.Toast;

import kz.drw.kaztest.MainActivity;
import kz.drw.kaztest.R;
import kz.drw.kaztest.utils.epay.fragment.EpayFragment;
import kz.drw.kaztest.utils.epay.utils.DataController;
import kz.drw.kaztest.utils.epay.utils.EpayCallback;
import kz.drw.kaztest.utils.epay.utils.EpayConstants;
import kz.drw.kaztest.utils.epay.utils.EpayLanguage;


public class MyActivity extends FragmentActivity {

   public static String BASE64,LOGIN;
    private DataController dc;

    ProgressDialog mProgressDialog;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_my);
        Intent i = getIntent();

        EpayFragment.signedOrderBase64_1 = i.getStringExtra("base64");



        dc = new DataController(this);


            dc.getOrderId(System.currentTimeMillis(),
                    new EpayCallback() {
                        @Override
                        public void process(Object o) {
                            Log.d("AAAAA","000000");
                            String order = (String) o;
                            String postLink = "http://kokzhiek.kz/user/test";
                            String template = "your_template";

                            Intent intent = new Intent(MyActivity.this, EpayActivity.class);
                            intent.putExtra(EpayConstants.EXTRA_TEST_MODE, false);
                            intent.putExtra(EpayConstants.EXTRA_SIGNED_ORDER_BASE_64, order);
                            intent.putExtra(EpayConstants.EXTRA_POST_LINK, postLink);
                            intent.putExtra(EpayConstants.EXTRA_LANGUAGE, EpayLanguage.RUSSIAN);
                            intent.putExtra(EpayConstants.EXTRA_TEMPLATE, template);
                            startActivityForResult(intent, EpayConstants.EPAY_PAY_REQUEST);
                            Log.d("AAAAA","11111");

                        }
                    },
                    new EpayCallback() {
                        @Override
                        public void process(Object o) {

                        }
                    }
            );
        }




    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        Log.d("AAAAA","ACTIVITYR");
        // Check which request we're responding to
        if (requestCode == EpayConstants.EPAY_PAY_REQUEST) {


            if (resultCode == EpayConstants.EPAY_PAY_SUCCESS) {


                Toast.makeText(this, "Удачно", Toast.LENGTH_LONG).show();

            } else {
                finish();
                Toast.makeText(this, "Ошибка", Toast.LENGTH_LONG).show();
            }

        }
    }


    @Override
    public void onBackPressed() {
        startActivity(new Intent(MyActivity.this, MainActivity.class));
    }




    }
