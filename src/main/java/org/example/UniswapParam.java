package org.example;

public class UniswapParam {
    private String amount;
    private String tokenIn;
    private String tokenOut;
    private String type;
    private Integer tokenInChainId;
    private Integer tokenOutChainId;


    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getTokenIn() {
        return tokenIn;
    }

    public void setTokenIn(String tokenIn) {
        this.tokenIn = tokenIn;
    }

    public String getTokenOut() {
        return tokenOut;
    }

    public void setTokenOut(String tokenOut) {
        this.tokenOut = tokenOut;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getTokenInChainId() {
        return tokenInChainId;
    }

    public void setTokenInChainId(Integer tokenInChainId) {
        this.tokenInChainId = tokenInChainId;
    }

    public Integer getTokenOutChainId() {
        return tokenOutChainId;
    }

    public void setTokenOutChainId(Integer tokenOutChainId) {
        this.tokenOutChainId = tokenOutChainId;
    }
}
