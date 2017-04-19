package com.katalozi.banjaluka.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.katalozi.banjaluka.R;
import com.katalozi.banjaluka.data.Constants;

/**
 * Fragment for show alert to user when version is changed
 */

public class MessageFragment extends DialogFragment {
    private SharedPreferences mSharedPrefs;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_fragment_layout, container, false);
        mSharedPrefs = getActivity().getSharedPreferences(Constants.NAME, Context.MODE_PRIVATE);

        // set up all view components and their listeners.
        TextView mTvFacebook = (TextView) view.findViewById(R.id.textView_facebook);
        mTvFacebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //start activity with facebook intent and close dialog.
                startActivity(getOpenFacebookIntent(getActivity()));
                dismiss();
            }
        });

        TextView mTvEmail = (TextView) view.findViewById(R.id.textView_email);
        mTvEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // send email window open and close dialog
                sendEmail();
                dismiss();
            }
        });

        TextView mTvPhone = (TextView) view.findViewById(R.id.textView_phone);
        mTvPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // new call with phone number for more info
                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + Constants.PHONE));
                startActivity(intent);
                dismiss();
            }
        });

        TextView mTvGooglePlay = (TextView) view.findViewById(R.id.textView_golePlay);
        mTvGooglePlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // open URL on Google Play and close dialog
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(Constants.GOOGLE_PLAY)));
                SharedPreferences.Editor editor = mSharedPrefs.edit();
                editor.putBoolean("change", false);
                editor.apply();
                dismiss();
            }
        });

        TextView mTvNo = (TextView) view.findViewById(R.id.textView_no);
        mTvNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // no save
                SharedPreferences.Editor editor = mSharedPrefs.edit();
                editor.putBoolean("change", false);
                editor.apply();
                dismiss();
            }
        });

        return view;
    }

    /**
     * Open facebook page
     *
     * @param context
     * @return intent which will be started
     */
    public static Intent getOpenFacebookIntent(Context context) {

        try {
            context.getPackageManager().getPackageInfo("com.facebook.katana", 0);
            return new Intent(Intent.ACTION_VIEW, Uri.parse(Constants.FACEBOOK));
        } catch (Exception e) {
            return new Intent(Intent.ACTION_VIEW, Uri.parse(Constants.FACEBOOK));
        }
    }

    /**
     * Send email for more info. Add to: and subject:.
     */
    private void sendEmail() {

        Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
        emailIntent.setData(Uri.parse("mailto:" + Constants.EMAIL));
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Android aplikacija");
        emailIntent.putExtra(Intent.EXTRA_TEXT, "");

        try {
            startActivity(Intent.createChooser(emailIntent, "Send email using..."));
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(getActivity().getApplicationContext(), "Email nije poslat", Toast.LENGTH_SHORT).show();
        }

    }

}
