package controller.servlets;

import command.Command;
import command.IndexCommand;
import controller.CommandMapper;
import controller.schedule.ScheduleTask;
import org.quartz.SchedulerException;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/*
  With Application Controller, responsibility of FrontController is to decide which Application Controller should process the request
  View Helper selected moved to Application Controller logic
 */

@WebServlet("/ContactBook/*")
@MultipartConfig
public class MainServlet extends HttpServlet {

    CommandMapper commandMapper = new CommandMapper();

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        processRequest(req, res);
    }


    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException  {
        processRequest(req, res);
    }

    @Override
    public void doPut(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException  {
        processRequest(req, res);
    }

    @Override
    public void doDelete(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException  {
        processRequest(req, res);
    }

    private void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException  {
        String uri = request.getRequestURI().replace("/ContactBook", "");
        Command requestProcessor = commandMapper.getRequestProcessor(uri, request.getMethod());
        if (requestProcessor == null)
            requestProcessor = new IndexCommand();
        requestProcessor.execute(request, response);
    }
}
