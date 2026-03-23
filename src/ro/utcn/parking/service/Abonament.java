package ro.utcn.parking.service;
import ro.utcn.parking.service.Taxabil;

public class Abonament implements Taxabil {
    private String idAbonat;

    public Abonament(String idAbonat) {
        this.idAbonat = idAbonat;
    }

    @Override
    public double calculeazaTaxa(long minuteStationare) {

        return 0.0;
    }
}