package gamecenter.core.utils;

import com.thoughtworks.xstream.XStream;
import gamecenter.core.beans.AppProfile;
import gamecenter.core.beans.wechat.WechatProfile;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Chevis on 14-7-19.
 */
public class XMLMessageConverter extends XStream {

    private final static String DEFAULT_ROOT_NODE = "messages";
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final String root_node;

    public XMLMessageConverter(String root_node) {
        this.root_node = root_node;
        this.alias(StringUtils.isNotEmpty(this.root_node) ? this.root_node : DEFAULT_ROOT_NODE, List.class);
        this.alias("application", AppProfile.class);
        this.alias(WechatProfile.class.getSimpleName(), WechatProfile.class);
    }

    public String convertObject2XML(Object obj) {
        String xmlStr = null;
        try {
            xmlStr = this.toXML(obj);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return xmlStr;
    }


    public List<AppProfile> convertXML2Messages(InputStream xmlFile) {
        List<AppProfile> msgList = new ArrayList<AppProfile>();
        try {
            msgList = (List<AppProfile>) this.fromXML(xmlFile);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return msgList;
    }
}
