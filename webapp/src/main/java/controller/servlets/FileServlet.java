package controller.servlets;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.net.URLDecoder;
import java.nio.file.Files;

@WebServlet("/files/*")
public class FileServlet extends HttpServlet {

    private static Logger logger = LogManager.getLogger(FileServlet.class);

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String filename = URLDecoder.decode(request.getPathInfo().substring(1), "UTF-8");
        logger.info("getting file named " + filename);
        File file = new File("/server/file_storage/", filename);

        File requestedFile = file;
        if (file.isDirectory()) {
            if(file.listFiles() == null) {
                response.sendError(404);
                return;
            }
            requestedFile = file.listFiles()[0];
        }
        if(requestedFile != null) {
            response.setHeader("Content-Type", getServletContext().getMimeType(requestedFile.getName()));
            response.setHeader("Content-Length", String.valueOf(requestedFile.length()));
            response.setHeader("Content-Disposition", "inline; filename=\"" + requestedFile.getName() + "\"");
            Files.copy(requestedFile.toPath(), response.getOutputStream());
        }
        else {
            response.sendError(404);
        }
    }

}
