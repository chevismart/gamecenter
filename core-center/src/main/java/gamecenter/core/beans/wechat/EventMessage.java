package gamecenter.core.beans.wechat;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "xml")
@XmlAccessorType(XmlAccessType.FIELD)
public class EventMessage {
	
	 	@XmlElement(name = "FromUserName")
	    private String fromUserName;

	    @XmlElement(name = "ToUserName")
	    private String toUserName;

	    @XmlElement(name = "MsgType")
	    private String msgType;

	    @XmlElement(name = "CreateTime")
	    private String createTime ;
	    
	    @XmlElement(name = "Event")
	    private String event ;

		public String getFromUserName() {
			return fromUserName;
		}

		public void setFromUserName(String fromUserName) {
			this.fromUserName = fromUserName;
		}

		public String getToUserName() {
			return toUserName;
		}

		public void setToUserName(String toUserName) {
			this.toUserName = toUserName;
		}

		public String getMsgType() {
			return msgType;
		}

		public void setMsgType(String msgType) {
			this.msgType = msgType;
		}

		public String getCreateTime() {
			return createTime;
		}

		public void setCreateTime(String createTime) {
			this.createTime = createTime;
		}

		public String getEvent() {
			return event;
		}

		public void setEvent(String event) {
			this.event = event;
		}
	    
	    
}
