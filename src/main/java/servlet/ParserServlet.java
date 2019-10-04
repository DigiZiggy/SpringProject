package servlet;

import servlet.util.FileUtil;

import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(name = "Parser", urlPatterns = "/api/parser")
public class ParserServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String string = FileUtil.readStream(request.getInputStream());

        //find all elements in " "
        Pattern pattern = Pattern.compile("\"(.*?)\"");
        Matcher matcher = pattern.matcher(string);
        int groupCount = matcher.groupCount();

        //add all those elements into list
        List<String> listOfAllElements = new ArrayList<>();
        while (matcher.find()) {
            for (int i = 0; i <= groupCount; i=i+2) {
                listOfAllElements.add(matcher.group(i));
            }
        }

        //build a json response to the request with reverted elements
        StringBuilder reversedPostReq = new StringBuilder();
        reversedPostReq.append("{");
        for (int i = 0; i <= listOfAllElements.size()-2; i=i+2) {
            reversedPostReq.append(new StringBuilder(listOfAllElements.get(i)).reverse().toString());
            reversedPostReq.append(":");
            reversedPostReq.append(new StringBuilder(listOfAllElements.get(i+1)).reverse().toString());
            reversedPostReq.append(",");
        }
        reversedPostReq.deleteCharAt(reversedPostReq.lastIndexOf(","));
        reversedPostReq.append("}");

        response.setHeader("Content-Type", "application/json");
        response.getWriter().print(reversedPostReq.toString());
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.getWriter().println("Parser!");
    }
}
