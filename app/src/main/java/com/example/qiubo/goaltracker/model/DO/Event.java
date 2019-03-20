package com.example.qiubo.goaltracker.model.DO;



import org.litepal.crud.LitePalSupport;

import java.io.Serializable;



public class Event extends LitePalSupport {
    private Long id;

    private String event;


    private Long userId;


    private String createTime;

    private Boolean done;

    private String uuid;

    private String planStartTime;

    private String planEndTime;

    private String completeTime;

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
     * @return event
     */
    public String getEvent() {
        return event;
    }

    /**
     * @param event
     */
    public void setEvent(String event) {
        this.event = event == null ? null : event.trim();
    }

    /**
     * @return user_id
     */
    public Long getUserId() {
        return userId;
    }

    /**
     * @param userId
     */
    public void setUserId(Long userId) {
        this.userId = userId;
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
     * @return done
     */
    public Boolean getDone() {
        return done;
    }

    /**
     * @param done
     */
    public void setDone(Boolean done) {
        this.done = done;
    }

    /**
     * @return uuid
     */
    public String getUuid() {
        return uuid;
    }

    /**
     * @param uuid
     */
    public void setUuid(String uuid) {
        this.uuid = uuid == null ? null : uuid.trim();
    }

    /**
     * @return plan_start_time
     */
    public String getPlanStartTime() {
        return planStartTime;
    }

    /**
     * @param planStartTime
     */
    public void setPlanStartTime(String planStartTime) {
        this.planStartTime = planStartTime == null ? null : planStartTime.trim();
    }

    /**
     * @return plan_end_time
     */
    public String getPlanEndTime() {
        return planEndTime;
    }

    /**
     * @param planEndTime
     */
    public void setPlanEndTime(String planEndTime) {
        this.planEndTime = planEndTime == null ? null : planEndTime.trim();
    }

    /**
     * @return complete_time
     */
    public String getCompleteTime() {
        return completeTime;
    }

    /**
     * @param completeTime
     */
    public void setCompleteTime(String completeTime) {
        this.completeTime = completeTime == null ? null : completeTime.trim();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", event=").append(event);
        sb.append(", userId=").append(userId);
        sb.append(", createTime=").append(createTime);
        sb.append(", done=").append(done);
        sb.append(", uuid=").append(uuid);
        sb.append(", planStartTime=").append(planStartTime);
        sb.append(", planEndTime=").append(planEndTime);
        sb.append(", completeTime=").append(completeTime);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}