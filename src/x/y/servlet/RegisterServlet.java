package x.y.servlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

public class RegisterServlet extends BaseServlet {
    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Map<String,String[]> maps=request.getParameterMap();
        for (String key:maps.keySet()){
            System.out.println(maps.get(key)[0]);
        }
       response.getWriter().write("琴瑟琵琶");
    }
}
