package edu.rice.comp504.model.obj;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class ChatBox {

    private int id;
    private User user_a;
    private User user_b;
    private ChatRoom room_of_origin;
    List<Message> chatBoxHistory;

    public ChatBox (int id, User a, User b, ChatRoom origin) {

        this.id = id;
        this.user_a = a;
        this.user_b = b;
        this.room_of_origin = origin;
        this.chatBoxHistory = new List<Message>() {
            @Override
            public int size() {
                return 0;
            }

            @Override
            public boolean isEmpty() {
                return false;
            }

            @Override
            public boolean contains(Object o) {
                return false;
            }

            @Override
            public Iterator<Message> iterator() {
                return null;
            }

            @Override
            public Object[] toArray() {
                return new Object[0];
            }

            @Override
            public <T> T[] toArray(T[] a) {
                return null;
            }

            @Override
            public boolean add(Message message) {
                return false;
            }

            @Override
            public boolean remove(Object o) {
                return false;
            }

            @Override
            public boolean containsAll(Collection<?> c) {
                return false;
            }

            @Override
            public boolean addAll(Collection<? extends Message> c) {
                return false;
            }

            @Override
            public boolean addAll(int index, Collection<? extends Message> c) {
                return false;
            }

            @Override
            public boolean removeAll(Collection<?> c) {
                return false;
            }

            @Override
            public boolean retainAll(Collection<?> c) {
                return false;
            }

            @Override
            public void clear() {

            }

            @Override
            public Message get(int index) {
                return null;
            }

            @Override
            public Message set(int index, Message element) {
                return null;
            }

            @Override
            public void add(int index, Message element) {

            }

            @Override
            public Message remove(int index) {
                return null;
            }

            @Override
            public int indexOf(Object o) {
                return 0;
            }

            @Override
            public int lastIndexOf(Object o) {
                return 0;
            }

            @Override
            public ListIterator<Message> listIterator() {
                return null;
            }

            @Override
            public ListIterator<Message> listIterator(int index) {
                return null;
            }

            @Override
            public List<Message> subList(int fromIndex, int toIndex) {
                return null;
            }
        }
    }




}
