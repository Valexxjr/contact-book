package command;

import DAO.Impl.DAOAttachmentImpl;
import DAO.Impl.DAOContactImpl;
import DAO.Impl.DAOPhoneImpl;
import com.fasterxml.jackson.core.JsonParseException;
import controller.Validation;
import model.Attachment;
import model.Contact;
import com.fasterxml.jackson.databind.ObjectMapper;
import model.Phone;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class CreateContactCommand implements Command {

    private static Logger logger = LogManager.getLogger(CreateContactCommand.class);

    @Override
    public void execute(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        res.setContentType("application/json;charset=UTF-8");
        res.setCharacterEncoding("UTF-8");
        ObjectMapper objectMapper = new ObjectMapper();
        Part contactPart = req.getPart("contact");
        Part imagePart = req.getPart("image");
        Part attachmentsInsertPart = req.getPart("attachmentsInsert");
        Part phonesInsertPart = req.getPart("phonesInsert");

        Contact contact;
        Attachment[] attachmentsInsert;
        Phone[] phonesInsert;

        try {
            contact = objectMapper.readValue(contactPart.getInputStream(), Contact.class);
            attachmentsInsert = objectMapper.readValue(attachmentsInsertPart.getInputStream(), Attachment[].class);
            phonesInsert = objectMapper.readValue(phonesInsertPart.getInputStream(), Phone[].class);
        } catch (JsonParseException e) {
            logger.error("error while parsing request json");
            res.sendError(400);
            return;
        }

        if(Validation.validateContact(contact)) {
            DAOContactImpl daoContact = new DAOContactImpl();

            if (daoContact.insertContact(contact)) {

                DAOPhoneImpl daoPhone = new DAOPhoneImpl();

                if (phonesInsert.length > 0) {
                    daoPhone.insertPhones(contact.getId(), phonesInsert);
                }

                DAOAttachmentImpl daoAttachment = new DAOAttachmentImpl();

                int ids = 1;

                if (attachmentsInsert.length > 0 && daoAttachment.insertAttachments(contact.getId(), attachmentsInsert)) {
                    String filesDirectory = "/server/file_storage/contacts/" + contact.getId() + "/attachments";
                    Path filesPath = Paths.get(filesDirectory);
                    if (!Files.exists(filesPath))
                        Files.createDirectories(filesPath);
                    for (Attachment attachment : attachmentsInsert) {
                        Part filePart = req.getPart("insert:" + ids++);
                        Path filePath = Paths.get(filesDirectory + "/" + attachment.getId());
                        if (!Files.exists(filePath))
                            Files.createDirectories(filePath);
                        File file = new File(filePath.toFile(), attachment.getFilename());
                        try (InputStream input = filePart.getInputStream()) {
                            Files.copy(input, file.toPath(), StandardCopyOption.REPLACE_EXISTING);
                        }

                    }
                }


                if (contact.getImage() != null) {
                    Path imagePath = Paths.get("/server/file_storage/contacts/" + contact.getId());
                    if (!Files.exists(imagePath))
                        Files.createDirectories(imagePath);
                    File image = new File(imagePath.toFile(), contact.getImage());
                    try (InputStream input = imagePart.getInputStream()) {
                        Files.copy(input, image.toPath(), StandardCopyOption.REPLACE_EXISTING);
                    }
                }
                res.setStatus(201);
            } else {
                res.sendError(200);
            }
        }
        else {
            res.sendError(400);
            logger.error("contact for update didn't pass the validation");
        }
    }
}
