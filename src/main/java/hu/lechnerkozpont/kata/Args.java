package hu.lechnerkozpont.kata;

import hu.lechnerkozpont.kata.exception.IllegalParametersException;
import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Args {
    private static final String DEFAULT_FILE_NAME = "";
    private static final boolean DEFAULT_LOGGING = false;
    String fileName = DEFAULT_FILE_NAME;
    boolean logging = DEFAULT_LOGGING;
    List<String> parameters = new ArrayList<>();

    public void setParameters(String[] parameters) {
        resetDefaults();
        check(parameters);
        collectUsefulParameters(parameters);
        parseAllParameters();
    }

    private void resetDefaults() {
        parameters = new ArrayList<>();
        setFileName(DEFAULT_FILE_NAME);
        logging = DEFAULT_LOGGING;
    }

    public String getFileName() {
        return fileName;
    }

    public boolean getLogging() {
        return logging;
    }

    private void check(String[] parameters) {
        if(parameters == null)
            throw new IllegalParametersException();
    }

    private void collectUsefulParameters(String[] parameters) {
        for (String parameter: parameters) {
            if(StringUtils.isNotEmpty(parameter)){
                this.parameters.add(parameter);
            }
        }
    }

    private void parseAllParameters() {
        setLoggingByParameters();
        setFileNameByParameters();
    }

    private void setFileNameByParameters() {
        Iterator<String> it = parameters.iterator();
        while (it.hasNext() && fileName.equals("")){
            setFileName(it.next());
        }
    }

    private void setLoggingByParameters(){
        Iterator <String> it = parameters.iterator();
        while (it.hasNext() && !logging){
            String parameter = it.next();
            if("-l".equals(parameter)) {
                setLoggingTrue();
                parameters.remove(parameter);
            }
        }
    }

    private void setLoggingTrue(){
        this.logging = true;
    }

    private void setFileName(String fileName) {
        this.fileName = fileName;
    }
}
