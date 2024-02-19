package com.tt.rc.config;

import com.tt.core.constant.MessageConst;
import com.tt.core.message.MessageManager;
import com.tt.core.net.util.MessageUtil;
import com.tt.message.msg.rc.req.IServerRegister;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public class MessageRegister {

	private MessageManager recoverMessageManager;
	private MessageManager sendMessageManager;
	private IServerRegister serverRegister;

	@Autowired
	public MessageRegister(@Qualifier(value = "recoverMessageManager") MessageManager recoverMessageManager,
						   @Qualifier(value = "sendMessageManager") MessageManager sendMessageManager,
						   IServerRegister serverRegister) throws Exception {
		this.recoverMessageManager = recoverMessageManager;
		this.sendMessageManager = sendMessageManager;
		this.serverRegister = serverRegister;
		registerMsg();
	}

	/**
	 * 注册协议失败报错停止启动
	 *
	 * @throws NoSuchMethodException
	 */
	private void registerMsg() throws Exception {
		registerReq();
		registerResp();
	}

	/**
	 * 注册请求
	 *
	 * @throws NoSuchMethodException
	 */
	private void registerReq() throws Exception {
		recoverMessageManager.registerMsg(serverRegister);
	}

	/**
	 * 注册回复
	 *
	 * @throws NoSuchMethodException
	 */
	private void registerResp() throws Exception {
		var set = MessageUtil.getMessageClzSet(MessageConst.RC_RESP_MESSAGE_PACKAGE_NAME);
		for (Class<?> clz : set) {
			sendMessageManager.registerMsg(clz);
		}
	}
}
