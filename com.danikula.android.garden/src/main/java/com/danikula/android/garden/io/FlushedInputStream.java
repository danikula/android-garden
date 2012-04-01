package com.danikula.android.garden.io;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * http://code.google.com/p/android/issues/detail?id=6066
 * 
 * @author danik
 */
public class FlushedInputStream extends FilterInputStream {
    
    public FlushedInputStream(InputStream inputStream) {
        super(inputStream);
    }

    @Override
    public long skip(long n) throws IOException {
        long totalBytesSkipped = 0L;
        while (totalBytesSkipped < n) {
            long bytesSkipped = in.skip(n - totalBytesSkipped);
            if (bytesSkipped == 0L) {
                int oneByte = read();
                if (oneByte < 0) {
                    break; // we reached EOF
                }
                bytesSkipped = 1; // we read one byte
            }
            totalBytesSkipped += bytesSkipped;
        }
        return totalBytesSkipped;
    }
}