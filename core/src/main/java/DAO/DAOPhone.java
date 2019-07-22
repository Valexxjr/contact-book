package DAO;

import model.Phone;

import java.util.List;

public interface DAOPhone {
    List<Phone> getAll();
    List<Phone> getAllByContact(Integer contactId);
    boolean insertPhones(Integer contactId, Phone[] phones);
    boolean updatePhones(Phone[] phones);
    boolean deletePhones(Phone[] phones);
}
