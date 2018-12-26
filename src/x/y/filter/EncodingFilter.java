package x.y.filter;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class EncodingFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest= (HttpServletRequest) servletRequest;
        HttpServletRequest request=new EncodingRequestWrapper(httpServletRequest);
        filterChain.doFilter(request,servletResponse);
    }

    @Override
    public void destroy() {

    }

    private class EncodingRequestWrapper extends HttpServletRequestWrapper{
        private HttpServletRequest request;
        public EncodingRequestWrapper(HttpServletRequest request) {
            super(request);
            this.request=request;
        }

        @Override
        public Map<String, String[]> getParameterMap() {
            String method=request.getMethod();
            ServletContext context=getServletContext();
            int serverVersion=Integer.parseInt(context.getServerInfo().split("/")[1].split("\\.")[0]);
            if (method.equals("GET")){
                if (serverVersion>=8){
                    return request.getParameterMap();
                }else{
                    Map<String,String[]> parameterMap=request.getParameterMap();
                    Map<String,String[]> resultMap=new HashMap<>();
                    for (String parameterName:parameterMap.keySet()){
                        String[] values=parameterMap.get(parameterName);
                        String[] resultValues=new String[values.length];
                        if (values!=null){
                            for (int i=0;i<values.length;i++){
                                try {
                                    resultValues[i]=new String(values[i].getBytes("iso-8859-1"),"utf-8");
                                } catch (UnsupportedEncodingException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                        resultMap.put(parameterName,resultValues);
                    }
                    return resultMap;
                }
            }else if (method.equals("POST")){
                try {
                    request.setCharacterEncoding("utf-8");
                    return request.getParameterMap();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
            return request.getParameterMap();

        }

        @Override
        public String getParameter(String name) {
            Map<String,String[]> parameterMap=getParameterMap();
            String[] values=parameterMap.get(name);
            if (values!=null){
                return values[0];
            }
            return null;
        }

        @Override
        public String[] getParameterValues(String name) {
            Map<String, String[]> parameterMap = getParameterMap();
            String[] values = parameterMap.get(name);
            if (values!=null){
                return values;
            }
            return  null;
        }
    }
}




