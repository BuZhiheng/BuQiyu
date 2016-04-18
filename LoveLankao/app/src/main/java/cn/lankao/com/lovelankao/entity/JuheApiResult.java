package cn.lankao.com.lovelankao.entity;

import com.google.gson.JsonElement;

/**
 * Created by BuZhiheng on 2016/4/18.
 */
public class JuheApiResult {
    private String reason;
    private Integer error_code;
    private JsonElement result;
    private JsonElement tngou;//天狗Top新闻

    public JsonElement getTngou() {
        return tngou;
    }

    public void setTngou(JsonElement list) {
        this.tngou = list;
    }

    public String getReason() {
        return reason;
    }


    public void setReason(String reason) {
        this.reason = reason;
    }

    public JsonElement getResult() {
        return result;
    }

    public void setResult(JsonElement result) {
        this.result = result;
    }

    public Integer getError_code() {
        return error_code;
    }

    public void setError_code(Integer error_code) {
        this.error_code = error_code;
    }
}
