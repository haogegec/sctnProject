package com.sctn.sctnet.Utils;



public class GenericException
    extends java.lang.Exception { 
	
	private static final long serialVersionUID = 1L;
	/**
	   * ���󼶱𣬷�ΪӦ�ô��󼶱��ϵͳ���󼶱�
	   */
	  public static int EXCEPTION_LEVEL_SYS = 1;
	  /**
	   * ���󼶱𣬷�ΪӦ�ô��󼶱��ϵͳ���󼶱�
	   */
	  public static int EXCEPTION_LEVEL_APP = 2;
	  
	  /**
	   * �����
	   */

	  String errId;

	  /**
	   * ������Ϣ����ǰ�������Ϣ��
	   */
	  String errMsg;

	  /**
	   * ԭ������Ϣ����׽��ԭ������ʱ�򣬼�¼��ԭ���������Ϣ��
	   */
	  String errMsgOri;
	  /**
	   * ԭ���������
	   */
	  Exception errOri;
	  public GenericException() {
	  }
	  public GenericException(Exception e) {
	    super(e);
	  }
	  /**
	   *
	   * ������Ϣ��¼��־��
	   * @param level int ���󼶱�Ӧ�ü���һ�㲻��¼��־������¼������Ϊerror����ϵͳ����Ҫ��¼��־������ΪFATAL
	   */
	  public void writeLog(int level) {
	    
	    //��¼���������־��Ϣ����������Ŷ�Ӧ�ļ���Ϣ��
	    if (level == EXCEPTION_LEVEL_SYS) {
	    }
	    else if (level == EXCEPTION_LEVEL_APP) {
	    }
	  }

	  public String getErrId() {
	    return errId;
	  }

	  public void setErrId(String errId) {
	    this.errId = errId;
	  }
	  public String getErrMsg() {
	    return errMsg;
	  }
	  
	  
	  public String getMessage() {
		  return errMsg;
	 }
	public String getErrMsgOri() {
	    return errMsgOri;
	  }

	  public Exception getErrOri() {
	    return errOri;
	  }

	  public void setErrMsg(String errMsg) {
	    this.errMsg = errMsg;
	  }
	  public void setErrMsgOri(String errMsgOri) {
	    this.errMsgOri = errMsgOri;
	  }

	  public void setErrOri(Exception errOri) {
	    this.errOri = errOri;
	  }


}
