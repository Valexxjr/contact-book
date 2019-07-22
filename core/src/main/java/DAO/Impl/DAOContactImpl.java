package DAO.Impl;

import DAO.ConnectionManager;
import DAO.DAOContact;
import dto.ContactWrapper;
import model.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.sql.DataSource;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class DAOContactImpl implements DAOContact {

    private static Logger logger = LogManager.getLogger(DAOAttachmentImpl.class);


    DataSource dataSource;

    private static final String GET_CONTACTS_SHORT_SQL = "select id, first_name, last_name, patronymic, birth_date, " +
            "place_of_work, country, city, street, email from contact order by id";
    private static final String GET_CONTACTS_SHORT_SQL_WITHOUT_ORDER = "select id, first_name, last_name, patronymic, birth_date, " +
            "place_of_work, country, city, street, email from contact";
    private static final String GET_CONTACT_BY_ID_SQL = "select id, first_name, last_name, " +
            "patronymic, birth_date, gender, marital_status, citizenship, " +
            "website, email, place_of_work, country, city, street, zip_code, image from contact where id = ?";

    private static final String UPDATE_CONTACT_SQL = "update contact set first_name = ?, last_name = ?, " +
            "patronymic = ?, birth_date = ?, gender = ?, marital_status = ?, citizenship = ?, " +
            "website = ?, email = ?, place_of_work = ?, country = ?, city = ?, street = ?, zip_code = ?, " +
            "image = ? where id = ?";

    private static final String UPDATE_CONTACT_WITHOUT_PHOTO = "update contact set first_name = ?, last_name = ?, " +
            "patronymic = ?, birth_date = ?, gender = ?, marital_status = ?, citizenship = ?, " +
            "website = ?, email = ?, place_of_work = ?, country = ?, city = ?, street = ?, zip_code = ? where id = ?";

    private static final String INSERT_CONTACT_SQL = "insert into contact (first_name, last_name, patronymic, birth_date, " +
            "gender, marital_status, citizenship, website, email, place_of_work, country, city, street, zip_code, " +
            "image) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";

    private static final String GET_CURRENT_AUTO_INCREMENT_SQL = "select auto_increment from information_schema.tables " +
            "where table_schema = 'contacts' and table_name = 'contact'";

    private static final String GET_CONTACTS_NUMBER_SQL = "select count(1) from contact";

    private static final String DELETE_CONTACT_SQL = "delete from contact where id = ?";

    public DAOContactImpl() {
        dataSource = new ConnectionManager().getDataSource();
    }

    public List<ContactWrapper> getAll() {
        Statement statement = null;
        Connection connection = null;
        List<ContactWrapper> contacts = null;
        try {
            connection = dataSource.getConnection();
            statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(GET_CONTACTS_SHORT_SQL);
            contacts = new ArrayList<>();
            while (rs.next()) {
                ContactWrapper contactWrapper = new ContactWrapper();
                contactWrapper.setId(rs.getInt(1));
                contactWrapper.setFirstName(rs.getString(2));
                contactWrapper.setLastName(rs.getString(3));
                contactWrapper.setPatronymic(rs.getString(4));
                Date date = rs.getDate(5);
                contactWrapper.setBirthdayDate((date == null)? null : date.toLocalDate());
                contactWrapper.setPlaceOfWork(rs.getString(6));
                contactWrapper.setCountry(rs.getString(7));
                contactWrapper.setCity(rs.getString(8));
                contactWrapper.setStreet(rs.getString(9));
                contactWrapper.setEmail(rs.getString(10));
                contacts.add(contactWrapper);
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        } finally {
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException se) {
                }
            }
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException se) {
                }
            }
            return contacts;
        }
    }

    public List<ContactWrapper> getAll(Integer page) {
        Statement statement = null;
        Connection connection = null;
        List<ContactWrapper> contacts = null;
        try {
            connection = dataSource.getConnection();
            statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(GET_CONTACTS_SHORT_SQL + " limit " + (page - 1) * 10 + ", 10");
            contacts = new ArrayList<>();
            while (rs.next()) {
                ContactWrapper contactWrapper = new ContactWrapper();
                contactWrapper.setId(rs.getInt(1));
                contactWrapper.setFirstName(rs.getString(2));
                contactWrapper.setLastName(rs.getString(3));
                contactWrapper.setPatronymic(rs.getString(4));
                Date date = rs.getDate(5);
                contactWrapper.setBirthdayDate((date == null)? null : date.toLocalDate());
                contactWrapper.setPlaceOfWork(rs.getString(6));
                contactWrapper.setCountry(rs.getString(7));
                contactWrapper.setCity(rs.getString(8));
                contactWrapper.setStreet(rs.getString(9));
                contactWrapper.setEmail(rs.getString(10));
                contacts.add(contactWrapper);
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        } finally {
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException se) {
                }
            }
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException se) {
                }
            }
            return contacts;
        }
    }

    public Contact get(Integer id) {
        PreparedStatement statement = null;
        Connection connection = null;
        Contact contact = null;
        try {
            connection = dataSource.getConnection();
            statement = connection.prepareStatement(GET_CONTACT_BY_ID_SQL);
            statement.setInt(1, id);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                contact = new Contact();
                contact.setId(rs.getInt(1));
                contact.setFirstName(rs.getString(2));
                contact.setLastName(rs.getString(3));
                contact.setPatronymic(rs.getString(4));
                Date date = rs.getDate(5);
                contact.setBirthdayDate((date == null)? null : date.toLocalDate());
                String gender = rs.getString(6);
                String maritalStatus = rs.getString(7);
                contact.setGender((gender == null) ? null : Gender.valueOf(gender.toUpperCase()));
                contact.setMaritalStatus((maritalStatus == null) ? null : MaritalStatus.valueOf(maritalStatus.toUpperCase()));
                contact.setCitizenship(rs.getString(8));
                contact.setWebsite(rs.getString(9));
                contact.setEmail(rs.getString(10));
                contact.setPlaceOfWork(rs.getString(11));
                contact.setCountry(rs.getString(12));
                contact.setCity(rs.getString(13));
                contact.setStreet(rs.getString(14));
                contact.setZipCode(rs.getString(15));
                contact.setImage(rs.getString(16));
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        } finally {
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException se) {
                }
            }
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException se) {
                }
            }
            return contact;
        }
    }

    public void update(Contact contact) {
        PreparedStatement statement = null;
        Connection connection = null;
        try {
            connection = dataSource.getConnection();
            if(contact.getImage() != null) {
                statement = connection.prepareStatement(UPDATE_CONTACT_SQL);
                statement.setString(15, contact.getImage());
                statement.setInt(16, contact.getId());
            }
            else {
                statement = connection.prepareStatement(UPDATE_CONTACT_WITHOUT_PHOTO);
                statement.setInt(15, contact.getId());
            }
            statement.setString(1, contact.getFirstName());
            statement.setString(2, contact.getLastName());
            statement.setString(3, contact.getPatronymic());
            LocalDate date = contact.getBirthdayDate();
            statement.setDate(4,(date == null)?(null):java.sql.Date.valueOf(date));
            statement.setString(5, (contact.getGender() == null)?(null):(contact.getGender().toString()));
            statement.setString(6, (contact.getMaritalStatus() == null)?(null):(contact.getMaritalStatus().toString()));
            statement.setString(7, contact.getCitizenship());
            statement.setString(8, contact.getWebsite());
            statement.setString(9, contact.getEmail());
            statement.setString(10, contact.getPlaceOfWork());
            statement.setString(11, contact.getCountry());
            statement.setString(12, contact.getCity());
            statement.setString(13, contact.getStreet());
            statement.setString(14, contact.getZipCode());

            statement.executeUpdate();
            logger.info("Contact updated successfully " + contact);

        } catch (SQLException e) {
            System.err.println(e.getMessage());
            logger.error("Error while updating contact " + contact);
        } finally {
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException se) {
                }
            }
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException se) {
                }
            }
        }
    }

    public boolean insertContact(Contact contact) {
        PreparedStatement statement = null;
        Connection connection = null;
        boolean isSuccessful = true;
        try {
            connection = dataSource.getConnection();
            connection.setAutoCommit(false);
            statement = connection.prepareStatement(INSERT_CONTACT_SQL);
            statement.setString(1, contact.getFirstName());
            statement.setString(2, contact.getLastName());
            statement.setString(3, contact.getPatronymic());
            LocalDate date = contact.getBirthdayDate();
            statement.setDate(4,(date == null)?(null):java.sql.Date.valueOf(date));
            statement.setString(5, (contact.getGender() == null)?(null):(contact.getGender().toString()));
            statement.setString(6, (contact.getMaritalStatus() == null)?(null):(contact.getMaritalStatus().toString()));
            statement.setString(7, contact.getCitizenship());
            statement.setString(8, contact.getWebsite());
            statement.setString(9, contact.getEmail());
            statement.setString(10, contact.getPlaceOfWork());
            statement.setString(11, contact.getCountry());
            statement.setString(12, contact.getCity());
            statement.setString(13, contact.getStreet());
            statement.setString(14, contact.getZipCode());
            statement.setString(15, contact.getImage());

            statement.executeUpdate();

            Statement statementGetAutoIncrement = connection.createStatement();
            ResultSet resultSet = statementGetAutoIncrement.executeQuery(GET_CURRENT_AUTO_INCREMENT_SQL);
            if(resultSet.next()) {
                Integer id = resultSet.getInt(1) - 1;
                contact.setId(id);
            }
            connection.commit();
            logger.info("Contact inserted");
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException e1) {
                logger.error("Cannot rollback because connection is already closed");
            }
            isSuccessful = false;
            logger.error("Error while inserting contact - rollback");
        } finally {
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException se) {
                }
            }
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException se) {
                }
            }

        }
        return isSuccessful;
    }

    public boolean deleteContacts(Integer[] ids) {
        PreparedStatement statement = null;
        Connection connection = null;
        boolean isSuccessful = true;
        try {
            connection = dataSource.getConnection();
            connection.setAutoCommit(false);
            for(Integer id: ids) {
                statement = connection.prepareStatement(DELETE_CONTACT_SQL);
                statement.setInt(1, id);
                statement.executeUpdate();
            }

            connection.commit();
            logger.info("Contacts deleted");
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException e1) {
                logger.error("Cannot rollback because connection is already closed");
            }
            isSuccessful = false;
            logger.error("Error while deleting contacts - rollback");
        } finally {
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException se) {
                }
            }
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException se) {
                }
            }

        }
        return isSuccessful;
    }


    public List<ContactWrapper> search(SearchParameters searchParameters, Integer page) {
        Statement statement = null;
        Connection connection = null;
        List<ContactWrapper> contacts = null;
        try {
            connection = dataSource.getConnection();
            statement = connection.createStatement();
            StringBuilder query = new StringBuilder(GET_CONTACTS_SHORT_SQL_WITHOUT_ORDER);

            List<String> conditions = new ArrayList<>();
            if(searchParameters.getFirstName() != null) {
                conditions.add(" first_name like '%" + searchParameters.getFirstName() + "%' ");
            }
            if(searchParameters.getLastName() != null) {
                conditions.add(" last_name like '%" + searchParameters.getLastName() + "%' ");
            }
            if(searchParameters.getPatronymic() != null) {
                conditions.add(" patronymic like '%" + searchParameters.getPatronymic() + "%' ");
            }
            if(searchParameters.getDateFrom() != null) {
                conditions.add(" birth_date >= '" + searchParameters.getDateFrom().toString() + "' ");
            }
            if(searchParameters.getDateTo() != null) {
                conditions.add(" birth_date <= '" + searchParameters.getDateTo().toString() + "' ");
            }
            if(searchParameters.getGender() != null) {
                conditions.add(" gender = '" + searchParameters.getGender().toString().toLowerCase() + "' ");
            }
            if(searchParameters.getMaritalStatus() != null) {
                conditions.add(" marital_status = '" + searchParameters.getMaritalStatus().toString().toLowerCase() + "' ");
            }
            if(searchParameters.getCitizenship() != null) {
                conditions.add(" citizenship like '%" + searchParameters.getCitizenship() + "%' ");
            }
            if(searchParameters.getCountry() != null) {
                conditions.add(" country like '%" + searchParameters.getCountry() + "%' ");
            }
            if(searchParameters.getCity() != null) {
                conditions.add(" city like '%" + searchParameters.getCity() + "%' ");
            }
            if(searchParameters.getStreet() != null) {
                conditions.add(" street like '%" + searchParameters.getStreet() + "%' ");
            }
            if(searchParameters.getZipCode() != null) {
                conditions.add(" zip_code like '%" + searchParameters.getZipCode() + "%' ");
            }
            String searchQuery;
            if(!conditions.isEmpty()) {
                query.append(" where ");
                for(String condition: conditions) {
                    query.append(condition).append("and");
                }
                searchQuery = query.substring(0, query.length() - 3);
            } else
                searchQuery = query.toString();
            ResultSet rs = statement.executeQuery(searchQuery  + " order by id limit " + (page - 1) * 10 + ", 10");
            contacts = new ArrayList<ContactWrapper>();
            while (rs.next()) {
                ContactWrapper contactWrapper = new ContactWrapper();
                contactWrapper.setId(rs.getInt(1));
                contactWrapper.setFirstName(rs.getString(2));
                contactWrapper.setLastName(rs.getString(3));
                contactWrapper.setPatronymic(rs.getString(4));
                Date date = rs.getDate(5);
                contactWrapper.setBirthdayDate((date == null)? null : date.toLocalDate());
                contactWrapper.setPlaceOfWork(rs.getString(6));
                contactWrapper.setCountry(rs.getString(7));
                contactWrapper.setCity(rs.getString(8));
                contactWrapper.setStreet(rs.getString(9));
                contactWrapper.setEmail(rs.getString(10));
                contacts.add(contactWrapper);
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        } finally {
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException se) {
                }
            }
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException se) {
                }
            }
            return contacts;
        }
    }

    public Integer getSearchLength(SearchParameters searchParameters) {
        Statement statement = null;
        Connection connection = null;
        Integer length = null;
        try {
            connection = dataSource.getConnection();
            statement = connection.createStatement();
            StringBuilder query = new StringBuilder(GET_CONTACTS_NUMBER_SQL);

            List<String> conditions = new ArrayList<>();
            if(searchParameters.getFirstName() != null) {
                conditions.add(" first_name like '%" + searchParameters.getFirstName() + "%' ");
            }
            if(searchParameters.getLastName() != null) {
                conditions.add(" last_name like '%" + searchParameters.getLastName() + "%' ");
            }
            if(searchParameters.getPatronymic() != null) {
                conditions.add(" patronymic like '%" + searchParameters.getPatronymic() + "%' ");
            }
            if(searchParameters.getDateFrom() != null) {
                conditions.add(" birth_date >= '" + searchParameters.getDateFrom().toString() + "' ");
            }
            if(searchParameters.getDateTo() != null) {
                conditions.add(" birth_date <= '" + searchParameters.getDateTo().toString() + "' ");
            }
            if(searchParameters.getGender() != null) {
                conditions.add(" gender = '" + searchParameters.getGender().toString().toLowerCase() + "' ");
            }
            if(searchParameters.getMaritalStatus() != null) {
                conditions.add(" marital_status = '" + searchParameters.getMaritalStatus().toString().toLowerCase() + "' ");
            }
            if(searchParameters.getCitizenship() != null) {
                conditions.add(" citizenship like '%" + searchParameters.getCitizenship() + "%' ");
            }
            if(searchParameters.getCountry() != null) {
                conditions.add(" country like '%" + searchParameters.getCountry() + "%' ");
            }
            if(searchParameters.getCity() != null) {
                conditions.add(" city like '%" + searchParameters.getCity() + "%' ");
            }
            if(searchParameters.getStreet() != null) {
                conditions.add(" street like '%" + searchParameters.getStreet() + "%' ");
            }
            if(searchParameters.getZipCode() != null) {
                conditions.add(" zip_code like '%" + searchParameters.getZipCode() + "%' ");
            }

            String searchQuery;
            if(!conditions.isEmpty()) {
                query.append(" where ");
                for(String condition: conditions) {
                    query.append(condition).append("and");
                }
                searchQuery = query.substring(0, query.length() - 3);
            } else
                searchQuery = query.toString();
            ResultSet rs = statement.executeQuery(searchQuery);
            if (rs.next()) {
                length = rs.getInt(1);
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        } finally {
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException se) {
                }
            }
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException se) {
                }
            }
            return length;
        }
    }

    public Integer getLength() {
        Statement statement = null;
        Connection connection = null;
        Integer length = null;
        try {
            connection = dataSource.getConnection();
            statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(GET_CONTACTS_NUMBER_SQL);
            if (rs.next()) {
                length = rs.getInt(1);
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        } finally {
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException se) {
                }
            }
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException se) {
                }
            }
        }
        return length;
    }

}
