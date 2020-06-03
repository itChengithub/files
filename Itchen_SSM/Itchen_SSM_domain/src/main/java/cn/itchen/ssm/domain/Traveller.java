package cn.itchen.ssm.domain;

import java.io.Serializable;

/**
 * 旅客实体类
 */
public class Traveller implements Serializable {
    private String id;//旅客id
    private String name;//旅客名称
    private String sex;//旅客性别
    private String phoneNum;//旅客手机号码
    private Integer credentialsType;//票据类型 0身份证号 1签证号
    private String credentialsTypeStr="未选择";// 0激活 1未激活
    private String credentialsNum;//票据状态
    private Integer travellerType=1;//旅客类型 0儿童1成人
    private String travellerTypeStr="成人";

    public String getCredentialsTypeStr() {
        if (credentialsType==0){
            credentialsTypeStr= "身份证";
        }else if(credentialsType==1){
            credentialsTypeStr=  "护照";
        }else if(credentialsType==2){
            credentialsTypeStr=  "军官证";
        }
        return credentialsTypeStr;
    }

    public void setCredentialsTypeStr(String credentialsTypeStr) {
        this.credentialsTypeStr = credentialsTypeStr;
    }

    public String getTravellerTypeStr() {
        if (travellerType==0){
            travellerTypeStr= "儿童";
        }
        return travellerTypeStr;
    }

    public void setTravellerTypeStr(String travellerTypeStr) {
        this.travellerTypeStr = travellerTypeStr;
    }


    @Override
    public String toString() {
        return "Traveller{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", sex='" + sex + '\'' +
                ", phoneNum='" + phoneNum + '\'' +
                ", credentialsType=" + credentialsType +
                ", credentialsNum='" + credentialsNum + '\'' +
                ", travellerType=" + travellerType +
                '}';
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public Integer getCredentialsType() {
        return credentialsType;
    }

    public void setCredentialsType(Integer credentialsType) {
        this.credentialsType = credentialsType;
    }

    public String getCredentialsNum() {
        return credentialsNum;
    }

    public void setCredentialsNum(String credentialsNum) {
        this.credentialsNum = credentialsNum;
    }

    public Integer getTravellerType() {
        return travellerType;
    }

    public void setTravellerType(Integer travellerType) {
        this.travellerType = travellerType;
    }
}
