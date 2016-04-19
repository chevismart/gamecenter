package gamecenter.core.beans;

import gamecenter.core.beans.wechat.PayNotification;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class GlobalPaymentBeanTest {

    private final PayNotification payNotification = new PayNotification();
    public GlobalPaymentBean bean;

    @Before
    public void setUp() throws Exception {
        payNotification.setOut_trade_no(RandomStringUtils.randomAlphanumeric(10));
        bean = new GlobalPaymentBean();
    }

    @Test
    public void receiveATradeNeverBeenProcessed() throws Exception {
        bean.newPayment(payNotification);
        assertThat(bean.isNewPayment(payNotification.getOut_trade_no()), is(true));
        assertThat(bean.isProcessingPayment(payNotification), is(false));
        assertThat(bean.isProcessedPayment(payNotification), is(false));
    }

    @Test
    public void receiveATradeWhichIsProcessing() throws Exception {
        bean.newPayment(payNotification);
        bean.startProcessingPayment(payNotification);
        assertThat(bean.isNewPayment(payNotification.getOut_trade_no()), is(false));
        assertThat(bean.isProcessingPayment(payNotification), is(true));
        assertThat(bean.isProcessedPayment(payNotification), is(false));
    }

    @Test
    public void receiveATradeWhichHasBeenProcessed() throws Exception {
        bean.newPayment(payNotification);
        bean.startProcessingPayment(payNotification);
        bean.completePayment(payNotification);
        assertThat(bean.isNewPayment(payNotification.getOut_trade_no()), is(false));
        assertThat(bean.isProcessingPayment(payNotification), is(false));
        assertThat(bean.isProcessedPayment(payNotification), is(true));
    }

}