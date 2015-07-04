package net.semanticmetadata.lire.imageanalysis;

import net.semanticmetadata.lire.utils.ImageUtils;
import java.awt.image.BufferedImage;
import java.util.StringTokenizer;

/**
 * The BGDATA feature was created, implemented and provided by ericeiffel<br/>
 * <i>BGDATA: 感知哈希算法(Perceptual Hash Algorithm) 
 * @author: ericeiffel, ericeiffel@gmail.com
 */

public class BGDATA implements LireFeature {
    
	public static final int HASH_SIZE =8;
	private BGDATA tmpFeature;
	public int HammingCount;
    public int HammingCompare;
    public int tmp;
    protected double[] data = new double[HASH_SIZE*HASH_SIZE];
    

    public void extract(BufferedImage image) {
    	//"感知哈希算法"(Perceptual Hash Algorithm)    	
    	BufferedImage img = new BufferedImage(HASH_SIZE, HASH_SIZE, BufferedImage.TYPE_INT_RGB);
    	img = ImageUtils.scaleImage(image, HASH_SIZE, HASH_SIZE);
    	
    	int     ImageAverage = 0;
    	int     ImageGraySum = 0;
        int[][] ImageGridRed = new int[HASH_SIZE][HASH_SIZE];
        int[][] ImageGridGreen = new int[HASH_SIZE][HASH_SIZE];
        int[][] ImageGridBlue = new int[HASH_SIZE][HASH_SIZE];
        int[][] ImageGridGray = new int[HASH_SIZE][HASH_SIZE];
        
        for (int x = 0; x < HASH_SIZE; x++) {
            for (int y = 0; y < HASH_SIZE; y++) {
                int pixel = img.getRGB(x, y);
                ImageGridRed[x][y] = (pixel >> 16) & 0xff;
                ImageGridGreen[x][y] = (pixel >> 8) & 0xff;
                ImageGridBlue[x][y] = (pixel) & 0xff;
                int gray = (int) (0.114 * ImageGridBlue[x][y] + 0.587 * ImageGridGreen[x][y] + 0.299 * ImageGridRed[x][y]);
                ImageGridGray[x][y] = gray;
                ImageGraySum += gray;
            }
        }
        ImageAverage = (int) ((float)ImageGraySum/(HASH_SIZE*HASH_SIZE));
        
        for (int x = 0; x < HASH_SIZE; x++) {
            for (int y = 0; y < HASH_SIZE; y++) {            	
            	data[x*HASH_SIZE+y] = ImageGridGray[x][y]>=ImageAverage ? 1 : 0;            	 
            }
        }    	
    }

    public float getDistance(LireFeature vd) { 
    	// Check if instance of the right class ...
        if (!(vd instanceof BGDATA))
            throw new UnsupportedOperationException("Wrong descriptor.");

        // casting ...
        tmpFeature = (BGDATA) vd;

        // check if parameters are fitting ...
        if ((tmpFeature.data.length != data.length))
            throw new UnsupportedOperationException("Histogram lengths or color spaces do not match");

        //Caculate the Hamming Distance 
        //计算汉明码距
        HammingCount = 0;
        HammingCompare = 0;
        
        System.out.println("data.length = " + tmpFeature.data.length);
        
        for (int i = 0; i < tmpFeature.data.length; i++) {
        	HammingCompare = (tmpFeature.data[i] == data[i]) ? 0 : 1;
        	HammingCount += HammingCompare;
        }
        
        return (float) HammingCount;
    }


    public String getStringRepresentation() {
        StringBuilder sb = new StringBuilder(data.length * 2 + 25);
        sb.append("bgdata");
        sb.append(' ');
        sb.append(data.length);
        sb.append(' ');
        for (double aData : data) {
            sb.append((int) aData);
            sb.append(' ');
        }
        return sb.toString().trim();
    }

    public void setStringRepresentation(String s) { 
        StringTokenizer st = new StringTokenizer(s);
        if (!st.nextToken().equals("cedd"))
            throw new UnsupportedOperationException("This is not a CEDD descriptor.");
        data = new double[Integer.parseInt(st.nextToken())];
        for (int i = 0; i < data.length; i++) {
            if (!st.hasMoreTokens())
                throw new IndexOutOfBoundsException("Too few numbers in string representation.");
            data[i] = Integer.parseInt(st.nextToken());
        }

    }


    public byte[] getByteArrayRepresentation() {
        byte[] result = new byte[data.length/2];
        for (int i = 0; i < result.length; i++) {
            tmp = ((int) data[(i << 1)]) << 3;
            tmp = (tmp | (int) data[(i << 1) +1]);
            result[i] = (byte) tmp;
        }
        return result;
    }


    public void setByteArrayRepresentation(byte[] in) {
        for (int i = 0; i < in.length; i++) {
            tmp = in[i];
            data[(i << 1) +1] = tmp & 0x0007;
            data[i << 1] = (tmp >> 3);
        }
    }

    @Override
    public void setByteArrayRepresentation(byte[] in, int offset, int length) {
        for (int i = offset; i < length; i++) {
            tmp = in[i];
            data[(i << 1) +1] = tmp & 0x0007;
            data[i << 1] = (tmp >> 3);
        }
    }

    public double[] getDoubleHistogram() {
        return data;
    }
}
