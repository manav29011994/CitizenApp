package com.pris.citizenapp.payments;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.paynimo.android.payment.PaymentActivity;
import com.paynimo.android.payment.PaymentModesActivity;
import com.paynimo.android.payment.model.Checkout;
import com.pris.citizenapp.R;
import com.pris.citizenapp.common.SessionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static com.pris.citizenapp.common.SessionManager.USER_EMAIL;

/**
 * Created by manav on 14/4/17.
 */

public class CheckoutActivity extends AppCompatActivity {

    protected TextView hid;
    protected TextView hid_txt;

    public MaterialDialog popAlert;

    protected TextView assessment;
    protected TextView assessment_txt;

    protected TextView citizen;
    protected TextView father;
    protected TextView aadhar;
    protected TextView aadhar_txt;
    protected TextView total,header;
    protected TextView total_txt,mailtxt,mobiletxt;
    EditText mail,mobile;

    protected LinearLayout duelist;
    protected TextView paynow;

    protected int final_total;

   SessionManager session;


    protected RelativeLayout fullView;

    TextView PayNow;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.checkout_page);
        Typeface head = Typeface.createFromAsset(getAssets(), "fonts/Roboto_Light.ttf");

        aadhar=(TextView)findViewById(R.id.aadhar);
        session=new SessionManager(this);
        hid = (TextView)findViewById(R.id.hid);
        hid_txt = (TextView)findViewById(R.id.hid_txt);
        hid.setTypeface(head);
        assessment = (TextView) findViewById(R.id.assessment);
        assessment_txt = (TextView) findViewById(R.id.assessment_txt);
        aadhar_txt.setTypeface(head);
        citizen = (TextView) findViewById(R.id.citizen);
        citizen.setTypeface(head);
        mail=(EditText)findViewById(R.id.mail);
        mobile=(EditText)findViewById(R.id.mobile);
        mailtxt=(TextView) findViewById(R.id.email);
        mailtxt.setTypeface(head);
        mobiletxt=(TextView) findViewById(R.id.phone);
        mobiletxt.setTypeface(head);

        father = (TextView) findViewById(R.id.father_name);
        father.setTypeface(head);
        aadhar = (TextView) findViewById(R.id.aadhar);
        aadhar_txt = (TextView)  findViewById(R.id.aadhar_txt);
        aadhar_txt.setTypeface(head);
        total = (TextView)  findViewById(R.id.total);
        total_txt = (TextView)  findViewById(R.id.total_txt);
        total_txt.setTypeface(head);
        header=(TextView)findViewById(R.id.taxtype);

        duelist = (LinearLayout)  findViewById(R.id.duelist);
        paynow = (TextView)  findViewById(R.id.pay_now);
        paynow.setTypeface(head);


        popAlert = new MaterialDialog.Builder(this)
                .title("Errors Found!")
                .content("Please check your data!")
                .positiveText("Ok").build();


        hid.setText(session.getStrVal("pay_hid"));
        aadhar.setText(session.getStrVal("pay_email"));
        father.setText(session.getStrVal("father"));
        total.setText(session.getStrVal("pay_amount"));
        mail.setText(session.getStrVal(USER_EMAIL));
        citizen.setText(session.getStrVal("pay_name"));
        mobile.setText(session.getStrVal("pay_mobile"));
        assessment.setText(session.getStrVal("pay_assessment"));
        header.setText(session.getStrVal("pay_purpose"));

        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v;

        try {
            JSONObject jsonObject=new JSONObject(session.getStrVal("dues_selected"));
            JSONArray jsonArray=new JSONArray(session.getStrVal("jsonArray"));

            for(int i=0;i<jsonArray.length();i++)
            {
                if(jsonObject.has(jsonArray.getString(i)))
                {

                    View row_view = (View) inflater.inflate(R.layout.dues_layout,null);


                    CheckBox due_year = (CheckBox) row_view.findViewById(R.id.due_year);
                    due_year.setChecked(true);
                    due_year.setClickable(false);
                    TextView due_amount = (TextView) row_view.findViewById(R.id.due_amount);
                    due_amount.setTypeface(head);


                    due_year.setText(jsonArray.getString(i));
                    due_year.setTypeface(head);
                    due_amount.setText(jsonObject.getString(jsonArray.getString(i)));
                    duelist.addView(row_view);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }


       paynow.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               boolean res=validate();
               if(res)
               {
                   Checkout checkout = new Checkout();
                   checkout.setMerchantIdentifier("T45181");  //where T1234 is the MERCHANT CODE, update it with Merchant Code provided by TPSL
                   checkout.setTransactionIdentifier("TXN003"); //where TXN001 is the Merchant Transaction Identifier, it should be different for each transaction (alphanumeric value, no special character allowed)
                   checkout.setTransactionReference ("ORD0003"); //where ORD0001 is the Merchant Transaction Reference number
                   checkout.setTransactionType ("Sale"); //Transaction Type
                   checkout.setTransactionSubType ("Debit"); //Transaction Subtype
                   checkout.setTransactionCurrency ("INR"); //Currency Type
                   checkout.setTransactionAmount ("1.00"); //Transaction Amount
                   checkout.setTransactionDateTime ("15-04-2017"); //Transaction Date
                   // setting Consumer fields values
                   checkout.setConsumerIdentifier (""); //Consumer Identifier, default value "", set this value as application user name if you want Instrument Vaulting, SI on Cards. Consumer ID should be alpha-numeric value with no space
                   checkout.setConsumerEmailID ("test@gmail.com"); //Consumer Email ID
                   checkout.setConsumerMobileNumber ("9827198271"); //Consumer Mobile Number
                   checkout.setConsumerAccountNo ("");
                   checkout.addCartItem("test","1.00","0.0","0.0","","tax","propertytax","www.pris.gov.in");
               }

           }
       });





        Checkout checkout = new Checkout();
        checkout.setMerchantIdentifier("T45181");  //where T1234 is the MERCHANT CODE, update it with Merchant Code provided by TPSL
        checkout.setTransactionIdentifier("TXN003"); //where TXN001 is the Merchant Transaction Identifier, it should be different for each transaction (alphanumeric value, no special character allowed)
        checkout.setTransactionReference ("ORD0003"); //where ORD0001 is the Merchant Transaction Reference number
        checkout.setTransactionType ("Sale"); //Transaction Type
        checkout.setTransactionSubType ("Debit"); //Transaction Subtype
        checkout.setTransactionCurrency ("INR"); //Currency Type
        checkout.setTransactionAmount ("1.00"); //Transaction Amount
        checkout.setTransactionDateTime ("15-04-2017"); //Transaction Date
        // setting Consumer fields values
        checkout.setConsumerIdentifier (""); //Consumer Identifier, default value "", set this value as application user name if you want Instrument Vaulting, SI on Cards. Consumer ID should be alpha-numeric value with no space
        checkout.setConsumerEmailID ("test@gmail.com"); //Consumer Email ID
        checkout.setConsumerMobileNumber ("9827198271"); //Consumer Mobile Number
        checkout.setConsumerAccountNo ("");
        checkout.addCartItem("test","1.00","0.0","0.0","","tax","propertytax","www.pris.gov.in");

        //Testing the push
  /*      checkout.setTransactionMerchantInitiated("Y");
        checkout.setPaymentInstrumentIdentifier("4012001037141112");
        checkout.setPaymentInstrumentExpiryMonth("12");
        checkout.setPaymentInstrumentExpiryYear("2017");
        checkout.setPaymentInstrumentVerificationCode("123");
        checkout.setPaymentInstrumentHolderName("Any Name");
        checkout.setTransactionIsRegistration("Y");*/

      /*  Intent intent=new Intent(CheckoutActivity.this,PaymentModesActivity.class);
        intent.putExtra(PaymentActivity.EXTRA_REQUESTED_PAYMENT_MODE,PaymentActivity.PAYMENT_METHOD_DEFAULT);
        intent.putExtra(PaymentActivity.ARGUMENT_DATA_CHECKOUT,checkout);
        intent.putExtra(PaymentActivity.EXTRA_PUBLIC_KEY,"6636259131GPLFAX");
        startActivityForResult(intent, PaymentActivity.REQUEST_CODE);*/
    }

    private boolean validate() {
        int error=0;
        String errorTxt="";
        if(mail.getText().toString().length() > 0 && error == 0){

            if(!Patterns.EMAIL_ADDRESS.matcher(mail.getText().toString()).matches()) {

                errorTxt = "Invalid Email";
                error++;
            }
        }

        if (error > 0) {
            error = 0;

            popAlert.setTitle("Oops! Errors Found");
            popAlert.setContent(errorTxt);

            popAlert.show();

            return false;

        }


        return true;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        if (requestCode == PaymentActivity.REQUEST_CODE) {
            // Make sure the request was successful
            if (resultCode == PaymentActivity.RESULT_OK) {
                Log.d("TAG", "Result Code :" + RESULT_OK);
                if (data != null) {

                    try {
                        Checkout checkout_res = (Checkout) data
                                .getSerializableExtra(PaymentActivity.ARGUMENT_DATA_CHECKOUT);
                        Log.d("Checkout Response Obj", checkout_res
                                .getMerchantResponsePayload().toString());

                        String transactionType = checkout_res.
                                getMerchantRequestPayload().getTransaction().getType();
                        String transactionSubType = checkout_res.
                                getMerchantRequestPayload().getTransaction().getSubType();
                        if (transactionType != null && transactionType						   .equalsIgnoreCase(PaymentActivity.TRANSACTION_TYPE_PREAUTH)
                                && transactionSubType != null && transactionSubType
                                .equalsIgnoreCase(PaymentActivity.TRANSACTION_SUBTYPE_RESERVE)){
                            // Transaction Completed and Got SUCCESS
                            if (checkout_res.getMerchantResponsePayload()
                                    .getPaymentMethod().getPaymentTransaction()
                                    .getStatusCode().equalsIgnoreCase(PaymentActivity.TRANSACTION_STATUS_PREAUTH_RESERVE_SUCCESS)) {
                                Toast.makeText(getApplicationContext(), "Transaction Status - Success", Toast.LENGTH_SHORT).show();
                                Log.v("TRANSACTION STATUS=>", "SUCCESS");

                                /**
                                 * TRANSACTION STATUS - SUCCESS (status code
                                 * 0200 means success), NOW MERCHANT CAN PERFORM
                                 * ANY OPERATION OVER SUCCESS RESULT
                                 */

                                if (checkout_res.getMerchantResponsePayload()
                                        .getPaymentMethod().getPaymentTransaction()
                                        .getInstruction().getId() != null && !checkout_res.getMerchantResponsePayload()
                                        .getPaymentMethod().getPaymentTransaction()
                                        .getInstruction().getId().isEmpty()) {

                                    /**
                                     * SI TRANSACTION STATUS - SUCCESS (received
                                     * Mandate ID means success)
                                     */
                                    Log.v("TRANSACTION SI STATUS=>", "SUCCESS");
                                }

                            } // Transaction Completed and Got FAILURE

                            else {
                                // some error from bank side
                                Log.v("TRANSACTION STATUS=>", "FAILURE");
                                Toast.makeText(getApplicationContext(),
                                        "Transaction Status - Failure",
                                        Toast.LENGTH_SHORT).show();
                            }

                        } else {

                            // Transaction Completed and Got SUCCESS
                            if (checkout_res.getMerchantResponsePayload().getPaymentMethod()					.getPaymentTransaction().getStatusCode().equalsIgnoreCase(
                                    PaymentActivity.TRANSACTION_STATUS_SALES_DEBIT_SUCCESS)) {
                                Toast.makeText(getApplicationContext(), "Transaction Status - Success", Toast.LENGTH_SHORT).show();
                                Log.v("TRANSACTION STATUS=>", "SUCCESS");

                                /**
                                 * TRANSACTION STATUS - SUCCESS (status code
                                 * 0300 means success), NOW MERCHANT CAN PERFORM
                                 * ANY OPERATION OVER SUCCESS RESULT
                                 */

                                if (checkout_res
                                        .getMerchantResponsePayload()
                                        .getPaymentMethod().getPaymentTransaction()
                                        .getInstruction().getId() != null && !checkout_res.getMerchantResponsePayload()
                                        .getPaymentMethod().getPaymentTransaction()
                                        .getInstruction().getId().isEmpty()) {

                                    /**
                                     * SI TRANSACTION STATUS - SUCCESS (received
                                     * Mandate ID means success)
                                     */
                                    Log.v("TRANSACTION SI STATUS=>", "SUCCESS");
                                }

                            } // Transaction Completed and Got FAILURE
                            else {
                                // some error from bank side
                                Log.v("TRANSACTION STATUS=>", "FAILURE");
                                Toast.makeText(getApplicationContext(),
                                        "Transaction Status - Failure",
                                        Toast.LENGTH_SHORT).show();
                            }

                        }
                        String result = "StatusCode : " + checkout_res
                                .getMerchantResponsePayload().getPaymentMethod()
                                .getPaymentTransaction().getStatusCode()
                                + "\nStatusMessage : " + checkout_res
                                .getMerchantResponsePayload().getPaymentMethod()
                                .getPaymentTransaction().getStatusMessage()
                                + "\nErrorMessage : "+ checkout_res
                                .getMerchantResponsePayload().getPaymentMethod()
                                .getPaymentTransaction().getErrorMessage()
                                + "\nAmount : " + checkout_res
                                .getMerchantResponsePayload().getPaymentMethod()			.getPaymentTransaction().getAmount()
                                + "\nDateTime : " + checkout_res.
                                getMerchantResponsePayload().getPaymentMethod()
                                .getPaymentTransaction().getDateTime()
                                + "\nMerchantTransactionIdentifier : "
                                + checkout_res.getMerchantResponsePayload()
                                .getMerchantTransactionIdentifier()
                                + "\nIdentifier : " + checkout_res
                                .getMerchantResponsePayload().getPaymentMethod()
                                .getPaymentTransaction().getIdentifier()
                                + "\nBankSelectionCode : " + checkout_res
                                .getMerchantResponsePayload().getPaymentMethod()
                                .getBankSelectionCode()
                                + "\nBankReferenceIdentifier : " + checkout_res
                                .getMerchantResponsePayload().getPaymentMethod()
                                .getPaymentTransaction().getBankReferenceIdentifier()
                                + "\nRefundIdentifier : " + checkout_res
                                .getMerchantResponsePayload().getPaymentMethod()
                                .getPaymentTransaction().getRefundIdentifier()
                                + "\nBalanceAmount : " + checkout_res
                                .getMerchantResponsePayload().getPaymentMethod()
                                .getPaymentTransaction().getBalanceAmount()
                                + "\nInstrumentAliasName : " + checkout_res
                                .getMerchantResponsePayload().getPaymentMethod()
                                .getInstrumentAliasName()
                                + "\nSI Mandate Id : " + checkout_res
                                .getMerchantResponsePayload().getPaymentMethod()
                                .getPaymentTransaction().getInstruction().getId()
                                + "\nSI Mandate Status : " + checkout_res
                                .getMerchantResponsePayload().getPaymentMethod()
                                .getPaymentTransaction().getInstruction().getStatusCode()
                                + "\nSI Mandate Error Code : " + checkout_res
                                .getMerchantResponsePayload().getPaymentMethod()
                                .getPaymentTransaction().getInstruction().getErrorcode()
                                + "\nSI MerchantAdditionalDetails : " + checkout_res
                                .getMerchantResponsePayload()
                                .getMerchantAdditionalDetails();

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            }
            else if (resultCode == PaymentActivity.RESULT_ERROR) {
                Log.d("TAG", "got an error");

                if (data.hasExtra(PaymentActivity.RETURN_ERROR_CODE) &&
                        data.hasExtra(PaymentActivity.RETURN_ERROR_DESCRIPTION)) {
                    String error_code = (String) data
                            .getStringExtra(PaymentActivity.RETURN_ERROR_CODE);
                    String error_desc = (String) data
                            .getStringExtra(PaymentActivity.RETURN_ERROR_DESCRIPTION);

                    Toast.makeText(getApplicationContext(), " Got error :"
                            + error_code + "--- " + error_desc, Toast.LENGTH_SHORT)
                            .show();
                    Log.d("TAG" + " Code=>", error_code);
                    Log.d("TAG" + " Desc=>", error_desc);

                }

            }
            else if (resultCode == PaymentActivity.RESULT_CANCELED) {
                Toast.makeText(getApplicationContext(), "Transaction Aborted by User",
                        Toast.LENGTH_SHORT).show();
                Log.d("TAG", "User pressed back button");

            }
        }
    }

}
