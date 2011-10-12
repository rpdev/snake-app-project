/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package se.chalmers.snake.snakeappwebpage.serverstorage;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import se.chalmers.snake.snakeappwebpage.lib.HttpServletBuilder;

/**
 *
 * @author alesan
 */
@WebServlet(name = "DatabaseServlet", urlPatterns = {"/DatabaseServlet"})
public class DatabaseServlet extends HttpServletBuilder {

    @XmlRootElement
    static class SnakeMapXml {

        @XmlElement
        private String mapName;
        @XmlElement
        private String difficulty;
        @XmlElement
        private int speed;
        @XmlElement
        private int growth;
        @XmlElement
        private String description;

        public SnakeMapXml() {
        }

        public SnakeMapXml(SnakeMap snakeMap) {
            if (snakeMap != null) {
                if(snakeMap.getMapName() != null)
                    this.mapName = snakeMap.getMapName();
                else
                    this.mapName = "";
                switch (snakeMap.getDifficuly()) {
                    case 1:
                        this.difficulty = "Easy";
                    case 2:
                        this.difficulty = "Normal";
                    case 3:
                        this.difficulty = "Hard";
                    case 4:
                        this.difficulty = "Very Hard";
                }

                this.speed = snakeMap.getGameSpeed().getIntValue();
                this.growth = snakeMap.getGrowthspeed().getIntValue();
                this.description = snakeMap.getMapDescription();
            }
        }
    }

    @Override
    protected void pageRequest(HttpMeta httpMeta, HttpOutput httpOutput) throws Exception {
        if (httpMeta.isMethodPostGet()) {
            String action = httpMeta.REQUEST("action");
            String requestedObjectId = httpMeta.REQUEST("id");
            if (action.equals("getSnakeMap")) {
                httpMeta.setContentType("text/xml;charset=UTF-8");
                PrintWriter out = httpOutput.getWriter();
                SnakeMap snakeMap = Database.getInstance()
                        .getEntity(SnakeMap.class, Long.valueOf(requestedObjectId));
                SnakeMapXml wrapper = new SnakeMapXml(snakeMap);
                JAXBContext jc;
                try {
                    jc = JAXBContext.newInstance(SnakeMapXml.class);
                    Marshaller m = jc.createMarshaller();
                    m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT,
                            Boolean.TRUE);
                    // Dump XML data
                    m.marshal(wrapper, out);
                } catch (JAXBException ex) {
                    Logger.getLogger(SnakeMapXml.class.getName()).log(Level.SEVERE, null, ex);
                } finally {
                    out.close();
                }
            }
        }

    }
}
