package command;

import DAO.Impl.DAOContactImpl;
import model.SearchParameters;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import com.fasterxml.jackson.databind.ObjectMapper;


public class SearchCommand implements Command {

    @Override
    public void execute(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        res.setContentType("application/json;charset=UTF-8");
        res.setCharacterEncoding("UTF-8");

        SearchParameters searchParameters = new ObjectMapper().readValue(req.getInputStream(), SearchParameters.class);
        Integer page = Integer.parseInt(req.getParameter("page"));
        String json = new ObjectMapper().writeValueAsString(new DAOContactImpl().search(searchParameters, page));
        res.getWriter().println(json);
    }
}
