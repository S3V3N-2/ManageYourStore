package com.example.manageyourstore.app;

public class Articolo {

    private String ID;
    private String nome;
    private String img;
    private String descrizione;
    private int quantita;
    private double prezzo;

    public Articolo() {}

    public Articolo(String ID, String nome, int quantita, String img, double prezzo, String descrizione) {
        this.ID = ID;
        this.nome = nome;
        this.prezzo = prezzo;
        this.quantita = quantita;
        this.img = img;
        this.descrizione = descrizione;
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

    public double getPrezzo() {
        return prezzo;
    }

    public int getQuantita() {
        return quantita;
    }

    public String getImg() {
        return img;
    }

    public String getDescrizione() {
        return descrizione;
    }
}
