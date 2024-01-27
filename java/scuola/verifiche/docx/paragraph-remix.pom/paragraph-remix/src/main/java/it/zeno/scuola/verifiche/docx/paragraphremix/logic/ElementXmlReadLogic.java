package it.zeno.scuola.verifiche.docx.paragraphremix.logic;

import it.zeno.scuola.verifiche.docx.paragraphremix.model.XMLElement;
import it.zeno.utils.functions.ConsumerThrow;

public abstract class ElementXmlReadLogic{
	
	protected ConsumerThrow<XMLElement>defaultConsumer;

	public void setDefaultConsumer(ConsumerThrow<XMLElement> defaultConsumer) {
		this.defaultConsumer = defaultConsumer;
	}
}
