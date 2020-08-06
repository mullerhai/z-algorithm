package com.zen.bandit.event;

import org.apache.commons.math3.linear.RealVector;


/**
 * @Author: xiongjun
 * @Date: 2020/7/28 11:17
 * @description
 * @reviewer
 */
public class CbEvent extends Event {
    private RealVector context;

    public CbEvent() {
        super();
    }

    public CbEvent(String requestId, String adId, String materialId, RealVector context) {
        super(requestId, adId, materialId);
        this.context = context;
    }

    public void setProperty(String requestId, String adId, String materialId) {
        setRequestId(requestId);
        setAdId(adId);
        setMaterialId(materialId);
    }

    public RealVector getContext() {
        return context;
    }

    public void setContext(RealVector context) {
        this.context = context;
    }

}
