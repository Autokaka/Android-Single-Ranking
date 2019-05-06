package it.unibo.cs.lbedogni;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class CalcolatriceActivity extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        Button btn_plus = (Button)findViewById(R.id.btn_plus);
        btn_plus.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				Toast.makeText(CalcolatriceActivity.this,"You pressed plus", Toast.LENGTH_LONG).show();
				setOperand("+");
			}
		});
        
        Button btn_minus = (Button)findViewById(R.id.btn_minus);
        btn_minus.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				Toast.makeText(CalcolatriceActivity.this,"You pressed minus", Toast.LENGTH_LONG).show();
				setOperand("-");
			}
		});
        
        Button btn_mul = (Button)findViewById(R.id.btn_mul);
        btn_mul.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				setOperand("x");
			}
		});
        
        Button btn_div = (Button)findViewById(R.id.btn_div);
        btn_div.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				setOperand(":");
			}
		});
        
        Button btn_pow = (Button)findViewById(R.id.btn_pow);
        btn_pow.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				setOperand("^");
			}
		});
        
        Button btn_equals = (Button)findViewById(R.id.btn_equal);
        btn_equals.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				EditText et_left = (EditText)findViewById(R.id.et_left);
				EditText et_right = (EditText)findViewById(R.id.et_right);
				EditText et_result = (EditText)findViewById(R.id.et_result);
				TextView tv_op = (TextView)findViewById(R.id.tv_op);
				
				String op = (String) tv_op.getText();
				
				int left = Integer.parseInt(et_left.getText().toString());
				int right = Integer.parseInt(et_right.getText().toString());
				double result = 0;
				if (op.equals("+")) {
					result =  left + right;
				} else if (op.equals("-")) {
					result = left - right;
				} else if (op.equals("x")) {
					result = left * right;
				} else if (op.equals(":")) {
					result = (float)left / right;
				} else if (op.equals("^")) {
					result = Math.pow(left, right);
				}
				et_result.setText(String.valueOf(result));
				
			}
		});
    }
    
    public void setOperand(String op) {
    	TextView tv_op = (TextView)findViewById(R.id.tv_op);
    	tv_op.setText(op);
    	
    	EditText et_left = (EditText)findViewById(R.id.et_left);
    	if (et_left.getText().toString().equals("")) {
    		et_left.requestFocus();
    	} else {
    		EditText et_right = (EditText)findViewById(R.id.et_right);
    		et_right.requestFocus();
    	}
    }
}