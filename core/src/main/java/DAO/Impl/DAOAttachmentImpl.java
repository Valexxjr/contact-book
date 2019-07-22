package DAO.Impl;

import DAO.ConnectionManager;
import DAO.DAOAttachment;
import model.Attachment;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DAOAttachmentImpl implements DAOAttachment {

    private static Logger logger = LogManager.getLogger(DAOAttachmentImpl.class);

    DataSource dataSource;

    private static final String GET_ATTACHMENTS_SQL = "select id, file_name, upload_date," +
            " note from attachment";

    private static final String GET_ATTACHMENTS_BY_CONTACT_SQL = "select id, file_name, upload_date," +
            " note from attachment where contact_id = ?";

    private static final String INSERT_ATTACHMENT_SQL = "insert into attachment " +
            "(contact_id, file_name, upload_date, note) values (?, ?, ?, ?)";

    private static final String GET_CURRENT_AUTO_INCREMENT_SQL = "select auto_increment from information_schema.tables " +
            "where table_schema = 'contacts' and table_name = 'attachment'";

    private static final String UPDATE_ATTACHMENT_NOTE_SQL = "update attachment set note = ? where id = ?";

    private static final String UPDATE_ATTACHMENT_SQL = "update attachment set file_name = ?," +
            " upload_date = ?, note = ? where id = ?";

    private static final String DELETE_ATTACHMENT_SQL = "delete from attachment where id = ?";



    public DAOAttachmentImpl() {
        dataSource = new ConnectionManager().getDataSource();
    }

    public List<Attachment> getAll() {
        Statement statement = null;
        Connection connection = null;
        List<Attachment> attachments = null;
        try {
            connection = dataSource.getConnection();
            statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(GET_ATTACHMENTS_SQL);
            attachments = new ArrayList<Attachment>();
            while (rs.next()) {
                Attachment attachment = new Attachment();
                attachment.setId(rs.getInt(1));
                attachment.setFilename(rs.getString(2));
                attachment.setUploadDate(rs.getDate(3).toLocalDate());
                attachment.setNote(rs.getString(4));
                attachments.add(attachment);
            }
        } catch (SQLException e) {
            logger.error("error while getting attachments");
        } finally {
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException se) {
                    logger.error("cannot close statement");
                }
            }
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException se) {
                    logger.error("cannot close connection");
                }
            }
            logger.info("Get all attachments");
            return attachments;
        }
    }

    public List<Attachment> getAllByContact(Integer contactId) {
        PreparedStatement statement = null;
        Connection connection = null;
        List<Attachment> attachments = null;
        try {
            connection = dataSource.getConnection();
            statement = connection.prepareStatement(GET_ATTACHMENTS_BY_CONTACT_SQL);
            statement.setInt(1, contactId);
            ResultSet rs = statement.executeQuery();
            attachments = new ArrayList<Attachment>();
            while (rs.next()) {
                Attachment attachment = new Attachment();
                attachment.setId(rs.getInt(1));
                attachment.setFilename(rs.getString(2));
                attachment.setUploadDate(rs.getDate(3).toLocalDate());
                attachment.setNote(rs.getString(4));
                attachments.add(attachment);
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
            return attachments;
        }
    }

    public boolean insertAttachments(Integer contactId, Attachment[] attachments) {
        PreparedStatement statement = null;
        Connection connection = null;
        boolean isSuccessful = true;
        try {
            connection = dataSource.getConnection();
            connection.setAutoCommit(false);
            for(Attachment attachment: attachments) {
                statement = connection.prepareStatement(INSERT_ATTACHMENT_SQL);
                statement.setInt(1, contactId);
                statement.setString(2, attachment.getFilename());
                statement.setDate(3, Date.valueOf(attachment.getUploadDate()));
                statement.setString(4, attachment.getNote());
                statement.executeUpdate();

                Statement statementGetAutoIncrement = connection.createStatement();
                ResultSet resultSet = statementGetAutoIncrement.executeQuery(GET_CURRENT_AUTO_INCREMENT_SQL);
                if(resultSet.next()) {
                    Integer id = resultSet.getInt(1) - 1;
                    attachment.setId(id);
                }
            }
            connection.commit();
            logger.info("Attachments inserted");
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException e1) {
                logger.error("Cannot rollback because connection is already closed");
            }
            isSuccessful = false;
            logger.error("Error while inserting attachments - rollback");
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

    public boolean updateAttachments(Attachment[] attachments) {
        PreparedStatement statement = null;
        Connection connection = null;
        boolean isSuccessful = true;
        try {
            connection = dataSource.getConnection();
            connection.setAutoCommit(false);
            for(Attachment attachment: attachments) {
                if (attachment.getFilename() == null) {
                    statement = connection.prepareStatement(UPDATE_ATTACHMENT_NOTE_SQL);
                    statement.setString(1, attachment.getNote());
                    statement.setInt(2, attachment.getId());
                } else {
                    statement = connection.prepareStatement(UPDATE_ATTACHMENT_SQL);
                    statement.setString(1, attachment.getFilename());
                    statement.setDate(2, Date.valueOf(attachment.getUploadDate()));
                    statement.setString(3, attachment.getNote());
                    statement.setInt(4, attachment.getId());
                }
                statement.executeUpdate();
            }

            connection.commit();
            logger.info("Attachments updated");
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException e1) {
                logger.error("Cannot rollback because connection is already closed");
            }
            isSuccessful = false;
            logger.error("Error while updating attachments - rollback");
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

    public boolean deleteAttachments(Attachment[] attachments) {
        PreparedStatement statement = null;
        Connection connection = null;
        boolean isSuccessful = true;
        try {
            connection = dataSource.getConnection();
            connection.setAutoCommit(false);
            for(Attachment attachment: attachments) {
                statement = connection.prepareStatement(DELETE_ATTACHMENT_SQL);
                statement.setInt(1, attachment.getId());
                statement.executeUpdate();
            }

            connection.commit();
            logger.info("Attachments deleted");
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException e1) {
                logger.error("Cannot rollback because connection is already closed");
            }
            isSuccessful = false;
            logger.error("Error while deleting attachments - rollback");
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
}
