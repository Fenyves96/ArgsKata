package hu.lechnerkozpont.kata;

import hu.lechnerkozpont.kata.exception.IllegalParametersException;
import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Args {
    String fileName = "";
    boolean logging = false;
    List<String> parameters = new ArrayList<>();

    public void setParameters(String[] parameters) {
        clearParameters();
        check(parameters);
        collectUsefulParameters(parameters);
        parseAllParameters();
    }

    private void clearParameters() {
        parameters = new ArrayList<>();
        setFileName("");
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
        if(parameters.stream().anyMatch(p -> p.equals("-l"))){
            parameters.remove("-l");
            setLoggingTrue();
        }
    }

    private void setLoggingTrue(){
        this.logging = true;
    }

    private void setFileName(String fileName) {
        this.fileName = fileName;
    }
}
