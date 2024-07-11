package io.easycrud.core.config.interceptor;

import io.easycrud.core.config.header.CommonHeaderManager;
import io.easycrud.core.config.header.CommonHeaders;
import io.micrometer.common.util.StringUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

@Slf4j
public class CommonHeadersInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
//        String userId = request.getHeader("X-User-Id");
        String userId = request.getParameter("user-id");
        if (request.getRequestURI().startsWith(request.getContextPath() + "/oa2-api")) {
            if (StringUtils.isBlank(userId)) {
                throw new RuntimeException("no user id");
            }
        }
        CommonHeaderManager.setCommonHeaders(CommonHeaders.builder()
                .userId(userId)
                .build());
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        // 在 controller 方法处理后，视图渲染前执行
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        // 在请求处理完成后执行，即视图渲染完成后执行
        // 清理 CommonHeaderManager 中的内容
        CommonHeaderManager.removeCommonHeaders();
    }
}
