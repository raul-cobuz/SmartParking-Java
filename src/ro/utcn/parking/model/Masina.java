package ro.utcn.parking.model;

import ro.utcn.parking.exceptions.NumarInmatriculareInvalidException;

import java.io.Serializable;

public class Masina implements Serializable {

    private static final long serialVersionUID = 1L;
    private String numarInmatriculare;
    private String marca;


    public Masina(String numarInmatriculare, String marca) {
        this.numarInmatriculare = numarInmatriculare;
        this.marca = marca;
    }


    public String getNumarInmatriculare() {
        return numarInmatriculare;
    }

    public String getMarca() {
        return marca;
    }


    public static void valideazaNumar(String nr) throws NumarInmatriculareInvalidException {
        if (nr == null) throw new NumarInmatriculareInvalidException("Numărul lipsește!");
        String nrClean = nr.toUpperCase().replaceAll("\\s+", "");


        String judete = "AB|AR|AG|BC|BH|BN|BT|BV|BR|BZ|CS|CL|CJ|CT|CV|DB|DJ|GL|GR|GJ|HR|HD|IL|IS|IF|MM|MH|MS|NT|OT|PH|SM|SJ|SB|SV|TR|TM|TL|VS|VL|VN";

        String regex = "^((B\\d{2,3})|((" + judete + ")\\d{2}))[A-Z]{3}$";

        if (!nrClean.matches(regex)) {
            throw new NumarInmatriculareInvalidException("Număr invalid! Asigurați-vă că județul și numărul de cifre sunt corecte.");
        }
    }

    @Override
    public String toString() {
        return "Masina " + marca + " (" + numarInmatriculare + ")";
    }
}