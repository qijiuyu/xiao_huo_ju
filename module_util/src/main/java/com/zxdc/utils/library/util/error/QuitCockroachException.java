package com.zxdc.utils.library.util.error;

/**
 * Created by wanjian on 2017/2/15.
 */

final class QuitCockroachException extends RuntimeException {
    public QuitCockroachException(String message) {
        super(message);
    }
}
