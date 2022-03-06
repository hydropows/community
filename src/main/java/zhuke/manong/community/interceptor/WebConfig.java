package zhuke.manong.community.interceptor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
/********************************************************************************
 * Purpose:拦截指定路径地址，校验用户是否登录
 * Author: Caijinbang
 * Created Date:2021-10-25
 * Modify Description:
 * 1.  新增拦截器(Caijinbang 2021-10-25)
 ** *****************************************************************************/
@Configuration
/**
 * EnableWebMvc注解会使页面无法加载css等文件
 * 拦截静态资源
 */
//@EnableWebMvc
public class WebConfig implements WebMvcConfigurer {
    @Autowired
    private SessionInterceptor sessionInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(sessionInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns("/callback/**", "/logout");
    }
}
