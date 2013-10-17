package com.danikula.android.garden.io;

import java.io.IOException;

/**
 * Error to be used for identifying aborting of IO operation
 * 
 * @author Alexey Danilov
 */
public class CancelException extends IOException {

    public CancelException(String message) {
        super(message);
    }
}
