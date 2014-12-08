package gamecenter.core.actions;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;

/**
 * Created by Chevis on 2014/12/8.
 */
public class ErrorProcesser extends ActionSupport{
    private Exception exception;

    public Exception getException() {
        return exception;
    }

    public void setException(Exception exception) {
        this.exception = exception;
    }
    @Override
    public String execute()
    {
        ActionContext.getContext().getValueStack().push(this.exception.getMessage());//放到值栈顶
        return this.SUCCESS;
    }
}
