package it.zeno.scuola.verifiche.docx.paragraphremix.logic;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

public class WriteXmlFileDocxInElaborazioneLogic implements AutoCloseable{
	
	private BufferedWriter documentXmlWriter;

	public void init(Path documentXmlPath) throws IOException {
		documentXmlWriter = Files.newBufferedWriter(documentXmlPath,StandardOpenOption.TRUNCATE_EXISTING);
	}
	
	public WriteXmlFileDocxInElaborazioneLogic writeXmlElement(String xml) throws IOException{
		documentXmlWriter.append(xml);
		return this;
	}

    public WriteXmlFileDocxInElaborazioneLogic writeXmlDocumentStart(String xml) throws IOException {
		return writeXmlElement("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>")
		.writeXmlElement(xml);
    }   
    
	public void close() throws IOException {
		documentXmlWriter.close();
	}

	public void flush() throws IOException {
		documentXmlWriter.flush();
	}
}
