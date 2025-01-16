package org.example;

import org.web3j.protocol.Web3j;
import org.web3j.protocol.http.HttpService;

public class Flash {


    public static void main(String[] args) {
        // 连接到以太坊节点（这里以 Infura 为例）
        String infuraUrl = "https://mainnet.infura.io/v3/YOUR_INFURA_PROJECT_ID";
        Web3j web3j = Web3j.build(new HttpService(infuraUrl));
        System.out.println("Connected to Ethereum network");
    }
}
