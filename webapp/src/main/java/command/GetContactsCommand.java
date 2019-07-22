package command;

import DAO.Impl.DAOContactImpl;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class GetContactsCommand implements Command {
    @Override
    public void execute(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        res.setContentType("application/json;charset=UTF-8");
        res.setCharacterEncoding("UTF-8");
        PrintWriter out = res.getWriter();
        Integer page = Integer.parseInt(req.getParameter("page"));
        String json = new ObjectMapper().writeValueAsString(new DAOContactImpl().getAll(page));
        out.println(json);
    }
}
