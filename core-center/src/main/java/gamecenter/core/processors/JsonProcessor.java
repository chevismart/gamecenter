package gamecenter.core.processors;

import gamecenter.core.beans.UserProfile;
import org.apache.struts2.ServletActionContext;

import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

public class JsonProcessor extends GeneralProcessor {
    private final UserProfile userProfile;

    public JsonProcessor(UserProfile userProfile) {
        this.userProfile = userProfile;
    }

    @Override
    public String execute() throws Exception {
        HttpServletResponse response = ServletActionContext.getResponse();
        response.setContentType("application/json;charset=utf-8");
        response.setStatus(200);
        PrintWriter out = response.getWriter();
        logger.debug("User is {}", userProfile);
        String jsonString = "{\"bonus\":\"" + userProfile.getBonus() + "\"}";
        logger.debug("Json result is {}", jsonString);
        out.println(jsonString);
        out.flush();
        out.close();
        return super.execute();
    }
}
