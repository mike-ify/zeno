package it.zeno.scuola.verifiche.docx;

import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import it.zeno.scuola.verifiche.docx.paragraphremix.model.StrutturaMischiaParagrafi;
import it.zeno.scuola.verifiche.docx.paragraphremix.service.MischiaParagrafiService;

public class MischiaParagrafiCMD {
	private static final Logger LOG = LoggerFactory.getLogger(MischiaParagrafiCMD.class);
	
	public static void main(String[] args) throws Exception {

		try {
			if (args.length < 1) {
				LOG.error("Al primo parametro devi passare il path alla dir che contiene il file word di partenza, es.:\n"
						+ "c:\\compito-in-classe-3a");
				System.exit(-1);
			}

			if (args.length < 2) {
				LOG.error("Al secondo parametro devi passare il nome del file word di partenza comprensivo di estensione,  es.:\n"
						+ "c:\\compito-in-classe-3a nome-file-crocett.docx");
				System.exit(-2);
			}

			if (args.length < 3) {
				LOG.error("Al terzo parametro devi passare i nomi e cognomi degli alunni della classe nel formato nome1|cognome1,nome2|cognome2 ecc.."
						+ "es.: \n c:\\compito-in-classe-3a nome-file-crocett.docx "
						+ "mario.rossi gian_maria.bianchi john_luca.aiardo_esposito \n"
						+ "oppure il numero di alunni per cui creare il mix di domande e risposte\n"
						+ "es.: \\n c:\\\\compito-in-classe-3a nome-file-crocett.docx 3");
				System.exit(-3);
			}
			
			try(MischiaParagrafiService mischiaParagrafiService = new MischiaParagrafiService(
				args[0], args[1],
				Arrays.copyOfRange(args, 2, args.length)
			)){
				mischiaParagrafiService.run();
			}
			
		} catch (Exception e) {
			LOG.error("main",e);
			System.exit(-4);
		}
	}
}
