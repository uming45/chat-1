//package com.example.gruper.chatgruperexample.xmpp;
//
///**
// * Created by gruper on 11/05/17.
// */
//
//
//import java.io.*;
//import java.net.*;
//import java.util.*;
//
//import org.xmlpull.v1.*;
//
//
//public final class SmackConfiguration {
//
//
//    private static final String SMACK_VERSION = "1.4.1";
//
//    private static int packetReplyTimeout = 5000;
//    private static int keepAliveInterval = 30000;
//
//    private SmackConfiguration() {
//    }
//
//    /**
//     * Loads the configuration from the smack-config.xml file.<p>
//     *
//     * So far this means that:
//     * 1) a set of classes will be loaded in order to execute their static init block
//     * 2) retrieve and set the current Smack release
//     */
//    static {
//        try {
//            // Get an array of class loaders to try loading the providers files from.
//            ClassLoader[] classLoaders = getClassLoaders();
//            for (int i = 0; i < classLoaders.length; i++) {
//                Enumeration configEnum = classLoaders[i].getResources("META-INF/smack-config.xml");
//                while (configEnum.hasMoreElements()) {
//                    URL url = (URL) configEnum.nextElement();
//                    InputStream systemStream = null;
//                    try {
//                        systemStream = url.openStream();
//                        XmlPullParserFactory factory =
//                                XmlPullParserFactory.newInstance(
//                                        "org.xmlpull.mxp1.MXParserFactory", null);
//                        factory.setNamespaceAware(true);
//                        XmlPullParser parser = factory.newPullParser();
//                        parser.setInput(systemStream, "UTF-8");
//                        int eventType = parser.getEventType();
//                        do {
//                            if (eventType == XmlPullParser.START_TAG) {
//                                if (parser.getName().equals("className")) {
//                                    // Attempt to load the class so that the class can get initialized
//                                    parseClassToLoad(parser);
//                                }
//                                else if (parser.getName().equals("packetReplyTimeout")) {
//                                    packetReplyTimeout = parseIntProperty(parser, packetReplyTimeout);
//                                }
//                                else if (parser.getName().equals("keepAliveInterval")) {
//                                    keepAliveInterval = parseIntProperty(parser, keepAliveInterval);
//                                }
//                            }
//                            eventType = parser.next();
//                        }
//                        while (eventType != XmlPullParser.END_DOCUMENT);
//                    }
//                    catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                    finally {
//                        try {
//                            systemStream.close();
//                        }
//                        catch (Exception e) {
//                        }
//                    }
//                }
//            }
//        }
//        catch (Exception e) {
//        }
//    }
//
//    /**
//     * Returns the Smack version information, eg "1.3.0".
//     *
//     * @return the Smack version information.
//     */
//    public static String getVersion() {
//        return SMACK_VERSION;
//    }
//
//    /**
//     * Returns the number of milliseconds to wait for a response from
//     * the server. The default value is 5000 ms.
//     *
//     * @return the milliseconds to wait for a response from the server
//     */
//    public static int getPacketReplyTimeout() {
//        // The timeout value must be greater than 0 otherwise we will answer the default value
//        if (packetReplyTimeout <= 0) {
//            packetReplyTimeout = 5000;
//        }
//        return packetReplyTimeout;
//    }
//
//    /**
//     * Sets the number of milliseconds to wait for a response from
//     * the server.
//     *
//     * @param timeout the milliseconds to wait for a response from the server
//     */
//    public static void setPacketReplyTimeout(int timeout) {
//        if (timeout <= 0) {
//            throw new IllegalArgumentException();
//        }
//        packetReplyTimeout = timeout;
//    }
//
//    /**
//     * Returns the number of milleseconds delay between sending keep-alive
//     * requests to the server. The default value is 30000 ms. A value of -1
//     * mean no keep-alive requests will be sent to the server.
//     *
//     * @return the milliseconds to wait between keep-alive requests, or -1 if
//     *      no keep-alive should be sent.
//     */
//    public static int getKeepAliveInterval() {
//        return keepAliveInterval;
//    }
//
//    /**
//     * Sets the number of milleseconds delay between sending keep-alive
//     * requests to the server. The default value is 30000 ms. A value of -1
//     * mean no keep-alive requests will be sent to the server.
//     *
//     * @param interval the milliseconds to wait between keep-alive requests,
//     *      or -1 if no keep-alive should be sent.
//     */
//    public static void setKeepAliveInterval(int interval) {
//        keepAliveInterval = interval;
//    }
//
//    private static void parseClassToLoad(XmlPullParser parser) throws Exception {
//        String className = parser.nextText();
//        // Attempt to load the class so that the class can get initialized
//        try {
//            Class.forName(className);
//        }
//        catch (ClassNotFoundException cnfe) {
//            System.err.println("Error! A startup class specified in smack-config.xml could " +
//                    "not be loaded: " + className);
//        }
//    }
//
//    private static int parseIntProperty(XmlPullParser parser, int defaultValue)
//            throws Exception
//    {
//        try {
//            return Integer.parseInt(parser.nextText());
//        }
//        catch (NumberFormatException nfe) {
//            nfe.printStackTrace();
//            return defaultValue;
//        }
//    }
//
//    /**
//     * Returns an array of class loaders to load resources from.
//     *
//     * @return an array of ClassLoader instances.
//     */
//    private static ClassLoader[] getClassLoaders() {
//        ClassLoader[] classLoaders = new ClassLoader[3];
//        classLoaders[0] = new SmackConfiguration().getClass().getClassLoader();
//        classLoaders[1] = Thread.currentThread().getContextClassLoader();
//        classLoaders[2] = ClassLoader.getSystemClassLoader();
//        return classLoaders;
//    }
//}
//
//
