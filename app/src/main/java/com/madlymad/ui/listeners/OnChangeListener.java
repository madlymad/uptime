package com.madlymad.ui.listeners;

/**
 * Created on 6/4/2018.
 *
 * @author mando
 */
public interface OnChangeListener {
    /**
     * @param newValue the new value that selected for the change
     * @return <code>true</code> in case the change should performed
     */
    boolean onChange(Object newValue);
}
