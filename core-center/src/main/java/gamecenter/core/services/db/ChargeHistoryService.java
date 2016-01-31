package gamecenter.core.services.db;

import gamecenter.core.dao.ChargeHistoryMapper;
import gamecenter.core.domain.ChargeHistory;

import java.util.Date;

public class ChargeHistoryService {
    private final ChargeHistoryMapper chargeHistoryMapper;

    public ChargeHistoryService(ChargeHistoryMapper chargeHistoryMapper) {
        this.chargeHistoryMapper = chargeHistoryMapper;
    }

    public void addChargeHistory(){
        ChargeHistory chargeHistory = new ChargeHistory();
        chargeHistory.setCenterId(1);
        chargeHistory.setChargeHistoryId(1);
        chargeHistory.setCustomerId(39);
        chargeHistory.setPaid(1.99);
        chargeHistory.setTimestamp(new Date());
        chargeHistory.setWechatId(1);
        chargeHistoryMapper.insert(chargeHistory);
    }
}
