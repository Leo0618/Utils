package com.leo618.utils;

import android.content.ClipboardManager;
import android.content.Context;

/**
 * function :剪切板工具类
 */
@SuppressWarnings("ALL")
public class ClipboardUtil {

    /**
     * 拷贝文本字符串到剪切板
     */
    public static void copyTextToClipboard(Context context, CharSequence content) {
        ClipboardManager clipboardManager = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        clipboardManager.setText(content);
    }

    /**
     * 从剪切板获取已剪切的文本
     */
    public static String pasteTextFromClipboard(Context context) {
        ClipboardManager clipboardManager = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        return clipboardManager.getText().toString().trim();
    }
}
