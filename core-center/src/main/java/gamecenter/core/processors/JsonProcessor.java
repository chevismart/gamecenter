package gamecenter.core.processors;

import gamecenter.core.beans.UserProfile;
import gamecenter.core.services.db.DBServices;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

import static org.apache.commons.lang3.StringUtils.EMPTY;

public class JsonProcessor extends GeneralProcessor {
    private final UserProfile userProfile;
    private final DBServices dbServices;

    public JsonProcessor(UserProfile userProfile, DBServices dbServices) {
        this.userProfile = userProfile;
        this.dbServices = dbServices;
    }

    @Override
    public String execute() throws Exception {
        HttpServletRequest request = getHttpRequest();
        HttpServletResponse response = getHttpResponse();
        response.setContentType("application/json;charset=utf-8");
        PrintWriter out = response.getWriter();
        response.setStatus(200);
        String query = request.getQueryString();
        logger.debug("Receive query string is {}", query);
        String jsonString = EMPTY;
        if (query.contains("bonus")) {
            logger.debug("User is {}", userProfile);
            jsonString = "{\"bonus\":\"" + userProfile.getBonus() + "\"}";
            logger.debug("Json result is {}", jsonString);
        } else if (query.contains("wallet")) {
            int wallet = dbServices.getCustomerService().getCustomerWalletBalanceByOpenId(userProfile.getOpenId());
            logger.debug("Wallet of {} is {}", userProfile.getOpenId(), wallet);
            jsonString = "{\"wallet\":\"" + wallet + "\"}";
        } else if(query.contains("charge")){

        } else {
            logger.debug("Nothing to do with query: {}", query);
        }
        out.println(jsonString);
        out.flush();
        out.close();
        return super.execute();
    }
}
