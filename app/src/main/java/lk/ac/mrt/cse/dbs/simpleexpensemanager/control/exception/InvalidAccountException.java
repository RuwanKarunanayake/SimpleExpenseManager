package lk.ac.mrt.cse.dbs.simpleexpensemanager.control.exception;

/**
 * Created by ruwan on 11/20/16.
 */

public class InvalidAccountException extends Exception {
    public InvalidAccountException(String detailMessage) {
        super(detailMessage);
    }

    public InvalidAccountException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }
}