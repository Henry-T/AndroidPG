package com.apowo.base.util;

public enum EHttpResponseStatus {
	Succeed(0),
	Failed(1),					// 未明确的逻辑
	HttpStatusNot200(2), 		// Http错误
	SocketTimedOut(3), 			// Socket超时
	ConnectionTimedOut(4), 		// 连接超时
	UnknownHost(5); 			// 域名无法解析

	private final int id;
	EHttpResponseStatus(int id) { this.id = id; }
	public int getValue() { return id; }
}
