package com.example.gruper.chatgruperexample.model;

import java.util.Arrays;
import java.util.List;

/**
 * Created by gruper on 10/05/17.
 */

public class GroupRespon {
    private List<ChatRoom> chatRoom;

    public List<ChatRoom> getChatRoom() {
        return chatRoom;
    }

    public void setChatRoom(List<ChatRoom> chatRoom) {
        this.chatRoom = chatRoom;
    }

    @Override
    public String toString() {
        return "GroupRespon{" +
                "chatRoom=" + chatRoom +
                '}';
    }
}
