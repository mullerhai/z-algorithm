package com.zen.bandit.cb;

import com.zen.bandit.event.CbEvent;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.*;
import java.util.concurrent.*;

/**
 * @Author: xiongjun
 * @Date: 2020/7/28 9:36
 * @description linucb
 * @reviewer
 */
public class LinUCB implements Serializable {
    private static final long serialVersionUID = -2997338994618574271L;
    private final Logger logger = LoggerFactory.getLogger(LinUCB.class);
    protected volatile List<RealMatrix> A_a;
    protected volatile List<RealMatrix> A_a_inverse;

    protected volatile List<RealMatrix> b_a;

    /** 参数 */
    protected volatile List<RealMatrix> theta_hat_a;

    /** 1+sqrt(ln(2/p)/2) */
    protected double alpha;
    protected volatile Map<String,Integer> ad2index;

    /** 特征数 */
    protected int d;

    /** 广告数 */
    protected volatile int n;

    protected transient DelayQueue<CbEvent> delayQueue;
    private transient ExecutorService updateThead;

    /**
     * @param d 特征数
     * @param adMaterialIds 广告列表
     * @param alpha 1+sqrt(ln(2/p)/2)
     */
    public LinUCB(int d, Set<String> adMaterialIds,double alpha){
        if(d<=0||adMaterialIds.size()<=0){
            throw new IllegalArgumentException("Number of features and of arms must be > 0");
        }
        logger.info("start initial paramters");
        ad2index = new HashMap<>();
        int i = 0;
        for(String adMaterialId:adMaterialIds){
            ad2index.put(adMaterialId,i++);
        }
        this.n = adMaterialIds.size();
        A_a = new ArrayList<>(n);
        A_a_inverse = new ArrayList<>(n);
        b_a = new ArrayList<>(n);
        theta_hat_a = new ArrayList<>(n);
        this.alpha = alpha;
        this.d = d;
        RealMatrix i_d = MatrixUtils.createRealIdentityMatrix(d); // identity matrix
        for (i = 0; i < adMaterialIds.size(); i++) {
            A_a.add(i_d.copy());

            // d维0向量
            b_a.add(new Array2DRowRealMatrix(1, d));

            A_a_inverse.add(MatrixUtils.inverse(A_a.get(i)));
            theta_hat_a.add(A_a_inverse.get(i).multiply(b_a.get(i).transpose()));
        }
        logger.info("initial paramters successful");

    }

    public void init() {
        logger.info("launch online training thread");
        delayQueue = new DelayQueue<>();
        updateThead = new ThreadPoolExecutor(1, 1,
                0L, TimeUnit.MILLISECONDS, new ArrayBlockingQueue<>(100000));
        updateThead.submit(new UpdateLinUCBParams(A_a,A_a_inverse,b_a,theta_hat_a,ad2index,delayQueue));
        logger.info("launch online training thread successful");
    }

    public void close(){
        logger.info("shutdown online training thread");
        updateThead.shutdownNow();
    }
    /**
     * Receive a reward for the given context and arm. Update the regression parameters accordingly.
     */
    public void receiveReward(RealVector context, int arm, double reward) {
        receiveRewards(new RealVector[]{context}, new int[]{arm}, new double[]{reward});
    }
    public void receiveReward(RealVector context,String adMaterialId,double reward){
        receiveRewards(new RealVector[]{context}, new int[]{ad2index.get(adMaterialId)}, new double[]{reward});
    }
    public void replaceReward(String requestId,String adId,String materialId,double reward){
        if (reward==0){
            logger.warn("receive reward is zero");
            return;
        }
        logger.info(String.format("receive requestId=%s adId=%s materialId=%s feedback reward is %s",
                requestId,adId,materialId,reward));
        String id = requestId+"_"+adId+"_"+materialId;
        for (CbEvent event : delayQueue) {
            if (event.getId().equals(id)) {
                event.setReward(reward);
                logger.info(String.format("replace requestId=%s adId=%s materialId=%s reward=%s successful"),
                        requestId,adId,materialId,reward);
                break;
            }
        }
    }
    /**
     * Receive multiple rewards for the given contexts and arms. Update the regression parameters accordingly.
     */
    public void receiveRewards(RealVector[] context, int[] arm, double[] reward) {
        if(context.length != arm.length || context.length != reward.length) {
            throw new IllegalArgumentException("Must give the same number of contexts, arms and rewards");
        }
        for (int i = 0; i < context.length; i++) {
            if(context[i].getDimension() != d) {
                throw new IllegalArgumentException("Context does not have the same dimension as given in the constructor (" + d + ")");
            }
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

    /**
     * @return the expected payoff for each arm for the given context
     */
    public double[] getPayoffs(RealVector context) {
        if(context.getDimension() != d) {
            throw new IllegalArgumentException("Context does not have the same dimension as given in the constructor (" + d + ")");
        }
        double[] payoffs = new double[n];

        RealMatrix x = new Array2DRowRealMatrix(context.toArray());
        RealMatrix x_t = x.transpose();

        for (int i = 0; i < n; i++) {
            RealMatrix secondProduct = x_t.multiply(A_a_inverse.get(i)).multiply(x);
            double secondElement = secondProduct.getEntry(0, 0);

            RealMatrix firstProduct = theta_hat_a.get(i).multiply(x_t);
            double firstElement = firstProduct.getEntry(0, 0);

            double secondElementSqTimesAlpha = alpha * Math.sqrt(Math.abs(secondElement));
            payoffs[i] = firstElement + secondElementSqTimesAlpha;
        }

        return payoffs;
    }
    public double[] computeReward(List<CbEvent> events){
        return getPayoffs(events);
    }
    public List<Map<String,Double>> predict(List<CbEvent> events){
        double[] payoffs = getPayoffs(events);
        double maxPayoffs = Double.MIN_VALUE;
        int size = events.size();
        boolean[] viableArms = new boolean[size];
        Arrays.fill(viableArms, false);
        int numberOfViableArms = 0;
        int lastViableArm = -1;
        for(int i=0;i<size;i++){
            if (payoffs[i]>maxPayoffs){
                Arrays.fill(viableArms,false);
                viableArms[i] = true;
                numberOfViableArms = 1;
                lastViableArm = i;
                maxPayoffs = payoffs[i];
            }else if(payoffs[i]==maxPayoffs){
                numberOfViableArms+=1;
                viableArms[i]=true;
                lastViableArm = i;
            }
        }
        List<Map<String,Double>> result = new ArrayList<>(size);
        if (numberOfViableArms == 0) {
            throw new RuntimeException("No viable arm!");
        } else if (numberOfViableArms == 1) {
            CbEvent event = events.get(lastViableArm);
            event.setTime(30000);
            delayQueue.offer(event);
            for (int i=0;i<size;i++){
                Map<String,Double> kv = new HashMap<>();
                kv.put("1",payoffs[i]);
                result.add(kv);
            }
        } else {
            // choose equally among viable arms
            int[] viableArmIndices = new int[numberOfViableArms];
            int counter = 0;
            for (int i = 0; i < viableArms.length; i++) {
                if (viableArms[i]) {
                    viableArmIndices[counter] = i;
                    counter++;
                }
            }

            Random random = new Random();
            int index = viableArmIndices[random.nextInt(viableArmIndices.length)];
            CbEvent event = events.get(index);
            event.setTime(30000);
            delayQueue.offer(event);
            for (int j=0;j<viableArmIndices.length;j++){
                if(viableArmIndices[j]!=index){
                    payoffs[viableArmIndices[j]]-=1e-7;
                }
            }
            for (int i = 0; i < size; i++) {
                Map<String,Double> kv = new HashMap<>();
                kv.put("1",payoffs[i]);
                result.add(kv);
            }
        }
        return result;
    }
    /**
     * 计算期望奖励
     * @param events
     * @return
     */
    public double[] getPayoffs(List<CbEvent> events){
        double[] payoffs = new double[events.size()];
        for (int i = 0; i < events.size(); i++) {
            CbEvent event = events.get(i);
            RealMatrix x = new Array2DRowRealMatrix(event.getContext().toArray());
            RealMatrix x_t = x.transpose();
            if(!ad2index.containsKey(event.getAdId()+"_"+event.getMaterialId())){
                elasticExpansion(event);
            }
            int index = ad2index.get(event.getAdId()+"_"+event.getMaterialId());
            RealMatrix firstProduct = theta_hat_a.get(index).multiply(x_t);
            double firstElement = firstProduct.getEntry(0,0);

            RealMatrix secondProduct = x_t.multiply(A_a_inverse.get(index)).multiply(x);
            double secondElement = secondProduct.getEntry(0, 0);
            double secondElementSqTimesAlpha = alpha * Math.sqrt(Math.abs(secondElement));
            payoffs[i] = firstElement + secondElementSqTimesAlpha;

        }
        return payoffs;
    }

    /**
     * 弹性扩容
     */
    private synchronized void elasticExpansion(CbEvent event){
        Set<String> adMaterialIdSet = ad2index.keySet();
        List<Integer> indexs = new ArrayList<>();
        for (String adMaterialId:adMaterialIdSet){
            String[] ad_id_material_id = adMaterialId.split("_");
            if (event.getAdId().equals(ad_id_material_id[0])){
                indexs.add(ad2index.get(adMaterialId));
            }
        }
        int cur = n;
        if(indexs.isEmpty()){
            A_a.add(MatrixUtils.createRealIdentityMatrix(d));
            b_a.add(new Array2DRowRealMatrix(1, d));
            A_a_inverse.add(MatrixUtils.inverse(A_a.get(cur)));
            theta_hat_a.add(A_a_inverse.get(cur).multiply(b_a.get(cur).transpose()));
        }else{
            RealMatrix cur_A_a = A_a.get(indexs.get(0));
            RealMatrix cur_b_a = b_a.get(indexs.get(0));
            RealMatrix cur_A_a_inverse = A_a_inverse.get(indexs.get(0));
            RealMatrix cur_theta_hat_a = theta_hat_a.get(indexs.get(0));
            for (int i = 1; i < indexs.size(); i++) {
                cur_A_a = cur_b_a.add(A_a.get(indexs.get(i)));
                cur_b_a = cur_b_a.add(b_a.get(indexs.get(i)));
                cur_A_a_inverse = cur_A_a_inverse.add(A_a_inverse.get(indexs.get(i)));
                cur_theta_hat_a = cur_theta_hat_a.add(theta_hat_a.get(indexs.get(i)));
            }
            double factor = 1.0/indexs.size();
            scalarDivide(cur_A_a.getRowDimension(),cur_A_a.getColumnDimension(),cur_A_a,factor);
            scalarDivide(cur_b_a.getRowDimension(),cur_b_a.getColumnDimension(),cur_b_a,factor);
            scalarDivide(cur_A_a_inverse.getRowDimension(),cur_A_a_inverse.getColumnDimension(),cur_A_a,factor);
            scalarDivide(cur_theta_hat_a.getRowDimension(),cur_theta_hat_a.getColumnDimension(),cur_A_a,factor);
            A_a.add(cur_A_a);
            b_a.add(cur_b_a);
            A_a_inverse.add(cur_A_a_inverse);
            theta_hat_a.add(cur_theta_hat_a);
            this.n++;
        }
        ad2index.put(event.getAdId()+"_"+event.getMaterialId(),cur);
    }

    public void scalarDivide(int rows,int columns,RealMatrix matrix,double factor){
        for (int i=0;i<rows;i++){
            for (int j=0;j<columns;j++){
                matrix.multiplyEntry(i,j,factor);
            }
        }
    }

    public double getAlpha() {
        return alpha;
    }

    public void setAlpha(double alpha) {
        this.alpha = alpha;
    }
}