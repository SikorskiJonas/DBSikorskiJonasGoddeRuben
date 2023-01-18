package be.kuleuven.vrolijkezweters;

import be.kuleuven.vrolijkezweters.jdbi.*;
import be.kuleuven.vrolijkezweters.model.*;
import org.jdbi.v3.core.Handle;
import org.jdbi.v3.core.Jdbi;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Random;

public class ImportExcel {
    private Handle h;

    //TODO constraints
    public void SaveToDb(String choice, List<Object> list) {
        WedstrijdDao wedstrijdDao = new WedstrijdDao();
        LoperDao loperDao = new LoperDao();
        MedewerkerDao medewerkerDao = new MedewerkerDao();
        LoopNummerDao loopNummerDao = new LoopNummerDao();
        EtappeDao etappeDao = new EtappeDao();

        for (Object o : list) {
            List<String> fields = Arrays.asList(o.toString().replace("[", "").replace("]", "").split(", "));
            DateTimeFormatter format = DateTimeFormatter.ofPattern("dd-MM-yyyy");
            if (Objects.equals(choice, "Wedstrijd")) {
                try {
                    Wedstrijd wedstrijd = new Wedstrijd(fields.get(0), LocalDate.parse(fields.get(1), DateTimeFormatter.ofPattern("dd-MM-yyyy")).toString(), fields.get(2), fields.get(3), fields.get(4));
                    wedstrijdDao.insert(wedstrijd);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (Objects.equals(choice, "Loper")) {
                try {
                    Loper loper = new Loper(LocalDate.parse(fields.get(0), DateTimeFormatter.ofPattern("d-M-yyyy")).toString(), fields.get(1), fields.get(2), fields.get(3), fields.get(4), fields.get(5), fields.get(6), fields.get(7), fields.get(8), generatePassword());
                    loperDao.insert(loper);
                } catch (Exception e){
                    e.printStackTrace();
                }
            } else if (Objects.equals(choice, "Medewerker")) {
                try {
                    Medewerker medewerker = new Medewerker(LocalDate.parse(fields.get(0), DateTimeFormatter.ofPattern("d-M-yyyy")).toString(), fields.get(1), fields.get(2), fields.get(3), fields.get(4), fields.get(5), fields.get(6), fields.get(7), fields.get(8), fields.get(9), generatePassword(), "false");
                    medewerkerDao.insert(medewerker);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (Objects.equals(choice, "Etappe")) {
                try{
                    Etappe etappe = new Etappe(Integer.parseInt(fields.get(0)), fields.get(1), fields.get(2), Integer.parseInt(fields.get(3)), fields.get(4));
                } catch (Exception e){
                    e.printStackTrace();
                }
            } else if (Objects.equals(choice, "LoopNummer")) {
                try {
                    LoopNummer loopNummer = new LoopNummer(Integer.parseInt(fields.get(0)), Integer.parseInt(fields.get(1)), Integer.parseInt(fields.get(2)), Integer.parseInt(fields.get(3)));
                    loopNummerDao.insert(loopNummer);
                } catch (Exception e){
                    e.printStackTrace();
                }
            } else {
                System.out.println("could find model to import");
            }
        }
    }

    public String generatePassword() {
        char[] chars = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRST" .toCharArray();

        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < 9; i++) {
            char c = chars[random.nextInt(chars.length)];
            sb.append(c);
        }

        return sb.toString();
    }
}
