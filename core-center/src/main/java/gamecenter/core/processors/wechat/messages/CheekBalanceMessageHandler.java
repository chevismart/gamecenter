package gamecenter.core.processors.wechat.messages;

import gamecenter.core.processors.Filter;
import gamecenter.core.processors.wechat.ProfileManager;
import gamecenter.core.services.db.CustomerService;
import weixin.popular.bean.EventMessage;
import weixin.popular.bean.message.Message;
import weixin.popular.bean.message.TextMessage;

public class CheekBalanceMessageHandler extends WechatMessageHandler {

    private static String MESSAGE_FORMAT = "你的钱包当前余额是: %s 币";
    private final CustomerService customerService;
    private final ProfileManager profileManager;

    public CheekBalanceMessageHandler(Filter<String> msgTypeFilter, Filter<String> eventTypeFilter, Filter<String> keyFilter, CustomerService customerService, ProfileManager profileManager) {
        super(msgTypeFilter, eventTypeFilter, keyFilter);
        this.customerService = customerService;
        this.profileManager = profileManager;
    }

    void handleIt(EventMessage eventMessage) {
        try {
            int walletBalance = customerService.getCustomerWalletBalanceByOpenId(openId(eventMessage));
            Message message = new TextMessage(openId(eventMessage), String.format(MESSAGE_FORMAT, walletBalance));
            replyClient(getAccessToken(), message);
        } catch (Exception e) {
            logger.error("Check account balance failed with reason: {}", e);
        }
    }

    private String getAccessToken() {
        return profileManager.getAppProfile("liyuanapp").getWechatProfile().getWechatAccessToken().getAccess_token();
    }
}
