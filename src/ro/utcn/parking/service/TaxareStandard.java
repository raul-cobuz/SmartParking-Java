package ro.utcn.parking.service;

public class TaxareStandard implements Taxabil {
    private static final double TARIF_ORAR = 5.0;

    @Override
    public double calculeazaTaxa(long minuteStationare) {
        if (minuteStationare <= 0) return 0.0;
        long ore = (long) Math.ceil(minuteStationare / 60.0);
        return ore * TARIF_ORAR;
    }
}