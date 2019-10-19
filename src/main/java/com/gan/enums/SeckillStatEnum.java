package com.gan.enums;


/**
 * @ClassName SeckillStatEnum
 * @Author Jason
 * @Description //常量信息枚举类
 * @Date 10:12 2019/10/19
 * @Version 1.0
 */
public enum SeckillStatEnum {
    //括号里是调用构造函数的，由编译器调用，无法人工调用
    SUCCESS(1,"秒杀成功"),
    END(0,"秒杀结束"),
    REPEAT_KILL(-1,"重复秒杀"),
    INNER_ERROR(-2,"系统异常"),
    DATE_REWRITE(-3,"重复秒杀");
    

    private int state;
    private String info;

    SeckillStatEnum(int state, String info) {
        this.state = state;
        this.info = info;
    }

    public int getState() {
        return state;
    }

    public String getInfo() {
        return info;
    }

    public static SeckillStatEnum stateOf(int index){
        //values()是继承于Enum类的一个静态方法，返回一个数组，装的是该枚举类型的所有值
        for(SeckillStatEnum state:values()){
            if(state.getState()==index)
                return state;
        }
        return null;
    }
}
