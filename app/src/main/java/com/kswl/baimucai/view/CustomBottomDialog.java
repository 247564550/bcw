package com.kswl.baimucai.view;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kswl.baimucai.R;

/**
 * @author wangjie
 * @package com.kswl.ggt.shipper.view
 * @desc
 * @date 2016-2016/12/7-15:11
 */

public class CustomBottomDialog extends Dialog {

    public CustomBottomDialog(Context context) {
        super(context);
    }

    public CustomBottomDialog(Context context, int themeResId) {
        super(context, themeResId);
    }

    protected CustomBottomDialog(Context context, boolean cancelable, OnCancelListener
            cancelListener) {
        super(context, cancelable, cancelListener);
    }

    public static class Builder {
        private Context context;
        private String title;
        private String positiveButtonText;
        private String negativeButtonText;
        private View contentView;
        private boolean cancelabel = true;
        private OnClickListener positiveButtonClickListener;
        private OnClickListener negativeButtonClickListener;

        public Builder(Context context) {
            this.context = context;
        }

        public Builder setCancelabel(boolean isCancelabel) {
            this.cancelabel = isCancelabel;
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

        public CustomBottomDialog create() {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            // instantiate the dialog with the custom Theme
            final CustomBottomDialog dialog = new CustomBottomDialog(context,
                    R.style.CustomDialog);
            View layout = inflater.inflate(R.layout.dialog_custom_bottom, null);
            dialog.addContentView(layout, new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

            // set the dialog title
            if (TextUtils.isEmpty(title)) {
                layout.findViewById(R.id.tv_title).setVisibility(
                        View.GONE);
            } else {
                layout.findViewById(R.id.tv_title).setVisibility(
                        View.VISIBLE);
                ((TextView) layout.findViewById(R.id.tv_title))
                        .setText(title);
            }

            // set the confirm button
            if (!TextUtils.isEmpty(positiveButtonText)) {
                ((TextView) layout.findViewById(R.id.tv_confirm))
                        .setText(positiveButtonText);
            }
            ((TextView) layout.findViewById(R.id.tv_confirm))
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
            if (!TextUtils.isEmpty(negativeButtonText)) {
                ((TextView) layout.findViewById(R.id.tv_cancel))
                        .setText(negativeButtonText);
            }
            ((TextView) layout.findViewById(R.id.tv_cancel))
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

            // set the content view
            if (contentView != null) {
                ((LinearLayout) layout).addView(contentView, new ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT));
            }
            dialog.setContentView(layout);
            dialog.setCancelable(cancelabel);
            Window window = dialog.getWindow();
            window.getDecorView().setPadding(0, 0, 0, 0);
            // 此处可以设置dialog显示的位置
            window.setGravity(Gravity.BOTTOM);
            // 添加动画
            window.setWindowAnimations(R.style.dialog_animation);
            return dialog;
        }
    }

}
