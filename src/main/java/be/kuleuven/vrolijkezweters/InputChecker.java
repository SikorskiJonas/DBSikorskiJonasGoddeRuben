package be.kuleuven.vrolijkezweters;

import be.kuleuven.vrolijkezweters.model.Loper;
import be.kuleuven.vrolijkezweters.model.Medewerker;
import be.kuleuven.vrolijkezweters.model.Wedstrijd;

import java.util.ArrayList;
import java.util.Objects;

public class InputChecker {

    public boolean checkInput(Object o){
        if (o.getClass() == Loper.class){
                /**return data.get(1).length() <= 100 && !data.get(1).isEmpty() &&
                        data.get(2).length() <= 100 && !data.get(2).isEmpty() &&
                        (Objects.equals(data.get(3), "M") || Objects.equals(data.get(3), "F") || Objects.equals(data.get(3), "X")) && data.get(3) != null &&
                        data.get(4).length() <= 100 && !data.get(4).isEmpty() &&
                        data.get(5).length() <= 100 && !data.get(5).isEmpty() &&
                        data.get(6).length() <= 100 && !data.get(6).isEmpty() && data.get(6).matches("(.*)@(.*).(.*)") &&
                        data.get(7).length() <= 100 && !data.get(7).isEmpty() &&
                        data.get(8).length() <= 100 && !data.get(8).isEmpty() &&
                        data.get(9).length() <= 100 && !data.get(9).isEmpty();*/
        }
        if (o.getClass() == Medewerker.class){
            /**return data.get(1).length() <= 100 && !data.get(1).isEmpty() &&
                    data.get(2).length() <= 100 && !data.get(2).isEmpty() &&
                    (Objects.equals(data.get(3), "M") || Objects.equals(data.get(3), "F") || Objects.equals(data.get(3), "X")) && !data.get(3).isEmpty() &&
                    data.get(5).length() <= 100 && !data.get(5).isEmpty() &&
                    data.get(6).length() <= 100 && !data.get(6).isEmpty() &&
                    data.get(7).length() <= 100 && !data.get(7).isEmpty() && data.get(7).matches("(.*)@(.*).(.*)") &&
                    data.get(8).length() <= 100 && !data.get(8).isEmpty() &&
                    data.get(9).length() <= 100 && !data.get(9).isEmpty() &&
                    data.get(11).length() <= 100 && !data.get(11).isEmpty();*/
        }
        if (o.getClass() == Wedstrijd.class){
            /**if(data.get(0).length() <= 100 && !data.get(0).isEmpty() &&
                    data.get(2).length() <= 100 && !data.get(2).isEmpty() &&
                    data.get(3).length() <= 100 && !data.get(3).isEmpty() &&
                    data.get(4).length() <= 100 && !data.get(4).isEmpty()){
                if (Double.parseDouble(data.get(3)) >= 0){
                    return true;
                }
            }*/
        }
        return true;
    }
}

