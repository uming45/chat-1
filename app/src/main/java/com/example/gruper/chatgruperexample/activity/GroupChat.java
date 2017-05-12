package com.example.gruper.chatgruperexample.activity;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.example.gruper.chatgruperexample.MyService;
import com.example.gruper.chatgruperexample.MyXMPP;
import com.example.gruper.chatgruperexample.R;

import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.SASLAuthentication;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smackx.muc.InvitationListener;
import org.jivesoftware.smackx.muc.MultiUserChat;
import org.jivesoftware.smackx.muc.MultiUserChatManager;
import org.jivesoftware.smackx.xdata.Form;
import android.app.Service;
import android.util.Log;

import static android.R.attr.password;
import static com.example.gruper.chatgruperexample.MyXMPP.connection;

/**
 * Created by gruper on 11/05/17.
 */

public class GroupChat {

//    XMPPTCPConnection connection = new XMPPTCPConnection(config);
//     connection.connect();
//     connection.login(ID1, password1);
//        Presence presence = new Presence(Presence.Type.available);
//     connection.sendPacket(presence);



    // Create the nickname.
//    Resourcepart nickname = Resourcepart.from("testbot");
//
//    // A other use (we may invite him to a MUC).
//    FullJid otherJid = JidCreate.fullFromm("user3@host.org/Smack");

    // Get the MultiUserChatManager
//        MultiUserChatManager manager = MultiUserChatManager.getInstanceFor(connection);
//
//        // Create a MultiUserChat using an XMPPConnection for a room
//        MultiUserChat muc2 = manager.getMultiUserChat("myroom@conference.jabber.org");
//
//
//        // User2 invites user3 to join to the room
//        muc2.invite(otherJid, "Meet me in this excellent room");
//        In this example we can see how to listen for room invitations and decline invitations:
//
//        // User3 listens for MUC invitations
//        MultiUserChatManager.getInstanceFor(connection).addInvitationListener(new InvitationListener() {
//        public void invitationReceived (XMPPConnection conn, String room, EntityFullJid
//        inviter, String reason, String password){
//        // Reject the invitation
//        MultiUserChat.decline(conn, room, inviter.asBareJid()s, "I'm busy right now");

//
//        }
//    }
}
