package command;

import DAO.Impl.DAOContactImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import model.SearchParameters;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class GetSearchLengthCommand implements Command {

    @Override
    public void execute(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        res.setContentType("text/html;charset=UTF-8");
        res.setCharacterEncoding("UTF-8");

        SearchParameters searchParameters = new ObjectMapper().readValue(req.getInputStream(), SearchParameters.class);

        PrintWriter out = res.getWriter();
        out.println(new DAOContactImpl().getSearchLength(searchParameters));
    }
}
