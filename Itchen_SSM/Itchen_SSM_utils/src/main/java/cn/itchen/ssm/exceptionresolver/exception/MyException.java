package cn.itchen.ssm.exceptionresolver.exception;

/**
 * 自定义异常
 */
public class MyException extends RuntimeException {
    private String message;

    public MyException(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
