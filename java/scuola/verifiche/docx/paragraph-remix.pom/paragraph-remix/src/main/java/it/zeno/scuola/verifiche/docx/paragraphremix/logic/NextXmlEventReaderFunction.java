package it.zeno.scuola.verifiche.docx.paragraphremix.logic;

import javax.xml.stream.events.XMLEvent;

@FunctionalInterface
public interface NextXmlEventReaderFunction {
    XMLEvent next() throws Exception;
}
