package com.example.qiubo.goaltracker.model.DO;



import java.io.Serializable;



public class User implements Serializable {

    private Long id;


    private Long roleId;

    private String name;

    private String pasword;

    private String createTime;


    private String destoryTime;


    private byte[] nickName;

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
     * @return name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name
     */
    public void setName(String name) {
        this.name = name == null ? null : name.trim();
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

    /**
     * @return nick_name
     */
    public byte[] getNickName() {
        return nickName;
    }

    /**
     * @param nickName
     */
    public void setNickName(byte[] nickName) {
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
        sb.append(", name=").append(name);
        sb.append(", pasword=").append(pasword);
        sb.append(", createTime=").append(createTime);
        sb.append(", destoryTime=").append(destoryTime);
        sb.append(", nickName=").append(nickName);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}