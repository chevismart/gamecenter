package gamecenter.core.processors.tasks;

import gamecenter.core.beans.DailyReport;
import gamecenter.core.domain.ChargeHistory;
import gamecenter.core.services.BroadcastService;
import gamecenter.core.services.db.DBServices;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class ReportGenerator implements ScheduleTask {
    private final static long daySpan = 24 * 60 * 60 * 1000;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd '22:30:00'");
    private final DBServices dbServices;
    private final BroadcastService broadcastService;

    public ReportGenerator(DBServices dbServices, BroadcastService broadcastService) {
        this.dbServices = dbServices;
        this.broadcastService = broadcastService;
    }

    public long interval() {
        return daySpan;
    }

    public long initDelay() {
        // 首次运行时间
        try {
            Date startTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(sdf.format(new Date()));
            Date now = new Date();
            long delay = now.before(startTime) ? startTime.getTime() - now.getTime() : DateUtils.addDays(startTime, 1).getTime() - now.getTime();
            return delay;
        } catch (ParseException e) {
            logger.error("Schedule {} error:", this.getClass().getSimpleName(), e);
        }
        return -1;
    }

    public void run() {
        try {
            Date today = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(sdf.format(new Date()));
            Date yesterday = DateUtils.addDays(today, -1);
            logger.debug("Generating report from {} to {}", yesterday, today);
            List<ChargeHistory> chargeHistoryList = dbServices.getChargeHistoryService().selectHistoryByDate(yesterday, today);
            int totalCoin = 0;
            double totalMoney = 0;
            for (ChargeHistory chargeHistory : chargeHistoryList) {
                totalCoin += chargeHistory.getCoin();
                totalMoney += chargeHistory.getPaid();
            }
            DailyReport report = new DailyReport(today, totalMoney, totalCoin, 0); //TODO: Check the actual coin output from table playrecord in db
            logger.info("Daily report is {}", report);
            broadcastService.sendDailyReportForOwner(report);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}
