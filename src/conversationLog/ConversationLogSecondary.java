package conversationLog;

import components.map.Map;
import components.map.Map1L;
import components.sequence.Sequence;
import components.sequence.Sequence1L;
import components.simplereader.SimpleReader;

import components.simplewriter.SimpleWriter;



/**
* Sequence-backed implementation of ConversationLog .
*/
// I don't use abstract class here because I want to implement all methods in this class directly.
public class ConversationLogSecondary implements ConversationLog {

    // Private inner class to represent a chat entry, this would help to store speaker and text together. What I meant is
    // I can stack them together in the sequence. Kind of similar to Map/ Array inside Sequence. But so much better because I can show
    // off new shit i learnt.
    private static class ChatEntry {
        String speaker;
        String text;

        ChatEntry(String speaker, String text) {
            this.speaker = speaker;
            this.text = text;
        }
    }

    // Sequence to hold chat entries
    private Sequence<ChatEntry> entries;

    /**
    * Constructor for ConversationLog1L.
    */
    public ConversationLogSecondary() {
        this.createNewRep();
    }
    private void createNewRep() {
        this.entries = new Sequence1L<>();  
    }

    // Standard interface methods
    @Override
    public ConversationLogSecondary newInstance() {
        return new ConversationLogSecondary();
    }

    @Override
    public void clear() {
        this.entries.clear();
    }

    @Override
    public void transferFrom(ConversationLog source) {
        if (this != source) {
        ConversationLogSecondary that = (ConversationLogSecondary) source;
        this.entries.transferFrom(that.entries);
        }
    }

    // Kernel interface methods
    @Override
    public int length() {
        return this.entries.length();
    }
    @Override
    public boolean isEmpty() {
        return this.entries.length() == 0;
    }
    @Override
    public void append(String speaker, String text) {
        ChatEntry newEntry = new ChatEntry(speaker, text);
        this.entries.add(this.entries.length(), newEntry);
    }
    @Override
    public Map<String, String> removeChat() {
        ChatEntry lastEntry = this.entries.remove(this.entries.length() - 1);
        Map<String, String> removedMap = new Map1L<>();
        removedMap.add("speaker", lastEntry.speaker);
        removedMap.add("text", lastEntry.text);
        return removedMap;
    }
    @Override
    public void reset() {
        // Probably the most LOL method ever
        this.clear();
    }

    @Override
    public ConversationLog segment(int start, int end) {
        Sequence<ChatEntry> newEntries = new Sequence1L<>();
        for (int i = start; i < end; i++) {
            newEntries.add(newEntries.length(), this.entries.entry(i));
        }
        this.entries = newEntries;
        return this;
    }

    // Secondary interface methods
    @Override
    public void exportChat(SimpleWriter out) {
        for (int i = 0; i < this.entries.length(); i++) {
            ChatEntry entry = this.entries.entry(i);
            out.println(entry.speaker + ": " + entry.text);
        }
        out.close();
    }
    @Override
    public void importChat(SimpleReader in) {
        while (!in.atEOS()) {
            String line = in.nextLine();
            int colonIndex = line.indexOf(": ");
            if (colonIndex != -1) {
                String speaker = line.substring(0, colonIndex);
                String text = line.substring(colonIndex + 2);
                this.append(speaker, text);
            }
        }
        in.close();
    }
    @Override
    public String toTranscript() {
        StringBuilder transcript = new StringBuilder();
        for (int i = 0; i < this.entries.length(); i++) {
            ChatEntry entry = this.entries.entry(i);
            transcript.append(entry.speaker).append(": ").append(entry.text).append("\n");
        }
        return transcript.toString();
    }
    @Override
    public Sequence<Integer> find(String substring) {
        Sequence<Integer> indices = new Sequence1L<>();
        for (int i = 0; i < this.entries.length(); i++) {
            ChatEntry entry = this.entries.entry(i);
            if (entry.text.contains(substring)) {
                indices.add(indices.length(), i);
            }
        }
        return indices;
    }
    @Override
    public Sequence<String> speakerChat(String speaker) {
        Sequence<String> chats = new Sequence1L<>();
        for (int i = 0; i < this.entries.length(); i++) {
            ChatEntry entry = this.entries.entry(i);
            if (entry.speaker.equals(speaker)) {
                chats.add(chats.length(), entry.text);
            }
        }
        return chats;
    }

    

}



    
