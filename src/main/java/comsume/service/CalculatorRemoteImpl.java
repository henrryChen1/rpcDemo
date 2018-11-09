package comsume.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import provider.service.Calcultor;
import request.CalculateRpcRequest;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class CalculatorRemoteImpl implements Calcultor {
    public static final int PORT = 9090;
    private static Logger log = LoggerFactory.getLogger(CalculatorRemoteImpl.class);

    public int add(int a, int b) {
        List<String> addressList = lookupProviders("Calculator.add");
        String address = chooseTarget(addressList);
        try {
            //获得远程调用对象ip
            Socket socket = new Socket(address, PORT);
            //序列化
            CalculateRpcRequest calculateRpcRequest = generateRequest(a, b);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            //将请求发给服务提供方
            objectOutputStream.writeObject(calculateRpcRequest);
            //将响应体反序列化
            ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
            Object response = objectInputStream.readObject();

            log.info("response is {}", response);

            if (response instanceof Integer) {
                return (Integer) response;
            } else {
                throw new InternalError();
            }
        } catch (IOException e) {
            log.error("error ip", e);
            throw new InternalError();
        } catch (ClassNotFoundException e) {
            log.error("error class", e);
            throw new InternalError();
        }

    }

    private String chooseTarget(List<String> providers) {
        if (null == providers || providers.size() == 0) {
            throw new IllegalArgumentException();
        }
        return providers.get(0);
    }

    private CalculateRpcRequest generateRequest(int a, int b) {
        CalculateRpcRequest calculateRpcRequest = new CalculateRpcRequest();
        calculateRpcRequest.setA(a);
        calculateRpcRequest.setB(b);
        calculateRpcRequest.setMethod("add");
        return calculateRpcRequest;
    }

    private List<String> lookupProviders(String name) {
        List<String> strings = new ArrayList<String>();
        strings.add("127.0.0.1");
        return strings;
    }
}
