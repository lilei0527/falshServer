package org.example;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import okhttp3.*;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.util.ArrayList;
import java.util.List;

public class Flash {

    public static final String UNISWAP_SWAP_URL = "https://trading-api-labs.interface.gateway.uniswap.org/v1/indicative_quote";
    public static final String SUSHISWAP_SWAP_URL = "https://api.sushi.com/swap/v5/";

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
                .post(body)
                .build();

        // 执行请求并获取响应
        try (Response response = client.newCall(request).execute()) {
            // 判断响应是否成功
            if (response.isSuccessful()) {
                assert response.body() != null;
                String responseBody = response.body().string();
                JSONObject jsonObject = JSON.parseObject(responseBody);
                JSONObject output = jsonObject.getJSONObject("output");
                return output.getString("amount");
            } else {
                throw new RuntimeException("Unexpected code " + response);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String getSushiSwapPrice(String tokenIn, String tokenOut, String amount, Integer tokenInChainId, Integer tokenOutChainId) {

        Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("127.0.0.1", 10809));
        OkHttpClient client = new OkHttpClient.Builder().proxy(proxy).build();

        // 构建 URL 带查询参数
        HttpUrl url = new HttpUrl.Builder()
                .scheme("https") // URL 协议
                .host("api.sushi.com") // 服务器域名
                .addPathSegment("swap") // 路径部分
                .addPathSegment("v5") // 路径部分
                .addPathSegment(String.valueOf(tokenInChainId)) // 路径部分
                .addQueryParameter("referrer", "sushi") // 添加查询参数
                .addQueryParameter("tokenIn", tokenIn)
                .addQueryParameter("tokenOut", tokenOut)
                .addQueryParameter("amount", amount)
                .addQueryParameter("maxSlippage", "0.005")
                .addQueryParameter("gasPrice", "1000000000")
                .addQueryParameter("to", "0x87fD8da2FE83f2259ee6c762A5af28bDEa4Fb063")
                .addQueryParameter("enableFee", "true")
                .addQueryParameter("feeReceiver", "0x87fD8da2FE83f2259ee6c762A5af28bDEa4Fb063")
                .addQueryParameter("fee", "0.0025")
                .addQueryParameter("feeBy", "output")
                .addQueryParameter("includeTransaction", "true")
                .build();

        // 创建 Request 对象
        Request request = new Request.Builder()
                .url(url)
                .addHeader("Content-Type", "application/json")
                .get()
                .build();

        // 执行请求并获取响应
        try (Response response = client.newCall(request).execute()) {
            // 判断响应是否成功
            if (response.isSuccessful()) {
                assert response.body() != null;
                String responseBody = response.body().string();
                JSONObject jsonObject = JSON.parseObject(responseBody);
                String swapPrice = jsonObject.getString("assumedAmountOut");
                return swapPrice;
            } else {
                throw new RuntimeException("Unexpected code " + response);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    private List<Pair<Token>> getPairs() {
        List<Token> tokens = new ArrayList<>();
        tokens.add(new Token("WBNB", "0xbb4CdB9CBd36B01bD1cBaEBF2De08d9173bc095c"));
        tokens.add(new Token("USDT", "0x55d398326f99059fF775485246999027B3197955"));
        tokens.add(new Token("USDC", "0x8AC76a51cc950d9822D68b83fE1Ad97B32Cd580d"));
        tokens.add(new Token("ETH", "0x2170Ed0880ac9A755fd29B2688956BD959F933F8"));
        tokens.add(new Token("BUSD", "0xe9e7CEA3DedcA5984780Bafc599bD69ADd087D56"));
        tokens.add(new Token("UNI", "0xBf5140A22578168FD562DCcF235E5D43A02ce9B1"));
        tokens.add(new Token("AAVE", "0xfb6115445Bff7b52FeB98650C87f44907E58f802"));
        tokens.add(new Token("FET", "0x031b41e504677879370e9DBcF937283A8691Fa7f"));
        tokens.add(new Token("INJ", "0xa2B726B1145A4773F68593CF171187d8EBe4d495"));
        tokens.add(new Token("LINK", "0xF8A0BF9cF54Bb92F17374d9e9A321E6a111a51bD"));
        tokens.add(new Token("SOL", "0xfA54fF1a158B5189Ebba6ae130CEd6bbd3aEA76e"));
        tokens.add(new Token("G", "0x9C7BEBa8F6eF6643aBd725e45a4E8387eF260649"));
        tokens.add(new Token("DAI", "0x1AF3F329e8BE154074D8769D1FFa4eE058B1DBc3"));
        tokens.add(new Token("ZRO", "0x6985884C4392D348587B19cb9eAAf157F13271cd"));
        tokens.add(new Token("FTM", "0xAD29AbB318791D579433D831ed122aFeAf29dcfe"));
        tokens.add(new Token("1INCH", "0x111111111117dC0aa78b770fA6A738034120C302"));
        tokens.add(new Token("PEPE", "0x25d887Ce7a35172C62FeBFD67a1856F20FaEbB00"));
        tokens.add(new Token("SUSHI", "0x947950BcC74888a40Ffa2593C5798F11Fc9124C4"));
        tokens.add(new Token("BEAM", "0x62D0A8458eD7719FDAF978fe5929C6D342B0bFcE"));
        tokens.add(new Token("AXS", "0x715D400F88C167884bbCc41C5FeA407ed4D2f8A0"));
        tokens.add(new Token("COMP", "0x52CE071Bd9b1C4B00A0b92D298c512478CaD67e8"));
        tokens.add(new Token("FRAX", "0x90C97F71E18723b0Cf0dfa30ee176Ab653E89F40"));
        tokens.add(new Token("STG", "0xB0D502E938ed5f4df2E681fE6E419ff29631d62b"));
        tokens.add(new Token("MATIC", "0xCC42724C6683B7E57334c4E856f4c9965ED682bD"));
        tokens.add(new Token("FLOKI", "0xfb5B838b6cfEEdC2873aB27866079AC55363D37E"));
        tokens.add(new Token("PRQ", "0xd21d29B38374528675C34936bf7d5Dd693D2a577"));
        tokens.add(new Token("AIOZ", "0x33d08D8C7a168333a85285a68C0042b39fC3741D"));
        tokens.add(new Token("UNFI", "0x728C5baC3C3e370E372Fc4671f9ef6916b814d8B"));
        tokens.add(new Token("ANKR", "0xf307910A4c7bbc79691fD374889b36d8531B08e3"));
        tokens.add(new Token("MIM", "0xfE19F0B51438fd612f6FD59C1dbB3eA319f433Ba"));
        tokens.add(new Token("ATA", "0xA2120b9e674d3fC3875f415A7DF52e382F141225"));
        tokens.add(new Token("YGG", "0x13Ab6739368a4e4abf24695bf52959224367391f"));
        tokens.add(new Token("GAL", "0xe4Cc45Bb5DBDA06dB6183E8bf016569f40497Aa5"));
        tokens.add(new Token("ELON", "0x7bd6FaBD64813c48545C9c0e312A0099d9be2540"));
        tokens.add(new Token("ALT", "0x8457CA5040ad67fdebbCC8EdCE889A335Bc0fbFB"));
        tokens.add(new Token("SXP", "0x47BEAd2563dCBf3bF2c9407fEa4dC236fAbA485A"));
        tokens.add(new Token("HIGH", "0x5f4Bde007Dc06b867f86EBFE4802e34A1fFEEd63"));

        List<Pair<Token>> pairs = new ArrayList<>();
        for (int i = 0; i < tokens.size(); i++) {
            for (int j = i + 1; j < tokens.size(); j++) {
                pairs.add(new Pair<>(tokens.get(i), tokens.get(j)));
            }
        }
        return pairs;
    }




    public static void main(String[] args) {
        // 连接到以太坊节点（这里以 Infura 为例）
//        String infuraUrl = "https://mainnet.infura.io/v3/YOUR_INFURA_PROJECT_ID";
//        Web3j web3j = Web3j.build(new HttpService(infuraUrl));
//        System.out.println("Connected to Ethereum network");


//        Flash flash = new Flash();
//        String uniswapSwapPrice = flash.getUniswapSwapPrice("0x0000000000000000000000000000000000000000",
//                "0x111111111117dc0aa78b770fa6a738034120c302", "1000000000000000000", 56, 56);
//        String sushiSwapPrice = flash.getSushiSwapPrice("0xEeeeeEeeeEeEeeEeEeEeeEEEeeeeEeeeeeeeEEeE",
//                "0x111111111117dc0aa78b770fa6a738034120c302", "1000000000000000000", 56, 56);

//        System.out.println(uniswapSwapPrice);
//        System.out.println(sushiSwapPrice);
        Flash flash = new Flash();
        List<Pair<Token>> pairs = flash.getPairs();
        for (Pair<Token> pair : pairs) {
            Token first = pair.getFirst();
            Token second = pair.getSecond();

            try {
                BigDecimal uniswapAmountin = new BigDecimal("1000000000000000000");
                String uniswapSwapOut = flash.getSushiSwapPrice(first.getAddress(), second.getAddress(), uniswapAmountin.toString(), 56, 56);
//                String uniswapSwapOut = flash.getUniswapSwapPrice(first.getAddress(), second.getAddress(), uniswapAmountin.toString(), 56, 56);
                String sushiSwapAmountOut = flash.getUniswapSwapPrice(second.getAddress(), first.getAddress(), uniswapSwapOut.toString(), 56, 56);
                BigDecimal sushiSwapAmountOutDecimal = new BigDecimal(sushiSwapAmountOut);
                BigDecimal divide = sushiSwapAmountOutDecimal.subtract(uniswapAmountin).divide(new BigDecimal("1000000000000000000"));

                System.out.println(String.format("%s-%s: 差价：%s",first.getToken(),second.getToken(), divide));
            }catch (Exception e){
                System.out.println(String.format("%s-%s: 无法获取价格",first.getToken(),second.getToken()));
            }

        }

    }
}
