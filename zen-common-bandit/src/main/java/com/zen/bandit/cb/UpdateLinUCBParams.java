package com.zen.bandit.cb;

import com.zen.bandit.event.CbEvent;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.DelayQueue;

/**
 * @Author: xiongjun
 * @Date: 2020/7/28 17:19
 * @description
 * @reviewer
 */
public class UpdateLinUCBParams implements Runnable {
    private Logger logger = LoggerFactory.getLogger(UpdateLinUCBParams.class);
    protected volatile List<RealMatrix> A_a;
    protected volatile List<RealMatrix> A_a_inverse;

    /** 奖励 */
    protected volatile List<RealMatrix> b_a;

    /** 参数 */
    protected volatile List<RealMatrix> theta_hat_a;
    protected Map<String,Integer> ad2index;

    protected DelayQueue<CbEvent> delayQueue;
    public UpdateLinUCBParams(List<RealMatrix> A_a, List<RealMatrix> A_a_inverse, List<RealMatrix> b_a,List<RealMatrix> theta_hat_a,
                              Map<String,Integer> ad2index, DelayQueue<CbEvent> delayQueue){
        this.A_a = A_a;
        this.A_a_inverse = A_a_inverse;
        this.b_a = b_a;
        this.theta_hat_a = theta_hat_a;
        this.ad2index = ad2index;
        this.delayQueue = delayQueue;
    }
    @Override
    public void run() {
        while (true){
            while (!delayQueue.isEmpty()){
                try {
                    CbEvent event = delayQueue.take();
                    receiveRewards(Collections.singletonList(event));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void receiveRewards(RealVector[] context, int[] arm, double[] reward) {
        if(context.length != arm.length || context.length != reward.length) {
            throw new IllegalArgumentException("Must give the same number of contexts, arms and rewards");
        }
        for (int i = 0; i < context.length; i++) {
            RealMatrix contextMatrix = new Array2DRowRealMatrix(context[i].toArray());
            RealMatrix contextMatrixTranspose = contextMatrix.transpose();

            RealMatrix xMultx_t = contextMatrix.multiply(contextMatrixTranspose);

            double[] contextMultipliedWithReward = context[i].mapMultiply(reward[i]).toArray();

            int curArm = arm[i];
            A_a.set(curArm,A_a.get(curArm).add(xMultx_t)); // update A[curArm] by adding x_t[curArm]*x_t[curArm]^transposed to it
            b_a.set(curArm,b_a.get(curArm).add(new Array2DRowRealMatrix(contextMultipliedWithReward).transpose()));// update b[curArm] by adding r_t * x_t[curArm] to it
            A_a_inverse.set(curArm,MatrixUtils.inverse(A_a.get(curArm)));
            theta_hat_a.set(curArm,A_a_inverse.get(curArm).multiply(b_a.get(curArm).transpose()));
        }
    }
    public void receiveRewards(List<CbEvent> events){
        RealVector[] contexts = new RealVector[events.size()];
        int[] arms = new int[events.size()];
        double[] rewards = new double[events.size()];
        for (int i = 0; i < events.size(); i++) {
            CbEvent event = events.get(i);
            contexts[i] = event.getContext();
            arms[i] = ad2index.get(event.getAdId()+"_"+event.getMaterialId());
            rewards[i] = event.getReward();
        }
        receiveRewards(contexts,arms,rewards);
    }
}
