package it.zeno.scuola.verifiche.word;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
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

@ApplicationScoped
@BlockChain("prepare-xml-input")
public class PreparaXmlInput extends BlockImpl{
	
	@Inject
	private QuestDocx data;
	
	@Inject
	@BlockChain("in progress...")
	private Block next;

	@Override
	public boolean conf() {
		data.setFileXMLInput(data.getDirElab()
		.resolve(FILE.nameLessExt(data.getFileDocxOrigin()))
		.resolve("word/document.xml"));
		
		return true;
	}
	
	@Produces
	public XMLStreamReader getXMLStreamReader() {
		XMLInputFactory xmlInputFactory = XMLInputFactory.newInstance();
		xmlInputFactory.setProperty(XMLConstants.ACCESS_EXTERNAL_DTD, "");
		xmlInputFactory.setProperty(XMLConstants.ACCESS_EXTERNAL_SCHEMA, "");
		try {
			return xmlInputFactory.createXMLStreamReader(new FileInputStream(data.getFileXMLInput().toFile()));
		} catch (FileNotFoundException | XMLStreamException e) {
			throw Log.error(e);
		}
	}
}
