package it.zeno.scuola.verifiche.word;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.nio.file.Path;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.CDI;
import javax.inject.Inject;
import javax.inject.Singleton;
import javax.xml.XMLConstants;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import it.zeno.blockchain.Block;
import it.zeno.blockchain.BlockChain;
import it.zeno.blockchain.BlockImpl;
import it.zeno.scuola.verifiche.word.model.QuestDocx;
import it.zeno.utils.base.Log;
import it.zeno.utils.file.FILE;

@BlockChain("prepare-xml-input")
public class PreparaXmlInput extends BlockImpl{
	
	@Inject
	private QuestDocx data;
	
	@Inject
	private XMLStreamReader xmlStreamReader;
	
	@Inject
	private XMLInputFactory xmlInputFactory;
	
	@Inject
	@BlockChain("OriginXmlInputReadEventLoop")
	private Block next;

	@Override
	public boolean conf() {
		
		String fileNameDocxOrigin = FILE.nameLessExt(data.getFileDocxOrigin());
		
		Path fileXmlInput = data.getDirElab()
		.resolve(fileNameDocxOrigin)
		.resolve("word/document.xml");
		
		data.setFileXMLInput(fileXmlInput);
		
		return true;
	}
	
	@Override
	public void input() {
		try {
			data.setOriginXmlEventReader(
				xmlInputFactory.createXMLEventReader(
					new FileInputStream(data.getFileXMLInput().toFile())
				)
			);
		} catch (FileNotFoundException | XMLStreamException e) {
			throw Log.error(e);
		}
	}
	
	@Override
	public void close() throws Exception {
		data.getOriginXmlEventReader().close();	
	}

}
