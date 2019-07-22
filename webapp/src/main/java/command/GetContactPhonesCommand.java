package command;

import DAO.Impl.DAOPhoneImpl;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class GetContactPhonesCommand implements Command{
    @Override
    public void execute(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        res.setContentType("application/json;charset=UTF-8");
        res.setCharacterEncoding("UTF-8");

        // split url /ContactBook/contacts/{id}/phones into [ContactBook, contacts, 3]
        String[] uriParts = req.getRequestURI().split("/");
        Integer contactId = Integer.parseInt(uriParts[3]);

        PrintWriter out = res.getWriter();
        String json = new ObjectMapper().writeValueAsString(new DAOPhoneImpl().getAllByContact(contactId));
        out.println(json);
    }
}
