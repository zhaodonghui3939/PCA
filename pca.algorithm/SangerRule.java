package pca.algorithm;

import java.util.Random;

import pca.math.Matrix;

public class SangerRule {
  private double eta;
	private Matrix W;
	
	/*Here we product a random for Marix W using init()*/
	private Matrix init(Matrix W){    
		Random rd = new Random();  
		for(int i = 0; i < W.getRowDimension(); ++i)
		for(int j = 0;j < W.getColumnDimension(); ++j){
			W.set(i, j, rd.nextDouble());
		}
		return W;	
	}
	
	public Matrix getW() {
		return W;
	}

	/*The constuctor with N, the dimension of the face picture 
	 * And the the number of main componets is K.*/
	public SangerRule(int N, int K) {
		// TODO Auto-generated constructor stub
		W = new Matrix(N,K);
		this.init(W);     //Init the W with random	
		eta = 0.01;
	}
	
		//Update the W: W = W + W_deta;
		//Here W_deta(i,j) = eta * Y(j) * (X(i) - sum(W(i,k) * Y(k)));
	public Matrix update(Matrix X){
		Matrix Y = new Matrix(1,W.getRowDimension());
		
		//Compute the Y: Y(j) = sum(W(j,i) * X(i));
		for(int j = 0;j < W.getRowDimension();++j)
		{
			double s = 0;
			for(int i = 0; i < W.getColumnDimension(); ++i)
			{
				s = s + W.get(j, i) * X.get(0, i);
			}
			Y.set(0, j, s);
		}
		

		Matrix W_deta = new Matrix(W.getRowDimension(),W.getColumnDimension());
		for(int i = 0; i < W.getRowDimension();++i)
			for(int j = 0; j < W.getColumnDimension();++j)
			{
				double s = 0;
				for(int k = 0; k < W.getColumnDimension(); ++k )
				{
					s = s + W.get(i, k) * Y.get(0, k);
				}
				W_deta.set(i, j, eta * Y.get(0, j) * (X.get(0, i) - s));			
			}
		

		W.plusEquals(W_deta);
		W.print(W.getColumnDimension(), 10);
		return W;
	
	}

}
