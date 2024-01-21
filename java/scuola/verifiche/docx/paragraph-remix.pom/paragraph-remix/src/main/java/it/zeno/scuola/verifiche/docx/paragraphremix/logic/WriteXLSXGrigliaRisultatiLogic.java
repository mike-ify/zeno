package it.zeno.scuola.verifiche.docx.paragraphremix.logic;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import it.zeno.scuola.verifiche.docx.paragraphremix.model.Domanda;
import it.zeno.scuola.verifiche.docx.paragraphremix.model.Paragrafo;
import it.zeno.scuola.verifiche.docx.paragraphremix.model.Questionario;
import it.zeno.scuola.verifiche.docx.paragraphremix.model.Risposta;

public class WriteXLSXGrigliaRisultatiLogic implements AutoCloseable{

	private Workbook workbook;
	private Sheet spreadsheet;
	private CellStyle style;
	private Font font;
	
	private List<Paragrafo> paragrafi;
	private Questionario questionario;
	
	private Path path;
	private int rowid;
	private Row row;
	private Cell cell;
	private int cellid;
	private Row rownu;

	public void setQuestionario(Questionario questionario) {
		this.questionario = questionario;
	}
	
	public void setParagrafi(List<Paragrafo> paragrafi) {
		this.paragrafi = paragrafi;
	}

	public WriteXLSXGrigliaRisultatiLogic(Path path) throws EncryptedDocumentException, IOException {
		this.path = path;
		if(Files.exists(path)) {
			workbook = WorkbookFactory.create(path.toFile());
		}else {
			workbook = new XSSFWorkbook();
			spreadsheet = workbook.createSheet("risultati");
			style = workbook.createCellStyle();
			font = workbook.createFont();
			font.setBold(true);
			style.setFont(font);
			style.setAlignment(HorizontalAlignment.CENTER);
		}
		spreadsheet = workbook.getSheet("risultati");
		
		rowid = 0;
		
		row = spreadsheet.createRow(rowid++);
		
		rownu = spreadsheet.createRow(rowid++);
		rownu.createCell(cellid).setCellValue("");
		rownu.createCell(cellid).setCellValue("");
		
		
	}

	public void logic() {

		cellid = 0;
		
		row = spreadsheet.createRow(rowid++);
		row.createCell(cellid++).setCellValue("");
		
		cell = row.createCell(cellid++);
		cell.setCellValue(questionario.getNumeroQuestionario());
		cell.setCellStyle(style);
		
		int nu = 1;
		
		CellStyle center = workbook.createCellStyle();
		center.setAlignment(HorizontalAlignment.CENTER);
		center.setBorderBottom(BorderStyle.THIN);
		center.setBorderTop(BorderStyle.THIN);
		center.setBorderLeft(BorderStyle.THIN);
		center.setBorderRight(BorderStyle.THIN);
		
		for (Paragrafo p : paragrafi) {
			Domanda d = (Domanda) p;
			List<String> s = new ArrayList<>();
			for (Risposta r : d.getRisposte().stream().filter(r -> r.isValid()).collect(Collectors.toList())) {
				s.add(r.getLettera());
			}
			cell = rownu.createCell(cellid);
			cell.setCellValue(nu++);
			cell.setCellStyle(style);
			cell = row.createCell(cellid++);
			cell.setCellValue(String.join("", s));
			cell.setCellStyle(center);
		}
	}
	
	public void close() throws Exception {
		FileOutputStream out = new FileOutputStream(path.toFile());
		workbook.write(out);
		out.close();
	}

}
