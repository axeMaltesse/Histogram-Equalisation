import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import uk.ac.aber.beautify.gui.HistogramGUI;

/**
 * @author axeMaltesse - Lukasz Wrzolek
 *
 */

public class HistogramEqualization{
	
	
	public BufferedImage histogramEqualization(BufferedImage inputImage){
		
		
		//prepare the table to do equalization
		ArrayList<int[]> equalize = uniformHistogram(inputImage);
		
		BufferedImage outputImage = new BufferedImage(inputImage.getWidth(), inputImage.getHeight(), inputImage.getType());
		
		int red;
		int green;
		int blue;
		
		for(int u=0; u<inputImage.getWidth(); u++){
			for(int v=0; v<inputImage.getHeight(); v++){
				// Get pixels by R, G, B
                red = new Color(inputImage.getRGB (u, v)).getRed();
                green = new Color(inputImage.getRGB (u, v)).getGreen();
                blue = new Color(inputImage.getRGB (u, v)).getBlue();
                
                //set new pixel values using the histogramTriangle method, equalize the values
                red = equalize.get(0)[red];
                green = equalize.get(1)[green];
                blue = equalize.get(2)[blue];
                
                //set up the pixel value to current channel 
                int pixels = colorToRGB(red,green,blue);
                
                //Write pixels into image
                outputImage.setRGB(u, v, pixels);
                
			}
		}
/*		HistogramGUI hist = new HistogramGUI();
		hist.displayHistogram(outputImage);
		HistogramGUI hist1 = new HistogramGUI();
		hist1.displayCumulativeHistogram(outputImage);
		//hist1.displayCumulativeHistogram(histogramEQ); 
		return outputImage;
		*/
	}
	
	/**
	 * 
	 * @param inputImage
	 * @return ArrayList of Red, Green, Blue channel after shifting  the values to create linear function
	 */
	public ArrayList<int[]> uniformHistogram(BufferedImage inputImage){
		/**
		 * Computing the histogram. Simple implementation for separate channels, 
		 * most taken from the lecture slide 7, implementing histogram. 
		 */
		int[] Redhistogram = new int[256];
		int[] Greenhistogram = new int[256];
		int[] Bluehistogram = new int[256];
		/*
		 * filling up the histogram by 0 values
		 */
		for (int i=0; i<Redhistogram.length; i++)
			Redhistogram[i] = 0;
		for (int i=0; i<Greenhistogram.length; i++)
			Greenhistogram[i] = 0;
		for (int i=0; i<Bluehistogram.length; i++)
			Bluehistogram[i] = 0;
		
		/*
		 * Loop to get the pixel values
		 * pixel contains three intensity values, one of each for red,green and blue
		 */
		
		for(int u=0; u<inputImage.getWidth(); u++){
			for(int v=0; v<inputImage.getHeight(); v++){
				//Get the values for red, green, blue using color constructor.
				//http://docs.oracle.com/javase/7/docs/api/java/awt/Color.html
				int red = new Color(inputImage.getRGB(u, v)).getRed();//returns red value in range 0-255
				int green = new Color(inputImage.getRGB(u, v)).getGreen();//returns green value in range 0-255
				int blue = new Color(inputImage.getRGB(u, v)).getBlue();//returns blue value in range 0-255
				
				//red,green,blue provides the index to the histogram
				Redhistogram[red]++;
				Greenhistogram[green]++;
				Bluehistogram[blue]++;
			}
		}
		
		//Storing the histogram values in array list. 
		ArrayList<int[]> histogram = new ArrayList<int[]>();
		histogram.add(Redhistogram);
		histogram.add(Greenhistogram);
		histogram.add(Bluehistogram);
		
		
		//declare three values to store the color value
		double val_r = 0;
		double val_g = 0;
		double val_b = 0;
		
		/**
		 * Function to perform histogram equalization for an image with K
		 * intensity levels of size m*n 
		 */
		int K = 256;
		//równanie funkcji liniowej danego obrazu w histogramie
		double linear = (((double)K-1) / (inputImage.getWidth() * (inputImage.getHeight())));
		
		//filling up the histogram array with the values from our image
		for(int i=0; i<Redhistogram.length; i++){
			// int the loop we add the value of the current pixel to our red channel (0) value
			// [i] is a pointer to the value stored in histogram
			val_r += histogram.get(0)[i];
			//calculate the new value by multiple this value with the linear function.
			//so we are shifting the values 
			int valr = (int) (val_r*linear);
			//!pamietaj o tresholdzie!
			//If the treshold exceeds 255, we write 255,
			if(valr > 255) {
				Redhistogram[i] = 255;
			}//else write the normal pixel values
			else 
				Redhistogram[i]=valr;
			//doing the same for green channel (1)
			val_g += histogram.get(1)[i];
            int valg = (int) (val_g * linear);
            if(valg > 255) {
                Greenhistogram[i] = 255;
            }
            else Greenhistogram[i] = valg;
            //and blue channel (2)
            val_b += histogram.get(2)[i];
            int valb = (int) (val_b * linear);
            if(valb > 255) {
                Bluehistogram[i] = 255;
            }
            else Bluehistogram[i] = valb;
			}
		//Create an ArrayList to store the values after equalisation
		ArrayList<int[]> equalizedHist = new ArrayList<int[]>();
		//Updating array list of cumulative histogram
		equalizedHist.add(Redhistogram);
		equalizedHist.add(Greenhistogram);
		equalizedHist.add(Bluehistogram);
		
		return equalizedHist;
	}
		
	/**
	 * 
	 * @param red - red value
	 * @param green	- green value
	 * @param blue	- blue value
	 * @return - shifted pixels for each channel
	 */
	private int colorToRGB(int red, int green, int blue){
		
		int pixel = 0;
		pixel += red; pixel = pixel << 8;
		pixel += green; pixel = pixel << 8;
		pixel += blue;
		return pixel;
	}
	

}
