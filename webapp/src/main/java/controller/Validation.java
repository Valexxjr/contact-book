package controller;

import model.Contact;

public class Validation {
    public static boolean validateContact(Contact contact) {
        if (contact.getFirstName() == null || contact.getFirstName().isEmpty() ||
                contact.getFirstName().length() > 50)
            return false;
        if (contact.getLastName() == null || contact.getLastName().isEmpty() ||
                contact.getLastName().length() > 50)
            return false;

        if(contact.getPatronymic() != null) {
            if(contact.getPatronymic().isEmpty() || contact.getPatronymic().length() > 50)
                return false;
        }

        if(contact.getCitizenship() != null && contact.getCitizenship().isEmpty())
            return false;
        if(contact.getWebsite() != null && contact.getWebsite().isEmpty())
            return false;
        if(contact.getEmail() != null && contact.getEmail().isEmpty())
            return false;
        if(contact.getPlaceOfWork() != null && contact.getPlaceOfWork().isEmpty())
            return false;
        if(contact.getCountry() != null && contact.getCountry().isEmpty())
            return false;
        if(contact.getCity() != null && contact.getCity().isEmpty())
            return false;
        if(contact.getStreet() != null && contact.getStreet().isEmpty())
            return false;
        if(contact.getZipCode() != null && contact.getZipCode().isEmpty())
            return false;

        return true;

    }
}
