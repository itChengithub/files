package cn.itchen.ssm.domain;

import cn.itchen.ssm.utils.DateUtils;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 订单实体类
 */
public class Order implements Serializable {
    private String id;//订单id
    private String orderNum;//订单编号
    private Date orderTime;//订单下单时间
    private String orderTimeStr;
    private Integer peopleCount;//订单人数
    private String orderDesc;//订单详情
    private Integer payType;//支付类型：0支付宝 1 微信 2 其他
    private String payTypeStr="未支付";
    private Integer orderStatus=0;//订单状态： 0未激活 1激活
    private String orderStatusStr="未支付";
    private List<Traveller> travellers;
    private Product product;//订单产品
    private Member member;//客户


    public List<Traveller> getTravellers() {
        return travellers;
    }

    public void setTravellers(List<Traveller> travellers) {
        this.travellers = travellers;
    }

    public String getOrderTimeStr() {
        if(orderTime!=null){
            String orderDateStr = DateUtils.dateToString(orderTime, "yyyy-MM-dd HH:mm");
            return orderDateStr;
        }
        return orderTimeStr;
    }

    public void setOrderTimeStr(String orderTimeStr) {
        this.orderTimeStr = orderTimeStr;
    }

    public String getPayTypeStr() {
        if(payType==0){
            payTypeStr= "支付宝";
        }else if(payType==1){
            payTypeStr= "微信";
        }else if(payType==2){
            payTypeStr= "现金";
        }else if(payType==2){
            payTypeStr= "刷卡";
        }
        return payTypeStr;
    }

    public void setPayTypeStr(String payTypeStr) {
        this.payTypeStr = payTypeStr;
    }

    public String getOrderStatusStr() {
        if(orderStatus!=0){
            orderStatusStr= "已支付";
        }
        return orderStatusStr;
    }

    public void setOrderStatusStr(String orderStatusStr) {
        this.orderStatusStr = orderStatusStr;
    }

    @Override
    public String toString() {
        return "Order{" +
                "id='" + id + '\'' +
                ", orderNum='" + orderNum + '\'' +
                ", orderTim=" + orderTime +
                ", peopleCount=" + peopleCount +
                ", orderDesc='" + orderDesc + '\'' +
                ", payType=" + payType +
                ", orderStatus=" + orderStatus +
                ", product=" + product +
                ", member=" + member +
                '}';
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(String orderNum) {
        this.orderNum = orderNum;
    }

    public Date getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(Date orderTime) {
        this.orderTime = orderTime;
    }

    public Integer getPeopleCount() {
        return peopleCount;
    }

    public void setPeopleCount(Integer peopleCount) {
        this.peopleCount = peopleCount;
    }

    public String getOrderDesc() {
        return orderDesc;
    }

    public void setOrderDesc(String orderDesc) {
        this.orderDesc = orderDesc;
    }

    public Integer getPayType() {
        return payType;
    }

    public void setPayType(Integer payType) {
        this.payType = payType;
    }

    public Integer getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(Integer orderStatus) {
        this.orderStatus = orderStatus;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Member getMember() {
        return member;
    }

    public void setMember(Member member) {
        this.member = member;
    }
}
