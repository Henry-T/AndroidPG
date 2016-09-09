package com.lolofinil.AndroidPG.Common.BaseLib.util;

public enum EHttpResponseStatus {
	Succeed(0),
	Failed(1),									// 未明确的逻辑
	HttpStatusNot200(2), 						// Http错误
	SocketTimedOut(3), 							// Socket超时
	ConnectionTimedOut(4), 						// 连接超时
	UnknownHost(5), 							// 域名无法解析
	NetworkUnavailable(6),						// 未连接网络
	UnexpectedResponseBodyFormat(7); 			// 响应信息格式与预期不符

	private final int id;
	EHttpResponseStatus(int id) { this.id = id; }
	public int getValue() { return id; }
}
