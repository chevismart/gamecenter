package gamecenter.core.processors.wechat;

import gamecenter.core.beans.AppProfile;
import gamecenter.core.beans.wechat.PayNotification;
import gamecenter.core.beans.wechat.WechatProfile;
import org.junit.Before;
import org.junit.Test;
import weixin.popular.bean.pay.PayNotify;
import weixin.popular.util.MapUtil;
import weixin.popular.util.SignatureUtil;
import weixin.popular.util.XMLConverUtil;

import java.util.Map;

import static junit.framework.TestCase.assertEquals;
import static org.mockito.Mockito.mock;

public class WechatPayNotificationProcessorTest {

    ProfileManager profileManager;
    AppProfile appProfile;
    WechatProfile wechatProfile;
    Map<String, AppProfile> profiles;
    WechatPayNotificationProcessor processor;

    @Before
    public void setUp() throws Exception {
        profileManager = mock(ProfileManager.class);
        appProfile = mock(AppProfile.class);
        wechatProfile = mock(WechatProfile.class);
        profiles = mock(Map.class);
        processor = new WechatPayNotificationProcessor();
    }

    @Test
    public void validateTheSignForPayNotification() throws Exception {
        PayNotification payNotification = new PayNotification();
        payNotification.setAppid("wxe89a9d2fa17df80f");
        payNotification.setAttach("ATM001");
        payNotification.setBank_type("CFT");
        payNotification.setCash_fee("1");
        payNotification.setFee_type("CNY");
        payNotification.setIs_subscribe("Y");
        payNotification.setMch_id("1224905202");
        payNotification.setNonce_str("oiqYaWCchi4fcVoCAkiVxryfBvC7oCtC");
        payNotification.setOpenid("oJpyYuBcMmRKmVCt6AaAKN9EDGac");
        payNotification.setOut_trade_no("trade_1420218007092");
        payNotification.setResult_code("SUCCESS");
        payNotification.setReturn_code("SUCCESS");
        payNotification.setTime_end("20150103010049");
        payNotification.setTotal_fee(1);
        payNotification.setTrade_type("NATIVE");
        payNotification.setTransaction_id("1003440412201501030009220721");
        payNotification.setSign("5D7787D69DDC7DF9E2BB1B6EDF9C32C6");

        String xml = "<xml><appid><![CDATA[wxe89a9d2fa17df80f]]></appid>\n" +
                "<attach><![CDATA[ATM001]]></attach>\n" +
                "<bank_type><![CDATA[CFT]]></bank_type>\n" +
                "<cash_fee><![CDATA[1]]></cash_fee>\n" +
                "<fee_type><![CDATA[CNY]]></fee_type>\n" +
                "<is_subscribe><![CDATA[Y]]></is_subscribe>\n" +
                "<mch_id><![CDATA[1224905202]]></mch_id>\n" +
                "<nonce_str><![CDATA[kI3zsmaijRDPRAo3xNwWb7iQXYqtwNFe]]></nonce_str>\n" +
                "<openid><![CDATA[oJpyYuBcMmRKmVCt6AaAKN9EDGac]]></openid>\n" +
                "<out_trade_no><![CDATA[trade_1420253467725]]></out_trade_no>\n" +
                "<result_code><![CDATA[SUCCESS]]></result_code>\n" +
                "<return_code><![CDATA[SUCCESS]]></return_code>\n" +
                "<sign><![CDATA[24D9BA0A3610A143050C15C8F0E1B4E6]]></sign>\n" +
                "<time_end><![CDATA[20150103105151]]></time_end>\n" +
                "<total_fee>1</total_fee>\n" +
                "<trade_type><![CDATA[NATIVE]]></trade_type>\n" +
                "<transaction_id><![CDATA[1003440412201501030009225342]]></transaction_id>\n" +
                "</xml>";

        PayNotification notification = XMLConverUtil.convertToObject(PayNotification.class, new String(xml.getBytes("iso-8859-1"), "utf-8"));

        String validationSign = SignatureUtil.generateSign(MapUtil.order(MapUtil.objectToMap(notification,"device_info","err_code","err_code_des","return_msg")), "wawaonline20150101wechatpaybilly");

        assertEquals(notification.getSign(), validationSign);

    }

    @Test
    public void raiseTheHttpRequestAndTopupCoinSuccessfullly() throws Exception {
//        processor.topupCoins("ABCDEF0000",1);

    }
}