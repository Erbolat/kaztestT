package kz.drw.kaztest.utils.epay;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import kz.drw.kaztest.Profile;
import kz.drw.kaztest.R;
import kz.drw.kaztest.utils.epay.fragment.EpayFragment;
import kz.drw.kaztest.utils.epay.utils.EpayCallback;
import kz.drw.kaztest.utils.epay.utils.EpayConstants;
import kz.drw.kaztest.utils.epay.utils.EpayLanguage;


/**
 * epay-sdk-android
 * Created by http://beemobile.kz on 3/29/15 6:38 PM.
 */
public class EpayActivity extends FragmentActivity {

    private EpayFragment epayFragment;

    private boolean testMode;

    private String order;
    private String postLink;
    private String template;

    private EpayLanguage language;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_epay);

        if (getIntent() != null) {
            EpayFragment.signedOrderBase64_1 = getIntent().getStringExtra("base64");
        }

        if (findViewById(R.id.content) != null) {
            // However, if we're being restored from a previous state,
            // then we don't need to do anything and should return or else
            // we could end up with overlapping fragments.
            if (savedInstanceState != null) {
                return;
            }

            epayFragment = EpayFragment.newInstance();

//            epayFragment.useTestMode(testMode);

//            epayFragment.setSignedOrderBase64(order);
//            epayFragment.setPostLink(postLink);
//            epayFragment.setLanguage(language);
//            epayFragment.setTemplate(template);

            epayFragment.setSuccessCallback(
                    new EpayCallback() {
                        @Override
                        public void process(Object o) {
                            setResult(EpayConstants.EPAY_PAY_SUCCESS);

                            finish();
                        }
                    }
            );

            epayFragment.setFailureCallback(
                    new EpayCallback() {
                        @Override
                        public void process(Object o) {
                            setResult(EpayConstants.EPAY_PAY_FAILURE);

                            finish();
                        }
                    }
            );

            replaceFragment(epayFragment, false);
        }
    }

    /**
     * Replace current fragment with another fragment
     * @param f - new fragment
     * @param addToBackStack - if true current fragment will be in stack
     */
    public void replaceFragment(Fragment f, boolean addToBackStack) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();

        if (addToBackStack) {
            transaction.addToBackStack(null);
        }

        transaction.replace(R.id.content, f);
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        transaction.commit();
    }
    @Override
    public void onBackPressed()
    {
        // code here to show dialog
        Profile.isBacked  = true;
        super.onBackPressed();  // optional depending on your needs

    }

}
