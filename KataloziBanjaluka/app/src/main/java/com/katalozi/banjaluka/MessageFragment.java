package com.katalozi.banjaluka;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by tamara on 3/28/17.
 */

public class MessageFragment extends DialogFragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_fragment_layout, container, false);

        TextView mTvFacebook = (TextView) view.findViewById(R.id.textView_facebook);
        mTvFacebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(getOpenFacebookIntent(getActivity()));
                dismiss();
            }
        });

        TextView mTvEmail = (TextView) view.findViewById(R.id.textView_email);
        mTvEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendEmail();
                dismiss();
            }
        });

        TextView mTvPhone = (TextView) view.findViewById(R.id.textView_phone);
        mTvPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + "Your Phone_number"));
                startActivity(intent);
                dismiss();
            }
        });

        TextView mTvGooglePlay = (TextView) view.findViewById(R.id.textView_golePlay);
        mTvGooglePlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(Constants.GOOGLE_PLAY)));
                dismiss();
            }
        });

        return view;
    }

    public static Intent getOpenFacebookIntent(Context context) {

        try {
            context.getPackageManager().getPackageInfo("com.facebook.katana", 0);
            return new Intent(Intent.ACTION_VIEW, Uri.parse("fb://page/tamara.nikolic.524"));
        } catch (Exception e) {
            return new Intent(Intent.ACTION_VIEW, Uri.parse(Constants.FACEBOOK));
        }
    }
    private void sendEmail(){

        Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
        emailIntent.setData(Uri.parse("mailto:" + Constants.EMAIL));
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Android aplikacija");
        emailIntent.putExtra(Intent.EXTRA_TEXT, "");

        try {
            startActivity(Intent.createChooser(emailIntent, "Send email using..."));
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(getActivity().getApplicationContext(),"Email nije poslat",Toast.LENGTH_SHORT).show();
        }

    }

}
