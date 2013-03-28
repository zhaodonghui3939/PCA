package pca.face;

import static com.googlecode.javacv.cpp.opencv_core.IPL_DEPTH_8U;
import static com.googlecode.javacv.cpp.opencv_core.cvGet2D;
import static com.googlecode.javacv.cpp.opencv_imgproc.CV_BGR2GRAY;
import static com.googlecode.javacv.cpp.opencv_imgproc.CV_INTER_LINEAR;
import static com.googlecode.javacv.cpp.opencv_imgproc.cvCvtColor;
import static com.googlecode.javacv.cpp.opencv_imgproc.cvResize;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import com.googlecode.javacv.cpp.opencv_core.CvScalar;
import com.googlecode.javacv.cpp.opencv_core.IplImage;

import pca.algorithm.SangerRule;
import pca.math.Matrix;

public class Test implements ActionListener {
	private JButton face = new JButton("Open the Face files");
	private int Dem = 16;
	private SangerRule faceLearning = new SangerRule(Dem * Dem, 3);
	private Matrix W_before = new Matrix(faceLearning.getW().getArrayCopy());
	public Test() {
		JFrame jframe = new JFrame("PCA Test");
		Container dataupdate = jframe.getContentPane();

		JPanel mianDATA = new JPanel();
		GridLayout laymainDATA = new GridLayout(1, 1, 40, 5); // set the layout
																// (1 * 2)
		mianDATA.setLayout(laymainDATA);
		dataupdate.add(mianDATA);

		GridBagConstraints c;
		int gridx, gridy, gridwidth, gridheight, anchor, fill, ipadx, ipady;
		double weightx, weighty;
		Insets inset;
		GridBagLayout gridbag = new GridBagLayout();

		JPanel f01 = new JPanel();
		f01.setLayout(gridbag);
		JLabel globalImage = new JLabel();
		ImageIcon beijing = new ImageIcon("resource/demo01.jpg");
		beijing.setImage(beijing.getImage().getScaledInstance(220, 280,
				Image.SCALE_DEFAULT)); // Resize the image
		globalImage.setHorizontalAlignment(SwingConstants.CENTER);
		globalImage.setIcon(beijing);
		gridx = 0;
		gridy = 0;
		gridwidth = 1;
		gridheight = 1;
		weightx = 1;
		weighty = 1;
		anchor = GridBagConstraints.CENTER;
		fill = GridBagConstraints.HORIZONTAL;
		inset = new Insets(0, 0, 0, 0);
		ipadx = 0;
		ipady = 0;
		c = new GridBagConstraints(gridx, gridy, gridwidth, gridheight,
				weightx, weighty, anchor, fill, inset, ipadx, ipady);
		gridbag.setConstraints(globalImage, c);

		face.setHorizontalAlignment(SwingConstants.CENTER); // set The alignment
															// direction
		face.addActionListener(this); // add the listerner
		gridx = 0;
		gridy = 1;
		gridwidth = 1;
		gridheight = 2;
		weightx = 1;
		weighty = 1;
		anchor = GridBagConstraints.CENTER;
		fill = GridBagConstraints.HORIZONTAL;
		inset = new Insets(0, 40, 10, 40);
		ipadx = 0;
		ipady = 0;
		c = new GridBagConstraints(gridx, gridy, gridwidth, gridheight,
				weightx, weighty, anchor, fill, inset, ipadx, ipady);
		gridbag.setConstraints(face, c);

		f01.add(globalImage);
		f01.add(face);
		mianDATA.add(f01);
		Toolkit tool = Toolkit.getDefaultToolkit(); // get the default tool kit
		Dimension screenSize = tool.getScreenSize(); // Get the size of screen
		jframe.setSize(600, 400);
		jframe.setLocation((screenSize.width - jframe.getWidth()) / 2,
				(screenSize.height - jframe.getHeight()) / 2); // Set the
																// location
		jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		jframe.setResizable(false);
		jframe.setVisible(true);

	}

	public static void main(String[] args) {

		new Test();


	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if (e.getSource() == face) {
			try {
				openFiletoLearning();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}

	}

	public void openFiletoLearning() throws FileNotFoundException, IOException {
		JFileChooser fileChooser = new JFileChooser(".");
		fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		fileChooser.setDialogTitle("Choose the Files Direction");
		int ret = fileChooser.showOpenDialog(null);
		BufferedImage newImage = null;
		
		if (ret == JFileChooser.APPROVE_OPTION) {
			// 文件夹路径
			String path1 = fileChooser.getSelectedFile().getAbsolutePath();
			// System.out.println(path1);
			File folderPath = new File(fileChooser.getSelectedFile()
					.getAbsolutePath());
			String files[] = folderPath.list();
			
			int breakCount = 0;
			
			//Matrix W_before = new Matrix(W_beforeO);
			for (int i = 0; i < files.length; ++i) {
				breakCount++;
				String path = path1 + "\\" + files[i];
				File file = new File(path);
				path = file.getAbsolutePath().replace("\\", "\\\\");
				if (path.contains(".jpg") || path.contains(".bmp")
						|| path.contains(".png")) {
					newImage = ImageIO.read(new FileInputStream(path));
				}
				// System.out.println(path);
				if (newImage != null) {
					IplImage image = IplImage.createFrom(newImage);
					IplImage imageGrey = IplImage.create(image.width(),
							image.height(), IPL_DEPTH_8U, 1);
					cvCvtColor(image, imageGrey, CV_BGR2GRAY);
					IplImage imageTemplete = IplImage.create(Dem, Dem,
							IPL_DEPTH_8U, 1);
					cvResize(imageGrey, imageTemplete, CV_INTER_LINEAR);
					CvScalar s;
					double[] data = new double[Dem * Dem];
					for (int k = 0; k < Dem; ++k) {
						for (int l = 0; l < Dem; ++l) {
							s = cvGet2D(imageTemplete, l, k);
							data[Dem * k + l] = ((double) s.getVal(0)) / 256;
						}
					}
					
					learning(data);

				}

				if (breakCount % 1 == 0) {
					double error_after = W_before.minus(faceLearning.getW())
							.normF()/W_before.getRowDimension() ;
					//W_before.print(1, 3);
					faceLearning.getW().print(1, 3);
					W_before = new Matrix(faceLearning.getW().getArrayCopy());
					
					System.out.println(error_after);
					
					
					if (error_after < 0.001)
					{
						
						System.out.println("We are out the circulation!");
						
						break;
					}
						
				}
				newImage = null;
			}
		}
	}

	public void learning(double[] data) {
		System.out.println("We are learning");
		faceLearning.update(data);
		//faceLearning.getW().print(1, 3);
		System.out.print("\n");
	}
}
