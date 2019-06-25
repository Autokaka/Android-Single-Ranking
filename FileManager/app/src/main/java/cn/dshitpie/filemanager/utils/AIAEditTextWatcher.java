package cn.dshitpie.filemanager.utils;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

/**
* AddItemActivityEditTextWatcher
*/
public class AIAEditTextWatcher implements TextWatcher {
    private int limit;// 字符个数限制
    private EditText text;// 编辑框控件
    private Context context;// 上下文对象
    int cursor = 0;// 用来记录输入字符的时候光标的位置
    int before_length;// 用来标注输入某一内容之前的编辑框中的内容的长度
    public AIAEditTextWatcher(EditText text, int limit,
                         Context context) {
        this.limit = limit;
        this.text = text;
        this.context = context;
    }
    @Override
    public void beforeTextChanged(CharSequence s, int start, int count,
                                  int after) {
        before_length = s.length();
    }
    /**
     * s 编辑框中全部的内容 、start 编辑框中光标所在的位置（从0开始计算）、count 从手机的输入法中输入的字符个数
     */
    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        cursor = start;
        // Log.e("此时光标的位置为", cursor + "");
    }
    @Override
    public void afterTextChanged(Editable s) {
        // 这里可以知道你已经输入的字数，大家可以自己根据需求来自定义文本控件实时的显示已经输入的字符个数
        Log.e("此时你已经输入了", "" + s.length());
        int after_length = s.length();// 输入内容后编辑框所有内容的总长度
        // 如果字符添加后超过了限制的长度，那么就移除后面添加的那一部分，这个很关键
        if (after_length > limit) {
            // 比限制的最大数超出了多少字
            int d_value = after_length - limit;
            // 这时候从手机输入的字的个数
            int d_num = after_length - before_length;
            int st = cursor + (d_num - d_value);// 需要删除的超出部分的开始位置
            int en = cursor + d_num;// 需要删除的超出部分的末尾位置
            // 调用delete()方法将编辑框中超出部分的内容去掉
            Editable s_new = s.delete(st, en);
            // 给编辑框重新设置文本
            text.setText(s_new.toString());
            // 设置光标最后显示的位置为超出部分的开始位置，优化体验
            text.setSelection(st);
            // 弹出信息提示已超出字数限制
            Toast.makeText(context, "已超出最大字数限制", Toast.LENGTH_SHORT).show();
        }
    }
}
