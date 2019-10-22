package com.gan.dto;

/**
 * @ClassName SeckillResult
 * @Author Jason
 * @Description //TODO 将所有的Ajax请求返回类型，全部封装成json数据
 * @Date 21:45 2019/10/19
 * @Version 1.0
 *
 * 用模板是为了既封装数据，又附加一些额外的信息
 */
public class SeckillResult<T> {


    private boolean success;

    private T data;
    private String error;


    public SeckillResult(boolean success,T data){
        this.success=success;
        this.data=data;
    }

    public SeckillResult(boolean success, String error) {
        this.success = success;
        this.error = error;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
