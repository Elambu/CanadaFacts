package com.task.facts.utils;

import android.app.Activity;
import android.support.design.widget.BottomSheetDialog;
import android.text.SpannableStringBuilder;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.task.facts.R;


public class AlertDialog {


    public interface PositiveButton {
        void onButtonClick();
    }

    public interface NegativeButton {
        void onButtonClick();
    }


    public static BottomSheetDialog openDialog(Activity activity, Object message, String btnPositive, String btnNegative, final PositiveButton positiveButton, final NegativeButton negativeButton) {
        final BottomSheetDialog dialog = new BottomSheetDialog(activity);
        try {
            View view = activity.getLayoutInflater().inflate(R.layout.alert_sheet, null);

            dialog.setContentView(view);
            TextView tvAlertText = view.findViewById(R.id.tvAlertText);
            TextView tvAlertPositive = view.findViewById(R.id.tvAlertPositive);
            TextView tvAlertNegative = view.findViewById(R.id.tvAlertNegative);

            if (message instanceof String)
                tvAlertText.setText((String) message);
            else if (message instanceof SpannableStringBuilder)
                tvAlertText.setText((SpannableStringBuilder) message);
            else
                tvAlertText.setText("");

            tvAlertPositive.setText(btnPositive);
            tvAlertNegative.setText(btnNegative);

            tvAlertPositive.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (positiveButton != null) {
                        positiveButton.onButtonClick();
                        dialog.dismiss();
                    }
                }
            });
            tvAlertNegative.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (negativeButton != null) {
                        negativeButton.onButtonClick();
                        dialog.dismiss();
                    }
                }
            });
            dialog.setCancelable(false);
            dialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dialog;
    }

}
