package comsume.app;

import comsume.service.CalculatorRemoteImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import provider.service.Calcultor;


public class ComsumerApp {
    private  static Logger log = LoggerFactory.getLogger(ComsumerApp.class);

    public static void main(String[] args) {
        Calcultor calcultor = new CalculatorRemoteImpl();
        int result = calcultor.add(1,2);
        System.out.print(result);
    }
}
