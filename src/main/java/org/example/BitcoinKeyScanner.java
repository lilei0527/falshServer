package org.example;

import org.bitcoinj.core.Address;
import org.bitcoinj.core.ECKey;
import org.bitcoinj.core.NetworkParameters;
import org.bitcoinj.params.MainNetParams;
import org.bitcoinj.script.Script;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

public class BitcoinKeyScanner {

    private static final String API_URL = "https://blockchain.info/q/addressbalance/";
    private static final String API_URL_1 = "https://api.blockchair.com/bitcoin/dashboards/address/";
    private static final String OUTPUT_FILE = "found_keys.txt";
    private static final int NUM_KEYS_TO_SCAN = 1000;  // 默认扫描1000个私钥



    public static void main(String[] args) {
        saveFoundKey("12", "123", 1);
            // 设置比特币主网的网络参数
            NetworkParameters params = MainNetParams.get();

            String url = API_URL;

            // 执行私钥撞库
            while (true) {
                // 生成随机私钥
                ECKey key = new ECKey();
                // 使用 Address.fromKey() 生成地址
                Address address = Address.fromKey(params, key, Script.ScriptType.P2PKH);

                // 打印正在检查的地址
                System.out.println("Checking address: " + address.toString());

                // 检查地址的余额
                long balance=0;
                try {
                 balance = getBalance(address.toString(),url);
                } catch (IOException e) {
                    if(url.equals(API_URL)){
                        url = API_URL_1;
                    }else{
                        url = API_URL;
                    }
                }

                // 如果地址有余额，保存私钥和地址
                if (balance > 0) {
                    String privateKey = key.getPrivateKeyEncoded(params).toString();
                    saveFoundKey(privateKey, address.toString(), balance);
                    System.out.println("Found key! Private key: " + privateKey + ", Address: " + address.toString() + ", Balance: " + balance);
                }
            }


    }

    // 获取比特币地址的余额
    private static long getBalance(String address,String url1) throws IOException {
        String urlString = API_URL + address;
        URL url = new URL(url1);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setConnectTimeout(10000);
        connection.setReadTimeout(10000);

        int responseCode = connection.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) {
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                String response = reader.readLine();
                return Long.parseLong(response);  // 返回余额
            }
        } else {
            return 0;  // 如果请求失败，则认为该地址余额为0
        }
    }

    // 将找到的私钥、地址和余额保存到文件
    private static void saveFoundKey(String privateKey, String address, long balance) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(OUTPUT_FILE, true))) {
            writer.write("Private Key: " + privateKey + ", Address: " + address + ", Balance: " + balance + "\n");
        } catch (IOException e) {
            System.err.println("Error writing to file: " + e.getMessage());
        }
    }
}
