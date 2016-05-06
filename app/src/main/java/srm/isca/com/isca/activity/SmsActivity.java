package srm.isca.com.isca.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import srm.isca.com.isca.R;
import srm.isca.com.isca.app.Config;
import srm.isca.com.isca.app.MyApplication;
import srm.isca.com.isca.helper.PrefManager;
import srm.isca.com.isca.service.HttpService;

public class SmsActivity extends AppCompatActivity implements View.OnClickListener {

    private static String TAG = SmsActivity.class.getSimpleName();

    private ViewPager viewPager;
    private ViewPagerAdapter adapter;
    private Button btnRequestSms, btnVerifyOtp;
    private EditText inputName, inputEmail, inputMobile, inputPassword, inputOtp,inputplace;
    private ProgressBar progressBar;
    private PrefManager pref;
    private ImageButton btnEditMobile;
    private TextView txtEditMobile;
    private LinearLayout layoutEditMobile;
    private TextInputLayout inputLayoutfName,inputLayoutlName,inputLayoutplace,inputLayoutemail,inputLayoutcontact;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sms);


        inputLayoutfName = (TextInputLayout) findViewById(R.id.input_layout_name);
        inputLayoutlName = (TextInputLayout) findViewById(R.id.input_layout_lname);
        inputLayoutplace = (TextInputLayout) findViewById(R.id.input_layout_place);
        inputLayoutemail = (TextInputLayout) findViewById(R.id.input_layout_email);
        inputLayoutcontact = (TextInputLayout) findViewById(R.id.input_layout_contact);

        viewPager = (ViewPager) findViewById(R.id.viewPagerVertical);
        inputName = (EditText) findViewById(R.id.inputName);
        inputPassword = (EditText) findViewById(R.id.inputPassword);
        inputEmail = (EditText) findViewById(R.id.inputEmail);
        inputMobile = (EditText) findViewById(R.id.inputMobile);
        inputOtp = (EditText) findViewById(R.id.inputOtp);
        inputplace = (EditText) findViewById(R.id.input_place);
        btnRequestSms = (Button) findViewById(R.id.btn_request_sms);
        btnVerifyOtp = (Button) findViewById(R.id.btn_verify_otp);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        btnEditMobile = (ImageButton) findViewById(R.id.btn_edit_mobile);
        txtEditMobile = (TextView) findViewById(R.id.txt_edit_mobile);
        layoutEditMobile = (LinearLayout) findViewById(R.id.layout_edit_mobile);

        // view click listeners
        btnEditMobile.setOnClickListener(this);
        btnRequestSms.setOnClickListener(this);
        btnVerifyOtp.setOnClickListener(this);

        // hiding the edit mobile number
        layoutEditMobile.setVisibility(View.GONE);

        pref = new PrefManager(this);

        // Checking for user session
        // if user is already logged in, take him to main activity
        if (pref.isLoggedIn()) {
            Intent intent = new Intent(SmsActivity.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);

            finish();
        }

        adapter = new ViewPagerAdapter();
        viewPager.setAdapter(adapter);
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


        /**
         * Checking if the device is waiting for sms
         * showing the user OTP screen
         */
        if (pref.isWaitingForSms()) {
            viewPager.setCurrentItem(1);
            layoutEditMobile.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_request_sms:
                submitForm();
                break;

            case R.id.btn_verify_otp:
                verifyOtp();
                break;

            case R.id.btn_edit_mobile:
                viewPager.setCurrentItem(0);
                layoutEditMobile.setVisibility(View.GONE);
                pref.setIsWaitingForSms(false);
                break;
        }
    }


    private void submitForm() {
        if (!validatefName()) {

            return;
        }
        if (!validatelName()) {

            return;
        }
        if (!validateplace()) {
            return;
        }
        if (!validateemail()) {

            return;
        }
        if (!validatecontact()) {

            return;
        }

        String fname = inputName.getText().toString().trim();
        String lname = inputPassword.getText().toString().trim();
        String email = inputEmail.getText().toString().trim();
        String contact = inputMobile.getText().toString().trim();
        String place = inputplace.getText().toString().trim();
        //progressBar.setVisibility(View.VISIBLE);

        // saving the mobile number in shared preferences
        pref.setMobileNumber(contact);
        requestForSMS(fname, email, lname, contact, place);
    }




    private boolean validatefName() {
        if (inputName.getText().toString().trim().isEmpty()) {
            inputLayoutfName.setError(getString(R.string.err_msg_fname));
            requestFocus(inputName);
            return false;
        } else {
            inputLayoutfName.setErrorEnabled(false);
        }

        return true;
    }


    private boolean validatelName() {
        if (inputPassword.getText().toString().trim().isEmpty()) {
            inputLayoutlName.setError(getString(R.string.err_msg_lname));
            requestFocus(inputPassword);
            return false;
        } else {
            inputLayoutlName.setErrorEnabled(false);
        }

        return true;
    }

    private boolean validateplace() {
        if (inputplace.getText().toString().trim().isEmpty()) {
            inputLayoutplace.setError(getString(R.string.err_msg_place));
            requestFocus(inputplace);
            return false;
        } else {
            inputLayoutplace.setErrorEnabled(false);
        }

        return true;
    }

    private boolean validateemail() {
        if (inputEmail.getText().toString().trim().isEmpty()) {
            inputLayoutemail.setError(getString(R.string.err_msg_email));
            requestFocus(inputEmail);
            return false;
        } else {
            inputLayoutemail.setErrorEnabled(false);
        }

        return true;
    }
    private boolean validatecontact() {
        if (inputMobile.getText().toString().trim().isEmpty()) {
            inputLayoutcontact.setError(getString(R.string.err_msg_contact));
            requestFocus(inputMobile);
            return false;
        } else {
            inputLayoutcontact.setErrorEnabled(false);
        }

        return true;
    }


    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }


    private class MyTextWatcher implements TextWatcher {

        private View view;

        private MyTextWatcher(View view) {
            this.view = view;
        }

        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void afterTextChanged(Editable editable) {
            switch (view.getId()) {
                case R.id.inputName:
                    validatefName();
                    break;
                case R.id.inputPassword:
                    validatelName();
                    break;
                case R.id.input_place:
                    validateplace();
                    break;
                case R.id.inputEmail:
                    validateemail();
                    break;
                case R.id.inputMobile:
                    validatecontact();
                    break;
            }
        }
    }



    /**
     * Validating user details form
     */
    private void validateForm() {
        String name = inputName.getText().toString().trim();
        String password = inputPassword.getText().toString().trim();
        String email = inputEmail.getText().toString().trim();
        String mobile = inputMobile.getText().toString().trim();

        // validating empty name and email
        if (name.length() == 0 || email.length() == 0) {
            Toast.makeText(getApplicationContext(), "Please enter your details", Toast.LENGTH_SHORT).show();
            return;
        }

        // validating mobile number
        // it should be of 10 digits length
        if (isValidPhoneNumber(mobile)) {

            // request for sms
            progressBar.setVisibility(View.VISIBLE);

            // saving the mobile number in shared preferences
            pref.setMobileNumber(mobile);

            // requesting for sms
            // requestForSMS(name, email,password, mobile);

        } else {
            Toast.makeText(getApplicationContext(), "Please enter valid mobile number", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Method initiates the SMS request on the server
     *
     * @param fname   user name
     * @param email  user email address
     * @param contact user valid mobile number
     * @param lname  user email address
     * @param place user valid mobile number
     */
    private void requestForSMS(final String fname,final String lname, final String email, final String contact, final String place) {
        Log.e("adi",fname);Log.e("adi",lname);Log.e("adi",email);Log.e("adi",contact);Log.d("adi",place);

        StringRequest strReq = new StringRequest(Request.Method.POST,
                Config.URL_REQUEST_SMS, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, response.toString());
                Log.e("adi",fname);Log.e("adi",lname);Log.e("adi",email);Log.e("adi",contact);Log.d("adi",place);

                try {
                    JSONObject responseObj = new JSONObject(response);

                    // Parsing json object response
                    // response will be a json object
                    boolean error = responseObj.getBoolean("error");
                    String message = responseObj.getString("message");

                    // checking for error, if not error SMS is initiated
                    // device should receive it shortly
                    if (!error) {
                        // boolean flag saying device is waiting for sms
                        pref.setIsWaitingForSms(true);

                        // moving the screen to next pager item i.e otp screen
                        viewPager.setCurrentItem(1);
                        txtEditMobile.setText(pref.getMobileNumber());
                        layoutEditMobile.setVisibility(View.VISIBLE);

                        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();

                    } else {
                        Toast.makeText(getApplicationContext(),
                                "Error: " + message,
                                Toast.LENGTH_LONG).show();
                    }

                    // hiding the progress bar
                    progressBar.setVisibility(View.GONE);

                } catch (JSONException e) {
                    Toast.makeText(getApplicationContext(),
                            "Error: " + e.getMessage(),
                            Toast.LENGTH_LONG).show();

                    progressBar.setVisibility(View.GONE);
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
            }
        }) {

            /**
             * Passing user parameters to our server
             * @return
             */
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("fname", fname);
                params.put("lname", lname);
                params.put("email", email);
                params.put("contact", contact);
                params.put("place", place);

                Log.e(TAG, "Posting params: " + params.toString());

                return params;
            }

        };

        int socketTimeout = 60000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        strReq.setRetryPolicy(policy);

        // Adding request to request queue
        MyApplication.getInstance().addToRequestQueue(strReq);
    }

    /**
     * sending the OTP to server and activating the user
     */
    private void verifyOtp() {
        String otp = inputOtp.getText().toString().trim();

        if (!otp.isEmpty()) {
            Intent grapprIntent = new Intent(getApplicationContext(), HttpService.class);
            grapprIntent.putExtra("otp", otp);
            startService(grapprIntent);
        } else {
            Toast.makeText(getApplicationContext(), "Please enter the OTP", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Regex to validate the mobile number
     * mobile number should be of 10 digits length
     *
     * @param mobile
     * @return
     */
    private static boolean isValidPhoneNumber(String mobile) {
        String regEx = "^[0-9]{10}$";
        return mobile.matches(regEx);
    }


    class ViewPagerAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == ((View) object);
        }

        public Object instantiateItem(View collection, int position) {

            int resId = 0;
            switch (position) {
                case 0:
                    resId = R.id.layout_sms;
                    break;
                case 1:
                    resId = R.id.layout_otp;
                    break;
            }
            return findViewById(resId);
        }
    }

}
