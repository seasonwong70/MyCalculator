package com.wangzixin.mycalculator;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;


public class CalculatorActivity extends AppCompatActivity implements View.OnClickListener {
//    private float mSum=0;
//    private float mOldSum=0;
//    private WebView mDisplayArea;
    private TextView mDisplayArea;
    private StringBuilder mMathString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculator);

        GridLayout gridLayout = (GridLayout) findViewById(R.id.grid_layout);
        gridLayout.setUseDefaultMargins(false);
        gridLayout.setAlignmentMode(GridLayout.ALIGN_BOUNDS);
        gridLayout.setRowOrderPreserved(false);
        //textView
        mDisplayArea = (TextView) findViewById(R.id.display_area);
//        mDisplayArea.getSettings().setJavaScriptEnabled(true);

        //maStringBuilder initiate
        mMathString = new StringBuilder();
//        mMathString.append('1');
//        updateWebView();


        //set button events
//        findViewById(R.id.plus_equal_btn).setOnClickListener(this);
//        findViewById(R.id.minus_btn).setOnClickListener(this);
//        findViewById(R.id.duplicate_btn).setOnClickListener(this);
//        findViewById(R.id.divide_by_btn).setOnClickListener(this);
//        findViewById(R.id.number_0_btn).setOnClickListener(this);
//        findViewById(R.id.number_1_btn).setOnClickListener(this);
//        findViewById(R.id.number_2_btn).setOnClickListener(this);
//        findViewById(R.id.number_3_btn).setOnClickListener(this);
//        findViewById(R.id.number_4_btn).setOnClickListener(this);
//        findViewById(R.id.number_5_btn).setOnClickListener(this);
//        findViewById(R.id.number_6_btn).setOnClickListener(this);
//        findViewById(R.id.number_7_btn).setOnClickListener(this);
//        findViewById(R.id.number_8_btn).setOnClickListener(this);
//        findViewById(R.id.number_9_btn).setOnClickListener(this);
//        findViewById(R.id.clear_btn).setOnClickListener(this);
        int[] idList={R.id.number_0_btn,R.id.number_1_btn,R.id.number_2_btn,R.id.number_3_btn,R.id.number_4_btn,R.id.number_5_btn,
                        R.id.number_6_btn,R.id.number_7_btn,R.id.number_8_btn,R.id.number_9_btn,R.id.plus_btn,R.id.minus_btn,
                          R.id.duplicate_btn,R.id.divide_by_btn,R.id.back_btn,R.id.clear_btn,R.id.point_btn,R.id.equal_btn};
        for (int id:idList){
            findViewById(id).setOnClickListener(this);
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.equal_btn:
                try {
                    if(mMathString.length()>0){
                        double result = eval(mMathString.toString());
                        System.out.println(result);
                        mMathString.delete(0,mMathString.length());
                        mMathString.append(result);
                    }
                }catch (Exception e){
                    Toast.makeText(this,"expression invalid",Toast.LENGTH_SHORT).show();
                }
                break;
//            case R.id.minus_btn:
//                break;
//            case R.id.duplicate_btn:
//                break;
//            case R.id.divide_by_btn:
//                break;
            case R.id.clear_btn:
                if(mMathString.length()>0)
                    mMathString.delete(0,mMathString.length());
                break;
            case R.id.back_btn:
                if(mMathString.length()>0)
                    mMathString.deleteCharAt(mMathString.length()-1);
                break;

            default:
                mMathString.append(((Button) v).getText());

        }
        updateTextView();
        System.out.println(((Button) v).getText());
    }


    /**
     * update the WebView, that is the display area
     */
//    private void updateWebView() {
//
//        StringBuilder builder = new StringBuilder();
//
//        builder.append("<html><body>");
//        builder.append("<script type=\"text/javascript\">document.write('");
//        builder.append(mMathString.toString());
//        builder.append("');");
//        builder.append("document.write('<br />=' + eval(\"");
//        builder.append(mMathString.toString());
//        builder.append("\"));</script>");
//        builder.append("</body></html>");
//
//        mDisplayArea.loadData(builder.toString(), "application/xhtml", "UTF-8");
//    }

    /**
     * update the textView
     */
    private void updateTextView(){
//        StringBuilder builder = new StringBuilder();
        mDisplayArea.setText(mMathString);
    }


    /**
     * the method calculate a string and return double
     */
    public static double eval(final String str) {
        class Parser {
            int pos = -1, c;

            void eatChar() {
                c = (++pos < str.length()) ? str.charAt(pos) : -1;
            }

            void eatSpace() {
                while (Character.isWhitespace(c)) eatChar();
            }

            double parse() {
                eatChar();
                double v = parseExpression();
                if (c != -1) throw new RuntimeException("Unexpected: " + (char)c);
                return v;
            }

            // Grammar:
            // expression = term | expression `+` term | expression `-` term
            // term = factor | term `*` factor | term `/` factor | term brackets
            // factor = brackets | number | factor `^` factor
            // brackets = `(` expression `)`

            double parseExpression() {
                double v = parseTerm();
                for (;;) {
                    eatSpace();
                    if (c == '+') { // addition
                        eatChar();
                        v += parseTerm();
                    } else if (c == '-') { // subtraction
                        eatChar();
                        v -= parseTerm();
                    } else {
                        return v;
                    }
                }
            }

            double parseTerm() {
                double v = parseFactor();
                for (;;) {
                    eatSpace();
                    if (c == '/') { // division
                        eatChar();
                        v /= parseFactor();
                    } else if (c == '*' || c == '(') { // multiplication
                        if (c == '*') eatChar();
                        v *= parseFactor();
                    } else {
                        return v;
                    }
                }
            }

            double parseFactor() {
                double v;
                boolean negate = false;
                eatSpace();
                if (c == '+' || c == '-') { // unary plus & minus
                    negate = c == '-';
                    eatChar();
                    eatSpace();
                }
                if (c == '(') { // brackets
                    eatChar();
                    v = parseExpression();
                    if (c == ')') eatChar();
                } else { // numbers
                    StringBuilder sb = new StringBuilder();
                    while ((c >= '0' && c <= '9') || c == '.') {
                        sb.append((char)c);
                        eatChar();
                    }
                    if (sb.length() == 0) throw new RuntimeException("Unexpected: " + (char)c);
                    v = Double.parseDouble(sb.toString());
                }
                eatSpace();
                if (c == '^') { // exponentiation
                    eatChar();
                    v = Math.pow(v, parseFactor());
                }
                if (negate) v = -v; // unary minus is applied after exponentiation; e.g. -3^2=-9
                return v;
            }
        }
        return new Parser().parse();
    }

}
