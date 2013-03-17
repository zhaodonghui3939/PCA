package pca.face;

import java.util.Random;

import pca.algorithm.OjaRule;
import pca.algorithm.SangerRule;
import pca.math.Matrix;

public class Test {
  
	public static void main(String[] args){
		int[][] a = {{0},{1},{2}};
	//OjaRule face = new OjaRule(3);
	//face.getW().print(3, 10);
		SangerRule face = new SangerRule(3,3);
	double[][] array = {{1,2,3},{4,5,6},{7,8,10}};
	Matrix A = new Matrix(array);
	Random rd = new Random();  
	for(int i = 0;i < 80; ++i){
		
	    Matrix temp = A.getMatrix(a[i % 3], 0,2);
	   // face.getW().print(3, 10);
	    face.update(temp);
	    //face.getW().print(3, 10);
	}
	face.getW().print(3, 10);
	}

}
