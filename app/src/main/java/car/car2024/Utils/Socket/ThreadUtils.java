package car.car2024.Utils.Socket;

/**
 * @author zhy
 * @create_date 2019-05-24 23:32
 */
public class ThreadUtils {

    public static void sleep(int  millis){
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void sleep(int millis, String mag){
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
            System.out.println(mag);
        }
    }
}
