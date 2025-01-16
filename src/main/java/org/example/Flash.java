package org.example;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import okhttp3.*;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.http.HttpService;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Proxy;

public class Flash {

    public static final String UNISWAP_SWAP_URL = "https://trading-api-labs.interface.gateway.uniswap.org/v1/indicative_quote";


    public void flash() {

    }


    public String getUniswapSwapPrice(String tokenIn, String tokenOut, String amount,Integer     tokenInChainId, Integer tokenOutChainId) {
        UniswapParam uniswapParam = new UniswapParam();
        uniswapParam.setTokenIn(tokenIn);
        uniswapParam.setTokenOut(tokenOut);
        uniswapParam.setAmount(amount);
        uniswapParam.setType("EXACT_INPUT");
        uniswapParam.setTokenInChainId(tokenInChainId);
        uniswapParam.setTokenOutChainId(tokenOutChainId);

        String jsonString = JSON.toJSONString(uniswapParam);
        MediaType mediaType = MediaType.get("application/json");
        RequestBody body = RequestBody.create(mediaType, jsonString);


        Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("127.0.0.1", 10809));
        OkHttpClient client = new OkHttpClient.Builder().proxy(proxy).build();

        // 创建 Request 对象
        Request request = new Request.Builder()
                .url(UNISWAP_SWAP_URL)
                .addHeader("Content-Type", "application/json")
                .addHeader("x-api-key", "JoyCGj29tT4pymvhaGciK4r1aIPvqW6W53xT1fwo")
                .addHeader("Connection", "keep-alive")
                .addHeader("User-Agent", "okhttp/3.14.9")
                .addHeader("Accept", "*/*")
                .addHeader("Accept-Encoding", "gzip, deflate, br")
                .post(body)
                .build();

        // 执行请求并获取响应
        try (Response response = client.newCall(request).execute()) {
            // 判断响应是否成功
            if (response.isSuccessful()) {
                String string = response.body().toString();
                JSONObject jsonObject = JSON.parseObject(response.body().toString());
                JSONObject output = jsonObject.getJSONObject("output");
                return output.getString("amount");
            } else {
                throw new RuntimeException("Unexpected code " + response);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }


    public static void main(String[] args) {
        // 连接到以太坊节点（这里以 Infura 为例）
//        String infuraUrl = "https://mainnet.infura.io/v3/YOUR_INFURA_PROJECT_ID";
//        Web3j web3j = Web3j.build(new HttpService(infuraUrl));
//        System.out.println("Connected to Ethereum network");


        Flash flash = new Flash();
        String uniswapSwapPrice = flash.getUniswapSwapPrice("0x0000000000000000000000000000000000000000",
                "0x111111111117dc0aa78b770fa6a738034120c302", "1230000000000000000", 56, 56);

        System.out.println(uniswapSwapPrice);

    }
}
