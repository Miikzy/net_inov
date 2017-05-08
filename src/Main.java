import server.ServerTCP;

public class Main {

    public static void main(String ... args){
        new ServerTCP(8080).start();
    }


}
