package org.example.Model;

public class Kunde {
    private int kundennummer;
    private String vorname;
    private String nachname;
    private String adresse;
    private String plz;

    public Kunde() {}

    public Kunde(int kundennummer, String vorname, String nachname, String adresse, String plz) {
        this.kundennummer = kundennummer;
        this.vorname = vorname;
        this.nachname = nachname;
        this.adresse = adresse;
        this.plz = plz;
    }

    public int getKundennummer() {
        return kundennummer;
    }

    public void setKundennummer(int kundennummer) {
        this.kundennummer = kundennummer;
    }

    public String getVorname() {
        return vorname;
    }

    public void setVorname(String vorname) {
        this.vorname = vorname;
    }

    public String getNachname() {
        return nachname;
    }

    public void setNachname(String nachname) {
        this.nachname = nachname;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public String getPlz() {
        return plz;
    }

    public void setPlz(String plz) {
        this.plz = plz;
    }
}

