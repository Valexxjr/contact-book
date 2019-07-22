package DAO;

import model.Attachment;

import java.util.List;

public interface DAOAttachment {
    List<Attachment> getAll();
    List<Attachment> getAllByContact(Integer contactId);
    boolean insertAttachments(Integer contactId, Attachment[] phones);
    boolean updateAttachments(Attachment[] phones);
    boolean deleteAttachments(Attachment[] phones);
}
