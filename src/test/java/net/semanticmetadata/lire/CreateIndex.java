package net.semanticmetadata.lire;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;

import net.semanticmetadata.lire.utils.FileUtils;
import net.semanticmetadata.lire.utils.LuceneUtils;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexWriter;

class CreateIndex{
	
	public static void main(String[] args) throws IOException{
		String FilePath = "F:\\testpic";
		String IndexPath = "F:\\index";
		CreateIndex ci = new CreateIndex();
		ci.CreateCEDDIndex(FilePath, IndexPath);
		
	}
	
    public void CreateCEDDIndex(String FilePath,String IndexPath) throws IOException {
    	
        ArrayList<String> images = FileUtils.getAllImages(new File(FilePath), true);
        //DocumentBuilder builder = DocumentBuilderFactory.getCEDDDocumentBuilder();
        DocumentBuilder builder = DocumentBuilderFactory.getBGDATADocumentBuilder();
        IndexWriter iw = LuceneUtils.createIndexWriter(IndexPath, true);
        for (String identifier : images) {
            Document doc = builder.createDocument(new FileInputStream(identifier), identifier);
            iw.addDocument(doc);
        }
        iw.close();
    }
	
}