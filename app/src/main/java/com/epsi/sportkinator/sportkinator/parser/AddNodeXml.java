package com.epsi.sportkinator.sportkinator.parser;

/**
 * Created by rbertal on 21/04/2015.
 */ import android.content.Context;
import android.os.Environment;
import android.util.Log;

import com.epsi.sportkinator.sportkinator.R;
import com.epsi.sportkinator.sportkinator.entities.Question;
import com.epsi.sportkinator.sportkinator.entities.Sport;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

public class AddNodeXml {

        private static Document document;
        private Element oldSportElement;
        private Element oldQuestionElement;

        private Context context;
        public void AddNode(Context c,String sport, Question newQustion, Sport newSport) throws Exception {
            context=c;
            InputStream inputStream=null;
            SAXBuilder sxb = new SAXBuilder();
            try
            {
                //inputStream = context.getResources().openRawResource(R.raw.connaissance);
               // File mFile = new File(context.getFilesDir()+ "/bdConnaissance.xml");
                File mFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/bdConnaissance.xml");
                try {
                    inputStream = new FileInputStream(mFile);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }


                document = sxb.build(inputStream);
            }
            catch (Exception e){

            }
            Element racine = document.getRootElement();
            findNode(racine,sport);

            if (oldSportElement != null)
            {
                oldQuestionElement = oldSportElement.getParentElement();
                Element elementQuestionToAddYes = null;
                Element elementQuestionToAddNo = null;
                Element elementToModify = oldQuestionElement.clone().detach();
                elementToModify.detach();
                elementToModify.setAttribute("id", new Date().toString());
                elementToModify.setAttribute("response", "oui");
                elementToModify.setAttribute("name", newQustion.getName());
                elementQuestionToAddYes = elementToModify.clone();
                elementToModify.setAttribute("response", "non");
                elementQuestionToAddNo = elementToModify;

                Element newSportToAdd = oldSportElement.clone().detach();
                newSportToAdd.setAttribute("response", newSport.getName());
                newSportToAdd.setAttribute("image", newSport.getImage());
                elementQuestionToAddYes.setContent(newSportToAdd);

                elementQuestionToAddNo.removeContent();
                elementQuestionToAddNo.setContent(oldSportElement.detach());


                oldQuestionElement.removeContent();
                //parentElement.detach();
                oldQuestionElement.addContent(elementQuestionToAddYes);
                oldQuestionElement.addContent(elementQuestionToAddNo);





                saveDocument(Environment.getExternalStorageDirectory() + File.separator + "bdConnaissance.xml");

                inputStream.close();
            }
        }

     private Element findNode(Element element, String sport)
     {
         if (element.isRootElement())
         {
             List<Element> childrens = element.getChildren();
             if (childrens.size() > 0)
             {
                 for(Element e: childrens)
                 {
                     findNode(e,sport);
                 }
             }
         }
         else
         {
             Log.d("Element : ",  element.getAttributeValue("response"));
             String value = element.getAttributeValue("response");
             Log.d("value : ",  value);
             if(value.equals(sport))
             {
                 oldSportElement = element;
                 return element;
             }
             else
             {
                 List<Element> childrens = element.getChildren();
                 if (childrens.size() > 0)
                 {
                     for(Element e: childrens)
                     {
                         findNode(e,sport);
                     }
                 }
             }
         }
         return null;
     }


    public void saveDocument(String file)
    {
        //JDOM document is ready now, lets write it to file now
        XMLOutputter xmlOutputter = new XMLOutputter(Format.getPrettyFormat());
        //output xml to console for debugging
        //xmlOutputter.output(doc, System.out);
        try {
            xmlOutputter.output(document, new FileOutputStream(file));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    }
