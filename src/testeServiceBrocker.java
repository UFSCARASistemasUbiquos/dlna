/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package projetosistemasubiquos;

import java.io.File;
import java.io.FileReader;
import org.jdom2.Comment;
import org.jdom2.Content;
import org.jdom2.Content.CType;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.filter.Filters;
import org.jdom2.input.SAXBuilder;
import org.jdom2.util.IteratorIterable;




/**
 *
 * @author elder
 */

public class testeServiceBrocker
{
private static String file1 = "src/projetosistemasubiquos/RequisicaoServiceBrocker.xml";
   public static void main(String[] args) throws Exception
   {
  
        // Use a SAX builder
        SAXBuilder builder = new SAXBuilder();
        // build a JDOM2 Document using the SAXBuilder.
        
        Document jdomDoc = builder.build(new File(file1));
        
        // get the document type
        System.out.println(jdomDoc.getDocType() +"  :" );
 
        
        
        //get the root element
        Element web_app = jdomDoc.getRootElement();
        System.out.println("Nome do root: " + web_app.getName());
        System.out.println("Valores:" + web_app.getValue());
         
        
        /*// iterate through the descendants and print non-Text and non-Comment values
        IteratorIterable<Content> contents = web_app.getDescendants();
        while (contents.hasNext()) {
            Content web_app_content = contents.next();
            if (!web_app_content.getCType().equals(CType.Text) && !web_app_content.getCType().equals(CType.Comment)) {
                System.out.println(web_app_content.toString() + web_app_content.getValue());
            }
        }*/
 
        // get comments using a Comment filter
        /*IteratorIterable<Comment> comments = web_app.getDescendants(Filters.comment());
        while (comments.hasNext()) {
            Comment comment = comments.next();
            System.out.println(comment);
        }*/
        
   }
}
