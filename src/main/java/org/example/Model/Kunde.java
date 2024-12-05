package org.example.Model;

public class Kunde {
    private int kundenID;  // Autoincrement in database
    private String vorname;
    private String nachname;
    private String adresse;
    private String plz;
    private String ort;

    public Kunde() {}

    public Kunde(int kundenID, String vorname, String nachname, String adresse, String plz, String ort) {
        this.kundenID = kundenID;
        this.vorname = vorname;
        this.nachname = nachname;
        this.adresse = adresse;
        this.plz = plz;
        this.ort = ort;
    }

    public int getKundenID() {
        return kundenID;
    }

    public void setKundenID(int kundenID) {
        this.kundenID = kundenID;
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

    public String getOrt() {
        return ort;
    }

    public void setOrt(String ort) {
        this.ort = ort;
    }

    @Override
    public String toString() {
        return vorname + " " + nachname;
    }
}
