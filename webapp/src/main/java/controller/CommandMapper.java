package controller;

import command.*;

import java.util.HashMap;
import java.util.Map;

public class CommandMapper  {
    private  Map<String, Class> processorsGet = new HashMap<String, Class>();

    private  Map<String, Class> processorsPut = new HashMap<String, Class>();

    private  Map<String, Class> processorsPost = new HashMap<String, Class>();

    private  Map<String, Class> processorsDelete = new HashMap<String, Class>();

    {
        processorsGet.put("\\/contacts$", GetContactsCommand.class);
        processorsGet.put("\\/contacts\\/\\d+$", GetContactCommand.class);
        processorsGet.put("\\/contacts\\/\\d+\\/phones$", GetContactPhonesCommand.class);
        processorsGet.put("\\/contacts\\/length", GetContactsLengthCommand.class);
        processorsGet.put("\\/contacts\\/\\d+\\/attachments$", GetContactAttachmentsCommand.class);
        processorsGet.put("\\/contacts\\/\\d+\\/phones\\/\\d+$", GetContactPhoneCommand.class);

        processorsPut.put("\\/contacts\\/\\d+$", UpdateContactCommand.class);

        processorsPost.put("\\/contacts$", CreateContactCommand.class);
        processorsPost.put("\\/search$", SearchCommand.class);
        processorsPost.put("\\/search\\/length$", GetSearchLengthCommand.class);
        processorsPost.put("\\/email$", SendEmailCommand.class);

        processorsDelete.put("\\/contacts$", DeleteContactsCommand.class);
    }

    protected Map<String, Class> getProcessorsGet() {
        return processorsGet;
    }

    public Command getRequestProcessor(String command, String method) {
        Map<String, Class> processors = null;
        switch (method){
            case "GET": {
                processors = processorsGet;
                break;
            }
            case "PUT": {
                processors = processorsPut;
                break;
            }
            case "POST": {
                processors = processorsPost;
                break;
            }
            case "DELETE": {
                processors = processorsDelete;
                break;
            }
        }

        Class processorClass = null;

        for(String processor : processors.keySet()) {
            if (command.matches(processor)) {
                processorClass = processors.get(processor);
                break;
            }
        }
        if(processorClass != null) {
            try {
                return (Command)processorClass.newInstance();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
