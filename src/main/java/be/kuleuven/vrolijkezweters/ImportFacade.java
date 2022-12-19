package be.kuleuven.vrolijkezweters;

import org.jdbi.v3.core.Handle;
import org.jdbi.v3.core.Jdbi;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

public class ImportFacade {
    private Jdbi jdbi;
    private Handle h;
    //TODO code to save models in database based on variable describing model
    //TODO how dates mirrored in right format
    //TODO constraints
    public void SaveToDb(String choice, List<Object> list){
        jdbi = Jdbi.create("jdbc:sqlite:databaseJonasRuben.db");
        h = jdbi.open();
        System.out.println("Connected to database");

        for(Object o : list){
            List<String> fields = Arrays.asList(o.toString().replace("[","").replace("]","").split(", "));
            DateFormat sourceFormat = new SimpleDateFormat("dd-MM-yyyy");
            DateTimeFormatter format = DateTimeFormatter.ofPattern("dd-MM-yyyy");
            System.out.println(o);
            if(choice == "Wedstrijd"){
                try {
                    h.execute("INSERT INTO wedstrijd (Naam, Datum, Plaats, Inschrijvingsgeld, CategorieId) values ('" +
                            fields.get(0) +"', '"+
                            LocalDate.parse(fields.get(1), DateTimeFormatter.ofPattern( "d-M-yyyy" )) +"', '"+
                            fields.get(2)+"', '"+
                            fields.get(3)+"', '"+
                            fields.get(4)+"')");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            else if(choice == "Loper"){
                h.execute("INSERT INTO loper (GeboorteDatum, Voornaam, Naam, Sex, Lengte, Telefoonnummer, 'E-mail', Gemeente, 'Straat + nr') values ('" +
                        LocalDate.parse(fields.get(0), DateTimeFormatter.ofPattern( "d-M-yyyy" )) +"', '"+
                        fields.get(1)+"', '"+
                        fields.get(2)+"', '"+
                        fields.get(3)+"', '"+
                        fields.get(4) +"', '"+
                        fields.get(5)+"', '"+
                        fields.get(6)+"', '"+
                        fields.get(7)+"', '"+
                        fields.get(8) +"')");
            }
            else if(choice == "Medewerker"){
                h.execute("INSERT INTO medewerker (GeboorteDatum, Voornaam, Naam, Sex, DatumTewerkstelling, FunctieId, Telefoonnummer, 'E-mail', Gemeente, 'Straat + nr') values ('" +
                        LocalDate.parse(fields.get(0), format) +"', '"+
                        fields.get(1)+"', '"+
                        fields.get(2)+"', '"+
                        fields.get(3)+"', '"+
                        fields.get(4) +"', '"+
                        fields.get(5)+"', '"+
                        fields.get(6)+"', '"+
                        fields.get(7)+"', '"+
                        fields.get(8)+"', '"+
                        fields.get(9) +"')");
            }
            else if(choice == "Etappe"){
                h.execute("INSERT INTO etappe (AfstandMeter, StartPlaats, EindPlaats, WedstrijdId, Naam) values ('" +
                        fields.get(0)+"', '"+
                        fields.get(1)+"', '"+
                        fields.get(2)+"', '"+
                        fields.get(3) +"', '"+
                        fields.get(4) +"')");
            }
            else if(choice == "LoopNummer"){
                h.execute("INSERT INTO loop_nummer (Nummer, Looptijd, LoperId, EtappeId) values ('" +
                        fields.get(0)+"', '"+
                        fields.get(1)+"', '"+
                        fields.get(2)+"', '"+
                        fields.get(3) +"')");
            }
            else{
                System.out.println("rip");
            }
        }


        h.close();
    }
}
