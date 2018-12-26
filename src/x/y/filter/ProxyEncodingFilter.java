package x.y.filter;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Map;

public class ProxyEncodingFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest= (HttpServletRequest) servletRequest;
        HttpServletRequest request=new EncodingProxy(httpServletRequest,httpServletRequest.getServletContext()).getProxy();
        filterChain.doFilter(request,servletResponse);

    }

    @Override
    public void destroy() {

    }

    private class EncodingProxy {
        private HttpServletRequest request;
        private ServletContext context;

        public EncodingProxy(HttpServletRequest request, ServletContext context) {
            this.request = request;
            this.context = context;
        }

        public HttpServletRequest getProxy() {
            return (HttpServletRequest) Proxy.newProxyInstance(EncodingProxy.class.getClassLoader(), request.getClass().getInterfaces(), new InvocationHandler() {
                @Override
                public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                    String m=request.getMethod();
                    int serverVersion=Integer.parseInt(context.getServerInfo().split("/")[1].split("\\.")[0]);
                    if (method.getName().equals("getParameterMap")){
                        if (m.equals("GET")){
                            if (serverVersion>=8){
                                return method.invoke(request,args);
                            }else{
                                Map<String,String[]> parameterMap=request.getParameterMap();
                                for (String parameterName:parameterMap.keySet()){
                                    String[] values=parameterMap.get(parameterName);
                                    if (values!=null){
                                        for (int i=0;i<values.length;i++){
                                            try {
                                                values[i]=new String(values[i].getBytes("iso-8859-1"),"utf-8");
                                            } catch (UnsupportedEncodingException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    }
                                }
                                return parameterMap;
                            }
                        }else if (m.equals("POST")){
                            request.setCharacterEncoding("utf-8");
                            return method.invoke(request,args);
                        }
                    }else if (method.getName().equals("getParameter")){
                        if (m.equals("GET")){
                            if (serverVersion>=8.0){
                                return method.invoke(request,args);
                            }else{
                                String value=request.getParameter((String) args[0]);
                                return new String(value.getBytes("iso-8859-1"),"utf-8");
                            }
                        }else if (m.equals("POST")){
                            request.setCharacterEncoding("utf-8");
                            return method.invoke(request,args);
                        }

                    }else if (method.getName().equals("getParameterValues")){
                        if (m.equals("GET")){
                            if (serverVersion>=8.0){
                                return method.invoke(request,args);
                            }else{
                                String[] values=request.getParameterValues((String) args[0]);
                                for (int i=0;i<values.length;i++){
                                    values[i]=new String(values[i].getBytes("iso-8859-1"),"utf-8");
                                }
                                return values;
                            }
                        }else if (m.equals("POST")){
                            request.setCharacterEncoding("utf-8");
                            return method.invoke(request,args);
                        }
                    }
                    return method.invoke(request,args);
                }
            });
        }
    }
}