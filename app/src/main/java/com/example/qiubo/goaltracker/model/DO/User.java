package com.example.qiubo.goaltracker.model.DO;



import java.io.Serializable;



public class User implements Serializable {

    private Long id;


    private Long roleId;

    private String userName;

    private String pasword;

    private String createTime;


    private String destoryTime;


    private String nickName;

    private static final long serialVersionUID = 1L;

    /**
     * @return id
     */
    public Long getId() {
        return id;
    }

    /**
     * @param id
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * @return role_id
     */
    public Long getRoleId() {
        return roleId;
    }

    /**
     * @param roleId
     */
    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }


    /**
     * @return user_name
     */
    public String getUserName() {
        return userName;
    }

    /**
     * @param userName
     */
    public void setUserName(String userName) {
        this.userName = userName == null ? null : userName.trim();
    }
    /**
     * @return pasword
     */
    public String getPasword() {
        return pasword;
    }

    /**
     * @param pasword
     */
    public void setPasword(String pasword) {
        this.pasword = pasword == null ? null : pasword.trim();
    }

    /**
     * @return create_time
     */
    public String getCreateTime() {
        return createTime;
    }

    /**
     * @param createTime
     */
    public void setCreateTime(String createTime) {
        this.createTime = createTime == null ? null : createTime.trim();
    }

    /**
     * @return destory_time
     */
    public String getDestoryTime() {
        return destoryTime;
    }

    /**
     * @param destoryTime
     */
    public void setDestoryTime(String destoryTime) {
        this.destoryTime = destoryTime == null ? null : destoryTime.trim();
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", roleId=").append(roleId);
        sb.append(", name=").append(userName);
        sb.append(", pasword=").append(pasword);
        sb.append(", createTime=").append(createTime);
        sb.append(", destoryTime=").append(destoryTime);
        sb.append(", nickName=").append(nickName);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}