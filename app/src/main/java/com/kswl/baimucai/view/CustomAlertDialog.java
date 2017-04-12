package com.kswl.baimucai.view;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kswl.baimucai.R;

/**
 * @author wangjie
 * @version V1.0
 * @Description: TODO(用一句话描述该文件做什么)
 * @date 2016-2-22 下午1:51:52
 */
public class CustomAlertDialog extends Dialog {

    public CustomAlertDialog(Context context) {
        super(context);
        // TODO Auto-generated constructor stub
    }

    public CustomAlertDialog(Context context, int theme) {
        super(context, theme);
        // TODO Auto-generated constructor stub
    }

    public CustomAlertDialog(Context context, boolean cancelable,
                             OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        // TODO Auto-generated constructor stub
    }

    public static class Builder {
        private Context context;
        private String title;
        private int resId;
        private String message;
        private String positiveButtonText;
        private String negativeButtonText;
        private View contentView;
        private boolean cancelabel = true;
        private boolean canceledOnTouchOutside = true;
        private OnClickListener positiveButtonClickListener;
        private OnClickListener negativeButtonClickListener;

        public Builder(Context context) {
            this.context = context;
        }

        public Builder setCancelabel(boolean isCancelabel) {
            this.cancelabel = isCancelabel;
            return this;
        }

        public Builder setCanceledOnTouchOutside(boolean canceledOnTouchOutside) {
            this.canceledOnTouchOutside = canceledOnTouchOutside;
            return this;
        }

        public Builder setTitle(int title) {
            this.title = (String) context.getText(title);
            return this;
        }

        public Builder setTitle(String title) {
            this.title = title;
            return this;
        }

        public Builder setIcon(int resId) {
            this.resId = resId;
            return this;
        }

        public Builder setMessage(String message) {
            this.message = message;
            return this;
        }

        public Builder setMessage(int message) {
            this.message = (String) context.getText(message);
            return this;
        }

        public Builder setContentView(View v) {
            this.contentView = v;
            return this;
        }

        public Builder setPositiveButton(int positiveButtonText,
                                         OnClickListener listener) {
            this.positiveButtonText = (String) context
                    .getText(positiveButtonText);
            this.positiveButtonClickListener = listener;
            return this;
        }

        public Builder setPositiveButton(String positiveButtonText,
                                         OnClickListener listener) {
            this.positiveButtonText = positiveButtonText;
            this.positiveButtonClickListener = listener;
            return this;
        }

        public Builder setNegativeButton(int negativeButtonText,
                                         OnClickListener listener) {
            this.negativeButtonText = (String) context
                    .getText(negativeButtonText);
            this.negativeButtonClickListener = listener;
            return this;
        }

        public Builder setNegativeButton(String negativeButtonText,
                                         OnClickListener listener) {
            this.negativeButtonText = negativeButtonText;
            this.negativeButtonClickListener = listener;
            return this;
        }

        @SuppressLint("NewApi")
        public CustomAlertDialog create() {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            // instantiate the dialog with the custom Theme
            final CustomAlertDialog dialog = new CustomAlertDialog(context,
                    R.style.CustomDialog);
            View layout = inflater.inflate(R.layout.dialog_custom_alert, null);
            dialog.addContentView(layout, new LayoutParams(
                    LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));

            // set the dialog title
            if (TextUtils.isEmpty(title)) {
                layout.findViewById(R.id.tv_dialog_title).setVisibility(
                        View.GONE);
            } else {
                ((TextView) layout.findViewById(R.id.tv_dialog_title))
                        .setText(title);
            }

            // set the dialog icon
            if (resId == 0) {
                layout.findViewById(R.id.iv_dialog_icon).setVisibility(
                        View.GONE);
            } else {
                ((ImageView) layout.findViewById(R.id.iv_dialog_icon))
                        .setImageResource(resId);
            }

            // set the dialog meesage
            if (TextUtils.isEmpty(message)) {
                layout.findViewById(R.id.tv_dialog_content).setVisibility(
                        View.GONE);
            } else {
                ((TextView) layout.findViewById(R.id.tv_dialog_content))
                        .setText(message);
            }

            // set the confirm button
            if (positiveButtonText != null) {
                ((Button) layout.findViewById(R.id.btn_dialog_positive))
                        .setText(positiveButtonText);
            }
            ((Button) layout.findViewById(R.id.btn_dialog_positive))
                    .setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {
                            if (positiveButtonClickListener != null) {
                                positiveButtonClickListener.onClick(dialog,
                                        DialogInterface.BUTTON_POSITIVE);
                            } else {
                                dialog.dismiss();
                            }
                        }
                    });

            // set the cancel button
            if (negativeButtonText != null) {
                layout.findViewById(R.id.v_line).setVisibility(View.VISIBLE);
                ((Button) layout.findViewById(R.id.btn_dialog_cancel)).setVisibility(View
                        .VISIBLE);
                ((Button) layout.findViewById(R.id.btn_dialog_cancel))
                        .setText(negativeButtonText);
                ((Button) layout.findViewById(R.id.btn_dialog_cancel))
                        .setOnClickListener(new View.OnClickListener() {
                            public void onClick(View v) {
                                if (negativeButtonClickListener != null) {
                                    negativeButtonClickListener.onClick(dialog,
                                            DialogInterface.BUTTON_NEGATIVE);
                                } else {
                                    dialog.dismiss();
                                }
                            }
                        });
            } else {
                layout.findViewById(R.id.v_line).setVisibility(View.GONE);
                ((Button) layout.findViewById(R.id.btn_dialog_cancel)).setVisibility(View
                        .GONE);
            }

            // set the content view
            if (contentView != null) {
                ((LinearLayout) layout.findViewById(R.id.ll_dialog_content))
                        .removeAllViews();
                ((LinearLayout) layout.findViewById(R.id.ll_dialog_content))
                        .addView(contentView, new LayoutParams(
                                LayoutParams.MATCH_PARENT,
                                LayoutParams.MATCH_PARENT));
                contentView.getViewTreeObserver().addOnGlobalLayoutListener(
                        new ViewTreeObserver.OnGlobalLayoutListener() {

                            @Override
                            public void onGlobalLayout() {
                                // TODO Auto-generated method stub
                                if (Build.VERSION.SDK_INT < 16) {
                                    contentView.getViewTreeObserver()
                                            .removeGlobalOnLayoutListener(this);
                                } else {
                                    contentView.getViewTreeObserver()
                                            .removeOnGlobalLayoutListener(this);
                                }
                                int h = contentView.getHeight();
                                int w = (int) (contentView.getWidth() * 0.7);
                                if (h > w) {
                                    LayoutParams p = contentView
                                            .getLayoutParams();
                                    p.height = w;
                                    contentView.setLayoutParams(p);
                                }
                            }
                        });
            } else {
                layout.findViewById(R.id.ll_dialog_content).setVisibility(
                        View.GONE);
            }
            dialog.setContentView(layout);
            dialog.setCancelable(cancelabel);
            dialog.setCanceledOnTouchOutside(canceledOnTouchOutside);
            return dialog;
        }
    }
}
