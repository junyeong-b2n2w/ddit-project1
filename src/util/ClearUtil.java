package util;

public class ClearUtil {

    static void clearMethod(){
        for(int i =0; i< 50 ; i++){
            System.out.println();
        }
    }


    static void sleepMethod(int milsec){
        try{
        Thread.sleep(milsec);
        }catch (InterruptedException e){

        }
    }
}
