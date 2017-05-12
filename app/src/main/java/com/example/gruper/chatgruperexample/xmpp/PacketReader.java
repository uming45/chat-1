//package com.example.gruper.chatgruperexample.xmpp;
//
///**
// * Created by gruper on 11/05/17.
// */
//
//import org.jivesoftware.smack.ConnectionListener;
//import org.jivesoftware.smack.XMPPException;
//import org.xmlpull.v1.*;
//
//import java.util.*;
//import java.util.List;
//import java.io.ObjectInputStream;
//import java.io.ByteArrayInputStream;
//
//import org.jivesoftware.smack.packet.*;
//import org.jivesoftware.smack.packet.XMPPError;
//import org.jivesoftware.smack.filter.PacketFilter;
//import org.jivesoftware.smack.util.*;
//import org.jivesoftware.smack.util.StringUtils;
//import org.jivesoftware.smack.provider.*;
//
//public class PacketReader {
//    /**
//     * Namespace used to store packet properties.
//     */
//    private static final String PROPERTIES_NAMESPACE =
//            "http://www.jivesoftware.com/xmlns/xmpp/properties";
//
//    private Thread readerThread;
//    private Thread listenerThread;
//
//    private XMPPConnection connection;
//    private XmlPullParser parser;
//    private boolean done = false;
//    protected List collectors = new ArrayList();
//    private List listeners = new ArrayList();
//    protected List connectionListeners = new ArrayList();
//
//    private String connectionID = null;
//    private Object connectionIDLock = new Object();
//
//    protected PacketReader(XMPPConnection connection) {
//        this.connection = connection;
//
//        readerThread = new Thread() {
//            public void run() {
//                parsePackets();
//            }
//        };
//        readerThread.setName("Smack Packet Reader");
//        readerThread.setDaemon(true);
//
//        listenerThread = new Thread() {
//            public void run() {
//                processListeners();
//            }
//        };
//        listenerThread.setName("Smack Listener Processor");
//        listenerThread.setDaemon(true);
//
//        try {
//            XmlPullParserFactory factory = XmlPullParserFactory.newInstance(
//                    "org.xmlpull.mxp1.MXParserFactory", null);
//            factory.setNamespaceAware(true);
//            parser = factory.newPullParser();
//            parser.setInput(connection.reader);
//        }
//        catch (XmlPullParserException xppe) {
//            xppe.printStackTrace();
//        }
//    }
//
//    /**
//     * Creates a new packet collector for this reader. A packet filter determines
//     * which packets will be accumulated by the collector.
//     *
//     * @param packetFilter the packet filter to use.
//     * @return a new packet collector.
//     */
//    public PacketCollector createPacketCollector(PacketFilter packetFilter) {
//        PacketCollector packetCollector = new PacketCollector(this, packetFilter);
//        return packetCollector;
//    }
//
//    /**
//     * Registers a packet listener with this reader. A packet filter determines
//     * which packets will be delivered to the listener.
//     *
//     * @param packetListener the packet listener to notify of new packets.
//     * @param packetFilter the packet filter to use.
//     */
//    public void addPacketListener(PacketListener packetListener, PacketFilter packetFilter) {
//        ListenerWrapper wrapper = new ListenerWrapper(this, packetListener,
//                packetFilter);
//        synchronized (listeners) {
//            listeners.add(wrapper);
//        }
//    }
//
//    /**
//     * Removes a packet listener.
//     *
//     * @param packetListener the packet listener to remove.
//     */
//    public void removePacketListener(PacketListener packetListener) {
//        synchronized (listeners) {
//            for (int i=0; i<listeners.size(); i++) {
//                ListenerWrapper wrapper = (ListenerWrapper)listeners.get(i);
//                if (wrapper != null && wrapper.packetListener.equals(packetListener)) {
//                    listeners.set(i, null);
//                }
//            }
//        }
//    }
//
//    /**
//     * Starts the packet reader thread and returns once a connection to the server
//     * has been established. A connection will be attempted for a maximum of five
//     * seconds. An XMPPException will be thrown if the connection fails.
//     *
//     * @throws XMPPException if the server fails to send an opening stream back
//     *      for more than five seconds.
//     */
//    public void startup() throws XMPPException {
//        readerThread.start();
//        listenerThread.start();
//        // Wait for stream tag before returing. We'll wait a couple of seconds before
//        // giving up and throwing an error.
//        try {
//            synchronized(connectionIDLock) {
//                connectionIDLock.wait(SmackConfiguration.getPacketReplyTimeout());
//            }
//        }
//        catch (InterruptedException ie) { }
//        if (connectionID == null) {
//            throw new XMPPException("Connection failed. No response from server.");
//        }
//        else {
//            connection.connectionID = connectionID;
//        }
//    }
//
//    /**
//     * Shuts the packet reader down.
//     */
//    public void shutdown() {
//        // Notify connection listeners of the connection closing if done hasn't already been set.
//        if (!done) {
//            ArrayList listenersCopy;
//            synchronized (connectionListeners) {
//                // Make a copy since it's possible that a listener will be removed from the list
//                listenersCopy = new ArrayList(connectionListeners);
//                for (Iterator i=listenersCopy.iterator(); i.hasNext(); ) {
//                    ConnectionListener listener = (ConnectionListener)i.next();
//                    listener.connectionClosed();
//                }
//            }
//        }
//        done = true;
//    }
//
//    /**
//     * Sends out a notification that there was an error with the connection
//     * and closes the connection.
//     *
//     * @param e the exception that causes the connection close event.
//     */
//    void notifyConnectionError(Exception e) {
//        done = true;
//        connection.close();
//        // Notify connection listeners of the error.
//        ArrayList listenersCopy;
//        synchronized (connectionListeners) {
//            // Make a copy since it's possible that a listener will be removed from the list
//            listenersCopy = new ArrayList(connectionListeners);
//            for (Iterator i=listenersCopy.iterator(); i.hasNext(); ) {
//                ConnectionListener listener = (ConnectionListener)i.next();
//                listener.connectionClosedOnError(e);
//            }
//        }
//    }
//
//    /**
//     * Sends out a notification that there was an error with the connection
//     * and closes the connection.
//     *
//     * @param e the exception that causes the connection close event.
//     */
//    void notifyConnectionAuthenticated() {
//        // Notify connection listeners that the connection is authenticated
//        ArrayList listenersCopy;
//        synchronized (connectionListeners) {
//            // Make a copy since it's possible that a listener will be removed from the list
//            listenersCopy = new ArrayList(connectionListeners);
//            for (Iterator i=listenersCopy.iterator(); i.hasNext(); ) {
//                ConnectionListener listener = (ConnectionListener)i.next();
//                if (listener instanceof ConnectionListener2)
//                    ((ConnectionListener2)listener).connectionAuthenticated();
//            }
//        }
//    }
//
//    /**
//     * Process listeners.
//     */
//    private void processListeners() {
//        boolean processedPacket = false;
//        while (!done) {
//            synchronized (listeners) {
//                if (listeners.size() > 0) {
//                    for (int i=listeners.size()-1; i>=0; i--) {
//                        if (listeners.get(i) == null) {
//                            listeners.remove(i);
//                        }
//                    }
//                }
//            }
//            processedPacket = false;
//            int size = listeners.size();
//            for (int i=0; i<size; i++) {
//                ListenerWrapper wrapper = (ListenerWrapper)listeners.get(i);
//                if (wrapper != null) {
//                    processedPacket = processedPacket || wrapper.notifyListener();
//                }
//            }
//            if (!processedPacket) {
//                try {
//                    Thread.sleep(100);
//                }
//                catch (InterruptedException ie) { }
//            }
//        }
//    }
//
//    /**
//     * Parse top-level packets in order to process them further.
//     */
//    private void parsePackets() {
//        try {
//            int eventType = parser.getEventType();
//            do {
//                if (eventType == XmlPullParser.START_TAG) {
//                    if (parser.getName().equals("message")) {
//                        processPacket(parseMessage(parser));
//                    }
//                    else if (parser.getName().equals("iq")) {
//                        processPacket(parseIQ(parser));
//                    }
//                    else if (parser.getName().equals("presence")) {
//                        processPacket(parsePresence(parser));
//                    }
//                    // We found an opening stream. Record information about it, then notify
//                    // the connectionID lock so that the packet reader startup can finish.
//                    else if (parser.getName().equals("stream")) {
//                        // Ensure the correct jabber:client namespace is being used.
//                        if ("jabber:client".equals(parser.getNamespace(null))) {
//                            // Get the connection id.
//                            for (int i=0; i<parser.getAttributeCount(); i++) {
//                                if (parser.getAttributeName(i).equals("id")) {
//                                    // Save the connectionID and notify that we've gotten it.
//                                    connectionID = parser.getAttributeValue(i);
//                                    synchronized(connectionIDLock) {
//                                        connectionIDLock.notifyAll();
//                                    }
//                                }
//                                else if (parser.getAttributeName(i).equals("from")) {
//                                    // Use the server name that the server says that it is.
//                                    connection.host = parser.getAttributeValue(i);
//                                }
//                            }
//                        }
//                    }
//                }
//                else if (eventType == XmlPullParser.END_TAG) {
//                    if (parser.getName().equals("stream")) {
//                        // Close the connection.
//                        connection.close();
//                    }
//                }
//                eventType = parser.next();
//            } while (!done && eventType != XmlPullParser.END_DOCUMENT);
//        }
//        catch (Exception e) {
//            if (!done) {
//                // Close the connection and notify connection listeners of the
//                // error.
//                notifyConnectionError(e);
//            }
//        }
//    }
//
//    /**
//     * Processes a packet after it's been fully parsed by looping through the installed
//     * packet collectors and listeners and letting them examine the packet to see if
//     * they are a match with the filter.
//     *
//     * @param packet the packet to process.
//     */
//    private void processPacket(Packet packet) {
//        if (packet == null) {
//            return;
//        }
//
//        // Remove all null values from the collectors list.
//        synchronized (collectors) {
//            for (int i=collectors.size()-1; i>=0; i--) {
//                if (collectors.get(i) == null) {
//                    collectors.remove(i);
//                }
//            }
//        }
//
//        // Loop through all collectors and notify the appropriate ones.
//        int size = collectors.size();
//        for (int i=0; i<size; i++) {
//            PacketCollector collector = (PacketCollector)collectors.get(i);
//            if (collector != null) {
//                // Have the collector process the packet to see if it wants to handle it.
//                collector.processPacket(packet);
//            }
//        }
//    }
//
//    /**
//     * Parses an IQ packet.
//     *
//     * @param parser the XML parser, positioned at the start of an IQ packet.
//     * @return an IQ object.
//     * @throws Exception if an exception occurs while parsing the packet.
//     */
//    private IQ parseIQ(XmlPullParser parser) throws Exception {
//        IQ iqPacket = null;
//
//        String id = parser.getAttributeValue("", "id");
//        String to = parser.getAttributeValue("", "to");
//        String from = parser.getAttributeValue("", "from");
//        IQ.Type type = IQ.Type.fromString(parser.getAttributeValue("", "type"));
//        XMPPError error = null;
//
//        boolean done = false;
//        while (!done) {
//            int eventType = parser.next();
//
//            if (eventType == XmlPullParser.START_TAG) {
//                String elementName = parser.getName();
//                String namespace = parser.getNamespace();
//                if (elementName.equals("error")) {
//                    error = parseError(parser);
//                }
//                else if (elementName.equals("query") && namespace.equals("jabber:iq:auth")) {
//                    iqPacket = parseAuthentication(parser);
//                }
//                else if (elementName.equals("query") && namespace.equals("jabber:iq:roster")) {
//                    iqPacket = parseRoster(parser);
//                }
//                else if (elementName.equals("query") && namespace.equals("jabber:iq:register")) {
//                    iqPacket = parseRegistration(parser);
//                }
//                // Otherwise, see if there is a registered provider for
//                // this element name and namespace.
//                else {
//                    Object provider = ProviderManager.getDefault().getIQProvider(elementName, namespace);
//                    if (provider != null) {
//                        if (provider instanceof IQProvider) {
//                            iqPacket = ((IQProvider)provider).parseIQ(parser);
//                        }
//                        else if (provider instanceof Class) {
//                            iqPacket = (IQ)PacketParserUtils.parseWithIntrospection(elementName,
//                                    (Class)provider, parser);
//                        }
//                    }
//                }
//            }
//            else if (eventType == XmlPullParser.END_TAG) {
//                if (parser.getName().equals("iq")) {
//                    done = true;
//                }
//            }
//        }
//        // Decide what to do when an IQ packet was not understood
//        if (iqPacket == null) {
//            if (IQ.Type.GET == type || IQ.Type.SET == type ) {
//                // If the IQ stanza is of type "get" or "set" containing a child element
//                // qualified by a namespace it does not understand, then answer an IQ of
//                // type "error" with code 501 ("feature-not-implemented")
//                iqPacket = new IQ() {
//                    public String getChildElementXML() {
//                        return null;
//                    }
//                };
//                iqPacket.setPacketID(id);
//                iqPacket.setTo(from);
//                iqPacket.setFrom(to);
//                iqPacket.setType(IQ.Type.ERROR);
//                iqPacket.setError(new XMPPError(501, "feature-not-implemented"));
//                connection.sendPacket(iqPacket);
//                return null;
//            }
//            else {
//                // If an IQ packet wasn't created above, create an empty IQ packet.
//                iqPacket = new IQ() {
//                    public String getChildElementXML() {
//                        return null;
//                    }
//                };
//            }
//        }
//
//        // Set basic values on the iq packet.
//        iqPacket.setPacketID(id);
//        iqPacket.setTo(to);
//        iqPacket.setFrom(from);
//        iqPacket.setType(type);
//        iqPacket.setError(error);
//
//        return iqPacket;
//    }
//
//    private Authentication parseAuthentication(XmlPullParser parser) throws Exception {
//        Authentication authentication = new Authentication();
//        boolean done = false;
//        while (!done) {
//            int eventType = parser.next();
//            if (eventType == XmlPullParser.START_TAG) {
//                if (parser.getName().equals("username")) {
//                    authentication.setUsername(parser.nextText());
//                }
//                else if (parser.getName().equals("password")) {
//                    authentication.setPassword(parser.nextText());
//                }
//                else if (parser.getName().equals("digest")) {
//                    authentication.setDigest(parser.nextText());
//                }
//                else if (parser.getName().equals("resource")) {
//                    authentication.setResource(parser.nextText());
//                }
//            }
//            else if (eventType == XmlPullParser.END_TAG) {
//                if (parser.getName().equals("query")) {
//                    done = true;
//                }
//            }
//        }
//        return authentication;
//    }
//
//    private RosterPacket parseRoster(XmlPullParser parser) throws Exception {
//        RosterPacket roster = new RosterPacket();
//        boolean done = false;
//        RosterPacket.Item item = null;
//        while (!done) {
//            int eventType = parser.next();
//            if (eventType == XmlPullParser.START_TAG) {
//                if (parser.getName().equals("item")) {
//                    String jid = parser.getAttributeValue("", "jid");
//                    String name = parser.getAttributeValue("", "name");
//                    // Create packet.
//                    item = new RosterPacket.Item(jid, name);
//                    // Set status.
//                    String ask = parser.getAttributeValue("", "ask");
//                    RosterPacket.ItemStatus status = RosterPacket.ItemStatus.fromString(ask);
//                    item.setItemStatus(status);
//                    // Set type.
//                    String subscription = parser.getAttributeValue("", "subscription");
//                    RosterPacket.ItemType type = RosterPacket.ItemType.fromString(subscription);
//                    item.setItemType(type);
//                }
//                if (parser.getName().equals("group")) {
//                    String groupName = parser.nextText();
//                    item.addGroupName(groupName);
//                }
//            }
//            else if (eventType == XmlPullParser.END_TAG) {
//                if (parser.getName().equals("item")) {
//                    roster.addRosterItem(item);
//                }
//                if (parser.getName().equals("query")) {
//                    done = true;
//                }
//            }
//        }
//        return roster;
//    }
//
//    private Registration parseRegistration(XmlPullParser parser) throws Exception {
//        Registration registration = new Registration();
//        Map fields = null;
//        boolean done = false;
//        while (!done) {
//            int eventType = parser.next();
//            if (eventType == XmlPullParser.START_TAG) {
//                if (parser.getName().equals("username")) {
//                    registration.setUsername(parser.nextText());
//                }
//                else if (parser.getName().equals("password")) {
//                    registration.setPassword(parser.nextText());
//                }
//                // Else if any other element that's in the jabber:iq:register namespace,
//                // attempt to parse it if it's in the form <name>value.
//                else if (parser.getNamespace().equals("jabber:iq:register")) {
//                    String name = parser.getName();
//                    String value = "";
//                    if (fields == null) {
//                        fields = new HashMap();
//                    }
//
//                    if (parser.next() == XmlPullParser.TEXT) {
//                        value = parser.getText();
//                    }
//                    // Ignore instructions, but anything else should be added to the map.
//                    if (!name.equals("instructions")) {
//                        fields.put(name, value);
//                    }
//                    else {
//                        registration.setInstructions(value);
//                    }
//                }
//                // Otherwise, it must be a packet extension.
//                else {
//                    registration.addExtension(
//                            PacketParserUtils.parsePacketExtension(
//                                    parser.getName(),
//                                    parser.getNamespace(),
//                                    parser));
//                }
//            }
//            else if (eventType == XmlPullParser.END_TAG) {
//                if (parser.getName().equals("query")) {
//                    done = true;
//                }
//            }
//        }
//        registration.setAttributes(fields);
//        return registration;
//    }
//
//    /**
//     * Parses error sub-packets.
//     *
//     * @param parser the XML parser.
//     * @return an error sub-packet.
//     * @throws Exception if an exception occurs while parsing the packet.
//     */
//    private XMPPError parseError(XmlPullParser parser) throws Exception {
//        String errorCode = null;
//        String message = null;
//        for (int i=0; i<parser.getAttributeCount(); i++) {
//            if (parser.getAttributeName(i).equals("code")) {
//                errorCode = parser.getAttributeValue("", "code");
//            }
//        }
//        // Get the error text in a safe way since we are not sure about the error message format
//        try {
//            message = parser.nextText();
//        }
//        catch (XmlPullParserException ex) {}
//        while (true) {
//            if (parser.getEventType() == XmlPullParser.END_TAG && parser.getName().equals("error")) {
//                break;
//            }
//            parser.next();
//        }
//        return new XMPPError(Integer.parseInt(errorCode), message);
//    }
//
//    /**
//     * Parses a message packet.
//     *
//     * @param parser the XML parser, positioned at the start of a message packet.
//     * @return a Message packet.
//     * @throws Exception if an exception occurs while parsing the packet.
//     */
//    private Packet parseMessage(XmlPullParser parser) throws Exception {
//        Message message = new Message();
//        String id = parser.getAttributeValue("", "id");
//        message.setPacketID(id == null ? Packet.ID_NOT_AVAILABLE : id);
//        message.setTo(parser.getAttributeValue("", "to"));
//        message.setFrom(parser.getAttributeValue("", "from"));
//        message.setType(Message.Type.fromString(parser.getAttributeValue("", "type")));
//
//        // Parse sub-elements. We include extra logic to make sure the values
//        // are only read once. This is because it's possible for the names to appear
//        // in arbitrary sub-elements.
//        boolean done = false;
//        String subject = null;
//        String body = null;
//        String thread = null;
//        Map properties = null;
//        while (!done) {
//            int eventType = parser.next();
//            if (eventType == XmlPullParser.START_TAG) {
//                String elementName = parser.getName();
//                String namespace = parser.getNamespace();
//                if (elementName.equals("subject")) {
//                    if (subject == null) {
//                        subject = parser.nextText();
//                    }
//                }
//                else if (elementName.equals("body")) {
//                    if (body == null) {
//                        body = parser.nextText();
//                    }
//                }
//                else if (elementName.equals("thread")) {
//                    if (thread == null) {
//                        thread = parser.nextText();
//                    }
//                }
//                else if (elementName.equals("error")) {
//                    message.setError(parseError(parser));
//                }
//                else if (elementName.equals("properties") &&
//                        namespace.equals(PROPERTIES_NAMESPACE))
//                {
//                    properties = parseProperties(parser);
//                }
//                // Otherwise, it must be a packet extension.
//                else {
//                    message.addExtension(
//                            PacketParserUtils.parsePacketExtension(elementName, namespace, parser));
//                }
//            }
//            else if (eventType == XmlPullParser.END_TAG) {
//                if (parser.getName().equals("message")) {
//                    done = true;
//                }
//            }
//        }
//        message.setSubject(subject);
//        message.setBody(body);
//        message.setThread(thread);
//        // Set packet properties.
//        if (properties != null) {
//            for (Iterator i=properties.keySet().iterator(); i.hasNext(); ) {
//                String name = (String)i.next();
//                message.setProperty(name, properties.get(name));
//            }
//        }
//        return message;
//    }
//
//    /**
//     * Parses a presence packet.
//     *
//     * @param parser the XML parser, positioned at the start of a presence packet.
//     * @return a Presence packet.
//     * @throws Exception if an exception occurs while parsing the packet.
//     */
//    private Presence parsePresence(XmlPullParser parser) throws Exception {
//        Presence.Type type = Presence.Type.fromString(parser.getAttributeValue("", "type"));
//
//        Presence presence = new Presence(type);
//        presence.setTo(parser.getAttributeValue("", "to"));
//        presence.setFrom(parser.getAttributeValue("", "from"));
//        String id = parser.getAttributeValue("", "id");
//        presence.setPacketID(id == null ? Packet.ID_NOT_AVAILABLE : id);
//
//        // Parse sub-elements
//        boolean done = false;
//        while (!done) {
//            int eventType = parser.next();
//            if (eventType == XmlPullParser.START_TAG) {
//                String elementName = parser.getName();
//                String namespace = parser.getNamespace();
//                if (elementName.equals("status")) {
//                    presence.setStatus(parser.nextText());
//                }
//                else if (elementName.equals("priority")) {
//                    try {
//                        int priority = Integer.parseInt(parser.nextText());
//                        presence.setPriority(priority);
//                    }
//                    catch (NumberFormatException nfe) { }
//                }
//                else if (elementName.equals("show")) {
//                    presence.setMode(Presence.Mode.fromString(parser.nextText()));
//                }
//                else if (elementName.equals("error")) {
//                    presence.setError(parseError(parser));
//                }
//                else if (elementName.equals("properties") &&
//                        namespace.equals(PROPERTIES_NAMESPACE))
//                {
//                    Map properties = parseProperties(parser);
//                    // Set packet properties.
//                    for (Iterator i=properties.keySet().iterator(); i.hasNext(); ) {
//                        String name = (String)i.next();
//                        presence.setProperty(name, properties.get(name));
//                    }
//                }
//                // Otherwise, it must be a packet extension.
//                else {
//                    presence.addExtension(
//                            PacketParserUtils.parsePacketExtension(elementName, namespace, parser));
//                }
//            }
//            else if (eventType == XmlPullParser.END_TAG) {
//                if (parser.getName().equals("presence")) {
//                    done = true;
//                }
//            }
//        }
//        return presence;
//    }
//
//    /**
//     * Parse a properties sub-packet. If any errors occur while de-serializing Java object
//     * properties, an exception will be printed and not thrown since a thrown
//     * exception will shut down the entire connection. ClassCastExceptions will occur
//     * when both the sender and receiver of the packet don't have identical versions
//     * of the same class.
//     *
//     * @param parser the XML parser, positioned at the start of a properties sub-packet.
//     * @return a map of the properties.
//     * @throws Exception if an error occurs while parsing the properties.
//     */
//    private Map parseProperties(XmlPullParser parser) throws Exception {
//        Map properties = new HashMap();
//        while (true) {
//            int eventType = parser.next();
//            if (eventType == XmlPullParser.START_TAG && parser.getName().equals("property")) {
//                // Parse a property
//                boolean done = false;
//                String name = null;
//                String type = null;
//                String valueText = null;
//                Object value = null;
//                while (!done) {
//                    eventType = parser.next();
//                    if (eventType == XmlPullParser.START_TAG) {
//                        String elementName = parser.getName();
//                        String namespace = parser.getNamespace();
//                        if (elementName.equals("name")) {
//                            name = parser.nextText();
//                        }
//                        else if (elementName.equals("value")) {
//                            type = parser.getAttributeValue("", "type");
//                            valueText = parser.nextText();
//                        }
//                    }
//                    else if (eventType == XmlPullParser.END_TAG) {
//                        if (parser.getName().equals("property")) {
//                            if ("integer".equals(type)) {
//                                value = new Integer(valueText);
//                            }
//                            else if ("long".equals(type))  {
//                                value = new Long(valueText);
//                            }
//                            else if ("float".equals(type)) {
//                                value = new Float(valueText);
//                            }
//                            else if ("double".equals(type)) {
//                                value = new Double(valueText);
//                            }
//                            else if ("boolean".equals(type)) {
//                                value = new Boolean(valueText);
//                            }
//                            else if ("string".equals(type)) {
//                                value = valueText;
//                            }
//                            else if ("java-object".equals(type)) {
//                                try {
//                                    byte [] bytes = StringUtils.decodeBase64(valueText);
//                                    ObjectInputStream in = new ObjectInputStream(new ByteArrayInputStream(bytes));
//                                    value = in.readObject();
//                                }
//                                catch (Exception e) {
//                                    e.printStackTrace();
//                                }
//                            }
//                            if (name != null && value != null) {
//                                properties.put(name, value);
//                            }
//                            done = true;
//                        }
//                    }
//                }
//            }
//            else if (eventType == XmlPullParser.END_TAG) {
//                if (parser.getName().equals("properties")) {
//                    break;
//                }
//            }
//        }
//        return properties;
//    }
//
//    /**
//     * A wrapper class to associate a packet collector with a listener.
//     */
//    private static class ListenerWrapper {
//
//        private PacketListener packetListener;
//        private PacketCollector packetCollector;
//
//        public ListenerWrapper(PacketReader packetReader, PacketListener packetListener,
//                               PacketFilter packetFilter)
//        {
//            this.packetListener = packetListener;
//            this.packetCollector = new PacketCollector(packetReader, packetFilter);
//        }
//
//        public boolean equals(Object object) {
//            if (object == null) {
//                return false;
//            }
//            if (object instanceof ListenerWrapper) {
//                return ((ListenerWrapper)object).packetListener.equals(this.packetListener);
//            }
//            else if (object instanceof PacketListener) {
//                return object.equals(this.packetListener);
//            }
//            return false;
//        }
//
//        public boolean notifyListener() {
//            Packet packet = packetCollector.pollResult();
//            if (packet != null) {
//                packetListener.processPacket(packet);
//                return true;
//            }
//            else {
//                return false;
//            }
//        }
//
//        public void cancel() {
//            packetCollector.cancel();
//            packetCollector = null;
//            packetListener = null;
//        }
//    }
//}
