package com.sctn.sctnet.Utils;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;


public class SysException extends GenericException {

	private static final long serialVersionUID = 1L;

	/**
	 * SysException�ĸ��ٵ�����Ϣ��һ���������ͷ�����
	 */
	private String trace = "";

	public SysException() {
	}

	public SysException(String errId, String errOwnMsg, Exception oriEx) {

			
		super(oriEx);
		this.errId = errId;
		this.errMsg = errOwnMsg;
		this.errOri = oriEx;
		
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		PrintStream p = new PrintStream(os);
		this.errOri.printStackTrace(p);
		this.errMsgOri = os.toString().trim();
		this.writeSysException();
	}

	public SysException(String errId, Exception oriEx) {
		super(oriEx);
		
		this.errId = errId;
		this.errOri = oriEx;
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		PrintStream p = new PrintStream(os);
		this.errOri.printStackTrace(p);
		this.errMsgOri = os.toString().trim();
		this.writeSysException();
	}

	/**
	 * ��Ӹ�����Ϣ��ֻ�в�׽��SysException��ʱ��ŵ��øú���
	 * 
	 * @param msg
	 *            String ������Ϣ
	 */
	public void appendMsg(String msg) {
		trace += msg;
	}

	/**
	 * ���ظ�����Ϣ
	 * 
	 * @return String
	 */
	public String getTrace() {
		return trace;
	}

	public String getMessage() {
		String message = "";
		/*if(errMsgOri.length()>100){
			message = errMsg + "[" + errMsgOri.substring(0,100) + "]";
		}else{
			message = errMsg + "[" + errMsgOri + "]";
		}*/
		message = errMsg + "[" + errMsgOri + "]";
		return message;
	}

	/**
	 * ��ӡԭ��������Ϣ
	 */
	public void printDebug() {
		this.errOri.printStackTrace();
	}

	private void writeSysException() {
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		PrintStream p = new PrintStream(os);
		this.errOri.printStackTrace(p);

	}

}
