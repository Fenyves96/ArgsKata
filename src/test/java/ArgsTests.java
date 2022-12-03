import hu.lechnerkozpont.kata.Args;
import hu.lechnerkozpont.kata.exception.IllegalParametersException;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;


public class ArgsTests {

    private Args args;

    @Before
    public void setUp() throws Exception {
        args = new Args();
    }

    @Test(expected = IllegalParametersException.class)
    public void testSetParametersIfParametersIsNullThenIllegalParametersException(){
        args.setParameters(null);
    }

    @Test
    public void testGetFileNameIfNullFileNameThenEmptyString(){
        assertFileParameter("", new String[] {null});
    }

    @Test
    public void testGetFileNameIfEmptyStringThenEmptyString(){
        assertFileParameter("", new String[] {""});
    }

    @Test
    public void testGetFileNameIfNoInputThenEmptyString(){
        assertFileParameter("", new String[] {});
    }

    @Test
    public void testGetFileNameIfFileNameGivenThenGivenFileName(){
        assertFileParameter("validFileName.txt",
                new String[] {"validFileName.txt"});
    }

    @Test
    public void testGetFileNameIfNotFirstThenGivenFileName(){
        assertFileParameter("validFileName.txt",
                new String[] {"", "validFileName.txt"});
    }

    @Test
    public void testGetFileNameIfFileNameHasNoExtensionThenFileName(){
        assertFileParameter("validFileNameWithoutExtension",
                new String[] {"validFileNameWithoutExtension"});
    }

    @Test
    public void testGetFileNameIfFileNameLikeFlagThenFileName(){
        assertFileParameter("-d", new String[] {"-d"});
    }

    @Test
    public void testSetFileNameWhenDoubleCallThenSecondFileName(){
        args.setParameters(new String[] {"filename1.txt"});
        assertEquals("filename1.txt", args.getFileName());
        args.setParameters(new String[] {"filename2.txt"});
        assertEquals("filename2.txt", args.getFileName());
    }

    @Test
    public void testGetLoggingIfFlagNotGivenThenFalse(){
        assertLoggingParameterFalse(new String[]{""});
        assertLoggingParameterFalse(new String [] {});
    }

    @Test
    public void testGetLoggingIfFlagGivenThenTrue(){
        assertLoggingParameterTrue(new String[]{"-l"});
        assertLoggingParameterTrue(new String[]{"-l", ""});
        assertLoggingParameterTrue(new String[]{"", "-l"});
    }

    @Test
    public void testGetLoggingIfFlagGiven(){
        assertAllParameters("filename.txt",
                new String[]{"filename.txt","-l"});
        assertAllParameters("filename.txt",
                new String[]{"-l", "filename.txt"});
        assertAllParameters("-l",new String[]{"-l","-l"});
    }

    private void assertAllParameters(String expectedFileName, String[] parameters) {
        assertLoggingParameterTrue(parameters);
        assertFileParameter(expectedFileName, parameters);
    }

    private void assertLoggingParameterFalse(String[] parameters) {
        args.setParameters(parameters);
        assertFalse(args.getLogging());
    }

    private void assertLoggingParameterTrue(String[] parameters) {
        args.setParameters(parameters);
        assertTrue(args.getLogging());
    }

    private void assertFileParameter(String fileName, String[] parameters) {
        args.setParameters(parameters);
        assertEquals(fileName, args.getFileName());
    }


}
