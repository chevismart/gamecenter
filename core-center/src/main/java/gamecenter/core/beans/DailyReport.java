package gamecenter.core.beans;

import java.util.Date;

public class DailyReport {
    private final Date date;
    private final double income;
    private final int output;

    public DailyReport(Date date, double income, int output) {
        this.date = date;
        this.income = income;
        this.output = output;
    }

    @Override
    public String toString() {
        return "DailyReport{" +
                "date=" + date +
                ", income=" + income +
                ", output=" + output +
                '}';
    }

    public Date getDate() {
        return date;
    }

    public double getIncome() {
        return income;
    }

    public int getOutput() {
        return output;
    }
}
