package com.danikula.android.garden.ui.list;

import android.view.View;
import android.view.View.OnClickListener;

/**
 * Выполняет longclick по указанному компоненту.
 * 
 * @author danik
 */
public class PerformLongClickOnClickListener implements OnClickListener {

    private View viewToClick;

    /**
     * Конструктор с указанием, по какому вью сделать лонгклик
     * 
     * @param viewToClick View view, по которому будет совершен лонгклик
     */
    public PerformLongClickOnClickListener(View viewToClick) {
        this.viewToClick = viewToClick;
    }

    @Override
    public void onClick(View v) {
        viewToClick.performLongClick();
    }
}