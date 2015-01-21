package SystemAction;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.io.IOException;

public class SystemAction {
    public static final String CLASSES = "files/Commands.xml";
    public static int numberOfCommands = 0;

    public static boolean act(String names) throws IOException{  //true, если выполнил команду
        if (names.endsWith("Couldn't recognize"))  {             //выполняет команду, содержащуюся в names
            return false;
        }
        for (String s: names.split(" ")) {
            if (getExecutionCommandName(s) != null) {
                Runtime.getRuntime().exec(getExecutionCommandName(s));
                return true;
            }
        }
        return false;
    }

    private static Document getDocument() {
        try {
            DocumentBuilderFactory f = DocumentBuilderFactory.newInstance();
            f.setValidating(false);
            DocumentBuilder builder = f.newDocumentBuilder();
            return builder.parse(new File(CLASSES));
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return null;
    }

    private static String[][] getFunctions(){
        NodeList methodNodes = getDocument().getElementsByTagName("command");
        String[][] res = new String[2][methodNodes.getLength()];
        numberOfCommands = methodNodes.getLength();
        if (methodNodes.getLength() == 0){
            System.err.println("Couldn't read XML");
            return null;
        }else {
            for (int i = 0; i < methodNodes.getLength(); i++) {
                Node node = methodNodes.item(i);
                NamedNodeMap attributes = node.getAttributes();
                Node nameAttrib = attributes.getNamedItem("name");
                String functionName = nameAttrib.getNodeValue();
                Node class_nameAttrib = attributes.getNamedItem("execution_command");
                String execution_command = class_nameAttrib.getNodeValue();
                res[0][i] = execution_command;
                res[1][i] = functionName;
            }
        }
        return res;
    }

    private static String getExecutionCommandName(String command){
        String[][] examples = getFunctions();
        for (int i = 0; i < numberOfCommands; i++){
            if (examples[1][i].equals(command))
                return examples[0][i];
        }
        return null;
    }
}
