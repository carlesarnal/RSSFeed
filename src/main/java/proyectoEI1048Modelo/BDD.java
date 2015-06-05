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
public class BDD {
    SAXBuilder builder = new SAXBuilder();
    File database;
    Document doc;
    ClassLoader classLoader;
    public BDD() throws  IOException, JDOMException {
        classLoader = getClass().getClassLoader();
        database = new File(classLoader.getResource("bdd.xml").getFile());
        doc = (Document)builder.build(database);
    }

      /*----------------------------------------------------------------------------------

    Parte de Carpetas

    --------------------------------------------------------------------------------*/

    public Map<String,Map<String,String>> getCarpetas(){
        Map<String,Map<String, String>> carpetas = new HashMap<String, Map<String,String>>();
        Element rootNode = doc.getRootElement();
        List list = rootNode.getChildren("carpeta");
        for (int i = 0; i < list.size(); i++) {
            //cogemos una carpeta
            Element node = (Element) list.get(i);
            //nombre de la carpeta
            String nombre = node.getChildText("nombre").toString();

            carpetas.put(nombre, getFuentes(nombre));
        }
        return carpetas;
    }
    public boolean existsCarpeta(String nombre) {

        if(nombre==null)
            return false;
        Element rootNode = doc.getRootElement();
        List<?> list = rootNode.getChildren("carpeta");
        for (int i = 0; i < list.size(); i++) {
            Element node = (Element) list.get(i);
            if (node.getChildText("nombre").toString().equals(nombre)) {
                return true;
            }
        }
        return false;
    }
    public boolean removeCarpeta(String nombre) throws IOException {
        if(existsCarpeta(nombre)) {
            Element rootNode = doc.getRootElement();
            List<?> list = rootNode.getChildren("carpeta");
            //Recorre todas las carpetas del xml
            for (int i = 0; i < list.size(); i++) {
                Element node = (Element) list.get(i);
                //si la fuente
                if (node.getChildText("nombre").toString().equals(nombre)) {
                    if(!node.getChild("fuentes").getChildren("fuente").isEmpty())
                        return false;
                    node.detach();
                    XMLOutputter xmlOutput = new XMLOutputter();
                    xmlOutput.setFormat(Format.getPrettyFormat());
                    xmlOutput.output(doc, new FileWriter("src/main/resources/bdd.xml"));
                    return true;
                }
            }
        }else{
            System.out.println("La carpeta que quieres borrar no existe");
        }
        return false;
    }
    public boolean addCarpeta(String nombre) throws IOException {
        if(!existsCarpeta(nombre)){
            Element element= new Element("carpeta");
            element.addContent(new Element("nombre").setText(nombre));
            element.addContent(new Element("fuentes"));
            doc.getRootElement().addContent(element);
            XMLOutputter xmlOutput = new XMLOutputter();

            // display nice nice
            xmlOutput.setFormat(Format.getPrettyFormat());
            xmlOutput.output(doc, new FileWriter("src/main/resources/bdd.xml"));
            return true;
        }
        System.out.println("La Carpeta que estas introduciendo ya existe");
        return false;
    }

    /*----------------------------------------------------------------------------------

    Parte de Fuente

    ------------------------------------------------------------------------------------*/

    public Map<String,String> getFuentes(String nCarpeta){
        Element rootNode = doc.getRootElement();
        List list = rootNode.getChildren("carpeta");
        Map<String,String> fuentes = new HashMap<String,String>();
        for (int i = 0; i < list.size(); i++) {
            //cogemos una carpeta
            Element node = (Element) list.get(i);
            //nombre de la carpeta
            String nombre = node.getChildText("nombre").toString();
            if (nombre.equals(nCarpeta)) {
                try {
                    Element fuenteRoot = node.getChild("fuentes");
                    List listaFuentes = fuenteRoot.getChildren("fuente");
                    for (int j = 0; j < listaFuentes.size(); j++) {
                        Element nodeFuente = (Element) listaFuentes.get(j);
                        fuentes.put(nodeFuente.getChildText("nombre").toString(), nodeFuente.getChildText("URL").toString());
                    }
                } catch (Exception e) {
                    //
                }
            }
        }
        return fuentes;
    }
    public boolean addFuente(String nombre,String url,String nCarpeta) throws IOException {

        if(!existsFuente(nombre, url)){
            Element element = new Element("fuente");
            element.addContent(new Element("nombre").setText(nombre));
            element.addContent(new Element("URL").setText(url));
            element.addContent(new Element("etiquetas"));
            Element rootNode = doc.getRootElement();
            List<?> list = rootNode.getChildren("carpeta");
            //Recorre todas las carpetas del xml
            for (int i = 0; i < list.size(); i++) {
                Element node = (Element) list.get(i);
                if (node.getChildText("nombre").toString().equals(nCarpeta)) {
                    node.getChild("fuentes").addContent(element);
                    XMLOutputter xmlOutput = new XMLOutputter();
                    xmlOutput.setFormat(Format.getPrettyFormat());
                    xmlOutput.output(doc, new FileWriter("src/main/resources/bdd.xml"));
                    return true;
                }
            }
        }else{
            System.out.println("La fuente que estas introduciendo ya existe");
            return false;
        }
        return false;
    }
    public boolean removeFuente(String nombre,String url,String nCarpeta) throws IOException {
        if(existsFuente(nombre, url)) {
            Element rootNode = doc.getRootElement();
            List list = rootNode.getChildren("carpeta");
            //Recorre todas las carpetas del xml
            for (int i = 0; i < list.size(); i++) {
                //cogemos carpeta
                Element node = (Element) list.get(i);
                //si la fuente

                if (node.getChildText("nombre").toString().equals(nCarpeta)) {
                    //System.out.println(node.getChildText("nombre").toString());
                    Element fuenteRoot =node.getChild("fuentes");
                    List listaFuentes = fuenteRoot.getChildren("fuente");
                    for(int j = 0; j< listaFuentes.size(); j++){
                        //fuente
                        Element nodeFuente = (Element) listaFuentes.get(j);
                        if(nodeFuente.getChildText("nombre").toString().equals(nombre) || nodeFuente.getChildText("URL").toString().equals(url)){
                            nodeFuente.detach();
                            XMLOutputter xmlOutput = new XMLOutputter();
                            xmlOutput.setFormat(Format.getPrettyFormat());
                            try {
                                xmlOutput.output(doc, new FileWriter("src/main/resources/bdd.xml"));
                            } catch (IOException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }
                            return true;
                        }
                    }

                }
            }
        }else{
            System.out.println("La fuente que quieres borrar no existe");
            return false;
        }
        return false;
    }
    public boolean existsFuente(String nombre,String url) {

        if(nombre==null || url == null)
            return false;
        Element rootNode = doc.getRootElement();
        List<?> list = rootNode.getChildren("carpeta");
        for (int i = 0; i < list.size(); i++) {
            Element node = (Element) list.get(i);

            Element fuenteRoot =node.getChild("fuentes");
            List listaFuentes = fuenteRoot.getChildren("fuente");
            for(int j = 0; j< listaFuentes.size(); j++) {
                //fuente
                Element nodeFuente = (Element) listaFuentes.get(j);
                if (nodeFuente.getChildText("nombre").toString().equals(nombre) || nodeFuente.getChildText("URL").toString().equals(url)) {
                    return true;
                }
            }

        }
        return false;
    }
    public List<String> info(String url2) throws IOException, IllegalArgumentException, FeedException {
        List<String> info = new ArrayList<String>();
        URL url = new URL(url2);
        XmlReader xmlReader = null;
        try {
            xmlReader = new XmlReader(url);
            SyndFeed feeder = new SyndFeedInput().build(xmlReader);
            if (feeder.getAuthor() != null)
                feeder.getAuthor();


            for (Object entradaObj : feeder.getEntries()) {
                SyndEntry entrada = (SyndEntry) entradaObj;
                info.add(entrada.getTitle());
                info.add("\n");
                info.add("Autor: " + entrada.getAuthor());
                info.add("\n");
                //String contenido = entrada.getContents().toString();
                //info.add(contenido.replace("\\<.*?\\>", ""));
                info.add(entrada.getLink());
                info.add("\n");
                String contenido = "";
                for (SyndContentImpl content : (List<SyndContentImpl>) entrada.getContents()){
                    contenido += content.getValue();
                }
                if(contenido.equals("")){
                    String descripcion = entrada.getDescription().getValue();
                  // System.out.println("DESCRIPCION: ");
                    //System.out.println(descripcion);
                    info.add("53727847");
                    info.add(descripcion);
                }
                else{
                   // System.out.println("CONTENIDO: ");
                    //System.out.println(contenido);
                    info.add("53727847");
                    info.add(contenido);
                }


                //System.out.println(entrada.getDescription().getValue());

                //info.add(valor);
                info.add("45802276");


                //info.add("\n");
                //info.add("\n");

            }
        } finally {
            if (xmlReader != null)
                xmlReader.close();
        }
        return info;

    }

    /*----------------------------------------------------------------------------------

    Parte de Etiquetas
    ------------------------------------------------------------------------------------*/

    public List<String> getEtiquetas(String nCarpeta,String nFuente){
        List<String> lista = new ArrayList<String>();
        Element rootNode = doc.getRootElement();
        List listCarp = rootNode.getChildren("carpeta");
        for (int x = 0; x < listCarp.size(); x++) {
            Element nodeCarp = (Element) listCarp.get(x);
            try {
                if (nodeCarp.getChildText("nombre").toString().equals(nCarpeta)) {
                    List list = nodeCarp.getChild("fuentes").getChildren("fuente");
                    for (int i = 0; i < list.size(); i++) {
                        Element node = (Element) list.get(i);
                        if (node.getChildText("nombre").toString().equals(nFuente)) {
                            List listaEt = node.getChild("etiquetas").getChildren("etiqueta");
                            for (int j = 0; j < listaEt.size(); j++) {
                                Element node2 = (Element) listaEt.get(j);
                                lista.add(node2.getChildText("value"));

                            }
                        }
                    }
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return lista;
    }
    public boolean addEtiqueta(String nCarpeta,String nFuente,String valor){
        if(!existsEtiqueta(nCarpeta,nFuente,valor)) {
            Element rootNode = doc.getRootElement();
            List listCarp = rootNode.getChildren("carpeta");
            for (int x = 0; x < listCarp.size(); x++) {
                Element nodeCarp = (Element) listCarp.get(x);
                try {
                    if (nodeCarp.getChildText("nombre").toString().equals(nCarpeta)) {
                        List list = nodeCarp.getChild("fuentes").getChildren("fuente");
                        for (int i = 0; i < list.size(); i++) {
                            Element node = (Element) list.get(i);
                            if (node.getChildText("nombre").toString().equals(nFuente)) {
                                Element element = new Element("etiqueta");
                                element.addContent(new Element("value").setText(valor));
                                node.getChild("etiquetas").addContent(element);
                                XMLOutputter xmlOutput = new XMLOutputter();
                                xmlOutput.setFormat(Format.getPrettyFormat());
                                xmlOutput.output(doc, new FileWriter("src/main/resources/bdd.xml"));
                                return true;
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    return false;
                }
            }
        }
        return false;
    }
    public boolean eliminarEtiqueta(String nCarpeta,String nFuente,String valor){
        if(existsEtiqueta(nCarpeta,nFuente,valor)) {
            Element rootNode = doc.getRootElement();
            List listCarp = rootNode.getChildren("carpeta");
            for (int x = 0; x < listCarp.size(); x++) {
                Element nodeCarp = (Element) listCarp.get(x);
                try {
                    if (nodeCarp.getChildText("nombre").toString().equals(nCarpeta)) {
                        List list = nodeCarp.getChild("fuentes").getChildren("fuente");
                        for (int i = 0; i < list.size(); i++) {
                            Element node = (Element) list.get(i);
                            if (node.getChildText("nombre").toString().equals(nFuente)) {
                                List listaEt = node.getChild("etiquetas").getChildren("etiqueta");
                                for (int j = 0; j < listaEt.size(); j++) {
                                    Element node2 = (Element) listaEt.get(j);
                                    if(node2.getChildText("value").equals(valor)){
                                        node2.detach();
                                        XMLOutputter xmlOutput = new XMLOutputter();
                                        xmlOutput.setFormat(Format.getPrettyFormat());
                                        xmlOutput.output(doc, new FileWriter("src/main/resources/bdd.xml"));
                                        return true;
                                    }
                                }
                            }
                        }
                    }
                }catch (Exception e){
                    e.printStackTrace();
                    return false;
                }
            }
        }
        return false;
    }
    public boolean existsEtiqueta(String nCarpeta,String nFuente,String valor){
        Element rootNode = doc.getRootElement();
        List listCarp = rootNode.getChildren("carpeta");
        for (int x = 0; x < listCarp.size(); x++) {
            Element nodeCarp = (Element) listCarp.get(x);
            try {
                if (nodeCarp.getChildText("nombre").toString().equals(nCarpeta)) {
                    List list = nodeCarp.getChild("fuentes").getChildren("fuente");
                    for (int i = 0; i < list.size(); i++) {
                        Element node = (Element) list.get(i);
                        if (node.getChildText("nombre").toString().equals(nFuente)) {
                            List listaEt = node.getChild("etiquetas").getChildren("etiqueta");
                            for (int j = 0; j < listaEt.size(); j++) {
                                Element node2 = (Element) listaEt.get(j);
                                if(node2.getChildText("value").equals(valor)){
                                    return true;
                                }
                            }
                        }
                    }
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return false;
    }
    public Map<String,String> busquedaPorEtiqueta(String etiqueta){
        Map<String,String> fuentes = new HashMap<String,String>();
        Element rootNode = doc.getRootElement();
        List list = rootNode.getChildren("carpeta");
        for (int i = 0; i < list.size(); i++) {
            //cogemos una carpeta
            Element node = (Element) list.get(i);
            //nombre de la carpeta
            String nombre = node.getChildText("nombre").toString();
            try {
                Element fuenteRoot = node.getChild("fuentes");
                List listaFuentes = fuenteRoot.getChildren("fuente");
                for (int j = 0; j < listaFuentes.size(); j++) {
                    Element nodeFuente = (Element) listaFuentes.get(j);
                    if(!nodeFuente.getChild("etiquetas").getChildren("etiqueta").isEmpty()) {
                        List listaEt = nodeFuente.getChild("etiquetas").getChildren("etiqueta");
                        for (int z = 0; z < listaEt.size(); z++) {
                            Element node2 = (Element) listaEt.get(z);
                            if (node2.getChildText("value").equals(etiqueta)) {
                                fuentes.put(nodeFuente.getChildText("nombre").toString(), nodeFuente.getChildText("URL").toString());
                            }
                        }
                    }
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return fuentes;
    }


    public int cambiarCarpeta(String nFuente,String nCarpetaAnt, String nCarpetaNuev,String nUrl) throws IOException {

        removeFuente(nFuente,nUrl,nCarpetaAnt);
        if(addFuente(nFuente,nUrl,nCarpetaNuev))
            return 0;
        else
            return -1;

    }

}








