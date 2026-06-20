package com.media.common.context;
// 上下文包，存放请求上下文相关类

public class BaseContext {

    // 1. 核心：ThreadLocal 变量（静态，全局唯一）
    public static ThreadLocal<Long> threadLocal = new ThreadLocal<>();

    // 2. 存储当前用户ID
    public static void setCurrentId(Long id) {
        threadLocal.set(id);
    }

    // 3. 获取当前用户ID
    public static Long getCurrentId() {
        return threadLocal.get();
    }

    // 4. 清理当前用户ID（防止内存泄漏）
    public static void removeCurrentId() {
        threadLocal.remove();
    }
}