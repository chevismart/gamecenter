package gamecenter.core.processors;

import gamecenter.core.beans.UserProfile;
import gamecenter.core.services.db.DBServices;

public class PocketProcessor extends GeneralProcessor {
    private final UserProfile userProfile;
    private final DBServices dbServices;
    private int wallet = 0;

    public PocketProcessor(UserProfile userProfile, DBServices dbServices) {
        this.userProfile = userProfile;
        this.dbServices = dbServices;
    }

    @Override
    public String execute() throws Exception {
        wallet = dbServices.getCustomerService().getCustomerWalletBalanceByOpenId(userProfile.getOpenId());
        logger.debug("User profile is {} and the wallet is {}", userProfile, wallet);
        return super.execute();
    }

    public UserProfile getUserProfile() {
        return userProfile;
    }

    public int getWallet() {
        return wallet;
    }
}
