//package com.zen.bandit.cb;
//
//import org.apache.commons.math3.linear.Array2DRowRealMatrix;
//import org.apache.commons.math3.linear.MatrixUtils;
//import org.apache.commons.math3.linear.RealMatrix;
//import org.apache.commons.math3.linear.RealVector;
//
//import java.util.Set;
//
///**
// * @Author: xiongjun
// * @Date: 2020/7/28 10:27
// * @description
// * @reviewer
// */
//public class HybridLinUCB extends LinUCB {
//    /** 共享模型的参数 */
//    private RealMatrix beta_hat;
//
//
//    private RealMatrix[] B_a;
//    private RealMatrix A_0;
//
//
//    private RealMatrix b_0;
//
//    /** 共享特征的数量 */
//    private int k;
//
//    /**
//     * @param d     非共享特征的数量
//     * @param k     共享特征的数量
//     * @param adMaterialIds     广告列表
//     * @param alpha 1+sqrt(ln(2/p)/2)
//     */
//    public HybridLinUCB(int d, int k, Set<String> adMaterialIds, double alpha) {
//        super(d, adMaterialIds, alpha);
//
//        if(k <= 0) {
//            throw new IllegalArgumentException("Number of features > 0. If there is are no shared features use @LinUCB");
//        }
//        this.k = k;
//
//        A_0 = MatrixUtils.createRealIdentityMatrix(k);
//        b_0 = new Array2DRowRealMatrix(k, 1);
//
//        beta_hat = MatrixUtils.inverse(A_0).multiply(b_0);
//
//        B_a = new RealMatrix[n];
//        for (int i = 0; i < n; i++) {
//            B_a[i] = new Array2DRowRealMatrix(d, k);
//        }
//    }
//
//    /**
//     * Receive a reward for the given context and arm. Update the regression parameters accordingly.
//     */
//    public double[] getPayoffs(RealVector sharedContext, RealVector combinedContext){
//        return getPayoffs(sharedContext.append(combinedContext));
//    }
//
//    /**
//     * Receive a reward for the given context and arm. Update the regression parameters accordingly.
//     * The given context must be of form [sharedContext,nonSharedContext].
//     */
//    @Override
//    public double[] getPayoffs(RealVector combinedContext) {
//        if(combinedContext.getDimension() != k + d) {
//            throw new IllegalArgumentException("The given context must be of form [sharedContext,nonSharedContext]!");
//        }
//        RealVector sharedContext = combinedContext.getSubVector(0, k);
//        RealVector nonSharedContext = combinedContext.getSubVector(k, d);
//
//        double[] payoffs = new double[n];
//
//        RealMatrix x = new Array2DRowRealMatrix(nonSharedContext.toArray());
//        RealMatrix x_t = x.transpose();
//
//        RealMatrix z = new Array2DRowRealMatrix(sharedContext.toArray());
//        RealMatrix z_t = z.transpose();
//
//        RealMatrix A_0_inv = MatrixUtils.inverse(A_0);
//
//        for(int i = 0; i < n; i++){
//            RealMatrix first = z_t.multiply(A_0_inv).multiply(z);
//            RealMatrix second = z_t.multiply(A_a_inverse[i]).multiply(B_a[i].transpose()).multiply(A_a_inverse[i]).multiply(x).scalarMultiply(2);
//            RealMatrix third = x_t.multiply(A_a_inverse[i]).multiply(x);
//            RealMatrix fourth = x_t.multiply(A_a_inverse[i]).multiply(B_a[i]).multiply(A_0_inv).multiply(B_a[i].transpose()).multiply(A_a_inverse[i]).multiply(x);
//
//            RealMatrix s = first.subtract(second).add(third).add(fourth);
//
//            double firstElement = z_t.multiply(beta_hat).getEntry(0,0);
//            double secondElement = x_t.multiply(theta_hat_a[i]).getEntry(0,0);
//
//            if(firstElement != 0) {
//                payoffs[i] = firstElement + secondElement + (alpha * Math.sqrt(Math.abs(s.getEntry(0, 0))));
//            }else {
//                payoffs[i] = firstElement + secondElement;
//            }
//        }
//
//        return payoffs;
//    }
//
//    /**
//     * Receive multiple rewards for the given contexts and arms. Update the regression parameters accordingly.
//     * The given contexts must be of form [sharedContext,nonSharedContext].
//     */
//    @Override
//    public void receiveRewards(RealVector[] combinedContexts, int[] arm, double[] reward) {
//        for (int i = 0; i < combinedContexts.length; i++) {
//            RealVector combinedContext = combinedContexts[i];
//            if(combinedContext.getDimension() != k + d) {
//                throw new IllegalArgumentException("The given context must be of form [sharedContext,nonSharedContext]!");
//            }
//            RealVector sharedContext = combinedContext.getSubVector(0, k);
//            RealVector nonSharedContext = combinedContext.getSubVector(k, d);
//
//            RealMatrix sharedContextMatrix = new Array2DRowRealMatrix(sharedContext.toArray());
//            RealMatrix sharedContextMatrixTranspose = sharedContextMatrix.transpose();
//            RealMatrix nonSharedContextMatrix = new Array2DRowRealMatrix(nonSharedContext.toArray());
//            RealMatrix nonSharedContextMatrixTranspose = nonSharedContextMatrix.transpose();
//
//            RealMatrix zMultz_t = sharedContextMatrix.multiply(sharedContextMatrixTranspose);
//            RealMatrix xMultx_t = nonSharedContextMatrix.multiply(nonSharedContextMatrixTranspose);
//            RealMatrix xMultz_t = nonSharedContextMatrix.multiply(sharedContextMatrixTranspose);
//
//            A_0 = A_0.add(B_a[arm[i]].transpose().multiply(A_a_inverse[arm[i]].transpose()).multiply(B_a[arm[i]]));
//
//            b_0 = b_0.add(B_a[arm[i]].transpose().multiply(A_a_inverse[arm[i]].transpose()).multiply(b_a[arm[i]].transpose()));
//
//            A_a[arm[i]] = A_a[arm[i]].add(xMultx_t); // update A[arm] by adding x_t[arm]*x_t[arm]^transposed to it
//            B_a[arm[i]] = B_a[arm[i]].add(xMultz_t);
//
//            double[] rMultx = nonSharedContext.mapMultiply(reward[i]).toArray();
//            double[] rMultz = sharedContext.mapMultiply(reward[i]).toArray();
//            b_a[arm[i]] = b_a[arm[i]].add(new Array2DRowRealMatrix(rMultx).transpose()); // update b[arm] by adding r_t * x_t[arm] to it
//
//            A_0 = A_0.add(zMultz_t).subtract(B_a[arm[i]].transpose().multiply(A_a_inverse[arm[i]].multiply(B_a[arm[i]])));
//            b_0 = b_0.add(new Array2DRowRealMatrix(rMultz)).subtract(B_a[arm[i]].transpose().multiply(A_a_inverse[arm[i]].multiply(b_a[arm[i]].transpose())));
//
//            for (int j = 0; j < A_a.length; j++) {
//                A_a_inverse[j] = MatrixUtils.inverse(A_a[j]);
//                theta_hat_a[j] = A_a_inverse[j].multiply(b_a[j].transpose());
//            }
//
//            beta_hat = MatrixUtils.inverse(A_0).multiply(b_0);
//        }
//    }
//}
