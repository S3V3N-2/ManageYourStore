package com.example.manageyourstore.app;

public class Garanzia {

    private String ID;
    private String img;
    private String nomeAcquirente;
    private String cognomeAcquirente;
    private String nomeArticolo;
    private String numeroTelefonoAcquirente;
    private int giornoRitiroAssistenza;
    private int meseRitiroAssistenza;
    private int annoRitiroAssistenza;
    private String descrizioneProblema;
    private int giornoFineGaranzia;
    private int meseFineGaranzia;
    private int annoFineGaranzia;
    private int statoAssistenza;

    public Garanzia() {}

    public Garanzia(String ID, String img, String nomeAcquirente, String cognomeAcquirente,
                    String nomeArticolo, String numeroTelefonoAcquirente,
                    int giornoRitiroAssistenza, int meseRitiroAssistenza, int annoRitiroAssistenza,
                    String descrizioneProblema, int giornoFineGaranzia, int meseFineGaranzia,
                    int annoFineGaranzia, int statoAssistenza) {
        this.ID = ID;
        this.img = img;
        this.nomeAcquirente = nomeAcquirente;
        this.cognomeAcquirente = cognomeAcquirente;
        this.nomeArticolo = nomeArticolo;
        this.numeroTelefonoAcquirente = numeroTelefonoAcquirente;
        this.giornoRitiroAssistenza = giornoRitiroAssistenza;
        this.meseRitiroAssistenza = meseRitiroAssistenza;
        this.annoRitiroAssistenza = annoRitiroAssistenza;
        this.descrizioneProblema = descrizioneProblema;
        this.giornoFineGaranzia = giornoFineGaranzia;
        this.meseFineGaranzia = meseFineGaranzia;
        this.annoFineGaranzia = annoFineGaranzia;
        this.statoAssistenza = statoAssistenza;
    }

    public String getID() {
        return ID;
    }

    public int getGiornoFineGaranzia() {
        return giornoFineGaranzia;
    }

    public int getMeseFineGaranzia() {
        return meseFineGaranzia;
    }

    public int getAnnoFineGaranzia() {
        return annoFineGaranzia;
    }

    public void setAnnoFineGaranzia(int annoFineGaranzia) {
        this.annoFineGaranzia = annoFineGaranzia;
    }

    public String getImg() {
        return img;
    }

    public String getNomeAcquirente() {
        return nomeAcquirente;
    }

    public String getCognomeAcquirente() {
        return cognomeAcquirente;
    }

    public String getNomeArticolo() {
        return nomeArticolo;
    }

    public int getStatoAssistenza() {
        return statoAssistenza;
    }

    public void setStatoAssistenza(int statoAssistenza) {
        this.statoAssistenza = statoAssistenza;
    }

    public String getDescrizioneProblema() {
        return descrizioneProblema;
    }

    public int getGiornoRitiroAssistenza() {
        return giornoRitiroAssistenza;
    }

    public int getMeseRitiroAssistenza() {
        return meseRitiroAssistenza;
    }

    public int getAnnoRitiroAssistenza() {
        return annoRitiroAssistenza;
    }
}
