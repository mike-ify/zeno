package it.zeno.scuola.verifiche.docx.paragraphremix.service;

import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import it.zeno.scuola.verifiche.docx.paragraphremix.logic.DocumentReaderRemixerWriter;
import it.zeno.utils.IO;

public class MischiaParagrafi {
    private static final Logger LOG = LoggerFactory.getLogger(MischiaParagrafi.class);
    private Instant start;

    public void run(Path basePath, String docxOriginFileExtName, String[]datiAlunni) throws Exception {
        
        inizioConteggioTempoEsecuzione();
        //TODO finire di usare metodi per ogni porzione di codice che fa determinate cose

        Path tempPath = IO.mkdir(basePath, "temp");
        Path outputPath = IO.mkdir(basePath, "temp");

        String docxOriginFileName = docxOriginFileExtName.substring(0, docxOriginFileExtName.indexOf('.'));

        Path docxOriginUnzippedDirPath = tempPath.resolve(docxOriginFileName);
        Path docxOriginFileExtPath = basePath.resolve(docxOriginFileExtName);

        IO.zipExplode(docxOriginFileExtPath, docxOriginUnzippedDirPath);

        String[] alunni = getAlunni(datiAlunni);

        int surveyNum = alunni.length, pipeIndex;

        String nome, cognome, dirName, fileName;

        Path docxCopyedPathExploded, docxCopyedPath;
        String documentXmlOriginPath = docxOriginUnzippedDirPath.resolve("word/document.xml").toString();

        DocumentReaderRemixerWriter business = new DocumentReaderRemixerWriter();

        for (int i = 0; i < surveyNum; i++) {

            if (alunni[i] == null)
                alunni[i] = ((i < 9 ? "0" : "") + (i + 1)) + "#surveyMix";

            pipeIndex = alunni[i].indexOf('#');

            cognome = alunni[i].substring(0, pipeIndex);
            nome = alunni[i].substring(pipeIndex + 1);

            dirName = docxOriginFileName + "-" + cognome + "#" + nome + "-student";

            fileName = dirName + ".docx";

            docxCopyedPathExploded = tempPath.resolve(dirName + Math.random());
            docxCopyedPath = tempPath.resolve(fileName);

            Files.copy(docxOriginFileExtPath, Files.newOutputStream(docxCopyedPath));

            IO.zipExplode(docxCopyedPath, docxCopyedPathExploded);

            business
                .createOriginXmlReader(documentXmlOriginPath)
                .setDestXmlWriter(docxCopyedPathExploded)
                .logic();

            docxCopyedPath.toFile().delete();

            IO.zipImplode(docxCopyedPathExploded, docxCopyedPath);

            Files.move(docxCopyedPath, outputPath.resolve(fileName));
        }

        IO.dirRemove(tempPath);

        fineConteggioTempoEsecuzione();
    }

    private void fineConteggioTempoEsecuzione() {
        LOG.info("Eseguito in ", ChronoUnit.MILLIS.between(start, Instant.now()), " millisecondi");
    }

    private void inizioConteggioTempoEsecuzione() {
        start = Instant.now();
    }

    private String[] getAlunni(String[] datiAlunni) {
        if (datiAlunni.length == 1)
            return new String[Integer.parseInt(datiAlunni[0])];
        else
            return datiAlunni;
    }
}
