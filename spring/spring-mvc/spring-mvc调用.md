### 一次请求的调用栈
- tomcat中的调用链
    ```java
    WsFilter.doFilter(ServletRequest request, ServletResponse response)
    ApplicationFilterChain.doFilter(ServletRequest request, ServletResponse response)
    ApplicationFilterChain.internalDoFilter(ServletRequest request, ServletResponse response)

    ```



- spring中的调用链  
    ```java 
    FrameworkServlet.service(HttpServletRequest request, HttpServletResponse response)
    FrameworkServlet.doGet(HttpServletRequest request, HttpServletResponse response)
    FrameworkServlet.processRequest(HttpServletRequest request, HttpServletResponse response)
    DispatcherServlet.doService(HttpServletRequest request, HttpServletResponse response)
    DispatcherServlet.doDispatch(HttpServletRequest request, HttpServletResponse response)
    AbstractHandlerMethodAdapter.handle(HttpServletRequest request, HttpServletResponse response, Object handler)
    RequestMappingHandlerAdapter.handleInternal(HttpServletRequest request, HttpServletResponse response, HandlerMethod handlerMethod)
    ServletInvocableHandlerMethod.invokeAndHandle(ServletWebRequest webRequest, ModelAndViewContainer mavContainer, Object... providedArgs)
    InvocableHandlerMethod.invokeForRequest(NativeWebRequest request, @Nullable ModelAndViewContainer mavContainer, Object... providedArgs)
    InvocableHandlerMethod.doInvoke(Object... args)
    ```

