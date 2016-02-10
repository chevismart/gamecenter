package gamecenter.core.services.db;

import gamecenter.core.dao.ChargeHistoryMapper;
import gamecenter.core.dao.PlayrecordMapper;
import gamecenter.core.domain.ChargeHistory;
import gamecenter.core.domain.Playrecord;

import java.util.Date;
import java.util.List;

public class ChargeHistoryService {
    private final ChargeHistoryMapper chargeHistoryMapper;
    private final PlayrecordMapper playrecordMapper;

    public ChargeHistoryService(ChargeHistoryMapper chargeHistoryMapper, PlayrecordMapper playrecordMapper) {
        this.chargeHistoryMapper = chargeHistoryMapper;
        this.playrecordMapper = playrecordMapper;
    }

    public void addChargeHistory(ChargeHistory chargeHistory) {
        chargeHistoryMapper.insert(chargeHistory);
    }

    public List<ChargeHistory> selectHistoryByDate(Date startDate, Date endDate) {
        return chargeHistoryMapper.selectHistoryRecordByDate(startDate, endDate);
    }

    public List<Playrecord> selectPlayRecordByDate(Date startDate, Date endDate){
        return playrecordMapper.selectPlayRecordByDate(startDate,endDate);
    }
}
