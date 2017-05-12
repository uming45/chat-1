//package com.example.gruper.chatgruperexample.xmpp;
//
//import org.jivesoftware.smack.packet.Packet;
//
///**
// * Created by gruper on 11/05/17.
// */
//
//import org.jivesoftware.smack.packet.Packet;
//import org.jivesoftware.smack.filter.PacketFilter;
//import org.jivesoftware.smack.packet.Stanza;
//
//import java.util.LinkedList;
//
//public class PacketCollector {
//
//    /**
//     * Max number of packets that any one collector can hold. After the max is
//     * reached, older packets will be automatically dropped from the queue as
//     * new packets are added.
//     */
//    private static final int MAX_PACKETS = 65536;
//
//    private PacketFilter packetFilter;
//    private LinkedList resultQueue;
//    private PacketReader packetReader;
//    private boolean cancelled = false;
//
//    /**
//     * Creates a new packet collector. If the packet filter is <tt>null, then
//     * all packets will match this collector.
//     *
//     * @param packetReader the packetReader the collector is tied to.
//     * @param packetFilter determines which packets will be returned by this collector.
//     */
//    protected PacketCollector(PacketReader packetReader, PacketFilter packetFilter) {
//        this.packetReader = packetReader;
//        this.packetFilter = packetFilter;
//        this.resultQueue = new LinkedList();
//        // Add the collector to the packet reader's list of active collector.
//        synchronized (packetReader.collectors) {
//            packetReader.collectors.add(this);
//        }
//    }
//
//    /**
//     * Explicitly cancels the packet collector so that no more results are
//     * queued up. Once a packet collector has been cancelled, it cannot be
//     * re-enabled. Instead, a new packet collector must be created.
//     */
//    public void cancel() {
//        // If the packet collector has already been cancelled, do nothing.
//        if (cancelled) {
//            return;
//        }
//        else {
//            cancelled = true;
//            // Remove object from collectors list by setting the value in the
//            // list at the correct index to null. The collector thread will
//            // automatically remove the actual list entry when it can.
//            synchronized (packetReader.collectors) {
//                int index = packetReader.collectors.indexOf(this);
//                packetReader.collectors.set(index, null);
//            }
//        }
//    }
//
//    /**
//     * Returns the packet filter associated with this packet collector. The packet
//     * filter is used to determine what packets are queued as results.
//     *
//     * @return the packet filter.
//     */
//    public PacketFilter getPacketFilter() {
//        return packetFilter;
//    }
//
//    /**
//     * Polls to see if a packet is currently available and returns it, or
//     * immediately returns <tt>null if no packets are currently in the
//     * result queue.
//     *
//     * @return the next packet result, or <tt>null if there are no more
//     *      results.
//     */
//    public synchronized Packet pollResult() {
//        if (resultQueue.isEmpty()) {
//            return null;
//        }
//        else {
//            return (Packet)resultQueue.removeLast();
//        }
//    }
//
//    /**
//     * Returns the next available packet. The method call will block (not return)
//     * until a packet is available.
//     *
//     * @return the next available packet.
//     */
//    public synchronized Packet nextResult() {
//        // Wait indefinitely until there is a result to return.
//        while (resultQueue.isEmpty()) {
//            try {
//                wait();
//            }
//            catch (InterruptedException ie) { }
//        }
//        return (Packet)resultQueue.removeLast();
//    }
//
//    /**
//     * Returns the next available packet. The method call will block (not return)
//     * until a packet is available or the <tt>timeout has elapased. If the
//     * timeout elapses without a result, <tt>null will be returned.
//     *
//     * @param timeout the amount of time to wait for the next packet (in milleseconds).
//     * @return the next available packet.
//     */
//    public synchronized Packet nextResult(long timeout) {
//        // Wait up to the specified amount of time for a result.
//        if (resultQueue.isEmpty()) {
//            try {
//                wait(timeout);
//            }
//            catch (InterruptedException ie) { }
//        }
//        // If still no result, return null.
//        if (resultQueue.isEmpty()) {
//            return null;
//        }
//        else {
//            return (Packet)resultQueue.removeLast();
//        }
//    }
//
//    /**
//     * Processes a packet to see if it meets the criteria for this packet collector.
//     * If so, the packet is added to the result queue.
//     *
//     * @param packet the packet to process.
//     */
//    protected synchronized void processPacket(Packet packet) {
//        if (packet == null) {
//            return;
//        }
//
//        //DIRUBAH
//        if (packetFilter == null || packetFilter.accept((Stanza) packet)) {
//            // If the max number of packets has been reached, remove the oldest one.
//            if (resultQueue.size() == MAX_PACKETS) {
//                resultQueue.removeLast();
//            }
//            // Add the new packet.
//            resultQueue.addFirst(packet);
//            // Notify waiting threads a result is available.
//            notifyAll();
//        }
//    }
//}
