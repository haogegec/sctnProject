package com.sctn.sctnet.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * 
 * @author wei
 *
 */
public class ResumeInfo implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -1382916604011056137L;

	public ResumeInfo(){
		
	}

	private long userid; //用户ID
	
	private String birthday;//出生日期
	
	private String graduateddate;//毕业日期这里用来做参加工作日期的时间
	
	private String address;//地址这里作为所在城市的字段来取
	
	private String marriagestate;//婚姻状况
	
	private String cardid;//身份证号
	
	private String drivecode;//驾驶证号
	
	private String degreecert;//学位证号
	
	private String graduatedcode;//毕业证号
	
	private String accountcity;//户口所在地
	
	private String type;//证件类型
	
	private long companyid;//企业编号
	
	private long jobsid;//职位编号
	
	private String companyname;//企业名称
	
	private String jobsname;//职位名称

	private  String reccontent;   //？？          
	private  String userhead;   // 用户头像地址？           
	private  String truename;    //真实姓名           
	private  String sex;                 //性别   
                 
	private  String birthplace;  //出生地址           
              
	private  String people; //民族                
	private  String political;   //政治面貌           
	private  long useheight; //身高              
          
	private  String healthstate;   //健康状况         
           
	private  String currentcity; //当前生活城市           
             
	private  String specialtycontent;  //？？？？     
	private  String resume;   //？？？              
	private  String currentprofessional;   //当前职业？ 
	private  long workexperience;    //工作经验？？为什么要用long型的 什么意思      
	private  String adminpost;   //？？           
	private  String workperformance; //工作表现       
	private  String education;  //教育？？？            
	private  String degree;  //学位？？？               
         
	private  String graduatedschool;   //毕业学校     
	private  String profession;   //专业          
	private  String aidprofession;  //？？        
	private  String technology;  //？？           
	private  String oneenglish; //第一外语            
	private  String twoenglish;         //第二外语    
	private  String onelevel;  //第一外语的水平             
	private  String twolevel; //第二外语的水平              
	private  String computerlevel;  //电脑水平        
                
	private  String usephone;  //电话号码             
	private  String contactsname;  //第二联系人姓名         
	private  String contactsphone; //第二联系人号码         
	private  String qqmsn;                  
	private  String email;                  
	private  String postalcode; //？？            
	private  long isresumehide;  //是否隐藏简历          
	private  String adapplicationttme; //？？      
	private  String adviewplaytime;  //？？        
	private  String adviewendtime;   //？？        
	private  String adAuditingtime;  //？？        
	private  long adapplicationtstate; //？？？    
	private  String endtime;   //？？              
	private  String upresumetime; //？？？           
	private  int ishide; //？？？                  
	private  int isjobs; //？？                  
	private  long clicknuum;//被查看的次数

	public long getUserid() {
		return userid;
	}
	public void setUserid(long userid) {
		this.userid = userid;
	}
	public String getBirthday() {
		return birthday;
	}
	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}
	public String getGraduateddate() {
		return graduateddate;
	}
	public void setGraduateddate(String graduateddate) {
		this.graduateddate = graduateddate;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getMarriagestate() {
		return marriagestate;
	}
	public void setMarriagestate(String marriagestate) {
		this.marriagestate = marriagestate;
	}
	public String getCardid() {
		return cardid;
	}
	public void setCardid(String cardid) {
		this.cardid = cardid;
	}
	public String getDrivecode() {
		return drivecode;
	}
	public void setDrivecode(String drivecode) {
		this.drivecode = drivecode;
	}
	public String getDegreecert() {
		return degreecert;
	}
	public void setDegreecert(String degreecert) {
		this.degreecert = degreecert;
	}
	public String getGraduatedcode() {
		return graduatedcode;
	}
	public void setGraduatedcode(String graduatedcode) {
		this.graduatedcode = graduatedcode;
	}
	public String getAccountcity() {
		return accountcity;
	}
	public void setAccountcity(String accountcity) {
		this.accountcity = accountcity;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public long getCompanyid() {
		return companyid;
	}
	public void setCompanyid(long companyid) {
		this.companyid = companyid;
	}
	public long getJobsid() {
		return jobsid;
	}
	public void setJobsid(long jobsid) {
		this.jobsid = jobsid;
	}
	public String getCompanyname() {
		return companyname;
	}
	public void setCompanyname(String companyname) {
		this.companyname = companyname;
	}
	public String getJobsname() {
		return jobsname;
	}
	public void setJobsname(String jobsname) {
		this.jobsname = jobsname;
	}
	public String getReccontent() {
		return reccontent;
	}
	public void setReccontent(String reccontent) {
		this.reccontent = reccontent;
	}
	public String getUserhead() {
		return userhead;
	}
	public void setUserhead(String userhead) {
		this.userhead = userhead;
	}
	public String getTruename() {
		return truename;
	}
	public void setTruename(String truename) {
		this.truename = truename;
	}
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	public String getBirthplace() {
		return birthplace;
	}
	public void setBirthplace(String birthplace) {
		this.birthplace = birthplace;
	}
	public String getPeople() {
		return people;
	}
	public void setPeople(String people) {
		this.people = people;
	}
	public String getPolitical() {
		return political;
	}
	public void setPolitical(String political) {
		this.political = political;
	}
	public long getUseheight() {
		return useheight;
	}
	public void setUseheight(long useheight) {
		this.useheight = useheight;
	}
	public String getHealthstate() {
		return healthstate;
	}
	public void setHealthstate(String healthstate) {
		this.healthstate = healthstate;
	}
	public String getCurrentcity() {
		return currentcity;
	}
	public void setCurrentcity(String currentcity) {
		this.currentcity = currentcity;
	}
	public String getSpecialtycontent() {
		return specialtycontent;
	}
	public void setSpecialtycontent(String specialtycontent) {
		this.specialtycontent = specialtycontent;
	}
	public String getResume() {
		return resume;
	}
	public void setResume(String resume) {
		this.resume = resume;
	}
	public String getCurrentprofessional() {
		return currentprofessional;
	}
	public void setCurrentprofessional(String currentprofessional) {
		this.currentprofessional = currentprofessional;
	}
	public long getWorkexperience() {
		return workexperience;
	}
	public void setWorkexperience(long workexperience) {
		this.workexperience = workexperience;
	}
	public String getAdminpost() {
		return adminpost;
	}
	public void setAdminpost(String adminpost) {
		this.adminpost = adminpost;
	}
	public String getWorkperformance() {
		return workperformance;
	}
	public void setWorkperformance(String workperformance) {
		this.workperformance = workperformance;
	}
	public String getEducation() {
		return education;
	}
	public void setEducation(String education) {
		this.education = education;
	}
	public String getDegree() {
		return degree;
	}
	public void setDegree(String degree) {
		this.degree = degree;
	}
	public String getGraduatedschool() {
		return graduatedschool;
	}
	public void setGraduatedschool(String graduatedschool) {
		this.graduatedschool = graduatedschool;
	}
	public String getProfession() {
		return profession;
	}
	public void setProfession(String profession) {
		this.profession = profession;
	}
	public String getAidprofession() {
		return aidprofession;
	}
	public void setAidprofession(String aidprofession) {
		this.aidprofession = aidprofession;
	}
	public String getTechnology() {
		return technology;
	}
	public void setTechnology(String technology) {
		this.technology = technology;
	}
	public String getOneenglish() {
		return oneenglish;
	}
	public void setOneenglish(String oneenglish) {
		this.oneenglish = oneenglish;
	}
	public String getTwoenglish() {
		return twoenglish;
	}
	public void setTwoenglish(String twoenglish) {
		this.twoenglish = twoenglish;
	}
	public String getOnelevel() {
		return onelevel;
	}
	public void setOnelevel(String onelevel) {
		this.onelevel = onelevel;
	}
	public String getTwolevel() {
		return twolevel;
	}
	public void setTwolevel(String twolevel) {
		this.twolevel = twolevel;
	}
	public String getComputerlevel() {
		return computerlevel;
	}
	public void setComputerlevel(String computerlevel) {
		this.computerlevel = computerlevel;
	}
	public String getUsephone() {
		return usephone;
	}
	public void setUsephone(String usephone) {
		this.usephone = usephone;
	}
	public String getContactsname() {
		return contactsname;
	}
	public void setContactsname(String contactsname) {
		this.contactsname = contactsname;
	}
	public String getContactsphone() {
		return contactsphone;
	}
	public void setContactsphone(String contactsphone) {
		this.contactsphone = contactsphone;
	}
	public String getQqmsn() {
		return qqmsn;
	}
	public void setQqmsn(String qqmsn) {
		this.qqmsn = qqmsn;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPostalcode() {
		return postalcode;
	}
	public void setPostalcode(String postalcode) {
		this.postalcode = postalcode;
	}
	public long getIsresumehide() {
		return isresumehide;
	}
	public void setIsresumehide(long isresumehide) {
		this.isresumehide = isresumehide;
	}
	public String getAdapplicationttme() {
		return adapplicationttme;
	}
	public void setAdapplicationttme(String adapplicationttme) {
		this.adapplicationttme = adapplicationttme;
	}
	public String getAdviewplaytime() {
		return adviewplaytime;
	}
	public void setAdviewplaytime(String adviewplaytime) {
		this.adviewplaytime = adviewplaytime;
	}
	public String getAdviewendtime() {
		return adviewendtime;
	}
	public void setAdviewendtime(String adviewendtime) {
		this.adviewendtime = adviewendtime;
	}
	public String getAdAuditingtime() {
		return adAuditingtime;
	}
	public void setAdAuditingtime(String adAuditingtime) {
		this.adAuditingtime = adAuditingtime;
	}
	public long getAdapplicationtstate() {
		return adapplicationtstate;
	}
	public void setAdapplicationtstate(long adapplicationtstate) {
		this.adapplicationtstate = adapplicationtstate;
	}
	public String getEndtime() {
		return endtime;
	}
	public void setEndtime(String endtime) {
		this.endtime = endtime;
	}
	public String getUpresumetime() {
		return upresumetime;
	}
	public void setUpresumetime(String upresumetime) {
		this.upresumetime = upresumetime;
	}
	public int getIshide() {
		return ishide;
	}
	public void setIshide(int ishide) {
		this.ishide = ishide;
	}
	public int getIsjobs() {
		return isjobs;
	}
	public void setIsjobs(int isjobs) {
		this.isjobs = isjobs;
	}
	public long getClicknuum() {
		return clicknuum;
	}
	public void setClicknuum(long clicknuum) {
		this.clicknuum = clicknuum;
	}      

	
}
