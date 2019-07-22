package command;

import DAO.DAOPhone;
import DAO.Impl.DAOPhoneImpl;
import model.Phone;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

public class GetContactPhoneCommand implements Command{
    @Override
    public void execute(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        res.setContentType("application/json;charset=UTF-8");
        res.setCharacterEncoding("UTF-8");

        String[] uriParts = req.getRequestURI().split("/");
        Integer contactId = Integer.parseInt(uriParts[3]);
        Integer phoneId = Integer.parseInt(uriParts[5]);
        PrintWriter out = res.getWriter();
        DAOPhone daoPhone = new DAOPhoneImpl();
        List<Phone> phones = daoPhone.getAllByContact(contactId);
        Phone phone = null;
        if(phoneId < phones.size())
            phone = phones.get(phoneId);
        String json = new ObjectMapper().writeValueAsString(phone);
        out.println(json);
    }
}
