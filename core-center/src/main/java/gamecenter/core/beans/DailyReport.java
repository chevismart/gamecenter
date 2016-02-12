package gamecenter.core.beans;

import java.util.Date;

public class DailyReport {
    private final Date date;
    private final double income;
    private final int output;
    private final int actualOutput;
    private final int newSubscribersQty;
    private final int allSubscribersQty;

    public DailyReport(Date date, double income, int output, int actualOutput, int newSubscribersQty, int allSubscribersQty) {
        this.date = date;
        this.income = income;
        this.output = output;
        this.actualOutput = actualOutput;
        this.newSubscribersQty = newSubscribersQty;
        this.allSubscribersQty = allSubscribersQty;
    }

    @Override
    public String toString() {
        return "DailyReport{" +
                "date=" + date +
                ", income=" + income +
                ", output=" + output +
                ", actualOutput=" + actualOutput +
                ", newSubscribersQty=" + newSubscribersQty +
                ", allSubscribersQty=" + allSubscribersQty +
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

    public int getAllSubscribersQty() {
        return allSubscribersQty;
    }

    public int getNewSubscribersQty() {

        return newSubscribersQty;
    }
}
