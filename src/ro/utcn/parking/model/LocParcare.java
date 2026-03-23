package ro.utcn.parking.model;

import java.io.Serializable;
import java.time.LocalDateTime;

public class LocParcare implements Serializable {
    private static final long serialVersionUID = 1L;
    private int id;
    private boolean esteOcupat;
    private Masina masinaCurenta;
    private LocalDateTime oraIntrare;

    public LocParcare(int id) {
        this.id = id;
        this.esteOcupat = false;
        this.masinaCurenta = null;
    }


    public void ocupeazaLoc(Masina masina) {
        this.masinaCurenta = masina;
        this.esteOcupat = true;
        this.oraIntrare = LocalDateTime.now();
        System.out.println("Locul " + id + " a fost ocupat de " + masina.getNumarInmatriculare() + " la ora " + oraIntrare);
    }


    public void elibereazaLoc() {
        this.masinaCurenta = null;
        this.esteOcupat = false;
        this.oraIntrare = null;
        System.out.println("Locul " + id + " este acum liber.");
    }


    public int getId() {
        return id;
    }

    public boolean isEsteOcupat() {
        return esteOcupat;
    }

    public Masina getMasinaCurenta() {
        return masinaCurenta;
    }
    public LocalDateTime getOraIntrare() {
        return oraIntrare;
    }
    public void setOraIntrareManuala(LocalDateTime ora) {
        this.oraIntrare = ora;
    }
}