package org.wso2.carbon.apimgt.core.exception;

/**
 * This is an Exception class for API file based operations related exceptions
 */
public class APIFileUtilException extends APIManagementException {

    public APIFileUtilException(String message, Throwable cause) {
        super(message, cause);
    }

    public APIFileUtilException(String message) {
        super(message);
    }

    public APIFileUtilException(String message, ErrorHandler code) {
        super(message, code);
    }

    public APIFileUtilException(String message, Throwable cause, ErrorHandler code) {
        super(message, cause, code);
    }
}
