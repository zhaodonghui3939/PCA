package pca.face;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Random;
import pca.algorithm.SangerRule;
import pca.math.Matrix;

public class Test {
	
	public static void main(String[] args){
		
		//Open the data for learning
		File file = new File("test-1.txt");
		Matrix data = null;
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader(file) );
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		try {
			Matrix temp1 = null;
			data = temp1.read(reader);	
		} catch (IOException e) {
			e.printStackTrace();
		}

		
		SangerRule face = new SangerRule(2,2);  // here init the face with 2 demension and 2 main component 
		
		
		double[][] data1 = data.getArray();
	    Random rd = new Random();  
	    for(int i = 0;i < data.getRowDimension(); ++i){
	    	int temp = rd.nextInt(data.getRowDimension()- 1);
	        face.update(data1[temp]);
	}
	    
	face.getW().print(data.getColumnDimension(), 10);       //print the W coefficients
	
	Matrix data_new = data.times(face.getW().transpose());  //compute the main components
	data_new.print(2, 10);               //print the result
	
	}
	

}
