package com.test.mycalc;

        import java.text.NumberFormat;
        import android.app.Activity;
        import android.os.Bundle;
        import android.view.View;
        import android.widget.Button;
        import android.widget.TextView;

public class MainActivity extends Activity {
    /** Called when the activity is first created. */

    double firstNum = 0;// 第一个输入的数据
    char currentSign = '+';// 记录第一次输入的符号
    StringBuffer currentNum = new StringBuffer();// 得到textview中的数据
    boolean isFirstPoint = false;// 第一个数据是否是小数点
    TextView txtResult;// 输出结果

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtResult = (TextView) findViewById(R.id.txtResult);
    }

    /**
     * 对数据进行初始化
     *
     * @return
     */
    public void init() {
        currentNum.delete(0, currentNum.length());// 设置当前textView中的值为0
        isFirstPoint = false;
    }

    /**
     *将输入的数据转换成double类型
     *
     * @return
     */
    public double stringToDouble() {
        if (currentNum.length() == 0) {// 如果没有输入的数据
            return 0;
        }
        double result = Double.parseDouble(currentNum.toString());
        return result;
    }
    /**
     * 进行计算处理
     * @return
     */
    public double calcu(){
        double result=0;
        switch(currentSign){
            case '+':
                result=firstNum+stringToDouble();
                break;
            case '-':
                result=firstNum-stringToDouble();
                break;
            case '*':
                result=firstNum*stringToDouble();
                break;
            case '/':
                result=firstNum/stringToDouble();
                break;
        }
        //对小数点后的数据进行格式化
        NumberFormat format =NumberFormat.getInstance();
        format.setMaximumFractionDigits(2);
        result=Double.parseDouble(format.format(result));
        return result;
    }
    /**
     * 显示数据
     */
    public void display(){

        txtResult.setText(currentNum.toString());
    }
    /**
     * 处理数据按钮的点击
     *
     * @param view
     */
    public void digital_click(View view) {
        Button btnDigital=(Button) view;
        char text=btnDigital.getText().charAt(0);
        currentNum.append(text);
        display();
    }

    /**
     * 处理加法
     *
     */
    public void add(View view) {
        double result=calcu();
        txtResult.setText(String.valueOf(result));
        firstNum=result;
        currentSign='+';
        init();
    }

    /**
     * 处理减法
     *
     */
    public void sub(View view) {
        double result=calcu();
        txtResult.setText(String.valueOf(result));
        firstNum=result;
        currentSign='-';
        init();
    }

    /**
     * 处理乘法
     *
     */
    public void mul(View view) {
        double result=calcu();
        txtResult.setText(String.valueOf(result));
        firstNum=result;
        currentSign='*';
        init();
    }

    /**
     * 处理除法
     *
     */
    public void div(View view) {
        double result=calcu();
        txtResult.setText(String.valueOf(result));
        firstNum=result;
        currentSign='/';
        init();
    }

    /**
     * 处理等于
     *
     */
    public void equ(View view) {
        double result=calcu();
        txtResult.setText(String.valueOf(result));
        firstNum=result;
        currentSign='+';
        init();
    }

    /**
     * 处理小数点
     *
     */
    public void point_click(View view) {
        if(isFirstPoint){//当地一个数据为小数点时程序返回
            return;
        }
        if(currentNum.length()==0){//当没有输入的数据时返回
            return;
        }
        Button btnPoint=(Button)view;
        char text=btnPoint.getText().charAt(0);
        currentNum.append(text);
        isFirstPoint=true;
        display();
    }
    /**
     * 处理删除数据
     */
    public void del(View view){

        if(currentNum.length()>=1)
        {
            currentNum.delete(currentNum.length()-1,currentNum.length());
        }
        if(currentNum.length()==0){

            init();
            display();
        }
        txtResult.setText(currentNum);
    }
}
