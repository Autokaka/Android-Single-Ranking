package cn.dshitpie.filemanager.utils;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

/**
 * EditTextWatcher
 * 监控EditText输入情况, 并返回输入结果
 **/
public class EditTextWatcher implements TextWatcher {
    private int cursor, editTextLimitLength, beforeLength;
    private Context context;
    private EditText editText;
    private Editable nowContent;

    public EditTextWatcher(Context context, EditText editText, int editTextLimitLength) {
        cursor = 0;
        this.context = context;
        this.editText = editText;
        this.editTextLimitLength = editTextLimitLength;
    }

    @Override
    public void beforeTextChanged(CharSequence content, int start, int count, int after) {
        beforeLength = content.length();
    }

    @Override
    public void onTextChanged(CharSequence content, int start, int before, int count) {
        cursor = start;
        Log.d(TagConsultant.TAG,"此时光标的位置为" + cursor);
    }

    @Override
    public void afterTextChanged(Editable content) {
        nowContent = content;
        // 这里可以知道你已经输入的字数，大家可以自己根据需求来自定义文本控件实时的显示已经输入的字符个数
        Log.d(TagConsultant.TAG, "你已经输入了 " + nowContent.length() + " 个字");
        // 输入内容后编辑框所有内容的总长度
        int afterLength = nowContent.length();
        if (afterLength <= editTextLimitLength) return;
        // 如果字符添加后超过了限制的长度，那么就移除后面添加的那一部分，这个很关键
        int overflowLength = afterLength - editTextLimitLength;
        // 这时候从手机输入的字的个数
        int insertedLength = afterLength - beforeLength;
        // 需要删除的超出部分的开始位置
        int start = cursor + (insertedLength - overflowLength);
        // 需要删除的超出部分的末尾位置
        int end = cursor + insertedLength;
        // 调用delete()方法将编辑框中超出部分的内容去掉
        nowContent = nowContent.delete(start, end);
        // 给编辑框重新设置文本
        editText.setText(nowContent.toString());
        // 设置光标最后显示的位置为超出部分的开始位置，优化体验
        editText.setSelection(start);
        // 弹出信息提示已超出字数限制
        Toast.makeText(context, "最大字数限制为20. 您已超出最大字数限制", Toast.LENGTH_LONG).show();
    }

    public String getNowContent() {
        if (null == nowContent || "".equals(nowContent)) return TagConsultant.FILE_DEFAULT_NAME;
        return nowContent.toString();
    }
}
