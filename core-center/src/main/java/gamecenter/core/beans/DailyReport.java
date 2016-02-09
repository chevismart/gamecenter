package gamecenter.core.beans;

import java.util.Date;

public class DailyReport {
    private final Date date;
    private final double income;
    private final int output;
    private final int actualOutput;

    public DailyReport(Date date, double income, int output, int actualOutput) {
        this.date = date;
        this.income = income;
        this.output = output;
        this.actualOutput = actualOutput;

    }

    @Override
    public String toString() {
        return "DailyReport{" +
                "date=" + date +
                ", income=" + income +
                ", output=" + output +
                ", actualOutput=" + actualOutput +
                '}';
    }

    public int getActualOutput() {
        return actualOutput;
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
