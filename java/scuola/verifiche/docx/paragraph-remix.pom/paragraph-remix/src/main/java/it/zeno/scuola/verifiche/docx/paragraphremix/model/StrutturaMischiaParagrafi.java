package it.zeno.scuola.verifiche.docx.paragraphremix.model;

import java.io.IOException;
import java.lang.reflect.MalformedParametersException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import it.zeno.utils.functions.ConsumerThrow;
import it.zeno.utils.io.Dir;
import it.zeno.utils.io.FILE;
import it.zeno.utils.io.Zip;

public class StrutturaMischiaParagrafi{
	private List<Alunno> alunni;
	
	private String strPathBase;
	private String nomeDocxRead;

	private Path pathTemp;
	private Path pathOutput;
	private Path pathUnzippedDocxRead;
    private Path pathBase;
	private Path pathFileDocxRead;

	private String nomeUnzippedDocxWrite;

	private Path pathUnzippedDocxWrite;
	
	//from cmd, ws o jsp, creare interfaccia con questi metodi MischiaParagrafiSettings
	public StrutturaMischiaParagrafi setPathBase(String strPathBase) throws IOException {
		this.strPathBase = strPathBase;
		pathBase = Paths.get(strPathBase);
		
		preparaCartellePerElaborazioneFileDocx();
		return this;
	}
	
	public StrutturaMischiaParagrafi setNomeFileDocxInput(String nomeFileDocxIniziale) throws IOException {
		if (!nomeFileDocxIniziale.endsWith(".docx"))
			throw new MalformedParametersException(
				"Il formato del file word di partenza deve essere di tipo docx"
			);
		
		this.nomeDocxRead = nomeFileDocxIniziale;
		
		preparaCartellePerElaborazioneFileDocx();
	
		return this;
	}

	public StrutturaMischiaParagrafi setAlunni(String[]alunni) throws IOException {
		this.alunni = Arrays
		.stream(preparaDatiAlunni(alunni))
		.map(Alunno::new)
		.collect(Collectors.toList());
		
		preparaCartellePerElaborazioneFileDocx();
		
		return this;
	}
	
	//usati nell'iterazione del numero di alunni, creare interfaccia MischiaParagrafiEach
	public StrutturaMischiaParagrafi eachAlunni(ConsumerThrow<Alunno>alunnoConsumer) throws Exception {
		
		for(Alunno alunno : alunni)
			alunnoConsumer.accept(alunno);
		return this;
	}
	
	public StrutturaMischiaParagrafi copiaFileDocxInputPerElaborazione(Alunno alunno) throws IOException {
		nomeUnzippedDocxWrite = nomeDocxRead
		.substring(0,nomeDocxRead.lastIndexOf('.'))
		.concat("-")
		.concat(alunno.toString());

		String nomeFileDocxRisultato = nomeUnzippedDocxWrite
		.concat(".docx");
		
		Path pathFileDocxInizialeCopiatoPerElaborazione = pathTemp
		.resolve(nomeFileDocxRisultato);
		
		Files.copy(
			pathFileDocxRead, 
			Files.newOutputStream(
				pathFileDocxInizialeCopiatoPerElaborazione
			)
		);
		
		return this;
	}
	
	public void estraiFileDocxInElaborazione() throws IOException {
		Path pathFileDocxInizialeCopiatoPerElaborazione = pathTemp
		.resolve(nomeUnzippedDocxWrite + ".docx");

		Zip.extract(
			pathFileDocxInizialeCopiatoPerElaborazione, 
			pathTemp.resolve(nomeUnzippedDocxWrite)
		);
		
		String nameFileDocxInizialeCopiatoPerElaborazione = FILE
		.getNameWithOutExtension(pathFileDocxInizialeCopiatoPerElaborazione);
		
		pathUnzippedDocxWrite = pathTemp
		.resolve(nameFileDocxInizialeCopiatoPerElaborazione);
	}
	
	public Path getPathFileDocxXMLDaLeggere() {
		return pathUnzippedDocxRead
		.resolve("word/document.xml");
	}
	
	public Path getPathFileDocxXMLDaScrivere() {
		return pathUnzippedDocxWrite
		.resolve("word/document.xml");
	}
	
	public void rimuoviCartellaElaborazione() throws IOException {
		Dir.remove(pathTemp);
	}

	public void spostaXmlElaborato(Alunno alunno) throws IOException {
		Zip.create(
			pathUnzippedDocxWrite,
			pathOutput
			.resolve(pathUnzippedDocxWrite.getFileName().toString() + ".docx")
		);
	}
	//--
	
	private StrutturaMischiaParagrafi preparaCartellePerElaborazioneFileDocx() throws IOException {
    	if(alunni == null || nomeDocxRead == null || strPathBase == null)
    		return this;
		setPathFileDocxElaborazioneRisultati();
    	estraiFileDocxInizialeComeZip();
    	return this;
	}
	
	private String[] preparaDatiAlunni(String[] datiAlunni) {
        if (datiAlunni.length == 1) {
        	datiAlunni = new String[Integer.parseInt(datiAlunni[0])];
        	StringBuilder sb = new StringBuilder();
        	String s = "alunno#";
        	for(int i = 0, l = datiAlunni.length; i < l; i++, sb.setLength(0))
        		if(datiAlunni[i] == null)
        			datiAlunni[i] = sb.append(s).append(i + 1).toString();
        }
        return datiAlunni;
	}
	
	private void estraiFileDocxInizialeComeZip() {
    	Zip.extract(
			pathFileDocxRead, 
			pathUnzippedDocxRead
		);
	}

	private void setPathFileDocxElaborazioneRisultati() throws IOException {
		
		Dir.remove(pathBase.resolve("temp"));
		pathTemp = Dir.mk(
			pathBase, "temp"
		);
		
		Dir.remove(pathBase.resolve("out"));
		pathOutput = Dir.mk(
			pathBase, "out"
		);
		Dir.remove(pathTemp);
        
        String nomeDocxReadNoExt = nomeDocxRead
		.substring(0, nomeDocxRead.indexOf('.'));
        
        pathUnzippedDocxRead = pathTemp
        .resolve(nomeDocxReadNoExt);
        
        pathFileDocxRead = pathBase
        .resolve(nomeDocxRead);
	}

}
