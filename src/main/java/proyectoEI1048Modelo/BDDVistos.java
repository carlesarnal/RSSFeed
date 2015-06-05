package proyectoEI1048Modelo;

import com.sun.syndication.feed.synd.SyndContentImpl;
import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.io.FeedException;
import com.sun.syndication.io.SyndFeedInput;
import com.sun.syndication.io.XmlReader;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Carlos on 23/12/2014.
 */
public class BDDVistos {
    SAXBuilder builder = new SAXBuilder();
    File database;
    Document doc;
    ClassLoader classLoader;

    public BDDVistos() throws IOException, JDOMException {
        classLoader = getClass().getClassLoader();
        database = new File(classLoader.getResource("bddvistos.xml").getFile());
        doc = (Document) builder.build(database);
    }

      /*----------------------------------------------------------------------------------

    Parte de Vistos

    --------------------------------------------------------------------------------*/

    public boolean fuenteLeida(String nombre) {

        if (nombre == null)
            return false;
        Element rootNode = doc.getRootElement();
        List<?> list = rootNode.getChildren("fuente");
        for (int i = 0; i < list.size(); i++) {
            Element node = (Element) list.get(i);
            if (node.getValue().toString().equals(nombre)) {
                return true;
            }
        }
        return false;
    }

    public boolean addLeido(String nombre) throws IOException {
        if (!fuenteLeida(nombre)) {
            Element element= new Element("fuente");
            element.setText(nombre);
            doc.getRootElement().addContent(element);
            XMLOutputter xmlOutput = new XMLOutputter();
            // display nice nice
            xmlOutput.setFormat(Format.getPrettyFormat());
            xmlOutput.output(doc, new FileWriter("src/main/resources/bddvistos.xml"));
            return true;
        }
        return false;
    }
}