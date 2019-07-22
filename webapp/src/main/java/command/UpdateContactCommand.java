package command;

import DAO.DAOPhone;
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

public class UpdateContactCommand implements Command {

    private static Logger logger = LogManager.getLogger(UpdateContactCommand.class);

    @Override
    public void execute(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        res.setContentType("application/json;charset=UTF-8");
        res.setCharacterEncoding("UTF-8");
        String uri = req.getRequestURI();
        Integer id = Integer.parseInt(uri.replace("/ContactBook/contacts/", ""));
        ObjectMapper objectMapper = new ObjectMapper();
        Part contactPart = req.getPart("contact");
        Part imagePart = req.getPart("image");
        Part attachmentsInsertPart = req.getPart("attachmentsInsert");
        Part attachmentsUpdatePart = req.getPart("attachmentsUpdate");
        Part attachmentsDeletePart = req.getPart("attachmentsDelete");
        Part phonesInsertPart = req.getPart("phonesInsert");
        Part phonesUpdatePart = req.getPart("phonesUpdate");
        Part phonesDeletePart = req.getPart("phonesDelete");

        Contact contact;

        Attachment[] attachmentsInsert;
        Attachment[] attachmentsUpdate;
        Attachment[] attachmentsDelete;

        Phone[] phonesInsert;
        Phone[] phonesUpdate;
        Phone[] phonesDelete;

        try {
            contact = objectMapper.readValue(contactPart.getInputStream(), Contact.class);

            attachmentsInsert = objectMapper.readValue(attachmentsInsertPart.getInputStream(), Attachment[].class);
            attachmentsUpdate = objectMapper.readValue(attachmentsUpdatePart.getInputStream(), Attachment[].class);
            attachmentsDelete = objectMapper.readValue(attachmentsDeletePart.getInputStream(), Attachment[].class);

            phonesInsert = objectMapper.readValue(phonesInsertPart.getInputStream(), Phone[].class);
            phonesUpdate = objectMapper.readValue(phonesUpdatePart.getInputStream(), Phone[].class);
            phonesDelete = objectMapper.readValue(phonesDeletePart.getInputStream(), Phone[].class);
        } catch (JsonParseException e) {
            logger.error("error while parsing request json");
            res.sendError(400);
            return;
        }

        if (Validation.validateContact(contact)) {

            DAOPhone daoPhone = new DAOPhoneImpl();

            if (phonesInsert.length > 0) {
                daoPhone.insertPhones(contact.getId(), phonesInsert);
            }

            if (phonesUpdate.length > 0) {
                daoPhone.updatePhones(phonesUpdate);
            }

            if (phonesDelete.length > 0) {
                daoPhone.deletePhones(phonesDelete);
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

            if (attachmentsUpdate.length > 0 && daoAttachment.updateAttachments(attachmentsUpdate)) {
                String filesDirectory = "/server/file_storage/contacts/" + contact.getId() + "/attachments";
                Path filesPath = Paths.get(filesDirectory);
                if (!Files.exists(filesPath))
                    Files.createDirectories(filesPath);
                for (Attachment attachment : attachmentsUpdate) {
                    if (attachment.getFilename() != null) {
                        Part filePart = req.getPart("update:" + attachment.getId());
                        Path filePath = Paths.get(filesDirectory + "/" + attachment.getId());
                        if (!Files.exists(filePath))
                            Files.createDirectories(filePath);
                        File file = new File(filePath.toFile(), attachment.getFilename());
                        try (InputStream input = filePart.getInputStream()) {
                            Files.copy(input, file.toPath(), StandardCopyOption.REPLACE_EXISTING);
                        }
                    }
                }
            }

            if (attachmentsDelete.length > 0 && daoAttachment.deleteAttachments(attachmentsDelete)) {
                for (Attachment attachment : attachmentsDelete) {
                    Path filePath = Paths.get("/server/file_storage/contacts/" + contact.getId() +
                            "/attachments/" + attachment.getId() + "/" + attachment.getFilename());
                    try {
                        Files.delete(filePath);
                    } catch (IOException e) {
                        System.out.println("! Error Caught In Deleting File As The File Is Not Present !");
                    }

                }
            }

            if (contact.getImage() != null) {
                Path imagePath = Paths.get("/server/file_storage/contacts/" + id);
                if (!Files.exists(imagePath))
                    Files.createDirectories(imagePath);
                File image = new File(imagePath.toFile(), contact.getImage());
                try (InputStream input = imagePart.getInputStream()) {
                    Files.copy(input, image.toPath(), StandardCopyOption.REPLACE_EXISTING);
                }
            }
            new DAOContactImpl().update(contact);
            res.setStatus(204);
        }
        else {
            res.sendError(400);
            logger.error("contact for update didn't pass the validation");
        }
    }
}
