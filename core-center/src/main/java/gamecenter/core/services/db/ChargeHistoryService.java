package gamecenter.core.services.db;

import gamecenter.core.dao.ChargeHistoryMapper;
import gamecenter.core.domain.ChargeHistory;

import java.util.Date;
import java.util.List;

public class ChargeHistoryService {
    private final ChargeHistoryMapper chargeHistoryMapper;

    public ChargeHistoryService(ChargeHistoryMapper chargeHistoryMapper) {
        this.chargeHistoryMapper = chargeHistoryMapper;
    }

    public void addChargeHistory(ChargeHistory chargeHistory) {
        chargeHistoryMapper.insert(chargeHistory);
    }

    public List<ChargeHistory> selectHistoryByDate(Date startDate, Date endDate) {
        return chargeHistoryMapper.selectHistoryRecordByDate(startDate, endDate);
    }
}
