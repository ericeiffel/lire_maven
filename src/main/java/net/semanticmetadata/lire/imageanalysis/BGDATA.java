
package net.semanticmetadata.lire.imageanalysis;

import net.semanticmetadata.lire.imageanalysis.cedd.*;

import java.awt.image.BufferedImage;
import java.util.StringTokenizer;

public class BGDATA implements LireFeature {
    
	public double T0;
    public double T1;
    public double T2;
    public double T3;
    public boolean Compact = false;
    protected double[] data = new double[144];
    int tmp;
    
    private double Result, Temp1, Temp2, TempCount1, TempCount2, TempCount3;
    private CEDD tmpFeature;
    private double iTmp1, iTmp2;


    // Apply filter
    // signature changed by mlux
    public void extract(BufferedImage image) {
      
    }

    public float getDistance(LireFeature vd) { 
    	
        return (float) 0;

    }


    public String getStringRepresentation() { // added by mlux
        StringBuilder sb = new StringBuilder(data.length * 2 + 25);
        sb.append("cedd");
        sb.append(' ');
        sb.append(data.length);
        sb.append(' ');
        for (double aData : data) {
            sb.append((int) aData);
            sb.append(' ');
        }
        return sb.toString().trim();
    }

    public void setStringRepresentation(String s) { // added by mlux
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
