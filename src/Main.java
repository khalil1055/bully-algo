import Main.Manager;

public class Main {


    public static void main(String[] args) {

        Manager manager = Manager.getInstance();
        manager.socketServe();

    }



}

