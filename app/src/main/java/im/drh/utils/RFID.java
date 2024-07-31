package im.drh.utils;

import car.car2024.Utils.Socket.Variable;

public class RFID {

    public static String[] spilt(){
        return Variable.RFID.toString().split("_");
    }

}
