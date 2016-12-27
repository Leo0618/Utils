package com.leo618.utils;

import android.util.SparseArray;
import android.view.View;

/**
 * function : findViewById带item复用功能的ViewHolder.
 */
@SuppressWarnings({"unchecked", "unused"})
public class ViewHolderUtil {

    public static <T extends View> T findViewById(View view, int id) {
        SparseArray<View> viewHolder = (SparseArray<View>) view.getTag();
        if (viewHolder == null) {
            viewHolder = new SparseArray<>();
            view.setTag(viewHolder);
        }
        View childView = viewHolder.get(id);
        if (childView == null) {
            childView = view.findViewById(id);
            viewHolder.put(id, childView);
        }
        return (T) childView;
    }
}
