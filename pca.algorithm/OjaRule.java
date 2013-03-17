package pca.algorithm;

import java.util.Random;

import pca.math.Matrix;

public class OjaRule {
  private  double eta;
	private  Matrix W;
    
	/*Here we product a random for Marix W using init()*/
	private Matrix init(Matrix W){    
		Random rd = new Random();     
		for(int j = 0;j < W.getColumnDimension(); ++j){
			W.set(0, j, rd.nextDouble());
		}
		return W;	
	}
	
	public Matrix getW() {
		return W;
	}

	/*The constuctor with N, the dimension of the face picture*/
	public OjaRule(int N) {
		// TODO Auto-generated constructor stub
		W = new Matrix(1,N);
		this.init(W);     //Init the W with random	
		eta = 0.01;
	}
	
	/*Update the W ( W = W + eta * y *(X - y * W); here y = W * X) */
	public Matrix update(Matrix X){
		Matrix Y = new Matrix(1,W.getRowDimension());
		
		double y = W.arrayTimes(X).trace();
		W = W.plus((X.minus((W.times(y))).times(eta*y)));
		return W;	
	}
}
