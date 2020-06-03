package cn.itchen.ssm.domain;

public class ToSecurity {
    private String oneId;
    private String twoId;

    public ToSecurity(String oneId, String twoId) {
        this.oneId = oneId;
        this.twoId = twoId;
    }

    public String getOneId() {
        return oneId;
    }

    public void setOneId(String oneId) {
        this.oneId = oneId;
    }

    public String getTwoId() {
        return twoId;
    }

    public void setTwoId(String twoId) {
        this.twoId = twoId;
    }
}
