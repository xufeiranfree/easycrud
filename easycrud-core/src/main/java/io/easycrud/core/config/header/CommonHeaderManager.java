package io.easycrud.core.config.header;

public class CommonHeaderManager {
    private static final ThreadLocal<CommonHeaders> THREAD_LOCAL = new InheritableThreadLocal<>();

    public static void setCommonHeaders(CommonHeaders commonHeaders) {
        THREAD_LOCAL.set(commonHeaders);
    }

    public static CommonHeaders getCommonHeaders() {
        return THREAD_LOCAL.get();
    }

    public static void removeCommonHeaders() {
        THREAD_LOCAL.remove();
    }
}
