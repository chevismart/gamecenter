package gamecenter.core.processors.wechat.messages;

import gamecenter.core.processors.Filter;
import gamecenter.core.processors.wechat.ProfileManager;
import gamecenter.core.services.BroadcastService;
import gamecenter.core.services.db.CustomerService;
import weixin.popular.bean.message.EventMessage;

public class CheekBalanceMessageHandler extends WechatMessageHandler {

    private static String MESSAGE_FORMAT = "你的钱包当前余额是: %s 币";
    private final CustomerService customerService;
    private final BroadcastService broadcastService;

    public CheekBalanceMessageHandler(Filter<String> msgTypeFilter, Filter<String> eventTypeFilter, Filter<String> keyFilter, CustomerService customerService, ProfileManager profileManager, BroadcastService broadcastService) {
        super(msgTypeFilter, eventTypeFilter, keyFilter, profileManager);
        this.customerService = customerService;
        this.broadcastService = broadcastService;
    }

    void handleIt(EventMessage eventMessage) {
        try {
            int walletBalance = customerService.getCustomerWalletBalanceByOpenId(openId(eventMessage));
            broadcastService.notifyBalance(openId(eventMessage), getAccessToken(eventMessage), walletBalance);
        } catch (Exception e) {
            logger.error("Check account balance failed with reason: {}", e);
        }
    }
}
