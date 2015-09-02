package gamecenter.core.utils;

import gamecenter.core.beans.AccessChannel;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;
import weixin.popular.api.MenuAPI;
import weixin.popular.bean.BaseResult;
import weixin.popular.bean.MenuButtons;
import weixin.popular.bean.MenuButtons.Button;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static java.util.Arrays.asList;

public class WechatMenuTool {

    public static void createMenu() {

        String accessToken = getAccessToken();
        System.err.println("Token got: " + accessToken);

        MenuButtons menuButtons = new MenuButtons();
        Button buttonGroup1 = new Button();
        buttonGroup1.setName("开始游戏");

        Button buttonPlayIt = new Button();
        buttonPlayIt.setName("使用钱包代币");
        buttonPlayIt.setType("view");
        buttonPlayIt.setUrl("http://wawaonline.net/corecenter/wechatLogin");

        Button buttonPlayFree = new Button();
        buttonPlayFree.setName("免费试玩");
        buttonPlayFree.setType("view");
        buttonPlayFree.setUrl("http://wawaonline.net/corecenter/wechatLogin");

        buttonGroup1.setSub_button(asList(buttonPlayFree, buttonPlayIt));

        Button buttonGroup2 = new Button();
        buttonGroup2.setName("我的账户");

        Button buttonBuyCoin = new Button();
        buttonBuyCoin.setName("微信购币");
        buttonBuyCoin.setType("scancode_push");
        buttonBuyCoin.setKey("buy_coin");

        Button buttonBalance = new Button();
        buttonBalance.setName("账户余额");
        buttonBalance.setType("click");
        buttonBalance.setKey("account_balance");
        buttonGroup2.setSub_button(asList(buttonBalance, buttonBuyCoin));

        Button buttonGroup3 = new Button();
        buttonGroup3.setName("联系我们");
        buttonGroup3.setType("click");
        buttonGroup3.setKey("contact_us");

        menuButtons.setButton(new Button[]{buttonGroup1, buttonGroup2, buttonGroup3});
        BaseResult result = MenuAPI.menuCreate(accessToken, menuButtons);
        System.out.println(result.getErrcode() + " " + result.getErrmsg());
    }

    private static String getAccessToken() {
        List<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
        params.add(new BasicNameValuePair("clientId", "client01"));
        params.add(new BasicNameValuePair("appId", "liyuanapp"));
        params.add(new BasicNameValuePair("credentialType", AccessChannel.WECHAT.name()));
        String token = null;
        try {
            HttpResponse response = HttpService.post("http://wawaonline.net/corecenter/credential", params);
            token = new JSONObject(IOUtils.toString(response.getEntity().getContent())).getString("access_token");
        } catch (IOException e) {
            System.err.println("Request token failure.");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return token;
    }

    public static void main(String[] args) {
        createMenu();
    }
}