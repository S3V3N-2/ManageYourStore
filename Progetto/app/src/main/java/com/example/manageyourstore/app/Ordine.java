package com.example.manageyourstore.app;

public class Ordine {

    private String ID;
    private String nome;
    private String img;
    private String nomeAcquirente;
    private String cognomeAcquirente;
    private String telefonoAcquirente;
    private String CFAcquirente;
    private String descrizioneArticolo;
    private int giornoArrivo;
    private int meseArrivo;
    private int annoArrivo;

    public Ordine() {}

    public Ordine(String ID, String nome, String img, String nomeAcquirente,
                  String cognomeAcquirente, String telefonoAcquirente, String CFAcquirente,
                  String descrizioneArticolo, int giornoArrivo, int meseArrivo, int annoArrivo) {
        this.ID = ID;
        this.nome = nome;
        this.img = img;
        this.nomeAcquirente = nomeAcquirente;
        this.cognomeAcquirente = cognomeAcquirente;
        this.telefonoAcquirente = telefonoAcquirente;
        this.CFAcquirente = CFAcquirente;
        this.descrizioneArticolo = descrizioneArticolo;
        this.giornoArrivo = giornoArrivo;
        this.meseArrivo = meseArrivo;
        this.annoArrivo = annoArrivo;
    }

    public String getID() {
        return ID;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCognomeAcquirente() {
        return cognomeAcquirente;
    }

    public String getImg() {
        return img;
    }

    public String getNomeAcquirente() {
        return nomeAcquirente;
    }

    public String getTelefonoAcquirente() {
        return telefonoAcquirente;
    }

    public String getCFAcquirente() {
        return CFAcquirente;
    }

    public String getDescrizioneArticolo() {
        return descrizioneArticolo;
    }

    public int getGiornoArrivo() {
        return giornoArrivo;
    }

    public int getMeseArrivo() {
        return meseArrivo;
    }

    public int getAnnoArrivo() {
        return annoArrivo;
    }
}
