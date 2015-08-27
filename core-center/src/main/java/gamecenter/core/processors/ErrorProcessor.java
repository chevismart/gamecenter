package gamecenter.core.processors;

import com.opensymphony.xwork2.ActionContext;

public class ErrorProcessor extends GeneralProcessor {
    private Exception exception;

    public Exception getException() {
        return exception;
    }

    public void setException(Exception exception) {
        this.exception = exception;
    }

    @Override
    public String execute() {
        ActionContext.getContext().getValueStack().push(this.exception.getMessage());//放到值栈顶
        return SUCCESS;
    }
}
