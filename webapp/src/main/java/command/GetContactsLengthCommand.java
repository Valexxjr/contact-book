package command;

import DAO.DAOContact;
import DAO.Impl.DAOContactImpl;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class GetContactsLengthCommand implements Command {

    @Override
    public void execute(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        res.setContentType("text/html;charset=UTF-8");
        res.setCharacterEncoding("UTF-8");
        PrintWriter out = res.getWriter();
        Integer length;
        DAOContact daoContact = new DAOContactImpl();
        length = daoContact.getLength();
        out.println(length);
    }
}
