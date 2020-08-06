package com.zen.bandit.event;

import java.io.Serializable;
import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

/**
 * @Author: xiongjun
 * @Date: 2020/7/28 11:00
 * @description
 * @reviewer
 */
public class Event implements Delayed, Serializable {
    private String id;
    private String requestId;
    private String adId;
    private String materialId;
    private int linsterEvent;
    private double reward;
    private long time;

    public Event() {
    }

    public Event(String requestId, String adId, String materialId) {
        this.requestId = requestId;
        this.adId = adId;
        this.materialId = materialId;
        this.id = requestId + "_" + adId + "_" + materialId;
    }

    public Event(String requestId, String adId, String materialId, int linsterEvent, double reward) {
        this.id = requestId + "_" + adId + "_" + materialId;
        this.requestId = requestId;
        this.adId = adId;
        this.materialId = materialId;
        this.linsterEvent = linsterEvent;
        this.reward = reward;
    }

    public String getId() {
        if (id == null) {
            this.id = requestId + "_" + adId + "_" + materialId;
        }
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getLinsterEvent() {
        return linsterEvent;
    }

    public void setLinsterEvent(int linsterEvent) {
        this.linsterEvent = linsterEvent;
    }

    public double getReward() {
        return reward;
    }

    public void setReward(double reward) {
        this.reward = reward;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String getAdId() {
        return adId;
    }

    public void setAdId(String adId) {
        this.adId = adId;
    }

    public String getMaterialId() {
        return materialId;
    }

    public void setMaterialId(String materialId) {
        this.materialId = materialId;
    }

    @Override
    public long getDelay(TimeUnit unit) {
        return this.time - System.currentTimeMillis();
    }

    @Override
    public int compareTo(Delayed o) {
        Event event = (Event) o;
        long diff = this.time - event.time;
        if (diff <= 0) {// 改成>=会造成问题
            return -1;
        } else {
            return 1;
        }
    }

    public void setTime(long time) {
        this.time = System.currentTimeMillis() + (time > 0 ? TimeUnit.MILLISECONDS.toMillis(time) : 0);
    }

    public void setTime(long time, TimeUnit unit) {
        this.time = System.currentTimeMillis() + (time > 0 ? unit.toMillis(time) : 0);
    }

    @Override
    public String toString() {
        return "Event{" +
                "id='" + id + '\'' +
                ", requestId='" + requestId + '\'' +
                ", adId='" + adId + '\'' +
                ", materialId='" + materialId + '\'' +
                ", linsterEvent=" + linsterEvent +
                ", reward=" + reward +
                ", time=" + time +
                '}';
    }
}
