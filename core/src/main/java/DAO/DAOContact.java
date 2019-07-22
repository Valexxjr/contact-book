package DAO;

import dto.ContactWrapper;
import model.Contact;

import java.util.List;

public interface DAOContact {
    List<ContactWrapper> getAll();
    List<ContactWrapper> getAll(Integer page);
    Contact get(Integer id);
    void update(Contact contact);
    boolean insertContact(Contact contact);
    boolean deleteContacts(Integer[] ids);

    Integer getLength();
}
