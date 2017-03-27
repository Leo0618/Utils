package com.leo618.utils;

import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

/**
 * function:Intent工具类
 *
 * <p></p>
 * Created by lzj on 2017/3/9.
 */
@SuppressWarnings("ALL")
public final class IntentLauncher {
    private IntentLauncher() {
    }

    private static WeakReference<Context> weakReference;
    private volatile static Intent mIntent = null;

    public static IntentLauncher with(Context sourceContext) {
        weakReference = new WeakReference<>(sourceContext);
        mIntent = new Intent();
        return new IntentLauncher();
    }

    public IntentLauncher put(String key, Object value) {
        if (value instanceof String) {
            mIntent.putExtra(key, (String) value);
        } else if (value instanceof Integer) {
            mIntent.putExtra(key, (int) value);
        } else if (value instanceof Double) {
            mIntent.putExtra(key, (double) value);
        } else if (value instanceof Float) {
            mIntent.putExtra(key, (float) value);
        } else if (value instanceof Boolean) {
            mIntent.putExtra(key, (boolean) value);
        } else if (value instanceof CharSequence) {
            mIntent.putExtra(key, (CharSequence) value);
        } else if (value instanceof Long) {
            mIntent.putExtra(key, (long) value);
        } else if (value instanceof Parcelable) {
            mIntent.putExtra(key, (Parcelable) value);
        } else {
            throw new IllegalArgumentException("当前传入的数据类型暂未添加，请先添加.");
        }
        return this;
    }

    public IntentLauncher putListInt(String key, ArrayList<Integer> value) {
        mIntent.putIntegerArrayListExtra(key, value);
        return this;
    }

    public IntentLauncher putListChar(String key, ArrayList<CharSequence> value) {
        mIntent.putCharSequenceArrayListExtra(key, value);
        return this;
    }

    public IntentLauncher putListString(String key, ArrayList<String> value) {
        mIntent.putStringArrayListExtra(key, value);
        return this;
    }

    public IntentLauncher putListParcelable(String key, ArrayList<? extends Parcelable> value) {
        mIntent.putParcelableArrayListExtra(key, value);
        return this;
    }

    public void launch(Class<?> cls) {
        if (weakReference != null && weakReference.get() != null) {
            Context context = weakReference.get();
            mIntent.setClass(context, cls);
            context.startActivity(mIntent);
            weakReference.clear();
            weakReference = null;
            mIntent = null;
        }
    }

}
