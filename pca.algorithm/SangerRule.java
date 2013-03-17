package pca.algorithm;

import java.util.Random;

import pca.math.Matrix;

public class SangerRule {
	private double eta;
	private Matrix W;
	
	/*Here we product a random for Marix W using init()*/
	private void init(Matrix W){    
		Random rd = new Random();  
		for(int i = 0; i < W.getRowDimension(); ++i)
		for(int j = 0;j < W.getColumnDimension(); ++j){
			W.set(i, j, rd.nextDouble());
		}	
	}
	
	public Matrix getW() {
		return W;
	}

	/*The constuctor with N, the dimension of the face picture 
	  And the the number of main componets is K.*/
	public SangerRule(int N, int K) {
		// TODO Auto-generated constructor stub
		W = new Matrix(K,N);
		this.init(W);     //Init the W with random	
		eta = 0.01;
	}
	
	/*Update the W ( W = W + eta * y *(X - y * W); here y = W * X) */
	public Matrix update(double[] X){
		Matrix Y = new Matrix(1,W.getRowDimension());
		
		//Compute the Y: Y(j) = sum(W(j,i) * X(i));
		for(int j = 0;j < W.getRowDimension();++j)
		{
			double s = 0;
			for(int i = 0; i < W.getColumnDimension(); ++i)
			{
				s = s + W.get(j, i) * X[i];
			}
			Y.set(0, j, s);
		}
		
		/*Update the W: W = W + W_deta;
		  Here W_deta(j,i) = eta * Y(j) * (X‘(i) - (W(j,i) * Y(j)));
		  And X‘(i) = X(i) - sum(W(k,i) * Y(k))(from 0 to j - 1 )*/
		Matrix W_deta = new Matrix(W.getRowDimension(),W.getColumnDimension());
		for(int j = 0; j < W.getRowDimension();++j)
			for(int i = 0; i < W.getColumnDimension();++i)
			{
				//Compute the sum(W(k,i) * Y(k))(from 0 to j-1)
				double s = 0;
				for(int k = 0; k < j; ++k )
				{
					s = s + W.get(k, i) * Y.get(0, k);
				}
				
				W_deta.set(j, i, eta * Y.get(0, j) * (X[i] - s - W.get(j, i) * Y.get(0, j)));	
			}
		

		W.plusEquals(W_deta);
		return W;
	
	}

}
