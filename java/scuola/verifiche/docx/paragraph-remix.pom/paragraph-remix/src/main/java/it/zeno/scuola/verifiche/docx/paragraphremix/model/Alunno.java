package it.zeno.scuola.verifiche.docx.paragraphremix.model;

public class Alunno {
	private String nome;
	private String cognome;
	
	public Alunno(String nomeCognomeSeparatiConHash) {
		int indexOfHash = nomeCognomeSeparatiConHash.indexOf('#');
		nome = nomeCognomeSeparatiConHash.substring(0,indexOfHash);
		cognome = nomeCognomeSeparatiConHash.substring(indexOfHash + 1);
	}
	
	@Override
	public String toString() {
		return cognome.concat("-").concat(nome);
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public void setCognome(String cognome) {
		this.cognome = cognome;
	}

	public String getCognome() {
		return cognome;
	}
}
