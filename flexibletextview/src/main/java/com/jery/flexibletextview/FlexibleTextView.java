package com.jery.flexibletextview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatTextView;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.AttributeSet;
import android.view.View;

import com.jery.flexibletextview.lib.R;


public class FlexibleTextView extends AppCompatTextView {
    private static final int DEFAULT_MAX_LINES = 1;
    private static final String SUFFIX = "...";
    private String mPreText;
    private String mMoreText;
    private int mMoreColor;
    private int mPreColor;
    private int mFlexibleMaxLines;


    public FlexibleTextView(Context context) {
        super(context);
    }

    public FlexibleTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public FlexibleTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }


    private void init(@Nullable AttributeSet attributeSet) {
        TypedArray typedArray = getContext().obtainStyledAttributes(attributeSet, R.styleable
                .FlexibleTextView, 0, 0);
        mPreColor = typedArray.getColor(R.styleable.FlexibleTextView_preColor, getCurrentTextColor());
        mMoreColor = typedArray.getColor(R.styleable.FlexibleTextView_moreColor, getCurrentTextColor());
        mFlexibleMaxLines = typedArray.getInt(R.styleable.FlexibleTextView_flexibleMaxLines, DEFAULT_MAX_LINES);
        typedArray.recycle();

        setMovementMethod(LinkMovementMethod.getInstance());
        setHighlightColor(Color.TRANSPARENT);

    }

    public void setFlexibleText(String preText, String moreText) {
        this.mPreText = preText;
        this.mMoreText = moreText;
        String newText = mPreText + mMoreText;

        SpannableString spannableString = new SpannableString(newText);
        spannableString.setSpan(mPreClickableSpan, 0, (mPreText).length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(mMoreClickableSpan, (mPreText).length(), newText.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        setText(spannableString);
    }

    @Override
    public boolean onPreDraw() {
        fixText();
        return super.onPreDraw();
    }


    public void fixText() {
        if (getLineCount() > mFlexibleMaxLines) {
            int lastIndex = getLayout().getLineEnd(mFlexibleMaxLines - 1);
            String temp = getText().toString().substring(0, lastIndex);
            int total = getLength(temp);
            int sub = getLength(SUFFIX + mMoreText);
            String textWithoutMore = check(temp, total - sub);
            String newText = textWithoutMore + SUFFIX + mMoreText;
            SpannableString spannableString = new SpannableString(newText);
            spannableString.setSpan(mPreClickableSpan, 0, (textWithoutMore + SUFFIX).length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            spannableString.setSpan(mMoreClickableSpan, (textWithoutMore + SUFFIX).length(), newText.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            setText(spannableString);
        }
    }

    private String check(String str, int endIndex) {
        int count = 0;
        for (int i = 0; i < str.length(); i++) {
            char item = str.charAt(i);
            if (item < 128) {
                count = count + 1;
            } else {
                count = count + 2;
            }
            if (count > endIndex) {
                return str.substring(0, i);
            }
        }
        return str.substring(0, str.length());
    }

    private int getLength(String str) {
        int count = 0;
        for (int i = 0; i < str.length(); i++) {
            char item = str.charAt(i);
            if (item < 128) {
                count = count + 1;
            } else {
                count = count + 2;
            }
        }
        return count;
    }


    private ClickableSpan mPreClickableSpan = new ClickableSpan() {

        @Override
        public void onClick(View widget) {
            if (mOnTextClickListener != null) {
                mOnTextClickListener.onPreTextClick(mPreText);
            }
        }

        @Override
        public void updateDrawState(TextPaint ds) {
            super.updateDrawState(ds);
            ds.setColor(mPreColor);
            ds.setUnderlineText(false);
            ds.clearShadowLayer();

        }
    };
    private ClickableSpan mMoreClickableSpan = new ClickableSpan() {

        @Override
        public void onClick(View widget) {
            if (mOnTextClickListener != null) {
                mOnTextClickListener.onMoreTextClick(mMoreText);
            }
        }

        @Override
        public void updateDrawState(TextPaint ds) {
            super.updateDrawState(ds);
            ds.setColor(mMoreColor);
            ds.setUnderlineText(false);
            ds.clearShadowLayer();

        }
    };

    public void setMoreText(String moreText) {
        this.mMoreText = moreText;
    }

    private OnTextClickListener mOnTextClickListener;

    public void setOnTextClickListener(OnTextClickListener onTextClickListener) {
        this.mOnTextClickListener = onTextClickListener;
    }

    public interface OnTextClickListener {
        void onPreTextClick(String text);

        void onMoreTextClick(String text);
    }

}
