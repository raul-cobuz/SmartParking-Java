package ro.utcn.parking.service;



import ro.utcn.parking.exceptions.ParcarePlinaException;
import ro.utcn.parking.exceptions.TimpDepasitException;
import ro.utcn.parking.model.LocParcare;
import ro.utcn.parking.model.Masina;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.io.*;
import java.nio.file.*;

public class ParcareManager {
    private List<LocParcare> locuri;

    public ParcareManager(int numarLocuri) {
        locuri = new ArrayList<>();
        for (int i = 1; i <= numarLocuri; i++) {
            locuri.add(new LocParcare(i));
        }
    }

    public void parcheazaMasina(Masina masina) throws ParcarePlinaException {
        for (LocParcare loc : locuri) {
            if (!loc.isEsteOcupat()) {
                loc.ocupeazaLoc(masina);
                return;
            }
        }
        throw new ParcarePlinaException("Parcarea este plină!");
    }

    public double iesireMasina(String numarInmatriculare, Taxabil metodaDePlata) throws TimpDepasitException {
        LocParcare locGasit = null;

        for (LocParcare loc : locuri) {
            if (loc.isEsteOcupat() && loc.getMasinaCurenta().getNumarInmatriculare().equalsIgnoreCase(numarInmatriculare)) {
                locGasit = loc;
                break;
            }
        }

        if (locGasit == null) {
            return 0.0;
        }

        LocalDateTime oraIntrare = locGasit.getOraIntrare();
        LocalDateTime oraIesire = LocalDateTime.now();

        long minuteStationare = Duration.between(oraIntrare, oraIesire).toMinutes();

        if (minuteStationare < 60) {
            minuteStationare = 60;
        }

        if (minuteStationare > 1440) { // 24h
            throw new TimpDepasitException("Vehiculul " + numarInmatriculare + " a depășit limita de 24h!");
        }


        double costTotal = metodaDePlata.calculeazaTaxa(minuteStationare);
        locGasit.elibereazaLoc();

        return costTotal;
    }

    public List<LocParcare> getLocuri() {
        return locuri;
    }


    public String genereazaRaport() {
        int ocupate = 0;
        for (LocParcare loc : locuri) {
            if (loc.isEsteOcupat()) {
                ocupate++;
            }
        }
        int libere = locuri.size() - ocupate;
        return "OCUPARE: " + ocupate + " | LIBERE: " + libere + " | TOTAL: " + locuri.size();
    }

    public boolean existaMasina(String nrCautat) {
        if (nrCautat == null || nrCautat.isEmpty()) {
            return false;
        }
        String nrCautatCurat = nrCautat.trim();
        for (LocParcare loc : locuri) {
            if (loc.isEsteOcupat() && loc.getMasinaCurenta() != null) {
                String nrExistent = loc.getMasinaCurenta().getNumarInmatriculare();
                if (nrExistent != null) {
                    if (nrExistent.trim().equalsIgnoreCase(nrCautatCurat)) {
                        return true;
                    }
                }
            }
        }
        return false;

    }

    public void salveazaDatele() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("parcare.dat"))) {
            oos.writeObject(this.locuri);
            System.out.println("Datele au fost salvate cu succes.");
        } catch (IOException e) {
            System.err.println("Eroare la salvare: " + e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    public void incarcaDatele() {
        File file = new File("parcare.dat");
        if (!file.exists()) return; // Dacă e prima rulare, nu avem ce încărca

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            this.locuri = (List<LocParcare>) ois.readObject();
            System.out.println("Datele au fost restaurate.");
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Eroare la încărcare: " + e.getMessage());
        }
    }

}