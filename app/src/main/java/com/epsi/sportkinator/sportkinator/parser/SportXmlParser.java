package com.epsi.sportkinator.sportkinator.parser;

import android.content.Context;
import android.util.Log;
import android.util.Xml;

import com.epsi.sportkinator.sportkinator.entities.Question;
import com.epsi.sportkinator.sportkinator.entities.Sport;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by rbertal on 10/04/2015.
 */
public class SportXmlParser {
    // We don't use namespaces
    private static final String ns = null;

    /**
     * Recupere une question a partir de l'inputstream . le paremetre question et reponse permettent
     * de recuperer la balise qui correspond a la question posée afin de passer a la balise suivante
     * et donc de poser la prochaine question
     * @param inputStream   fichier XML a lire
     * @param question Question precedente qui a été posé. si c'est la premiere question alors
     *                 parametre null
     * @param response Reponse precedente que l'utilisateur a repondu. si c'est la premiere question
     *                 alors parametre null
     * @return question si une question peut etre lue
     */
    public Question readQuestion(InputStream inputStream, Question question, String response) {
        Question newQuestion = new Question();
        try {
            XmlPullParser parser = getXmlPullParser(inputStream);

            newQuestion = getQuestion(parser, question, response);

        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return newQuestion;

    }

    /**
     * Creer un objet XmlPullParser qui permet de parser le fichier XML.
     * @param inputStream   le fichier XML a parser
     * @return XmlPullParser si le mouvement d'échec est valide ou faux(false) si invalide
     */
    private XmlPullParser getXmlPullParser(InputStream inputStream) throws XmlPullParserException {
        XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
        factory.setNamespaceAware(true);

        XmlPullParser parser = factory.newPullParser();
        parser.setInput(inputStream, null);
        return parser;
    }

    /**
     * Recupere la 1er question à poser ou la question suivante en fonction de la question et de la
     * reponse precedente
     * @param parser   le fichier XML a parser
     * @param questionFind Question precedente qui a été posé. si c'est la premiere question alors
     *                 parametre null
     * @param response Reponse precedente que l'utilisateur a repondu. si c'est la premiere questio
     * @return Question remplie si une question a été trouvée
     */
    private Question getQuestion(XmlPullParser parser, Question questionFind, String response) throws XmlPullParserException, IOException {
        Question question = new Question();
        boolean find = false;
        int eventType = parser.getEventType();

        while (eventType != XmlPullParser.END_DOCUMENT && find == false) {
            String text;

            switch (eventType) {
                case XmlPullParser.START_TAG:
                    find = findQuestion(parser, questionFind, response);
                    break;

                case XmlPullParser.TEXT:
                    text = parser.getText();
                    break;
                case XmlPullParser.END_TAG:
                    find = findQuestion(parser, questionFind, response);
                default:
                    break;
            }
            if (find) {
                question = readOneQuestion(parser);
                Log.d("Parser Question ", readOneQuestion(parser).toString());
            } else {
                eventType = parser.next();
            }

        }

        return question;
    }

    /**
     * Parse le fichier XML et recupere la 1er question ou la question suivante selon la question et la
     * reponse precedente
     * @param parser   le fichier XML a parser
     * @param questionFind Question precedente qui a été posé. si c'est la premiere question alors
     *                 parametre null
     * @param response Reponse precedente que l'utilisateur a repondu. si c'est la premiere questio
     * @return vrai(true) si la question a été trouvé
     */
    public boolean findQuestion(XmlPullParser parser, Question questionFind, String response) throws IOException, XmlPullParserException {
        boolean find = false;
        String nameParser = parser.getName();
        if (null == questionFind) {
            if (nameParser.equals("question")) {
                Log.d("findQuestion nameParser ", parser.getName());
                find = true;
            }
        } else {

            if (nameParser.equals("question")) {
                if (questionFind.getName().equalsIgnoreCase(parser.getAttributeValue(null, "name"))
                        && response.equalsIgnoreCase(parser.getAttributeValue(null, "response"))
                        && questionFind.getId().equalsIgnoreCase(parser.getAttributeValue(null, "id"))
                        ) {

                    parser.nextTag();
                    find = true;
                }
            }
        }

        return find;
    }

    /**
     *
     * Retourne un objet Question remplie et creer à partir des attributs de la balise dans le fichier XML
     * @param parser   le fichier XML a parser
     *  @return question question valide
     */
    private Question readOneQuestion(XmlPullParser parser) throws XmlPullParserException, IOException {
        String name = parser.getAttributeValue(null, "name");
        String id = parser.getAttributeValue(null, "id");
        String response = parser.getAttributeValue(null, "response");


        return new Question(name, response, id);
    }

    public boolean sportAllReadyExists (InputStream inputStream, String sport) {
        boolean exists = false;
        try {
            XmlPullParser parser = getXmlPullParser(inputStream);

            int eventType = parser.getEventType();

            while (eventType != XmlPullParser.END_DOCUMENT && exists == false) {
                String text;

                switch (eventType) {
                    case XmlPullParser.START_TAG:
                        if (parser.getName().equals("sport")) {
                            if(sport.equalsIgnoreCase(parser.getAttributeValue(null, "response")))
                                exists=true;
                        }
                        break;

                    case XmlPullParser.TEXT:
                        text = parser.getText();
                        break;
                    case XmlPullParser.END_TAG:
                        if (parser.getName().equals("sport")) {
                            if(sport.equalsIgnoreCase(parser.getAttributeValue(null, "response")))
                                exists=true;
                        }
                    default:
                        break;
                }
                eventType = parser.next();


            }

        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return exists;

    }


    public void addQuestion(Context context,String sport, Question newQustion, Sport newSport) throws Exception {
        AddNodeXml addNode = new AddNodeXml();
        addNode.AddNode(context,sport, newQustion, newSport);
    }

}
