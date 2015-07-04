package net.semanticmetadata.lire;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.store.FSDirectory;

class ImageSearch{
	
	
	public static void main(String[] args) throws IOException{
		
		String indexPath = "F:\\index";
		String imagePath = "F:\\testpic\\image3.jpg";		
		ImageSearch is = new ImageSearch();
		is.Search(indexPath, imagePath);	    	
	}
	
    public void Search(String indexPath,String imagePath ) throws IOException {
        IndexReader reader = DirectoryReader.open(FSDirectory.open(new File(indexPath)));
        int numDocs = reader.numDocs();
        System.out.println("numDocs = " + numDocs);
       ImageSearcher searcher = ImageSearcherFactory.createBGDATAImageSearcher(10);
        FileInputStream imageStream = new FileInputStream(imagePath);
        
        BufferedImage bimg = ImageIO.read(imageStream);
        ImageSearchHits hits = null;
        
        hits = searcher.search(bimg, reader);
        
        for (int i = 0; i < 10; i++) {
            System.out.println(hits.score(i) + ": " + hits.doc(i).getField(DocumentBuilder.FIELD_NAME_IDENTIFIER).stringValue());
        }

    }
}